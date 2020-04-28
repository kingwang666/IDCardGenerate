package com.wang.wheel.model;

import androidx.annotation.NonNull;

/**
 * Created by wang
 * on 2016/4/12
 */
public class DataModel {
    /**
     * 地区id
     */
    public int id;
    /**
     * 地区名称
     */
    public String name;

    public DataModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DataModel(){

    }
}
