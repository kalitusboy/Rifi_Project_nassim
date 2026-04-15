package com.example.rifi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView listView;
    EditText search;
    ArrayList<Integer> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        search = findViewById(R.id.search);

        load("");

        search.addTextChangedListener(new android.text.TextWatcher(){
            public void onTextChanged(CharSequence s,int a,int b,int c){load(s.toString());}
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void afterTextChanged(android.text.Editable s){}
        });

        listView.setOnItemClickListener((p,v,pos,id)->{
            Intent i=new Intent(this,SurveyActivity.class);
            i.putExtra("id",ids.get(pos));
            startActivity(i);
        });
    }

    void load(String q){
        ids.clear();
        ArrayList<String> names=new ArrayList<>();

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM beneficiaries WHERE completed=0 AND name LIKE '%"+q+"%'",null);

        while(c.moveToNext()){
            ids.add(c.getInt(0));
            names.add(c.getString(2)+" | "+c.getString(1));
        }

        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,names));
    }
}
