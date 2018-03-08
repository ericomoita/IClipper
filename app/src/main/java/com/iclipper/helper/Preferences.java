package com.iclipper.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Erico on 23/02/2018.
 */

public class Preferences {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    final private String PREFERENCIA = "ArquivoPreferencia";
    final private String USERID = "USERID";
    private String userId;

    public Preferences(Context context){
        this.sharedPreferences = context.getSharedPreferences(PREFERENCIA,0);
        this.editor = sharedPreferences.edit();
        this.userId = sharedPreferences.getString(USERID,null);

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        editor.putString(USERID,userId);
        editor.commit();

    }
}
