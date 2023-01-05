package com.cjf.util;

import java.sql.Connection;

public interface ConnectionPool {
    void init(Integer maxSize);

    void destory();

    Connection getConnection() throws Exception;

    void releaseConnection(Connection connection) throws Exception;

}
