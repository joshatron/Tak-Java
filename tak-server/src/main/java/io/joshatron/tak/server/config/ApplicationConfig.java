package io.joshatron.tak.server.config;

import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.database.DatabaseManager;
import io.joshatron.tak.server.database.GameDAOSqlite;
import io.joshatron.tak.server.database.SocialDAOSqlite;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class ApplicationConfig {

    @Bean
    public GameDAOSqlite gameDAOSqlite() {
        return new GameDAOSqlite(accountDAOSqlite(), socialDAOSqlite(), connection());
    }

    @Bean
    public SocialDAOSqlite socialDAOSqlite() {
        return new SocialDAOSqlite(accountDAOSqlite(), connection());
    }

    @Bean
    public AccountDAOSqlite accountDAOSqlite() {
        return new AccountDAOSqlite(connection());
    }

    @Bean
    public Connection connection() {
        return DatabaseManager.getConnection();
    }
}
