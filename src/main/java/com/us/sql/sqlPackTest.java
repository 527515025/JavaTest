package com.us.sql;

import org.apache.commons.collections4.map.HashedMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by yangyibo on 2018/12/17.
 */
public class sqlPackTest {
    public static void main(String[] args) {
        //LogMap 需要删除的
        Map<String, Object> statisticalInfoMap = new HashedMap();
        statisticalInfoMap.put("id", 2123);
        statisticalInfoMap.put("sys", "sys1");
        statisticalInfoMap.put("plat", null);
        statisticalInfoMap.put("sysver", null);
        statisticalInfoMap.put("mfo", "mfo1");
        statisticalInfoMap.put("mfov", "mfov1");
        try {
            sql("pn", statisticalInfoMap, "pns");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int sql(String tableName, Map<String, Object> valueMap, String dataBase) throws SQLException {

        Set<String> Set = valueMap.keySet();
        List<String> keyNullSet = new ArrayList<String>();
        Set.stream().forEach(x -> {
            if (null == valueMap.get(x)) {
                keyNullSet.add(x);
            }
        });
        keyNullSet.stream().forEach(x -> {
            valueMap.remove(x);
        });

        Set<String> keySet = valueMap.keySet();
        StringBuffer columnSql = new StringBuffer();
        StringBuffer unknownMarkSql = new StringBuffer();
        StringBuffer updateSql = new StringBuffer();
        Object[] bindArgs = new Object[valueMap.size()];

        keySet.stream().forEach(x -> {
            columnSql.append(x);
            columnSql.append(",");
            unknownMarkSql.append("?");
            unknownMarkSql.append(",");
            updateSql.append(x);
            updateSql.append("=");
            updateSql.append("?");
            updateSql.append(",");
        });
        columnSql.deleteCharAt(columnSql.length() - 1);
        unknownMarkSql.deleteCharAt(unknownMarkSql.length() - 1);
        updateSql.deleteCharAt(updateSql.length() - 1);

        /**开始拼插入的sql语句**/
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(columnSql);
        sql.append(" )  VALUES (");
        sql.append(unknownMarkSql);
        sql.append(" )");
        sql.append(" ON DUPLICATE KEY UPDATE ");
        sql.append(updateSql);
        return executeUpdate(sql.toString(), bindArgs, "sdf");
    }

    public static int executeUpdate(String sql, Object[] bindArgs, String dataBase) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql.toString());
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            if (bindArgs != null) {
                /**绑定参数设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
                /**执行sql**/
                affectRowCount = preparedStatement.executeUpdate();
            } else {
                /**执行查询*/
                preparedStatement.executeQuery();
            }

            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return affectRowCount;
    }
}
