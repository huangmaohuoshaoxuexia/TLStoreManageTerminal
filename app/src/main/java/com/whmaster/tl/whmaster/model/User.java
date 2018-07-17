package com.whmaster.tl.whmaster.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/6.
 */

public class User implements Serializable{
    private String mobile;
    private String username;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
