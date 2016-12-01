package com.meetdesk.model;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class EntityRegion {

    private int regionID;
    private String regionName;
    private String regionCode;

    public EntityRegion()
    {
        regionID = 0;
        regionName = "";
        regionCode = "";
    }

    public void setRegionID(int id)
    {
        this.regionID = id;
    }

    public int getRegionID()
    {
        return regionID;
    }

    public void setRegionName(String name)
    {
        this.regionName = name;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionCode(String code)
    {
        this.regionCode = code;
    }

    public String getRegionCode()
    {
        return regionCode;
    }




}
