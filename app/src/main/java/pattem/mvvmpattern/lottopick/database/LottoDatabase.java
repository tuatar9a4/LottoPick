package pattem.mvvmpattern.lottopick.database;

import android.provider.BaseColumns;

public class LottoDatabase {

    public static final class CreateDB implements BaseColumns {
        public static final String UID = "_id";
        public static final String LOTTOROUND = "lottoRound";
        public static final String NO_1 = "no_1";
        public static final String NO_2 = "no_2";
        public static final String NO_3 = "no_3";
        public static final String NO_4 = "no_4";
        public static final String NO_5 = "no_5";
        public static final String NO_6 = "no_6";
        public static final String NOBONUS = "noBonus";
        public static final String WINNERPRIZE="winnerPrize";
        public static final String _LOTTOTBLNAME = "lottoTable";
        public static final String _CTEATETBLE = "create table if not exists " +
                _LOTTOTBLNAME +" (" +
                UID + " integer primary key autoincrement," +
                LOTTOROUND + " integer not null," +
                NO_1 + " integer not null," +
                NO_2 + " integer not null," +
                NO_3 + " integer not null," +
                NO_4 + " integer not null," +
                NO_5 + " integer not null," +
                NO_6 + " integer not null," +
                NOBONUS + " integer not null," +
                WINNERPRIZE+ " integer not null);";
    }

    public static final class CreateExpectDB implements BaseColumns{
        public static String _TBLNAME="";
        public static final String UID = "_id";
        public static final String LOTTOROUND = "lottoRound";
        public static final String NO_1 = "no_1";
        public static final String NO_2 = "no_2";
        public static final String NO_3 = "no_3";
        public static final String NO_4 = "no_4";
        public static final String NO_5 = "no_5";
        public static final String NO_6 = "no_6";
        public static final String WINNERPRIZE="winnerPrize";
        public static final String _CTEATETBLE = "create table if not exists " +
                _TBLNAME +" (" +
                UID + " integer primary key autoincrement," +
                LOTTOROUND + " integer not null," +
                NO_1 + " integer not null," +
                NO_2 + " integer not null," +
                NO_3 + " integer not null," +
                NO_4 + " integer not null," +
                NO_5 + " integer not null," +
                NO_6 + " integer not null," +
                WINNERPRIZE+ " integer not null);";

    }

}


