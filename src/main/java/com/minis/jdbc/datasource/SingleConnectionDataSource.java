package com.minis.jdbc.datasource;

import com.minis.util.StringUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author czy
 * @date 2023/04/25
 **/
public class SingleConnectionDataSource implements DataSource {


    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Properties connectionProperties;
    private Connection connection;

    public SingleConnectionDataSource() {
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 实际建立数据库连接
     *
     * @return 连接对象
     * @throws SQLException sql异常
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    /**
     * 将参数组织成Properties结构，然后拿到实际的数据库连接
     *
     * @param username 数据库账号
     * @param password 数据库密码
     * @return 连接对象
     * @throws SQLException sql异常
     */
    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (null != connProps) {
            mergedProps.putAll(connProps);
        }
        if (!StringUtils.isEmpty(username)) {
            mergedProps.setProperty("user", username);
        }
        if (!StringUtils.isEmpty(password)) {
            mergedProps.setProperty("password", password);
        }
        return this.connection = getConnectionFromDriverManager(getUrl(), mergedProps);
    }

    /**
     * 通过DriverManager.getConnection()建立实际的连接
     *
     * @param url   数据库地址
     * @param props 数据库配置
     * @return 连接对象
     * @throws SQLException sql异常
     */
    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }

    /**
     * 通过手动的方式输入账号密码
     *
     * @param username the database user on whose behalf the connection is
     *                 being made
     * @param password the user's password
     * @return 连接对象
     * @throws SQLException sql异常
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        return this.getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
