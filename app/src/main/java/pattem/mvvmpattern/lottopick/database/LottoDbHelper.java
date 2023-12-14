package pattem.mvvmpattern.lottopick.database;

import static pattem.mvvmpattern.lottopick.database.LottoDatabase.CreateDB.LOTTOROUND;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class LottoDbHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION =1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mLottoDbHelper;
    private Context context;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(LottoDatabase.CreateDB._CTEATETBLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+LottoDatabase.CreateDB._LOTTOTBLNAME);
            onCreate(db);
        }

    }

    public LottoDbHelper (Context context){
        this.context=context;
    }

    public LottoDbHelper open() throws SQLException{
        mLottoDbHelper=new DatabaseHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        mDB=mLottoDbHelper.getWritableDatabase();
        return this;
    }

    //데이터 삽입(insert 문)
    public long insertColumn(String lottoRound,String no_1,String no_2,String no_3,
                             String no_4,String no_5,String no_6,String noBonus,String winnerPrize){
        ContentValues values= new ContentValues();
        values.put(LOTTOROUND,Integer.parseInt(lottoRound));
        values.put(LottoDatabase.CreateDB.NO_1,Integer.parseInt(no_1));
        values.put(LottoDatabase.CreateDB.NO_2,Integer.parseInt(no_2));
        values.put(LottoDatabase.CreateDB.NO_3,Integer.parseInt(no_3));
        values.put(LottoDatabase.CreateDB.NO_4,Integer.parseInt(no_4));
        values.put(LottoDatabase.CreateDB.NO_5,Integer.parseInt(no_5));
        values.put(LottoDatabase.CreateDB.NO_6,Integer.parseInt(no_6));
        values.put(LottoDatabase.CreateDB.NOBONUS,Integer.parseInt(noBonus));
        values.put(LottoDatabase.CreateDB.WINNERPRIZE,Long.parseLong(winnerPrize.replace("\"","")));

        return mDB.insert(LottoDatabase.CreateDB._LOTTOTBLNAME,null,values);

    }

    public Cursor getAllTBLName(){
//        return mDB.rawQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'InnerDatabase(SQLite).db'",null);
        return mDB.rawQuery("SELECT name FROM sqlite_master WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite_%' UNION ALL SELECT name FROM sqlite_temp_master WHERE type IN ('table', 'view') ORDER BY 1;",null);
    }

    //Select 문
    public Cursor selectColumns(){
        return mDB.query(LottoDatabase.CreateDB._LOTTOTBLNAME,null,null,null,
                null,null,null);
    }

    public Cursor sortColumn(String sort){
        return mDB.rawQuery( "SELECT * FROM "+LottoDatabase.CreateDB._LOTTOTBLNAME+" ORDER BY " + sort + " desc;", null);
    }

    public Cursor selectRoundNum(String round){
        return mDB.rawQuery( "SELECT * FROM "+LottoDatabase.CreateDB._LOTTOTBLNAME+" WHERE "+LOTTOROUND+" = '"+round+"'", null);
    }

    //drop 문
    public void dropTable(){
        mDB.execSQL("DROP TABLE IF EXISTS " + LottoDatabase.CreateDB._LOTTOTBLNAME);
//        return mDB.delete(LottoDatabase.CreateDB._LOTTOTBLNAME,null,null);
    }

    public void deleteRow(String name){
        mDB.execSQL("DELETE FROM "+LottoDatabase.CreateDB._LOTTOTBLNAME+" WHERE lottoRound ='"+name+"';");
    }


    public void create(){
        mLottoDbHelper.onCreate(mDB);
    }
    public void close(){
        mLottoDbHelper.close();
    }



}
