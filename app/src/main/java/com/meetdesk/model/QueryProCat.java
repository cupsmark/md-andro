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
public class QueryProCat {

    SQLiteOpenHelper sqliteHelper;

    public QueryProCat(SQLiteOpenHelper sqliteHelper)
    {
        this.sqliteHelper = sqliteHelper;
    }

    public void AddCategory(EntityProductCategory category)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HelperEntityColumn.COL_PROCAT_ID, category.getCategoryID());
        value.put(HelperEntityColumn.COL_PROCAT_NAME, category.getCategoryName());
        value.put(HelperEntityColumn.COL_PROCAT_DESC, category.getCategoryDesc());
        value.put(HelperEntityColumn.COL_PROCAT_ICON, category.getCategoryIcon());
        db.insert(HelperEntityColumn.TBL_PROCAT, null, value);
        db.close();
    }

    public void UpdateCategory(EntityProductCategory category)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HelperEntityColumn.COL_PROCAT_ID, category.getCategoryID());
        value.put(HelperEntityColumn.COL_PROCAT_NAME, category.getCategoryName());
        value.put(HelperEntityColumn.COL_PROCAT_DESC, category.getCategoryDesc());
        value.put(HelperEntityColumn.COL_PROCAT_ICON, category.getCategoryIcon());
        db.update(HelperEntityColumn.TBL_PROCAT, value, HelperEntityColumn.COL_PROCAT_ID + " = ?", new String[]
                {String.valueOf(category.getCategoryID())});
        db.close();
    }

    public List<EntityProductCategory> GetAllCategory()
    {
        List<EntityProductCategory> listCategory = new ArrayList<EntityProductCategory>();
        String query = "SELECT * FROM " + HelperEntityColumn.TBL_PROCAT +" ORDER BY " + HelperEntityColumn.COL_PROCAT_ID + " DESC";
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                EntityProductCategory category = new EntityProductCategory();
                category.setCategoryID(Integer.parseInt(cursor.getString(0)));
                category.setCategoryName(cursor.getString(1));
                category.setCategoryDesc(cursor.getString(2));
                category.setCategoryIcon(cursor.getString(3));
                listCategory.add(category);
            }while (cursor.moveToNext());
        }
        return listCategory;
    }

    public EntityProductCategory GetDetailCategory(int id)
    {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.query(HelperEntityColumn.TBL_PROCAT, new String[]{
                HelperEntityColumn.COL_PROCAT_ID, HelperEntityColumn.COL_PROCAT_NAME, HelperEntityColumn.COL_PROCAT_DESC,
        HelperEntityColumn.COL_PROCAT_ICON}, HelperEntityColumn.COL_PROCAT_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        EntityProductCategory category = new EntityProductCategory();
        category.setCategoryID(Integer.parseInt(cursor.getString(0)));
        category.setCategoryName(cursor.getString(1));
        category.setCategoryDesc(cursor.getString(2));
        category.setCategoryIcon(cursor.getString(3));
        return category;
    }

    public void DeleteCategory(EntityProductCategory category)
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperEntityColumn.TBL_PROCAT + " WHERE " + HelperEntityColumn.COL_PROCAT_ID + " = " + Integer.toString(category.getCategoryID()));
        db.close();
    }

    public void ResetCategory()
    {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + HelperEntityColumn.TBL_PROCAT);
        db.close();
    }

    public boolean CheckItemCategory(int id)
    {
        String query = "SELECT * FROM " + HelperEntityColumn.TBL_PROCAT + " WHERE " + HelperEntityColumn.COL_PROCAT_ID + " = " + Integer.toString(id);
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

    public void CreateDBCategory(SQLiteDatabase db)
    {
        String CREATE_PROCAT = "CREATE TABLE " + HelperEntityColumn.TBL_PROCAT +
                " (" + HelperEntityColumn.COL_PROCAT_ID + " INTEGER PRIMARY KEY, "+
                HelperEntityColumn.COL_PROCAT_NAME + " TEXT, "+
                HelperEntityColumn.COL_PROCAT_DESC + " TEXT, "+
                HelperEntityColumn.COL_PROCAT_ICON +" TEXT) ";
        db.execSQL("DROP TABLE IF EXISTS " + HelperEntityColumn.TBL_PROCAT);
        db.execSQL(CREATE_PROCAT);
    }

}
