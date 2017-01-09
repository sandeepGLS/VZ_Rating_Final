package vztrack.gls.com.vztrack_user.utils;

import android.app.backup.BackupDataOutput;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sandeep on 16/3/16.
 */
public class SheredPref {

    public static String getLoginInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VALIDATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.LOGIN_CHECKED,"");
    }
    public static void setLoginInfo(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.VALIDATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.LOGIN_CHECKED,city);
        editor.commit();
    }


    public static String getSociety_Name(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYNAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOCIETYNAME,"");
    }
    public static void setSociety_Name(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOCIETYNAME,city);
        editor.commit();
    }


    public static String getFlat_No(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FLATNO, Context.MODE_PRIVATE);
        return sp.getString(Finals.FLATNO,"");
    }
    public static void setFlat_No(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.FLATNO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FLATNO,city);
        editor.commit();
    }


    public static String getUsername(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERNAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.USERNAME,"");
    }
    public static void setUSername(Context context,String username){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.USERNAME,username);
        editor.commit();
    }


    public static String getPassword(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PASSWORD, Context.MODE_PRIVATE);
        return sp.getString(Finals.PASSWORD,"");
    }
    public static void setPassword(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PASSWORD,city);
        editor.commit();
    }

    public static String getExecute(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.Exe, Context.MODE_PRIVATE);
        return sp.getString(Finals.Exe,"");
    }
    public static void setExecute(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.Exe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.Exe,city);
        editor.commit();
    }

    public static String getExecuteOffline(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        return sp.getString(Finals.Exe,"");
    }
    public static void setExecuteOffline(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.Exe,city);
        editor.commit();
    }

    public static String getFirstExecute(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FirstExe, Context.MODE_PRIVATE);
        return sp.getString(Finals.FirstExe,"");
    }
    public static void setFirstExecute(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.FirstExe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FirstExe,city);
        editor.commit();
    }

    public static String getDate(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE,"");
    }
    public static void setDate(Context context,String Sname){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE,Sname);
        editor.commit();
    }

    public static String getDateFor_Visitors(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VISITORS, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE,"");
    }
    public static void setDateFor_Visitors(Context context,String Sname){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VISITORS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE,Sname);
        editor.commit();
    }


    public static String getRun(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RUN, Context.MODE_PRIVATE);
        return sp.getString(Finals.RUN,"");
    }
    public static void setRun(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.RUN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.RUN,check);
        editor.commit();
    }

    public static String getNotification(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.NOTIFICATION,"");
    }
    public static void setNotification(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.NOTIFICATION,check);
        editor.commit();
    }

    public static String getSound(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOUND, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOUND,"");
    }
    public static void setSound(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOUND, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOUND,check);
        editor.commit();
    }

    public static String getVibration(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VIBRATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.VIBRATION,"");
    }
    public static void setVibration(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.VIBRATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.VIBRATION,check);
        editor.commit();
    }

    public static String getWingName(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.WING, Context.MODE_PRIVATE);
        return sp.getString(Finals.WING,"");
    }
    public static void setWingName(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.WING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.WING,check);
        editor.commit();
    }

    public static String getSocietyId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYID, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOCIETYID,"");
    }
    public static void setSocietyId(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOCIETYID,check);
        editor.commit();
    }

    public static String getFamilyId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FAMILYID, Context.MODE_PRIVATE);
        return sp.getString(Finals.FAMILYID,"");
    }
    public static void setFamilyId(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.FAMILYID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FAMILYID,check);
        editor.commit();
    }

    public static String getDateForApi(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR_API, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE_FOR_API,"");
    }
    public static void setDateForApi(Context context,String date){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE_FOR_API,date);
        editor.commit();
    }


    public static boolean getTutorialFlag(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.TUTORIAL_FLAG, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.TUTORIAL_FLAG,false);
    }
    public static void setTutorialFlag(Context context,Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.TUTORIAL_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.TUTORIAL_FLAG,flag);
        editor.commit();
    }
}