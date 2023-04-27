package com.minis.jdbc.pool;

import com.minis.util.CollectionUtils;
import com.minis.util.StringUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author czy
 * @date 2023/04/26
 **/
public class PooledDataSource implements DataSource {

    /**
     * 忙的连接
     */
    private ArrayBlockingQueue<PooledConnection> busy;

    /**
     * 空闲的连接
     */
    private ArrayBlockingQueue<PooledConnection> idle;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private int initialSize = 2;

    private AtomicInteger size;

    private int maxActive;
    private long maxWait;

    private Properties connectionProperties;


    public PooledDataSource() {

    }

    /**
     * 初始化连接
     */
    private void initPool() {
        System.out.println("********connection pool init*********");
        try {
            for (int i = 0; i < size.get(); i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connection, false);
                //初始化连接的时候放入空闲连接
                this.idle.add(pooledConnection);
                System.out.println("********add connection pool*********");
            }
            System.out.println("********connection pool end*********");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        try {
            return getConnectionFromDriver(getUsername(), getPassword());
        } catch (PoolExhaustedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            return getConnectionFromDriver(username, password);
        } catch (PoolExhaustedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @param username 用户名
     * @param password 密码
     * @return 连接对象
     */
    private Connection getConnectionFromDriver(String username, String password) throws SQLException, PoolExhaustedException {
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
        //如果空闲连接和忙的连接都为空则说明没有初始化，则初始化连接池链接
        if (CollectionUtils.isEmpty(this.idle) && CollectionUtils.isEmpty(this.busy)) {
            initPool();
        }
        //获取连接
        return getAvailableConnection();
    }

    /**
     * 获取有效连接
     *
     * @return 有效连接对象
     */
    private PooledConnection getAvailableConnection() throws PoolExhaustedException, SQLException {
        long now = System.currentTimeMillis();
        // 尝试从空闲连接池中获取连接
        PooledConnection conn = idle.poll();
        while (null == conn) {
            // 如果没有可用连接，则尝试创建一个新连接
            if (size.get() < maxActive) {
                if (size.addAndGet(1) > maxActive) {
                    size.decrementAndGet();
                } else {
                    conn = createConnection();
                }
            }
            // 等待直到有可用连接或超时
            if ((System.currentTimeMillis() - now) >= maxWait) {
                throw new PoolExhaustedException("Timeout: Unable to fetch a connection in " + (maxWait / 1000) + " seconds.");
            } else {
                try {
                    wait(maxWait);
                } catch (InterruptedException e) {
                    throw new SQLException(e);
                }
                conn = idle.poll();
            }
        }
        // 将连接添加到忙碌连接池中
        conn.setActive(true);
        busy.add(conn);
        return conn;
    }

    private PooledConnection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        PooledConnection pooledConnection = new PooledConnection(connection, false);
        idle.add(pooledConnection);
        notifyAll();
        return pooledConnection;
    }

    /**
     * 数据库连接回收
     *
     * @param conn
     */
    public synchronized void releaseConnection(PooledConnection conn) {
        busy.remove(conn);
        idle.add(conn);
        notifyAll();
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
        this.size = new AtomicInteger(initialSize);
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
        busy = new ArrayBlockingQueue<>(maxActive);
        idle = new ArrayBlockingQueue<>(maxActive);
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
