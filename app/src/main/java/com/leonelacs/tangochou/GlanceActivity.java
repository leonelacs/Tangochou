package com.leonelacs.tangochou;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GlanceActivity extends AppCompatActivity {

    List<TangoItem> tangoChou = new ArrayList<TangoItem>();


    DBOHelper dbo = new DBOHelper(this, "saved_tango.db", null, 1);


    LinearLayoutManager layoutManagerChou = new LinearLayoutManager(this);
    TangoAdapter adapterChou;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    findViewById(R.id.SearchPage).setVisibility(View.VISIBLE);
                    findViewById(R.id.SavedChouPage).setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    findViewById(R.id.SearchPage).setVisibility(View.INVISIBLE);
                    findViewById(R.id.SavedChouPage).setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    findViewById(R.id.SearchPage).setVisibility(View.INVISIBLE);
                    findViewById(R.id.SavedChouPage).setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glance);
        EditText chouInput = (EditText)findViewById(R.id.ChouInput);
        RecyclerView chouResult = (RecyclerView)findViewById(R.id.ChouResult);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DictOperate dictOperate = new DictOperate(getBaseContext());
        final SQLiteDatabase bingDict = dictOperate.openDB();
        final EditText searchInput = (EditText)findViewById(R.id.SearchInput);
        RecyclerView searchResult = (RecyclerView)findViewById(R.id.SearchResult);
        final List<TangoItem> tangoList = new ArrayList<TangoItem>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResult.setLayoutManager(layoutManager);
        final TangoAdapter adapter = new TangoAdapter(tangoList);
        searchResult.setAdapter(adapter);

        searchInChou("");
        adapterChou = new TangoAdapter(tangoChou);
        chouResult.setLayoutManager(layoutManagerChou);
        chouResult.setAdapter(adapterChou);




        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    String curr = searchInput.getText().toString();
                    Cursor first = bingDict.query("Dict", new String[] {"word", "autoSugg"}, "word = ?", new String[] {curr}, null, null, null, "1");
                    Cursor cursor = bingDict.query("Dict", new String[]{"word", "autoSugg", "freq"}, "word like ? and word <> ?", new String[]{curr+"%", curr}, null, null, "freq desc");

                    tangoList.clear();

                    if (first.moveToFirst()) {
                        TangoItem firstTango = new TangoItem();
                        String word = first.getString(first.getColumnIndex("word"));
                        String definition = first.getString(first.getColumnIndex("autoSugg"));
                        firstTango.setWord(word);
                        firstTango.setDefinition(definition);
                        tangoList.add(firstTango);
                        if (cursor.moveToFirst()) {
                            do {
                                word = cursor.getString(cursor.getColumnIndex("word"));
                                definition = cursor.getString(cursor.getColumnIndex("autoSugg"));
                                TangoItem temp = new TangoItem();
                                temp.setWord(word);
                                temp.setDefinition(definition);
                                tangoList.add(temp);
                            }
                            while (cursor.moveToNext());
                        }
                    }

                    adapter.notifyDataSetChanged();
                    cursor.close();
                    first.close();
                    return true;
                }
                return false;
            }
        });

        chouInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            EditText chouInput = (EditText)findViewById(R.id.ChouInput);
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    String curr = chouInput.getText().toString();
                    searchInChou(curr);
                    adapterChou.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText chouInput = (EditText)findViewById(R.id.ChouInput);
        String curr = chouInput.getText().toString();
        searchInChou(curr);
        adapterChou.notifyDataSetChanged();
    }

    protected void searchInChou(String input) {
        SQLiteDatabase chouDB = dbo.getWritableDatabase();
        Cursor cursor = chouDB.query("Chou", new String[]{"word", "definition"}, "word like ?", new String[]{input+"%"}, null, null, "word");
        String word;
        String definition;
        tangoChou.clear();
        if (cursor.moveToFirst()) {
            do {
                word = cursor.getString(cursor.getColumnIndex("word"));
                definition = cursor.getString(cursor.getColumnIndex("definition"));
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
