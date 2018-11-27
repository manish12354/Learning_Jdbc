package com.tw;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

public class TableManager {
    public static final String CONFIGURATION = "configuration";
    private static final String jdbcDriverClassName = "org.postgresql.Driver";

    private static String INSERT_SQL = "INSERT INTO " + CONFIGURATION + " (id, config_key, config_value) VALUES (? , ?, ?)";

    private static String DELETE_SQL = "DELETE FROM " + CONFIGURATION + " WHERE CONFIG_KEY LIKE 'sa\\_%'";

    public TableManager() throws ClassNotFoundException {
        Class.forName(jdbcDriverClassName);
    }

    public static void main(String[] args) throws Exception {
        String oozieWorkflowPropertiesPath = "/Users/manishy/my_projects/learning_spark/src/psql/resources/config.property";
        TableManager propertyLoader = new TableManager();
        Properties oozieConfig = propertyLoader.readWorkflowProperties(oozieWorkflowPropertiesPath);
        propertyLoader.updateDB(oozieConfig);
    }

    public Properties readWorkflowProperties(String filename) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(filename);
        properties.load(in);
        logProperties(properties);
        return properties;
    }

    public void updateDB(Properties oozieConfig) throws Exception {
        Connection conn = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = DbConnections.create();
            statement = conn.createStatement();
            preparedStatement = conn.prepareStatement(INSERT_SQL);
            statement.executeUpdate(DELETE_SQL);
            int id =500;
            for (String name : oozieConfig.stringPropertyNames()) {
                if (name.startsWith("sa_")) {
                    String value = oozieConfig.getProperty(name);
                    preparedStatement.setInt(1, id++);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, value);
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
            conn.commit();
        } catch (Exception e) {
            DbConnections.rollback(conn);
            throw e;
        }finally {
            DbConnections.closeStatement(statement);
            DbConnections.closeStatement(preparedStatement);
            DbConnections.destroy(conn);
        }
    }

    private void logProperties(Properties workflowProperties) {
        for (String name : workflowProperties.stringPropertyNames()) {
            System.out.println(name + " : " + workflowProperties.getProperty(name));
        }
    }
}


