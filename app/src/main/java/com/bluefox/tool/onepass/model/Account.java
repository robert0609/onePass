package com.bluefox.tool.onepass.model;

public class Account {
    public long Id;
    public long SiteId;
    public String UserName;
    public String Password;

    public Account() {
        this.Id = 0;
        this.SiteId = 0;
    }

    public void validate() throws Exception {
        if (this.SiteId <= 0) {
            throw new Exception("SiteId is invalid!");
        }
        if (this.UserName == null || this.UserName.equals("")) {
            throw new Exception("UserName is empty!");
        }
        if (this.Password == null || this.Password.equals("")) {
            throw new Exception("Password is empty!");
        }
    }
}
