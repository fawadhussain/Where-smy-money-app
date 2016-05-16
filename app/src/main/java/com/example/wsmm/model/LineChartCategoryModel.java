package com.example.wsmm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abubaker on 15/05/2016.
 */
public class LineChartCategoryModel extends RealmObject{
    @PrimaryKey
    private int categoryChartId;
    private float price;
    private String date;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    private String catName;

    public int getId() {
        return categoryChartId;
    }

    public void setId(int id) {
        this.categoryChartId = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
