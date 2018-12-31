package com.leonelacs.tangochou;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SavedChouActivity extends AppCompatActivity {

    List<TangoItem> tangoChou = new ArrayList<TangoItem>();
    EditText chouInput = (EditText)findViewById(R.id.ChouInput);
    RecyclerView chouResult = (RecyclerView)findViewById(R.id.ChouResult);
    DBOHelper dbo = new DBOHelper(this, "saved_tango.db", null, 1);
    SQLiteDatabase chouDB = dbo.getWritableDatabase();

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    TangoAdapter adapter = new TangoAdapter(tangoChou);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_chou);

        adapter = new TangoAdapter(tangoChou);
        chouResult.setLayoutManager(layoutManager);
        chouResult.setAdapter(adapter);

        searchInChou("");

        chouInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    String curr = chouInput.getText().toString();
                    searchInChou(curr);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String curr = chouInput.getText().toString();
        searchInChou(curr);
        adapter.notifyDataSetChanged();
    }

    protected void searchInChou(String input) {
        Cursor cursor = chouDB.query("Chou", new String[]{"word", "autoSugg"}, "word like ?", new String[]{input+"%"}, null, null, "word");
        String word;
        String definition;
        TangoItem ti = new TangoItem();
        ti.setWord("xxTest");
        ti.setDefinition("xxTESTxx");
        tangoChou.clear();
        tangoChou.add(ti);
        if (cursor.moveToFirst()) {
            do {
                word = cursor.getString(cursor.getColumnIndex("word"));
                definition = cursor.getString(cursor.getColumnIndex("autoSugg"));
                TangoItem temp = new TangoItem();
                temp.setWord(word);
                temp.setDefinition(definition);
                tangoChou.add(temp);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }
}
