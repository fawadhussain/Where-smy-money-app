package com.example.wsmm.db;


import com.example.wsmm.model.Category;


import io.realm.Realm;
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



    public void close() {
        mRealm.close();
    }

    public boolean isClosed() {
        return mRealm.isClosed();
    }
}
