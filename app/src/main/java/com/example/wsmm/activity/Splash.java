package com.example.wsmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.CategoryItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by abubaker on 19/03/2016.
 */
public class Splash extends AppCompatActivity {

    private DBClient dbClient;
    private static final int SPLASH_TIME = 2 * 1000;
    private List<String> categoryList;
    private CategoryItem categoryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
   /*     dbClient = new DBClient();
        categoryList = Arrays.asList(getResources().getStringArray(R.array.categories));

        for (int i =0; i<categoryList.size();i++){

            categoryItem = new CategoryItem();
            categoryItem.setCategoryItemName(categoryList.get(i));
            dbClient.saveCategoryList(categoryItem);
        }
*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                startActivity(new Intent(Splash.this, MainActivity.class));

                Splash.this.finish();

            }
        }, SPLASH_TIME);

    }

}
