package com.dss.web;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: DSS
 * Date: 2018/12/10
 * Time: 17:17
 * Description: Database Manager
 */
public class DBManager {
    private static DataSource sDataSource;

    static {
        sDataSource = new ComboPooledDataSource("ajaxApp");
    }

    public Connection getConnection() {
        Connection connection = null;
        if (sDataSource != null) {
            try {
                connection = sDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private DBManager() {

    }

    private static DBManager sDBManager = new DBManager();

    public static DBManager getInstance() {
        return sDBManager;
    }
}
