package com.leonelacs.tangochou;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TextView resultWord = (TextView)findViewById(R.id.ResultWord);
        TextView resultDefi = (TextView)findViewById(R.id.ResultDefi);
        Intent intent = getIntent();
        final String word = intent.getStringExtra("word");
        final String definition = intent.getStringExtra("definition");
        resultWord.setText(word);
        resultDefi.setText(definition);
        count = 0;
        DBOHelper dbo = new DBOHelper(this, "saved_tango.db", null, 1);
        final SQLiteDatabase chouDB = dbo.getWritableDatabase();
        Cursor cursor = chouDB.query("Chou", new String[] {"count(*) as cnt"}, "word=?", new String[] {word}, null, null, null, "1");
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("cnt"));
        }
        cursor.close();
        if (count != 1) {
            fab.setImageResource(R.drawable.big_penta_w);
        }
        else {
            fab.setImageResource(R.drawable.big_penta_y);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String showOnSnackbar = "已将此单词";
                if (count != 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("word", word);
                    contentValues.put("definition", definition);
                    chouDB.insert("Chou", null, contentValues);
                    count = 1;
                    fab.setImageResource(R.drawable.big_penta_y);
                    showOnSnackbar = showOnSnackbar + "加入单词帐";
                }
                else {
                    chouDB.delete("Chou", "word=?", new String[] {word});
                    count = 0;
                    fab.setImageResource(R.drawable.big_penta_w);
                    showOnSnackbar = showOnSnackbar + "从单词帐移除";
                }
                Snackbar.make(view, showOnSnackbar, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
