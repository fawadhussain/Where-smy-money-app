package com.example.wsmm.db;


import com.example.wsmm.model.Category;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by abubaker on 12/03/2016.
 */
public class DBClient {

    private Realm mRealm;

    public DBClient() {
        mRealm = Realm.getDefaultInstance();
    }

    public void saveTransaction(final Category category, Realm.Transaction.OnSuccess callback) {

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

               // realm.beginTransaction();

                if (category.getCategoryId() < 1) {
                    Number max = realm.where(Category.class).max("categoryId");
                    if (max == null) {
                        max = new Integer(0);
                    }
                    category.setCategoryId((int) (max.longValue() + 1));
                }
                realm.copyToRealmOrUpdate(category);
               // realm.commitTransaction();
                //close();

            }
        }, callback);

    }


    public RealmResults<Category> getAllItems() {

        RealmResults<Category> results = mRealm.where(Category.class).findAll();
        return results;
    }

    public RealmResults<Category> getRecords(){
        // Realm mRealm = Realm.getInstance(realmConfig);
        mRealm.beginTransaction();
       // RealmResults<Category> results = mRealm.where(Category.class).findAll();
        RealmResults<Category> results = mRealm.where(Category.class).findAll().distinct("stringDate");
        mRealm.commitTransaction();




        //mRealm.close();
        return results;
    }

    public RealmList<Category>  getParticularRecord(int day , int month , int year){

        mRealm.beginTransaction();
        RealmList<Category> records = new RealmList<Category>();

        RealmResults<Category> results = mRealm.where(Category.class).findAll();

        for (int i = results.size() -1; i >=0; i--) {

            if (convertDate(results.get(i).getDate(),day , month, year)){

                records.add(results.get(i));
            }

        }
        mRealm.commitTransaction();
        return records;

    }



    public void close() {
        mRealm.close();
    }

    public boolean isClosed() {
        return mRealm.isClosed();
    }

    private boolean convertDate(long date , int day , int month , int year) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year;


    }


    public ArrayList<Category> getAllDateRecordsList(){

        mRealm.beginTransaction();
        ArrayList<Category> records = new ArrayList<Category>();

        RealmResults<Category> results = mRealm.where(Category.class).findAll();

        for (int i = 0 ; i < results.size();i++) {

            for (int j = 0 ; j < results.size(); j++) {

                if (j == 0 && i == 0){
                    records.add(results.get(i));
                }else {

                    if (!convertMilliToDate(results.get(i).getDate() , results.get(j).getDate())){



                        records.add(results.get(i));

                    }
                }


            }


        }

        int size = records.size();
        mRealm.commitTransaction();
        return  records;

    }

    private boolean convertMilliToDate(Long date, Long otherDate){

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(date);

        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(otherDate);


        return dateCalendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH)
                && dateCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
                && dateCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);

    }
}
