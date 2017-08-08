package com.andy.dolphin.database;

import android.content.Context;

/**
 * 数据库操作接口
 * <p>
 * Created by andy on 17-8-8.
 */

public class DbManager {
    private final String dbName = "dolphin_db";
    private static DbManager mDbManager;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (mDbManager == null) {
            mDbManager = new DbManager();
        }
        return mDbManager;
    }

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

}
