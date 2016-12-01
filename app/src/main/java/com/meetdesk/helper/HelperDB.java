package com.meetdesk.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meetdesk.model.QueryProCat;
import com.meetdesk.model.QueryRegion;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class HelperDB extends SQLiteOpenHelper{

    QueryRegion region;
    QueryProCat procat;

    public HelperDB(Context context) {
        super(context, HelperEntityColumn.DB_NAME, null, HelperEntityColumn.DB_VERSION);
        region = new QueryRegion(this);
        procat = new QueryProCat(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        region.CreateDBRegion(db);
        procat.CreateDBCategory(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
