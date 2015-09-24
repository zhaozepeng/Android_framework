package com.android.libcore.database;

import java.util.ArrayList;

/**
 * Description: 所有数据库DB类应该声明一个枚举类，用来封装该数据库中的所有表名，
 * 和表的相关列名，这样将表名和列名封装起来，外部使用封装之后的枚举进行操作
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-22
 */
public interface IBaseDBTable {
    /**
     * 用来返回表名
     */
    String getTableName();

    /**
     * 用来返回该表中所有的列名
     */
    ArrayList<String> getTableColumns();
}
