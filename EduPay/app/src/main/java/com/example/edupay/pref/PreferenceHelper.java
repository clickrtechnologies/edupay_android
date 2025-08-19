package com.example.edupay.pref;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper {

    public static final String COUNT_DOWN_TIMER = "count_down_timer";
    public static final String COUNT_DOWN_TIMER_5_SEC = "count_down_timer_5_sec";
    private static final String PREF_KEY_USER_ID = "pref_key_user_id";

    private static final String PREF_KEY_PAGEVIEW_NOTIF_COUNT = "pref_key_pageview_notif_count";
    private static final String PREF_KEY_USER_NAME= "pref_key_user_name";
    private static final String PREF_KEY_BIZ_NAME= "pref_key_biz_name";
    private static final String PREF_KEY_USER_COUNTRY_CODE= "pref_key_user_country_code";
    private static final String PREF_DEVICE_ID = "pref_device_id";
    private static final String PREF_IS_DEVICE_ID_SAVED = "pref_is_device_id_saved";
    public static String PREF_EXPERT = "pref_expert";


    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_AD_ALERTS = "PREF_KEY_CURRENT_USER_AD_ALERTS";
    private static final String PREF_KEY_CURRENT_USER_AD_STATUS = "PREF_KEY_CURRENT_USER_AD_STATUS";
    private static final String PREF_KEY_CURRENT_USER_AD_ANNOUNCEMENT = "PREF_KEY_CURRENT_USER_AD_ANNOUNCEMENT";








    public static final String PREFS_NAME = "GIRAFFE_PREFS";

    public static final String IS_DEVICE_CONNECTED = "IS_DEVICE_CONNECTED";
    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";


    public static final String LAST_LAT = "LAST_LAT";
    public static final String LAST_LONG = "LAST_LONG";

    public static final String USER_ID ="user_id" ;



    Context context;

    public static final String MANUAL_AUDIO_URL="manual_audio_url";
    public static final String MANUAL_AUDIO_DURATION="manual_audio_duration";
    public static final String MANUAL_AUDIO_DURATION_MILISECOND="manual_audio_duration_milisecond";

    public PreferenceHelper(Context context) {
        this.context=context;
    }

    public void save(String PREFS_KEY, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(PREFS_KEY, text); //3

        editor.commit(); //4
    }
    public void save(String PREFS_KEY, int text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putInt(PREFS_KEY, text); //3

        editor.commit(); //4
    }
    public void saveLong(String PREFS_KEY, long text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putLong(PREFS_KEY, text); //3

        editor.commit(); //4
    }
    public void save(String PREFS_KEY, boolean text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putBoolean(PREFS_KEY, text); //3

        editor.commit(); //4
    }
    public boolean getValue(String PREFS_KEY,boolean isValue) {
        SharedPreferences settings;
        boolean text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getBoolean(PREFS_KEY, false);
        return text;
    }
    public String getValue(String PREFS_KEY,String value) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY, value);
        return text;
    }
    public int getValue(String PREFS_KEY,int value) {
        SharedPreferences settings;
        int text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getInt(PREFS_KEY, value);
        return text;
    }
    public long getLongValue(String PREFS_KEY) {
        SharedPreferences settings;
        long text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getLong(PREFS_KEY, 0l);
        return text;
    }


    public void clearSharedPreference() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(String PREFS_KEY) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }

    public void saveImmediate(String PREFS_KEY, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY, text);
        editor.commit(); // commit forces the write before continuing
    }
}
