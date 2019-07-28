package com.wenchao.superorm;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * @author wenchao
 * @date 2019/7/28.
 * @time 22:34
 * description：
 */
public class BaseDaoFactory {

    private static final BaseDaoFactory instance = new BaseDaoFactory();

    /**
     * 数据库存储的位置
     */
    private String sqliteDataBasePath;
    private SQLiteDatabase sqLiteDatabase;

    private BaseDaoFactory() {
        sqliteDataBasePath = "/data/data/com.wenchao.superorm/databases/supersql.db";
        File file = new File(sqliteDataBasePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //创建数据库对象 并且返回该数据库的操作对象
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDataBasePath, null);
    }

    public static BaseDaoFactory getInstance() {
        return instance;
    }

    public <T> BaseDao getBaseDao(Class<T> entityClazz) {
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        baseDao.init(sqLiteDatabase, entityClazz);
        return baseDao;
    }
}
