package com.bluefox.tool.onepass.model;

import java.util.ArrayList;
import java.util.List;

public class Site {
    public long Id;
    public String Name;
    public String Url;
    public int Level;
    public List<Account> AccountList;

    public Site() {
        this.Id = 0;
        this.AccountList = new ArrayList<>();
    }

    public void validate() throws Exception {
        if (this.Name == null || this.Name.equals("")) {
            throw new Exception("Name is empty!");
        }
        if (this.Url == null || this.Url.equals("")) {
            throw new Exception("Url is empty!");
        }
        if (this.Level <= 0) {
            throw new Exception("Level is invalid!");
        }
    }
}
