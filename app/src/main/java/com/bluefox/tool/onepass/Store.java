package com.bluefox.tool.onepass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.bluefox.tool.onepass.model.Account;
import com.bluefox.tool.onepass.model.Site;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store extends SQLiteOpenHelper {
    private static final char splitChar = (char)200;

    private static Store __instance;

    public static Store getInstance(Context context) {
        if (__instance == null) {
            __instance = new Store(context.getApplicationContext(), "op.db", null, 1);
        }
        return __instance;
    }

    private Store(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer primary key autoincrement, token varchar(100))");
        db.execSQL("create table site(id integer primary key autoincrement, name varchar(50), url varchar(100), level integer)");
        db.execSQL("create table certification(id integer primary key autoincrement, siteId integer, uid varchar(50), pwd varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from user order by id desc limit 0,1", null);
            String token = null;
            while (cursor.moveToNext()) {
                token = cursor.getString(cursor.getColumnIndex("token"));
            }
            return token;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public long saveUser(String token) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("token", token);
            return db.insert("user", null, values);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public List<Site> getSite(long id, int level) throws Exception {
        return this.getSite(id, level, 0, 1000);
    }

    public List<Site> getSite(long id, int level, int pageIndex, int pageSize) throws Exception {
        return this.getSite(id, level, pageIndex, pageSize, false).SiteList;
    }

    public SitePageList getSite(long id, int level, int pageIndex, int pageSize, boolean returnTotalPageCount) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            SitePageList result = new SitePageList();
            if (returnTotalPageCount) {
                Cursor totalCountCursor = null;
                if (id > 0) {
                    totalCountCursor = db.query("site", null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
                }
                else {
                    totalCountCursor = db.query("site", null, "level=?", new String[] {String.valueOf(level)}, null, null, null);
                }
                result.TotalPageCount = (int)Math.ceil(((double)totalCountCursor.getCount()) / pageSize);
            }

            result.PageIndex = pageIndex;

            String limit = (pageIndex * pageSize) + "," + pageSize;
            Cursor cursor = null;
            if (id > 0) {
                cursor = db.query("site", null, "id=?", new String[] {String.valueOf(id)}, null, null, null, limit);
            }
            else {
                cursor = db.query("site", null, "level=?", new String[] {String.valueOf(level)}, null, null, null, limit);
            }
            while (cursor.moveToNext()) {
                Site site = new Site();
                site.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                site.Name = cursor.getString(cursor.getColumnIndex("name"));
                site.Url = cursor.getString(cursor.getColumnIndex("url"));
                site.Level = Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));
                result.SiteList.add(site);
            }
            return result;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public long saveSite(Site site) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            site.validate();
            ContentValues values = new ContentValues();
            values.put("name", site.Name);
            values.put("url", site.Url);
            values.put("level", site.Level);
            if (site.Id > 0) {
                Cursor cursor = db.query("site", new String[]{"id"}, "id=?", new String[] {String.valueOf(site.Id)}, null, null, null);
                if (cursor.getCount() == 0) {
                    throw new Exception("Id[" + site.Id + "] row does not exist in table site!");
                }
                int n = db.update("site", values, "id=?", new String[]{String.valueOf(site.Id)});
                if (n != 1) {
                    throw new Exception("update failed!");
                }
                return site.Id;
            }
            else {
                return db.insert("site", null, values);
            }
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public List<Account> getAccount(long id, long siteId) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = null;
            if (id > 0) {
                cursor = db.query("certification", null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
            }
            else {
                cursor = db.query("certification", null, "siteId=?", new String[] {String.valueOf(siteId)}, null, null, null);
            }
            List<Account> result = new ArrayList<>();
            while (cursor.moveToNext()) {
                Account account = new Account();
                account.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                account.SiteId = Long.parseLong(cursor.getString(cursor.getColumnIndex("siteId")));
                account.UserName = cursor.getString(cursor.getColumnIndex("uid"));
                account.Password = cursor.getString(cursor.getColumnIndex("pwd"));
                result.add(account);
            }
            return result;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public long saveAccount(Account account) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            account.validate();
            ContentValues values = new ContentValues();
            values.put("siteId", account.SiteId);
            values.put("uid", account.UserName);
            values.put("pwd", account.Password);
            if (account.Id > 0) {
                Cursor cursor = db.query("certification", new String[]{"id"}, "id=?", new String[] {String.valueOf(account.Id)}, null, null, null);
                if (cursor.getCount() == 0) {
                    throw new Exception("Id[" + account.Id + "] row does not exist in table account!");
                }
                int n = db.update("certification", values, "id=?", new String[]{String.valueOf(account.Id)});
                if (n != 1) {
                    throw new Exception("update failed!");
                }
                return account.Id;
            }
            else {
                return db.insert("certification", null, values);
            }
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public List<Site> search(String keyword, boolean withAccount) {
        return this.search(keyword, withAccount, 0, 1000);
    }

    public List<Site> search(String keyword, boolean withAccount, int pageIndex, int pageSize) {
        return this.search(keyword, withAccount, pageIndex, pageSize, false).SiteList;
    }

    public SitePageList search(String keyword, boolean withAccount, int pageIndex, int pageSize, boolean returnTotalPageCount) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            SitePageList result = new SitePageList();
            if (returnTotalPageCount) {
                Cursor totalCountCursor = db.query("site", null, "name like ? or url like ?", new String[] { "%" + keyword + "%", "%" + keyword + "%" }, null, null, null);
                result.TotalPageCount = (int)Math.ceil(((double)totalCountCursor.getCount()) / pageSize);
            }

            result.PageIndex = pageIndex;

            String limit = (pageIndex * pageSize) + "," + pageSize;
            Cursor cursor = db.query("site", null, "name like ? or url like ?", new String[] { "%" + keyword + "%", "%" + keyword + "%" }, null, null, null, limit);
            Map<Long, Integer> siteIndexs = new HashMap<>();
            while (cursor.moveToNext()) {
                Site site = new Site();
                site.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                site.Name = cursor.getString(cursor.getColumnIndex("name"));
                site.Url = cursor.getString(cursor.getColumnIndex("url"));
                site.Level = Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));
                result.SiteList.add(site);
                siteIndexs.put(site.Id, result.SiteList.size() - 1);
            }

            if (withAccount) {
                List<String> siteIdArgs = new ArrayList<>();
                List<String> siteIds = new ArrayList<>();
                if (!result.SiteList.isEmpty()) {
                    for (int i = 0; i < result.SiteList.size(); ++i) {
                        siteIdArgs.add("?");
                        siteIds.add(String.valueOf(result.SiteList.get(i).Id));
                    }
                }
                String[] ids = new String[siteIds.size()];
                Cursor cursor1 = db.query("certification", null, "siteId in (" + TextUtils.join(",", siteIdArgs) + ")", siteIds.toArray(ids), null, null, null);
                while (cursor1.moveToNext()) {
                    Account account = new Account();
                    account.Id = Long.parseLong(cursor1.getString(cursor1.getColumnIndex("id")));
                    account.SiteId = Long.parseLong(cursor1.getString(cursor1.getColumnIndex("siteId")));
                    account.UserName = cursor1.getString(cursor1.getColumnIndex("uid"));
                    account.Password = cursor1.getString(cursor1.getColumnIndex("pwd"));
                    result.SiteList.get(siteIndexs.get(account.SiteId)).AccountList.add(account);
                }
            }
            return result;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            db.close();
        }
    }

    public InputStream exportToStream(String auth) throws UnsupportedEncodingException {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select s.level, s.name, s.url, c.uid, c.pwd from site s left join certification c on s.id = c.siteId order by s.name", null);
            StringBuilder sb = new StringBuilder();
            while (cursor.moveToNext()) {
                sb.append(cursor.getString(cursor.getColumnIndex("level")));
                sb.append(splitChar);
                sb.append(cursor.getString(cursor.getColumnIndex("name")));
                sb.append(splitChar);
                sb.append(cursor.getString(cursor.getColumnIndex("url")));
                sb.append(splitChar);
                sb.append(cursor.getString(cursor.getColumnIndex("uid")));
                sb.append(splitChar);
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                sb.append(Aes.decrypt(auth, pwd));
                sb.append('\n');
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
            return inputStream;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            db.close();
        }
    }

    public void importFromStream(String auth, InputStream stream) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(stream, "utf-8");
            bufferedReader = new BufferedReader(reader);
            String currentSiteName = null;
            long currentSiteId = 0;
            String line = bufferedReader.readLine();
            while (line != null) {
                // Handle per line data
                String[] fields = line.split(String.valueOf(splitChar));
                int level;
                try {
                    level = Integer.parseInt(fields[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    level = 0;
                }
                String name = fields[1];
                String url = fields[2];
                String uid = fields[3];
                String pwd = fields[4];

                if (validateSite(name, level)) {
                    if (!name.equals(currentSiteName)) {
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("url", url);
                        values.put("level", level);
                        currentSiteId = db.insert("site", null, values);

                        currentSiteName = name;
                    }

                    if (validateAccount(uid, pwd)) {
                        Log.i("restore data", line);

                        ContentValues values1 = new ContentValues();
                        values1.put("siteId", currentSiteId);
                        values1.put("uid", uid);
                        values1.put("pwd", Aes.encrypt(auth, pwd));
                        db.insert("certification", null, values1);
                    }
                }

                line = bufferedReader.readLine();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            db.endTransaction();
            db.close();
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private boolean validateSite(String name, int level) {
        boolean result = true;
        if (name == null || name.equals("") || name.toLowerCase().equals("null")) {
            result = false;
        }
        if (level < 1 || level > 3) {
            result = false;
        }
        return result;
    }


    private boolean validateAccount(String uid, String pwd) {
        boolean result = true;
        if (uid == null || uid.equals("") || uid.toLowerCase().equals("null") || pwd == null || pwd.equals("") || pwd.toLowerCase().equals("null")) {
            result = false;
        }
        return result;
    }

    public class SitePageList {
        public List<Site> SiteList = new ArrayList<>();
        public int PageIndex = 0;
        public int TotalPageCount = 1;

        public boolean getHasMore() {
            return PageIndex < TotalPageCount - 1;
        }
    }
}
