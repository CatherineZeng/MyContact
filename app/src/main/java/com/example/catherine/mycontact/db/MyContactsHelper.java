package com.example.catherine.mycontact.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.catherine.mycontact.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MyContactsHelper extends SQLiteOpenHelper {

    public  static final String CREAT_CONTACTS = "create table mycontacts("
            + "fname text,"
            + "fphoneNum text primary key,"
            + "femail text,"
            + "fcompany text,"
            + "fgroup text,"
            + "fpic BLOB)";
    private Context mContext;

    public MyContactsHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAT_CONTACTS);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
