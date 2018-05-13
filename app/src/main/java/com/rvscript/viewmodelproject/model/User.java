package com.rvscript.viewmodelproject.model;

/**
 * Created by ga-mlsdiscovery on 4/5/18.
 */

public class User {
    //final says that it cannot be modified after it is created
    public final String login;

    public User(String login){
        this.login = login;
    }
}
