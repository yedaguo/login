package renren.io.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import renren.io.BaseApplication.BaseApplication;


/**
 *    本地存储
 */
public class SharedPreferencesUtil {
    static  String SP_NAME="MyDemo";
    /**
     * 保存数据到默认的sharedPreference
     *
     * @param key
     * @param data
     */
    public static void saveData(String key, Object data) {
//        saveData(Constant.SP_NAME, key, data);
      saveData(SP_NAME,key,data);
    }


    /**
     * 保存数据到sharedPreference
     *
     * @param spName
     * @param key
     * @param data
     */
    public static void saveData(String spName, String key, Object data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = BaseApplication.context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key     键
     * @param data    数据
     */
    public static void saveData(Context context, String spName, String key, Object data) {

        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.apply();
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String spName, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        // defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * @param spName
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(String spName, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = BaseApplication.context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        // defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;

    }

    /**
     * 使用默认的SharedPreference的空间名
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(String key, Object defValue) {
        return getData(SP_NAME, key, defValue);
    }

    /**
     * 清空数据
     * @param spName
     */
    public static void delData(String spName){
        SharedPreferences sharedPreferences = BaseApplication.context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }


}
