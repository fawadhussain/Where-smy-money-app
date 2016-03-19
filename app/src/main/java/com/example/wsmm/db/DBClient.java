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

    public void saveTransaction(final Category category, Realm.Transaction.Callback callback) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                mRealm.beginTransaction();

                if (category.getCategoryId() < 1) {
                    Number max = mRealm.where(Category.class).max("categoryId");
                    if (max == null) {
                        max = new Integer(0);
                    }
                    category.setCategoryId((int) (max.longValue() + 1));
                }
                mRealm.copyToRealmOrUpdate(category);
                mRealm.commitTransaction();
                close();

            }
        }, callback);

    }


    public RealmResults<Category> getAllItems() {

        RealmResults<Category> results = mRealm.where(Category.class).findAll();
        return results;
    }

    public int getRecords(){
        // Realm mRealm = Realm.getInstance(realmConfig);
        mRealm.beginTransaction();
        RealmResults<Category> results = mRealm.where(Category.class).findAll();
        mRealm.close();
        return results.size();
    }



    public void close() {
        mRealm.close();
    }

    public boolean isClosed() {
        return mRealm.isClosed();
    }
}
