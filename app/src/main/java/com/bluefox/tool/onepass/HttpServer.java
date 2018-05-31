package com.bluefox.tool.onepass;

import android.content.Context;
import android.util.Log;

import com.bluefox.tool.onepass.model.Account;
import com.bluefox.tool.onepass.model.Site;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
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
            String requestPath = session.getUri();
            Log.println(Log.DEBUG, "request", "url: " + requestPath);
            InputStream inputStream = null;
            if (requestPath.startsWith("/webapi/")) {
                return this.handleWebApi(session);
            }
            else if (requestPath.startsWith("/static/")) {
                inputStream = this.context.getAssets().open("content" + requestPath);
                return newChunkedResponse(Response.Status.OK, null, inputStream);
            }
            else {
                switch (requestPath) {
                    case "/favicon.ico":
                        return this.handleNotFound();
                    case "/backup.db":
                        return this.handleBackup();
                    default:
                        inputStream = this.context.getAssets().open("content/index.html");
                        return newChunkedResponse(Response.Status.OK, "text/html", inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return this.handleError();
        } catch (Exception e) {
            e.printStackTrace();
            return this.handleError();
        }
    }

    private Response handleWebApi(IHTTPSession session) {
        try {
            String path = session.getUri().toLowerCase();
            String[] segments = path.substring(1).split("/");
            if (segments.length < 2) {
                return this.handleNotFound();
            }
            String entity = segments[1];
            String operation = segments.length > 2 ? segments[2] : "get";
            if (session.getMethod().equals(Method.POST)) {
                Map<String, String> files = new HashMap<>();
                session.parseBody(files);//TODO: test files' data
            }
            switch (entity) {
                case "site":
                    return this.handleSite(operation, session.getParameters());
                case "account":
                    return this.handleAccount(operation, session.getParameters());
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
        if (keywords.size() > 0) {
            keyword = keywords.get(0);
        }
        if (keyword == null || keyword == "") {
            throw new Exception("Search failed! Keyword is null!");
        }
        List<String> withaccounts = parameters.get("withaccount");
        if (withaccounts.size() > 0) {
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
                if (postBodies.isEmpty()) {
                    throw new Exception("Save site failed! Body is null");
                }
                Site site = gson.fromJson(postBodies.get(0), Site.class);
                site.Name = URLDecoder.decode(site.Name);
                long siteId = Store.getInstance(this.context).saveSite(site);
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<SaveSiteResult>(new SaveSiteResult(siteId))));
            case "fetch":
            default:
                List<String> ids = parameters.get("id");
                if (ids.isEmpty()) {
                    throw new Exception("Fetch site failed! Id is null");
                }
                List<Site> sites = Store.getInstance(this.context).getSite(Long.parseLong(ids.get(0)));
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<List<Site>>(sites)));
        }
    }

    private Response handleAccount(String operation, Map<String, List<String>> parameters) throws Exception {
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
                if (postBodies.isEmpty()) {
                    throw new Exception("Save account failed! Body is null");
                }
                long accountId = Store.getInstance(this.context).saveAccount(gson.fromJson(postBodies.get(0), Account.class));
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<SaveAccountResult>(new SaveAccountResult(accountId))));
            case "fetch":
            default:
                List<String> ids = parameters.get("id");
                List<String> siteIds = parameters.get("siteid");
                if (ids.isEmpty() && siteIds.isEmpty()) {
                    throw new Exception("Fetch account failed! Id and SiteId is null");
                }
                List<Account> accounts = Store.getInstance(this.context).getAccount(ids.size() > 0 ? Long.parseLong(ids.get(0)) : 0, siteIds.size() > 0 ? Long.parseLong(siteIds.get(0)) : 0);
                return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(new WebApiResponse<List<Account>>(accounts)));
        }
    }

    private Response handleError() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Internal Error!");
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

    private Response handleBackup() {
        try {
            String pkgName = this.context.getPackageName();
            File db = new File("/data/data/" + pkgName + "/databases/op.db");
            FileInputStream inputStream = new FileInputStream(db);
            return newChunkedResponse(Response.Status.OK, "application/octet-stream", inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return this.handleNotFound();
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
