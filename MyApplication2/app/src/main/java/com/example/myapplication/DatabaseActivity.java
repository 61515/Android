package com.example.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        ////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //////////////////////////////////
        //在第二个活动中的textview控件中设置
        //数据库中的数据databasetxt
        TextView textView=findViewById(R.id.databaseview);
        StringBuilder txt;
        txt=findData();
        textView.setText(txt);


        //////////////////////////////////
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public StringBuilder findData()
    {
        StringBuilder sh=new StringBuilder();
        List<Book> books=DataSupport.findAll(Book.class);
        for(Book book:books)
        {
            sh.append(book.getimagepath()).append('\n');
        }
        return sh;
    }

}
