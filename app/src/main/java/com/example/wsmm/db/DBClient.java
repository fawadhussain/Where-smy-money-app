package com.example.wsmm.db;


import com.example.wsmm.model.Category;


import java.util.Calendar;

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
        RealmResults<Category> results = mRealm.where(Category.class).findAll();
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
}
