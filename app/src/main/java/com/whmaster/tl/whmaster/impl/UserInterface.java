package com.whmaster.tl.whmaster.impl;

/**
 * Created by admin on 2017/10/26.
 */

public interface UserInterface {
    public void login(String username, String password,String type);
    public void loginToken(String username, String password,String type);
    public void islogin();
    public void loginout();
    public void perms();

}
