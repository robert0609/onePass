package com.bluefox.tool.onepass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bluefox.tool.onepass.model.Account;
import com.bluefox.tool.onepass.model.Site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store extends SQLiteOpenHelper {
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
        db.execSQL("create table site(id integer primary key autoincrement, name varchar(50), url varchar(100), level integer)");
        db.execSQL("create table certification(id integer primary key autoincrement, siteId integer, uid varchar(50), pwd varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Site> getSite(long id) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query("site", null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
            List<Site> result = new ArrayList<>();
            while (cursor.moveToNext()) {
                Site site = new Site();
                site.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                site.Name = cursor.getString(cursor.getColumnIndex("name"));
                site.Url = cursor.getString(cursor.getColumnIndex("url"));
                site.Level = Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));
                result.add(site);
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
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query("site", null, "name like '%?%' or url like '%?%'", new String[] { keyword }, null, null, null);
            List<Site> result = new ArrayList<>();
            Map<Long, Integer> siteIndexs = new HashMap<>();
            while (cursor.moveToNext()) {
                Site site = new Site();
                site.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                site.Name = cursor.getString(cursor.getColumnIndex("name"));
                site.Url = cursor.getString(cursor.getColumnIndex("url"));
                site.Level = Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));
                result.add(site);
                siteIndexs.put(site.Id, result.size() - 1);
            }

            if (withAccount) {
                StringBuilder sbSiteIds = new StringBuilder();
                if (!result.isEmpty()) {
                    sbSiteIds.append(result.get(0).Id);
                    for (int i = 1; i < result.size(); ++i) {
                        sbSiteIds.append(',').append(result.get(i).Id);
                    }
                }
                Cursor cursor1 = db.query("certification", null, "siteId in (?)", new String[]{sbSiteIds.toString()}, null, null, null);
                while (cursor1.moveToNext()) {
                    Account account = new Account();
                    account.Id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                    account.SiteId = Long.parseLong(cursor.getString(cursor.getColumnIndex("siteId")));
                    account.UserName = cursor.getString(cursor.getColumnIndex("uid"));
                    account.Password = cursor.getString(cursor.getColumnIndex("pwd"));
                    result.get(siteIndexs.get(account.SiteId)).AccountList.add(account);
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
}
