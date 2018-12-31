package com.leonelacs.tangochou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBOHelper extends SQLiteOpenHelper {

    public static final String CREATE_CHOU = "create table Chou ("
            + "id integer primary key autoincrement, "
            + "word text, "
            + "definition text)";
//            + "pronunciation text)";

    private Context dboContext;

    public DBOHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dboContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CHOU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
