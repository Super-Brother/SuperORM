package com.wenchao.superorm;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wenchao
 * @date 2019/7/28.
 * @time 17:37
 * description：
 */
public class BaseDao<T> implements IBaseDao<T> {

    /**
     * 持有一个数据库的引用
     */
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 表名
     */
    private String tableName;
    /**
     * 要存入数据库的对象类型
     */
    private Class<T> entityClazz;
    /**
     * 是否已经初始化
     */
    private boolean isInit = false;
    /**
     * 缓存数据库对应的字段名
     */
    private HashMap<String, Field> cacheMap;

    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClazz) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClazz = entityClazz;
        if (!isInit) {
            //得到表名
            tableName = this.entityClazz.getAnnotation(DbTable.class).value();
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen() || tableName == null) {
                return isInit;
            }
            // 获取表字段
            cacheMap = new HashMap<>();
            initFields();
            //创建数据库表
            sqLiteDatabase.execSQL(getCreateTableSql());
            isInit = true;
        }
        return isInit;
    }

    private String getCreateTableSql() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");
        Field[] declaredFields = this.entityClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            //获取成员变量的类型
            Class type = field.getType();
            //读取成员变量注解的值
            String fieldName = field.getAnnotation(DbField.class).value();
            if (type == String.class) {
                stringBuffer.append(fieldName + " TEXT,");
            } else if (type == Integer.class) {
                stringBuffer.append(fieldName + " INTEGER,");
            } else if (type == Long.class) {
                stringBuffer.append(fieldName + " LONG,");
            }
        }
        //去掉最后的逗号
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        Log.e("sql", stringBuffer.toString());
        return stringBuffer.toString();
    }

    private void initFields() {
        Field[] declaredFields = this.entityClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            DbField db = field.getAnnotation(DbField.class);
            if (db != null) {
                String fieldName = db.value();
                cacheMap.put(fieldName, field);
            }
        }
    }

    @Override
    public long insert(T entity) {
        //获取一下要插入对象的值
        final Map<String, String> map = getValue(entity);
        ContentValues contentValues = getContentValues(map);
        return sqLiteDatabase.insert(tableName, null, contentValues);
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = map.get(key);
            if (null != value) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    private Map<String, String> getValue(T entity) {
        Map<String, String> map = new HashMap<>();
        //从缓存map中获取成员变量
        final Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            //拿到成员变量对象
            Field field = iterator.next();
            field.setAccessible(true);
            try {
                final Object o = field.get(entity);
                if (o == null) {
                    continue;
                }
                String value = o.toString();
                final String key = field.getAnnotation(DbField.class).value();
                if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                    map.put(key, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
