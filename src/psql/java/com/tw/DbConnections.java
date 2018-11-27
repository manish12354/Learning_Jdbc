package com.tw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnections {
//  private static final Logger LOGGER = LoggerFactory.getLogger(WebDbConnections.class);

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection create() {

        String connectionUrl = DbProperties.connectionUrl();
        String user = DbProperties.user();
        String password = DbProperties.password();
//    LOGGER.info("Making connection using : connection user " + connectionUrl + "with user " + user);
        try {
            Connection connection = DriverManager.getConnection(connectionUrl, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
//        LOGGER.error("cannot close statement", ex);
            }
        }
    }


    public static void destroy(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void rollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
//      LOGGER.warn("Unable to rollback connection", e);
        }
    }

}


