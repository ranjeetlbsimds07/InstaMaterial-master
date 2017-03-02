package com.universe.blog;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ranjeet on 2/3/17.
 */

public class UserSharePreference {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Context context;

    private String USERINFODETAILS = "USERINFODETAILS";
    private String FIRSTNAME = "FIRSTNAME";
    private String LASTTNAME = "LASTTNAME";
    private String UID = "UID";

    public UserSharePreference(Context context){
        this.context=context;
        sharedPreferences = context.getSharedPreferences(USERINFODETAILS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
    }


//first Name
    public void setFirstName(String firstName){
        editor.putString(FIRSTNAME, firstName);
        editor.commit();
    }

    public String getFirstName(){
        return sharedPreferences.getString(FIRSTNAME, null);
    }

    // last Name
    public void setLirstName(String lastName){
        editor.putString(LASTTNAME, lastName);
        editor.commit();
    }

    public String getLirstName(){
        return sharedPreferences.getString(LASTTNAME, null);
    }
//UID
    public void setUid(String uid){
        editor.putString(UID, uid);
        editor.commit();
    }

    public String getUid(){
        return sharedPreferences.getString(UID, null);
    }



}
