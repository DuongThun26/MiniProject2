package com.example.miniproject2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username) {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(Constants.KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(Constants.KEY_USERNAME, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
