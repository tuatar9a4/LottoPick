package pattem.mvvmpattern.lottopick.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExpectDBHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION =1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mExpectDbHelper;
    private Context context;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createQuery);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+"d");
            onCreate(db);
        }

    }

    public ExpectDBHelper (Context context){
        this.context=context;
    }

    public ExpectDBHelper open() throws SQLException {
        mExpectDbHelper =new DatabaseHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        mDB= mExpectDbHelper.getWritableDatabase();
        return this;
    }

    //데이터 삽입(insert 문)
    public long insertColumn(String tblName,String lottoRound,String no_1,String no_2,String no_3,
                             String no_4,String no_5,String no_6,String winnerPrize){
        ContentValues values= new ContentValues();
        values.put(LottoDatabase.CreateExpectDB.LOTTOROUND,Integer.parseInt(lottoRound));
        values.put(LottoDatabase.CreateExpectDB.NO_1,Integer.parseInt(no_1));
        values.put(LottoDatabase.CreateExpectDB.NO_2,Integer.parseInt(no_2));
        values.put(LottoDatabase.CreateExpectDB.NO_3,Integer.parseInt(no_3));
        values.put(LottoDatabase.CreateExpectDB.NO_4,Integer.parseInt(no_4));
        values.put(LottoDatabase.CreateExpectDB.NO_5,Integer.parseInt(no_5));
        values.put(LottoDatabase.CreateExpectDB.NO_6,Integer.parseInt(no_6));
        values.put(LottoDatabase.CreateExpectDB.WINNERPRIZE,Long.parseLong(winnerPrize.replace("\"","")));

        return mDB.insert(tblName,null,values);

    }

    public Cursor getAllTBLName(){
//        return mDB.rawQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'InnerDatabase(SQLite).db'",null);
        return mDB.rawQuery("SELECT name FROM sqlite_master WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite_%' UNION ALL SELECT name FROM sqlite_temp_master WHERE type IN ('table', 'view') ORDER BY 1;",null);
    }

    //Select 문
    public Cursor selectColumns(String tblName){
        return mDB.query(tblName,null,null,null,
                null,null,null);
    }

    public Cursor sortColumn(String tblName,String sort){
        return mDB.rawQuery( "SELECT * FROM "+tblName+" ORDER BY " + sort + " desc;", null);
    }

    //drop 문
    public void dropTable(String tblName){
        mDB.execSQL("DROP TABLE IF EXISTS " + tblName);
//        return mDB.delete(LottoDatabase.CreateDB._LOTTOTBLNAME,null,null);
    }

    String createQuery;
    public void create(String tblName){
        createQuery="create table if not exists "+tblName+" ("+
                "_id integer primary key autoincrement,"+
                "lottoRound integer not null," +
                "no_1 integer not null," +
                "no_2 integer not null," +
                "no_3 integer not null," +
                "no_4 integer not null," +
                "no_5 integer not null," +
                "no_6 integer not null," +
                "winnerPrize integer not null);";

        mExpectDbHelper.onCreate(mDB);
//        mDB.rawQuery(createQuery,null);
    }

    public void close(){
        mExpectDbHelper.close();
    }

}
