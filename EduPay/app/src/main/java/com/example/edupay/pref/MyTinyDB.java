package com.example.edupay.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.edupay.model.parent_register.Parent;
import com.example.edupay.model.parent_register.ParentData;
import com.example.edupay.model.parent_register.PaymentMehod;
import com.example.edupay.model.payment.PaymentParentRequest;
import com.example.edupay.model.regstudents.StudentReg;
import com.example.edupay.model.school_register.School;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

//import com.google.gson.Gson;


public class MyTinyDB {
    private static final String SAMPLE = "MY_SAMPLE_EXAMPLE";
    static SharedPreferences.Editor editor;
    private static Encryptor encryptor;
    private static Decryptor decryptor;
    private static Context mcontext;
    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public MyTinyDB(Context appContext) {
        mcontext = appContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        editor = preferences.edit();
    }

    /**
     * Check if external storage is writable or not
     *
     * @return true if writable, false otherwise
     */
    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Check if external storage is readable or not
     *
     * @return true if readable, false otherwise
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * Decodes the Bitmap from 'path' and returns it
     *
     * @param path image path
     * @return the Bitmap from 'path'
     */
    public Bitmap getImage(String path) {
        Bitmap bitmapFromPath = null;
        try {
            bitmapFromPath = BitmapFactory.decodeFile(path);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return bitmapFromPath;
    }

    /**
     * Returns the String path of the last saved image
     *
     * @return string path of the last saved image
     */
    public String getSavedImagePath() {
        return lastImagePath;
    }

    /**
     * Saves 'theBitmap' into folder 'theFolder' with the name 'theImageName'
     *
     * @param theFolder    the folder path dir you want to save it to e.g "DropBox/WorkImages"
     * @param theImageName the name you want to assign to the image file e.g "MeAtLunch.png"
     * @param theBitmap    the image you want to save as a Bitmap
     * @return returns the full path(file system address) of the saved image
     */
    public String putImage(String theFolder, String theImageName, Bitmap theBitmap) {
        if (theFolder == null || theImageName == null || theBitmap == null)
            return null;

        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
        String mFullPath = setupFullPath(theImageName);

        if (!mFullPath.equals("")) {
            lastImagePath = mFullPath;
            saveBitmap(mFullPath, theBitmap);
        }

        return mFullPath;
    }

    /**
     * Saves 'theBitmap' into 'fullPath'
     *
     * @param fullPath  full path of the image file e.g. "Images/MeAtLunch.png"
     * @param theBitmap the image you want to save as a Bitmap
     * @return true if image was saved, false otherwise
     */
    public boolean putImageWithFullPath(String fullPath, Bitmap theBitmap) {
        return !(fullPath == null || theBitmap == null) && saveBitmap(fullPath, theBitmap);
    }

    /**
     * Creates the path for the image with name 'imageName' in DEFAULT_APP.. directory
     *
     * @param imageName name of the image
     * @return the full path of the image. If it failed to create directory, return empty string
     */
    private String setupFullPath(String imageName) {
        File mFolder = new File(Environment.getExternalStorageDirectory(), DEFAULT_APP_IMAGEDATA_DIRECTORY);

        if (isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder");
                return "";
            }
        }

        return mFolder.getPath() + '/' + imageName;
    }

    /**
     * Saves the Bitmap as a PNG file at path 'fullPath'
     *
     * @param fullPath path of the image file
     * @param bitmap   the image as a Bitmap
     * @return true if it successfully saved, false otherwise
     */
    private boolean saveBitmap(String fullPath, Bitmap bitmap) {
        if (fullPath == null || bitmap == null)
            return false;

        boolean fileCreated = false;
        boolean bitmapCompressed = false;
        boolean streamClosed = false;

        File imageFile = new File(fullPath);

        if (imageFile.exists())
            if (!imageFile.delete())
                return false;

        try {
            fileCreated = imageFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bitmapCompressed = bitmap.compress(CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
            bitmapCompressed = false;

        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    streamClosed = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    streamClosed = false;
                }
            }
        }

        return (fileCreated && bitmapCompressed && streamClosed);
    }

    public int getInt(String key) {
        byte[] bytes = getData(key, "");
        if (bytes != null) {
            return ByteBuffer.wrap(bytes).getInt();
        } else {
            return 0;
        }
//            return preferences.getInt(key, 0);
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Integers
     */
    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<Integer>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }

    /**
     * Get long value from SharedPreferences at 'key'. If key not found, return 'defaultValue'
     *
     * @param key          SharedPreferences key
     * @param defaultValue long value returned if key was not found
     * @return long value at 'key' or 'defaultValue' if key not found
     */
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    /**
     * Get double value from SharedPreferences at 'key'. If exception thrown, return 'defaultValue'
     *
     * @param key          SharedPreferences key
     * @param defaultValue double value returned if exception is thrown
     * @return double value at 'key' or 'defaultValue' if exception is thrown
     */
    public double getDouble(String key, double defaultValue) {
        String number = getString(key);

        try {
            return Double.parseDouble(number);

        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get parsed ArrayList of Double from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Double
     */
    public ArrayList<Double> getListDouble(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Double> newList = new ArrayList<Double>();

        for (String item : arrayToList)
            newList.add(Double.parseDouble(item));

        return newList;
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Longs
     */
    public ArrayList<Long> getListLong(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Long> newList = new ArrayList<Long>();

        for (String item : arrayToList)
            newList.add(Long.parseLong(item));

        return newList;
    }

    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     *
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    public String getString(String key) {
        byte[] bytes = getData(key, "");
        if (bytes != null) {
            return new String(bytes);
        } else {
            return "";
        }
//        return preferences.getString(key, "");
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        byte[] bytes = getData(key, "");
        if (bytes != null) {
            ArrayList<String> dummy = new ArrayList<String>(Arrays.asList(TextUtils.split(new String(bytes), "‚‗‚")));
            return dummy;
        } else {
            return new ArrayList<String>();
        }
    }

    public ArrayList<String> getReverseListString(String key) {
        ArrayList<String> dummy = new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
        Collections.reverse(dummy);
        return dummy;
    }

    public boolean getBoolean(String key) {
        byte[] bytes = getData(key, "");

        if (bytes != null) {
            return bytes[0] == 1;
        } else {
            return false;
        }
//        return preferences.getBoolean(key, false);
    }


//    public <T> T getObject(String key, Class<T> classOfT){
//
//        String json = getString(key);
//        Object value = new Gson().fromJson(json, classOfT);
//        if (value == null)
//            throw new NullPointerException();
//        return (T)value;
//    }


    // Put methods

    /**
     * Get parsed ArrayList of Boolean from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Boolean
     */
    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = getListString(key);
        ArrayList<Boolean> newList = new ArrayList<Boolean>();

        for (String item : myList) {
            if (item.equals("true")) {
                newList.add(true);
            } else {
                newList.add(false);
            }
        }

        return newList;
    }

    public ArrayList<Object> getListObject(String key, Class<?> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Object> objects = new ArrayList<Object>();

        for (String jObjString : objStrings) {
            Object value = gson.fromJson(jObjString, mClass);
            objects.add(value);
        }
        return objects;
    }

    /**
     * Put int value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value int value to be added
     */
    public void putInt(String key, int value) {
        checkForNullKey(key);
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(value);
        byte[] result = b.array();
        saveData(key, result);
//        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key' and save
     *
     * @param key     SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    public void putListInt(String key, ArrayList<Integer> intList) {
        checkForNullKey(key);
        Integer[] myIntList = intList.toArray(new Integer[intList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    /**
     * Put long value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value long value to be added
     */
    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Put ArrayList of Long into SharedPreferences with 'key' and save
     *
     * @param key      SharedPreferences key
     * @param longList ArrayList of Long to be added
     */
    public void putListLong(String key, ArrayList<Long> longList) {
        checkForNullKey(key);
        Long[] myLongList = longList.toArray(new Long[longList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply();
    }

    /**
     * Put float value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value float value to be added
     */
    public void putFloat(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }

    /**
     * Put double value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value double value to be added
     */
    public void putDouble(String key, double value) {
        checkForNullKey(key);
        putString(key, String.valueOf(value));
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    public void putListDouble(String key, ArrayList<Double> doubleList) {
        checkForNullKey(key);
        Double[] myDoubleList = doubleList.toArray(new Double[doubleList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value String value to be added
     */
    public void putString(String key, String value) {
        checkForNullKey(key);
        checkForNullValue(value);
        saveData(key, value.getBytes());
//        preferences.edit().putString(key, value).apply();
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        try {
            checkForNullKey(key);
            checkForNullList(stringList);
            String[] myStringList = stringList.toArray(new String[stringList.size()]);
            String finalJoinedValue = TextUtils.join("‚‗‚", myStringList);
            saveData(key, finalJoinedValue.getBytes());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void checkForNullList(ArrayList<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Put boolean value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value boolean value to be added
     */
    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        byte[] vOut = new byte[]{(byte) (value ? 1 : 0)};
        saveData(key, vOut);
//        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Put ArrayList of Boolean into SharedPreferences with 'key' and save
     *
     * @param key      SharedPreferences key
     * @param boolList ArrayList of Boolean to be added
     */
    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        checkForNullKey(key);
        ArrayList<String> newList = new ArrayList<String>();

        for (Boolean item : boolList) {
            if (item) {
                newList.add("true");
            } else {
                newList.add("false");
            }
        }

        putListString(key, newList);
    }

    /**
     * Put ObJect any type into SharedPrefrences with 'key' and save
     *
     * @param key SharedPreferences key
     * @param obj is the Object you want to put
     */
    public void putObject(String key, Object obj) {
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    public void putListObject(String key, ArrayList<Object> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }


    /**
     * Remove SharedPreferences item with 'key'
     *
     * @param key SharedPreferences key
     */
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Delete image file at 'path'
     *
     * @param path path of image file
     * @return true if it successfully deleted, false otherwise
     */
    public boolean deleteImage(String path) {
        return new File(path).delete();
    }

    /**
     * Clear SharedPreferences (remove everything)
     */
    public void clear() {
        preferences.edit().clear().apply();
    }

    public void clearSpecificList(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Retrieve all values from SharedPreferences. Do not modify collection return by method
     *
     * @return a Map representing a list of key/value pairs from SharedPreferences
     */
    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    /**
     * Register SharedPreferences change listener
     *
     * @param listener listener object of OnSharedPreferenceChangeListener
     */
    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister SharedPreferences change listener
     *
     * @param listener listener object of OnSharedPreferenceChangeListener to be unregistered
     */
    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    public void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }

    //    Encryption Methods start
    private byte[] getData(String key, String defValue) {
        try {
            try {
                byte[] bytes;
                decryptor = new Decryptor(mcontext, preferences);
                final String encryptedUser = preferences.getString(key, null);
                final String encryptedUserIV = preferences.getString(key + "_iv", null);
                /* If you throw null as I do, handle it when you implement it ! */
                if (encryptedUser == null) {
                    return null;
                }
                /* For API < 23 the IV stored is null on purpose */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (encryptedUserIV == null) return null;
                }
                bytes = decryptor.decryptData(SAMPLE, encryptedUser, encryptedUserIV);
                return bytes;
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void saveData(String key, byte[] value) {
        encryptor = new Encryptor(mcontext, preferences);
        try {
            encryptor = new Encryptor(mcontext, preferences);
            final String encryptedUser = encryptor.encryptText(SAMPLE, value);
            final String iv_user = encryptor.getIv();
            editor.putString(key, encryptedUser);
            editor.putString(key + "_iv", iv_user);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }





    public void putChildData(String key, ArrayList<StudentReg> setUpRequests) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (StudentReg obj : setUpRequests) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }


    public ArrayList<StudentReg> getChildData(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<StudentReg> objects = new ArrayList<>();

        for (String jObjString : objStrings) {
            StudentReg value = gson.fromJson(jObjString, StudentReg.class);
            objects.add(value);
        }
        return objects;
    }
    public void putPaymentMethod(String key, ArrayList<PaymentMehod> setUpRequests) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (PaymentMehod obj : setUpRequests) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }


    public ArrayList<PaymentMehod> getPaymentMethod(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<PaymentMehod> objects = new ArrayList<>();

        for (String jObjString : objStrings) {
            PaymentMehod value = gson.fromJson(jObjString, PaymentMehod.class);
            objects.add(value);
        }
        return objects;
    }

public void putSchoolDataList(String key, ArrayList<School> setUpRequests) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (School obj : setUpRequests) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }


    public ArrayList<School> getSchoolDataList(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<School> objects = new ArrayList<>();

        for (String jObjString : objStrings) {
            School value = gson.fromJson(jObjString, School.class);
            objects.add(value);
        }
        return objects;
    }

    public void putSchoolData(String key, School userModel) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        // for (ProfileSetUpRequest obj : setUpRequests) {
        objStrings.add(gson.toJson(userModel));
        // }
        putListString(key, objStrings);
    }

    public School getSchoolData(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        School value=null;
        // for (String jObjString : objStrings) {
        if(objStrings!=null&&objStrings.size()>0)
        {
            value = gson.fromJson(objStrings.get(0), School.class);
        }
        //objects.add(value);

        return value;
    }

    public void putParentData(String key, Parent userModel) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        // for (ProfileSetUpRequest obj : setUpRequests) {
        objStrings.add(gson.toJson(userModel));
        // }
        putListString(key, objStrings);
    }

    public Parent getParentData(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        Parent value=null;
        // for (String jObjString : objStrings) {
        if(objStrings!=null&&objStrings.size()>0)
        {
            value = gson.fromJson(objStrings.get(0), Parent.class);
        }
        //objects.add(value);

        return value;
    }

 public void putPaymentData(String key, PaymentParentRequest userModel) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        // for (ProfileSetUpRequest obj : setUpRequests) {
        objStrings.add(gson.toJson(userModel));
        // }
        putListString(key, objStrings);
    }

    public PaymentParentRequest getPaymentData(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        PaymentParentRequest value=null;
        // for (String jObjString : objStrings) {
        if(objStrings!=null&&objStrings.size()>0)
        {
            value = gson.fromJson(objStrings.get(0), PaymentParentRequest.class);
        }
        //objects.add(value);

        return value;
    }

 public void putPayStudentData(String key, StudentReg userModel) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        // for (ProfileSetUpRequest obj : setUpRequests) {
        objStrings.add(gson.toJson(userModel));
        // }
        putListString(key, objStrings);
    }

    public StudentReg getPayStudentData(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        StudentReg value=null;
        // for (String jObjString : objStrings) {
        if(objStrings!=null&&objStrings.size()>0)
        {
            value = gson.fromJson(objStrings.get(0), StudentReg.class);
        }
        //objects.add(value);

        return value;
    }


}

