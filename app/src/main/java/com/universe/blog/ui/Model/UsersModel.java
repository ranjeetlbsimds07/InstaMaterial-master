package com.universe.blog.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ranjeet on 2/3/17.
 */

public class UsersModel {

    public String uid;
    public String firstName;
    public String lastName;


    public boolean isDone;


    public UsersModel() {

    }

    public UsersModel(String uid, String firstName, String lastName, boolean isDone) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDone = isDone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("isDone", isDone);

        return result;
    }
}