package com.example.wsmm.db;

import io.realm.Realm;

/**
 * Created by abubaker on 12/03/2016.
 */
public class DBClient {

    private Realm realm;

    public DBClient() {
        realm = Realm.getDefaultInstance();
    }


    public void close() {
        realm.close();
    }

    public boolean isClosed() {
        return realm.isClosed();
    }
}
