package org.sazonpt.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;

public class DBConfig {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                Dotenv dotenv = Dotenv.load();
                String host = dotenv.get("DB_HOST");
                String dbName = dotenv.get("DB_SCHEMA");
                String jdbcUrl = String.format("jdbc:mysql://%s:3306/%s?useSSL=false&allowPublicKeyRetrieval=true", host, dbName);
                
                System.out.println("Conectando a: " + jdbcUrl); // Debug
                
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(dotenv.get("DB_USER"));
                config.setPassword(dotenv.get("DB_PASS"));
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(10);
                config.setConnectionTimeout(30000);
                
                dataSource = new HikariDataSource(config);
                System.out.println("Conexión a BD establecida correctamente");
            } catch (Exception e) {
                System.err.println("Error al conectar con la BD: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dataSource;
    }
}