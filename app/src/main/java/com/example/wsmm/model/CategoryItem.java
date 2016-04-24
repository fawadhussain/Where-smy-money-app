package com.example.wsmm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CategoryItem extends RealmObject{

    @PrimaryKey
    private int categoryId;
    private String categoryItemName;
    private String categoryTitle;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryItemName() {
        return categoryItemName;
    }

    public void setCategoryItemName(String categoryName) {
        this.categoryItemName = categoryName;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
