package com.meetdesk.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meetdesk.helper.HelperEntityColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class QueryRegion {

    SQLiteOpenHelper sqliteHelper;

    public QueryRegion(SQLiteOpenHelper sqliteHelper)
    {
        this.sqliteHelper = sqliteHelper;
    }

    public void setSqliteHelper(SQLiteOpenHelper helper)
    {
        this.sqliteHelper = helper;
    }

    public void AddRegion(EntityRegion region)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HelperEntityColumn.COL_REGION_ID, region.getRegionID());
        value.put(HelperEntityColumn.COL_REGION_NAME, region.getRegionName());
        value.put(HelperEntityColumn.COL_REGION_CODE, region.getRegionCode());
        db.insert(HelperEntityColumn.TBL_REGION, null, value);
        db.close();
    }

    public void UpdateRegion(EntityRegion region)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HelperEntityColumn.COL_REGION_ID, region.getRegionID());
        value.put(HelperEntityColumn.COL_REGION_NAME, region.getRegionName());
        value.put(HelperEntityColumn.COL_REGION_CODE, region.getRegionCode());
        db.update(HelperEntityColumn.TBL_REGION, value, HelperEntityColumn.COL_REGION_ID + " = ?", new String[]
                {String.valueOf(region.getRegionID())});
        db.close();
    }

    public List<EntityRegion> GetAllRegion()
    {
        List<EntityRegion> listRegion = new ArrayList<EntityRegion>();
        String query = "SELECT * FROM " + HelperEntityColumn.TBL_REGION +" ORDER BY " + HelperEntityColumn.COL_REGION_ID + " DESC";
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                EntityRegion region = new EntityRegion();
                region.setRegionID(Integer.parseInt(cursor.getString(0)));
                region.setRegionName(cursor.getString(1));
                region.setRegionCode(cursor.getString(2));
                listRegion.add(region);
            }while (cursor.moveToNext());
        }
        return listRegion;
    }

    public EntityRegion GetDetailRegion(int id)
    {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.query(HelperEntityColumn.TBL_REGION, new String[]{
                HelperEntityColumn.COL_REGION_ID, HelperEntityColumn.COL_REGION_NAME, HelperEntityColumn.COL_REGION_CODE
        }, HelperEntityColumn.COL_REGION_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        EntityRegion region = new EntityRegion();
        region.setRegionID(Integer.parseInt(cursor.getString(0)));
        region.setRegionName(cursor.getString(1));
        region.setRegionCode(cursor.getString(2));
        return region;
    }

    public void DeleteRegion(EntityRegion region)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperEntityColumn.TBL_REGION + " WHERE " + HelperEntityColumn.COL_REGION_ID + " = " + Integer.toString(region.getRegionID()));
        db.close();
    }

    public void ResetRegion()
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperEntityColumn.TBL_REGION);
        db.close();
    }

    public boolean CheckItemRegion(int id)
    {
        String query = "SELECT * FROM " + HelperEntityColumn.TBL_REGION + " WHERE " + HelperEntityColumn.COL_REGION_ID + " = " + Integer.toString(id);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void CreateDBRegion(SQLiteDatabase db)
    {
        String CREATE_REGION = "CREATE TABLE " + HelperEntityColumn.TBL_REGION +
                " (" + HelperEntityColumn.COL_REGION_ID + " INTEGER PRIMARY KEY, "+
                HelperEntityColumn.COL_REGION_NAME + " TEXT, "+
                HelperEntityColumn.COL_REGION_CODE + " TEXT) ";
        db.execSQL("DROP TABLE IF EXISTS " + HelperEntityColumn.TBL_REGION);
        db.execSQL(CREATE_REGION);
    }

}
