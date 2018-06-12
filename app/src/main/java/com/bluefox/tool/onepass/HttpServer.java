package com.bluefox.tool.onepass;

import android.content.Context;
import android.util.Log;

import com.bluefox.tool.onepass.model.Account;
import com.bluefox.tool.onepass.model.Site;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {
    private Context context;

    public HttpServer(Context context) {
        super(18888);
        this.context = context.getApplicationContext();
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<>();
            if (session.getMethod().equals(Method.POST)) {
                session.parseBody(files);//TODO: test files' data
            }
            String requestPath = session.getUri();
            InputStream inputStream = null;
            if (requestPath.startsWith("/webapi/")) {
                return this.checkAuth(session) ? this.handleWebApi(session) : this.needAuthReponse();
            }
            else if (requestPath.startsWith("/static/")) {
                inputStream = this.context.getAssets().open("content" + requestPath);
                return newChunkedResponse(Response.Status.OK, null, inputStream);
            }
            else if (requestPath.startsWith("/login")) {
                return this.handleLogin(session);
            }
            else {
                switch (requestPath) {
                    case "/favicon.ico":
                        return this.handleNotFound();
                    case "/backup":
                        return this.checkAuth(session) ? this.handleBackup(session) : this.needAuthReponse();
                    case "/restore":
                        return this.checkAuth(session) ? this.handleRestore(session, files) : this.needAuthReponse();
//                    case "/register":
//                        return this.handleRegister();
                    default:
                        inputStream = this.context.getAssets().open("content/index.html");
                        return newChunkedResponse(Response.Status.OK, "text/html", inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return this.handleError(e);
        } catch (Exception e) {
            e.printStackTrace();
            return this.handleError(e);
        }
    }

    private boolean checkAuth(IHTTPSession session) throws Exception {
        String auth = null;
        //先从parameter中取
        Map<String, List<String>> parameters = session.getParameters();
        List<String> authes = parameters.get("authority");
        if (authes != null && authes.size() > 0) {
            auth = authes.get(0);
        }
        //parameter中不存在，再从cookie中取
        if (auth == null || auth.equals("")) {
            auth = session.getCookies().read("authority");
        }

        if (auth == null || auth.equals("")) {
            return false;
        }
        String md5AuthInDB = Store.getInstance(this.context).getUser();
        if (md5AuthInDB == null) {
            throw new Exception("There is no auth in db!");
        }
        String md5Auth = Md5.execute(auth);
        return md5Auth.equals(md5AuthInDB);
    }

    private Response needAuthReponse() {
        Gson gson = new Gson();
        return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse(1, "need auth!")));
    }

    private Response handleLogin(IHTTPSession session) throws Exception {
        if (this.checkAuth(session)) {
            Gson gson = new Gson();
            return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse()));
        }
        else {
            return this.needAuthReponse();
        }
    }

//    private Response handleRegister() throws Exception {
//        long id = Store.getInstance(this.context).saveUser(Md5.execute("yanghuiping521"));
//        return newFixedLengthResponse(Response.Status.OK, "text/plain", String.valueOf(id));
//    }

    private Response handleWebApi(IHTTPSession session) {
        try {
            String path = session.getUri().toLowerCase();
            String[] segments = path.substring(1).split("/");
            if (segments.length < 2) {
                return this.handleNotFound();
            }
            String entity = segments[1];
            String operation = segments.length > 2 ? segments[2] : "get";
            switch (entity) {
                case "site":
                    return this.handleSite(operation, session.getParameters());
                case "account":
                    return this.handleAccount(operation, session.getCookies().read("authority"), session.getParameters());
                case "search":
                    return this.handleSearch(session.getParameters());
                default:
                    return this.handleNotFound();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Gson gson = new Gson();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", gson.toJson(new WebApiResponse(ex.hashCode(), ex.getMessage())));
        }
    }

    private Response handleSearch(Map<String, List<String>> parameters) throws Exception {
        Gson gson = new Gson();
        String keyword = null;
        boolean withAccount = false;
        List<String> keywords = parameters.get("keyword");
        if (keywords != null && keywords.size() > 0) {
            keyword = keywords.get(0);
        }
        if (keyword == null || keyword.equals("")) {
            throw new Exception("Search failed! Keyword is null!");
        }
        List<String> withaccounts = parameters.get("withaccount");
        if (withaccounts != null && withaccounts.size() > 0) {
            withAccount = Boolean.parseBoolean(withaccounts.get(0));
        }
        List<Site> sites = Store.getInstance(this.context).search(keyword, withAccount);
        return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<List<Site>>(sites)));
    }

    private Response handleSite(String operation, Map<String, List<String>> parameters) throws Exception {
        class SaveSiteResult {
            public long SiteId;
            public SaveSiteResult(long siteId) {
                this.SiteId = siteId;
            }
        }

        Gson gson = new Gson();
        switch (operation) {
            case "save":
                List<String> postBodies = parameters.get("site");
                if (postBodies == null || postBodies.isEmpty()) {
                    throw new Exception("Save site failed! Body is null");
                }
                Site site = gson.fromJson(postBodies.get(0), Site.class);
                site.Name = URLDecoder.decode(site.Name);
                long siteId = Store.getInstance(this.context).saveSite(site);
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<SaveSiteResult>(new SaveSiteResult(siteId))));
            case "fetch":
            default:
                List<String> ids = parameters.get("id");
                List<String> levels = parameters.get("level");
                if ((ids == null || ids.isEmpty()) && (levels == null || levels.isEmpty())) {
                    throw new Exception("Fetch site failed! Id and Level is null");
                }
                long id = ids != null && ids.size() > 0 ? Long.parseLong(ids.get(0)) : 0;
                int level = levels != null && levels.size() > 0 ? Integer.parseInt(levels.get(0)) : 0;
                List<Site> sites = Store.getInstance(this.context).getSite(id, level);
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<List<Site>>(sites)));
        }
    }

    private Response handleAccount(String operation, String auth, Map<String, List<String>> parameters) throws Exception {
        class SaveAccountResult {
            public long AccountId;
            public SaveAccountResult(long accountId) {
                this.AccountId = accountId;
            }
        }

        Gson gson = new Gson();
        switch (operation) {
            case "save":
                List<String> postBodies = parameters.get("account");
                if (postBodies == null || postBodies.isEmpty()) {
                    throw new Exception("Save account failed! Body is null");
                }
                Account newAccount = gson.fromJson(postBodies.get(0), Account.class);
                newAccount.Password = Aes.encrypt(auth, newAccount.Password);
                long accountId = Store.getInstance(this.context).saveAccount(newAccount);
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<SaveAccountResult>(new SaveAccountResult(accountId))));
            case "fetch":
            default:
                List<String> ids = parameters.get("id");
                List<String> siteIds = parameters.get("siteid");
                if ((ids == null || ids.isEmpty()) && (siteIds == null || siteIds.isEmpty())) {
                    throw new Exception("Fetch account failed! Id and SiteId is null");
                }
                long id = ids != null && ids.size() > 0 ? Long.parseLong(ids.get(0)) : 0;
                long siteId = siteIds != null && siteIds.size() > 0 ? Long.parseLong(siteIds.get(0)) : 0;
                List<Account> accounts = Store.getInstance(this.context).getAccount(id, siteId);
                for (int i = 0; i < accounts.size(); ++i) {
                    Account acc = accounts.get(i);
                    acc.Password = Aes.decrypt(auth, acc.Password);
                }
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<List<Account>>(accounts)));
        }
    }

    private Response handleError(Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append(e.getMessage());
        StackTraceElement[] traces = e.getStackTrace();
        for (int i = 0; i < traces.length; ++i) {
            builder.append(traces[i]);
        }
        builder.append("</body></html>\n");
        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", builder.toString());
    }

    private Response handleNotFound() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Not Found!");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", builder.toString());
    }

//    private Response handleBackup() {
//        try {
//            String pkgName = this.context.getPackageName();
//            File db = new File("/data/data/" + pkgName + "/databases/op.db");
//            FileInputStream inputStream = new FileInputStream(db);
//            return newChunkedResponse(Response.Status.OK, "application/octet-stream", inputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return this.handleNotFound();
//        }
//    }

    private Response handleBackup(IHTTPSession session) {
        try {
            InputStream inputStream = Store.getInstance(this.context).exportToStream(session.getCookies().read("authority"));
            return newChunkedResponse(Response.Status.OK, "application/octet-stream", inputStream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return this.handleNotFound();
        }
    }

    private Response handleRestore(IHTTPSession session, Map<String, String> files) {
        try {
            Gson gson = new Gson();
            FileInputStream fileInputStream = new FileInputStream(files.get("backup"));
            Store.getInstance(this.context).importFromStream(session.getCookies().read("authority"), fileInputStream);
            return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse()));
        } catch (Exception e) {
            e.printStackTrace();
            return this.handleError(e);
        }
    }

    class WebApiResponse<T> {
        public int errorCode;
        public String message;
        public T data;
        public boolean isSuccess;

        public WebApiResponse() {
            this(0, null, null);
        }

        public WebApiResponse(T data) {
            this(0, null, data);
        }

        public WebApiResponse(int errorCode, String message) {
            this(errorCode, message, null);
        }

        private WebApiResponse(int errorCode, String message, T data) {
            this.errorCode = errorCode;
            this.message = message;
            this.data = data;
            this.setSuccess();
        }

        private void setSuccess() {
            this.isSuccess = this.errorCode == 0;
        }
    }
}
