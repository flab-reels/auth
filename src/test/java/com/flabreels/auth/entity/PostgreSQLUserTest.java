package com.flabreels.auth.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class}
)
class PostgreSQLUserTest {

    @DisplayName("PostgreSQL 연결 테스트")
    @Test
    public void insertTest(){
        // Given
        String jdbcDriver =  "jdbc:postgresql://localhost:5432/user";
        String dbUser = "root";
        String dbPass = "qwer1234";
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);

        }catch (Exception e){
            e.printStackTrace();
        }
        assertThat(connection).isNotNull();
    }

}