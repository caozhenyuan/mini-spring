package com.minis.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author czy
 * @date 2023/04/25
 **/
public interface StatementCallback {

    Object doInStatement(Statement stmt) throws SQLException;
}
