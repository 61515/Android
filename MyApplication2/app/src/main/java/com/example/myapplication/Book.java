package com.example.myapplication;

import org.litepal.crud.DataSupport;

/**
 * Created by 宋健 on 2018/7/27.
 */

public class Book extends DataSupport {
    private String imagepath;
    public String getimagepath()
    {
        return imagepath;
    }
    public void setImagepath(String imagepath)
    {
        this.imagepath=imagepath;
    }

}
