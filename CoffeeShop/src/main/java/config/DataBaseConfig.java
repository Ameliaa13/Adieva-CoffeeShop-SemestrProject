package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConfig {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
    }

    private DataBaseConfig(){}

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")
            );
        }catch (SQLException e){
            throw new RuntimeException("Error connection", e);
        }
    }
}
