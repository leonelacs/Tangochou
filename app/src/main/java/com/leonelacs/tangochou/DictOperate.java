package com.leonelacs.tangochou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DictOperate {
    private final String DB_PATH = "/data/data/com.leonelacs.tangochou/databases/";
    private final String SRC_DB_FILENAME = "ms_bing_dict.db";
    private Context context;
    public DictOperate(Context context) {
        this.context = context;
    }
    public SQLiteDatabase openDB() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String srcDBName = DB_PATH + SRC_DB_FILENAME;

        try {
            InputStream inputStream = context.getResources().getAssets().open("ms_bing_dict.db");
            FileOutputStream fos = new FileOutputStream(srcDBName);
            byte[] buf = new byte[1024 * 8];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.close();
            inputStream.close();
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(srcDBName, null);
            return database;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
