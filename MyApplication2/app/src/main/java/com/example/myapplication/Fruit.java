package com.example.myapplication;

/**
 * Created by 宋健 on 2018/7/25.
 */

public class Fruit {
    private String name;
    private int imageId;
    public Fruit(String name,int imageId)
    {
        this.name=name;
        this.imageId=imageId;
    }
    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return imageId;
    }

}
