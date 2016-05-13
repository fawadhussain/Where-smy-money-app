package com.example.wsmm.db;


import android.content.Context;
import android.util.Log;

import com.example.wsmm.model.Category;
import com.example.wsmm.model.CategoryItem;
import com.example.wsmm.util.GeneralUtils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by abubaker on 12/03/2016.
 */
public class DBClient {

    private Realm mRealm;

    public DBClient() {
        mRealm = Realm.getDefaultInstance();
    }


    public RealmResults<CategoryItem> getCategoryList() {

        mRealm.beginTransaction();

        RealmResults<CategoryItem> results = mRealm.where(CategoryItem.class).findAll();
        mRealm.commitTransaction();
        return results;
    }


    public void saveCategoryList(CategoryItem item) {

        mRealm.beginTransaction();

        if (item.getCategoryId() < 1) {
            Number max = mRealm.where(CategoryItem.class).max("categoryId");
            if (max == null) {
                max = new Integer(0);
            }
            item.setCategoryId((int) (max.longValue() + 1));
        }
        mRealm.copyToRealmOrUpdate(item);

        mRealm.commitTransaction();


    }


    public void updateCategoryItem(final CategoryItem category, Realm.Transaction.OnSuccess callback) {

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                CategoryItem record = new CategoryItem();
                record = realm.where(CategoryItem.class).equalTo("categoryId", category.getCategoryId()).findFirst();
                record.setCategoryItemName(category.getCategoryItemName());
                realm.copyToRealmOrUpdate(category);


            }
        }, callback);


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

    public RealmResults<Category> getRecords() {
        mRealm.beginTransaction();
        RealmResults<Category> results = mRealm.where(Category.class).findAll().distinct("stringDate");
        results.sort("stringDate", Sort.ASCENDING);

        mRealm.commitTransaction();
        return results;
    }

    public RealmList<Category> getParticularRecord(int day, int month, int year) {

        mRealm.beginTransaction();
        RealmList<Category> records = new RealmList<Category>();

        RealmResults<Category> results = mRealm.where(Category.class).findAll();

        for (int i = results.size() - 1; i >= 0; i--) {

            if (convertDate(results.get(i).getDate(), day, month, year)) {

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

    private boolean convertDate(long date, int day, int month, int year) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year;


    }


    public ArrayList<Category> getAllDateRecordsList() {

        mRealm.beginTransaction();
        ArrayList<Category> records = new ArrayList<Category>();

        RealmResults<Category> results = mRealm.where(Category.class).findAll();

        for (int i = 0; i < results.size(); i++) {

            for (int j = 0; j < results.size(); j++) {

                if (j == 0 && i == 0) {
                    records.add(results.get(i));
                } else {

                    if (!convertMilliToDate(results.get(i).getDate(), results.get(j).getDate())) {


                        records.add(results.get(i));

                    }
                }


            }


        }

        int size = records.size();
        mRealm.commitTransaction();
        return records;

    }

    private boolean convertMilliToDate(Long date, Long otherDate) {

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(date);

        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(otherDate);


        return dateCalendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH)
                && dateCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
                && dateCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);

    }

    public void deleteTransaction(final int categoryId, Realm.Transaction.OnSuccess callback) {

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmQuery<Category> realmQuery = realm.where(Category.class);
                realmQuery.equalTo("categoryId", categoryId);
                realmQuery.findAll().clear();

            }
        }, callback);


    }


    public Category getResultFromId(int categoryId) {

        mRealm.beginTransaction();
        Category record = new Category();
        record = mRealm.where(Category.class).equalTo("categoryId", categoryId).findFirst();
        mRealm.commitTransaction();
        return record;


    }


    public void updateTransaction(final Category category, Realm.Transaction.OnSuccess callback) {

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Category record = new Category();
                record = realm.where(Category.class).equalTo("categoryId", category.getCategoryId()).findFirst();
                record.setStringDate(category.getStringDate());
                record.setDate(category.getDate());
                record.setCategoryName(category.getCategoryName());
                record.setCategoryTitle(category.getCategoryTitle());
                record.setImagePath(category.getImagePath());
                record.setPrice(category.getPrice());


            }
        }, callback);


    }


    public void exportDataToCSV(final Context context, Realm.Transaction.OnSuccess callback) {


        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Category> results = realm.where(Category.class).findAll();


                if (results.size() > 0) {

                    try {
                        FileOutputStream outputStream = new FileOutputStream(GeneralUtils.createCSVFile(context), false);
                        OutputStreamWriter osw = new OutputStreamWriter(outputStream);


                        String csvData = "categoryId,categoryName,categoryTitle,price,date,stringDate,imagePath";
                        csvData += "\n";


                        for (int i = 0; i < results.size(); i++) {

                            csvData += results.get(i).getCategoryId() + "," + results.get(i).getCategoryName() + "," + results.get(i).getCategoryTitle() + "," +
                                    results.get(i).getPrice() + "," + results.get(i).getDate() + "," + results.get(i).getStringDate() + "," +
                                    results.get(i).getImagePath();

                            csvData += "\n";
                        }

                        Log.d("csvFile", csvData);

                        osw.write(csvData);
                        osw.flush();
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, callback);


    }


    public RealmResults<Category> getLastSevenDaysData() {
        mRealm.beginTransaction();


        RealmResults<Category> results = mRealm.where(Category.class).between("date",
                GeneralUtils.getPreviousDate(7), GeneralUtils.getCurrentSystemDate())
                .findAll();

        mRealm.commitTransaction();


        return results;


    }


    public RealmResults<Category> getLastMonthDaysData() {

        mRealm.beginTransaction();


        RealmResults<Category> results = mRealm.where(Category.class).between("date",
                GeneralUtils.getPreviousDate(30), GeneralUtils.getCurrentSystemDate())
                .findAll();

        mRealm.commitTransaction();


        return results;

    }


    public RealmResults<Category> getCustomDateData(long from, long to) {

        mRealm.beginTransaction();


        RealmResults<Category> results = mRealm.where(Category.class).between("date",
                from, to).findAll();

        mRealm.commitTransaction();


        return results;

    }


}
