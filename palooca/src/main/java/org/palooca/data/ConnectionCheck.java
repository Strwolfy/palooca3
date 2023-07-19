package org.palooca.data;

import org.olap4j.*;
import org.olap4j.metadata.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionCheck {
    public static void main(String[] args) {
        try {
            // Подключение к MDX-совместимой базе данных
            Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");
            String connectionUrl = "jdbc:xmla:Server=http://192.168.169.3:7080/;Catalog=learn_kylin";
            Connection connection = DriverManager.getConnection(connectionUrl, "username", "password");

            // Проверка соединения
            if (connection.isValid(5)) {
                System.out.println("Соединение установлено.");
            } else {
                System.out.println("Соединение не установлено.");
            }

            // Закрытие подключения
            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при подключении: " + e.getMessage());
        }
    }
}
