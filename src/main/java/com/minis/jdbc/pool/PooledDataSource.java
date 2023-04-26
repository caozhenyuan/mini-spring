package com.minis.jdbc.pool;

import com.minis.util.CollectionUtils;
import com.minis.util.StringUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author czy
 * @date 2023/04/26
 **/
public class PooledDataSource implements DataSource {

    private List<PooledConnection> connections = null;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private int initialSize = 2;

    private Properties connectionProperties;


    public PooledDataSource() {
    }

    /**
     * 初始化连接
     */
    private void initPool() {
        this.connections = new ArrayList<>(initialSize);
        System.out.println("********connection pool init*********");
        try {
            for (int i = 0; i < initialSize; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connection, false);
                this.connections.add(pooledConnection);
                System.out.println("********add connection pool*********");
            }
            System.out.println("********connection pool end*********");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    /**
     * 获取数据库连接
     *
     * @param username 用户名
     * @param password 密码
     * @return 连接对象
     */
    private Connection getConnectionFromDriver(String username, String password) {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (null != connProps) {
            mergedProps.putAll(connProps);
        }
        if (!StringUtils.isEmpty(username)) {
            mergedProps.put("user", username);
        }
        if (!StringUtils.isEmpty(password)) {
            mergedProps.put("password", password);
        }
        if (CollectionUtils.isEmpty(this.connections)) {
            initPool();
        }
        PooledConnection pooledConnection = getAvailableConnection();

        while (null == pooledConnection) {
            pooledConnection = getAvailableConnection();
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return pooledConnection;
    }

    /**
     * 获取有效连接
     *
     * @return 有效连接对象
     */
    private PooledConnection getAvailableConnection() {
        for (PooledConnection pooledConnection : this.connections) {
            if (!pooledConnection.isActive()) {
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }
        return null;
    }

    public List<PooledConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<PooledConnection> connections) {
        this.connections = connections;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
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

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
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
