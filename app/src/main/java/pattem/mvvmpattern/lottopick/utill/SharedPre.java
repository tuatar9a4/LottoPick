package pattem.mvvmpattern.lottopick.utill;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPre {
    private static Context mAppContext;

    private static final String TEST="test";
    private static final String LOTTOINFOCOUNT="lotto_info_count";
    private static final String RECENTROUND="RECENTROUND";
    private static final String LIMITCOUNT="limitCount";
    private static final String MANYORMIN="manyOrMin";

    private SharedPre(){

    }

    public static void init(Context context){
        mAppContext=context;
    }

    private static SharedPreferences getSharedPreference(){
        return mAppContext.getSharedPreferences("DW_Lotto_Data",Context.MODE_PRIVATE);
    }

    public static void setTest(int a){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putInt(TEST,a).apply();
    }

    public static int getTEST() {
        return getSharedPreference().getInt(TEST,0);
    }

    //정보가 몇개인지
    public static void setINFOCOUNT(int a){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putInt(LOTTOINFOCOUNT,a).apply();
    }

    public static int getINFOCOUNT() {
        return getSharedPreference().getInt(LOTTOINFOCOUNT,0);
    }


    //최근 몇 회
    public static void setRECENTROUND(int a){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putInt(RECENTROUND,a).apply();
    }

    public static int getRECENTROUND() {
        return getSharedPreference().getInt(RECENTROUND,0);
    }

    //몇 개
    public static void setLIMITCOUNT(int a){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putInt(LIMITCOUNT,a).apply();
    }

    public static int getLIMITCOUNT() {
        return getSharedPreference().getInt(LIMITCOUNT,0);
    }


    //이상 이하
    public static void setMANYORMIN(boolean a){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putBoolean(MANYORMIN,a).apply();
    }

    public static boolean getMANYORMIN() {
        return getSharedPreference().getBoolean(MANYORMIN,false);
    }

}
