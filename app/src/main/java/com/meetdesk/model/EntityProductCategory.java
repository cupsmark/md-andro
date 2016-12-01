package com.meetdesk.model;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class EntityProductCategory {

    private int categoryID;
    private String categoryName;
    private String categoryDesc;
    private String categoryIcon;

    public EntityProductCategory()
    {
        this.categoryID = 0;
        this.categoryName = "";
        this.categoryDesc = "";
        this.categoryIcon = "";
    }

    public void setCategoryID(int id)
    {
        this.categoryID = id;
    }

    public int getCategoryID()
    {
        return categoryID;
    }

    public void setCategoryName(String name)
    {
        this.categoryName = name;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryDesc(String desc)
    {
        this.categoryDesc = desc;
    }

    public String getCategoryDesc()
    {
        return categoryDesc;
    }

    public void setCategoryIcon(String icon)
    {
        this.categoryIcon = icon;
    }

    public String getCategoryIcon()
    {
        return categoryIcon;
    }


}
