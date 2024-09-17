package se.lu.ics.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHandler {
    private String connectionURL;

    public ConnectionHandler() throws IOException {

        Properties connectionProperties = new Properties();

        // Fetch the config file from the classpath
        // We do not use a catch block here because we want the exception to propagate to the caller
        try (InputStream inputStream = getClass().getResourceAsStream("/config/config.properties")) {

            if (inputStream != null) {
                connectionProperties.load(inputStream);
            } else {
                throw new IOException("Config file 'config.properties' not found in classpath");
            }
        }

        String databaseServerName = connectionProperties.getProperty("database.server.name");
        String databaseServerPort = connectionProperties.getProperty("database.server.port");
        String databaseName = connectionProperties.getProperty("database.name");
        String databaseUserName = connectionProperties.getProperty("database.user.name");
        String databaseUserPassword = connectionProperties.getProperty("database.user.password");

        connectionURL = "jdbc:sqlserver://"
                + databaseServerName + ":"
                + databaseServerPort + ";"
                + "database=" + databaseName + ";"
                + "user=" + databaseUserName + ";"
                + "password=" + databaseUserPassword + ";"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionURL);
    }
}