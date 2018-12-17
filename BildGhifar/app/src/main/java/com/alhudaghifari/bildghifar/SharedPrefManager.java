package com.alhudaghifari.bildghifar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Alhudaghifari on 11/9/2017.
 * Kelas yang digunakan untuk mengatur session dengan
 * memainkan sharedpreference
 */
public class SharedPrefManager extends Application {
    // LogCat tag
    private static String TAG = SharedPrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = Constant.this_app;

    private static final String KEY_IS_IMG_ANALYZED = "isImgAnlyzd";
    private static final String KEY_TOTAL_CHAIN_CODE = "totalchaincode";
    private static final String KEY_FACE_RECOGNITION = "facename";

    /**
     * constructor session manager wajib mengirim context aktivitas
     * @param context context activity
     */
    public SharedPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void clearAllSharedPref() {
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void setAnalyzed(boolean isAnalyzed) {
        editor = pref.edit();
        editor.putBoolean(KEY_IS_IMG_ANALYZED, isAnalyzed);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isAnalyzed() {
        return pref.getBoolean(KEY_IS_IMG_ANALYZED, false);
    }

    public void saveChainCode(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);

        // commit changes
        editor.commit();

        Log.d(TAG, "chain code saved with key : " + key);
    }

    public String getChainCode(String key) {
        return pref.getString(key, "kosong");
    }

    public void setKeyTotalChainCode(int tot) {
        editor = pref.edit();
        editor.putInt(KEY_TOTAL_CHAIN_CODE, tot);

        // commit changes
        editor.commit();

        Log.d(TAG, "total chain code modified!");
    }

    public int getTotalChainCode() {
        return pref.getInt(KEY_TOTAL_CHAIN_CODE, 0);
    }

    public void setFaceName(String value) {
        editor = pref.edit();
        editor.putString(KEY_FACE_RECOGNITION, value);

        // commit changes
        editor.commit();

        Log.d(TAG, "face name modified!");
    }

    public String getFaceName() {
        return pref.getString(KEY_FACE_RECOGNITION, "");
    }

}