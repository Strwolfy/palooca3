package org.palooca.data;

import com.jedox.palojlib.main.ConnectionConfiguration;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {

    static String host = "http://192.168.169.3:7080/mdx/xmla/learn_kylin";
    static String port = "7080";
    static String username = "ADMIN";
    static String password = "KYLIN";
    static String database = "sales";
    private  String connectionString;

    private static OlapConnection connection;
    public OlapConnection getOlapConnection() {
        return  connection;
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connections connections = new Connections();

        Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");

        connections.connectionString = "jdbc:xmla:Server="+ host;
        // Вызываем метод open() для открытия соединения
        ConnectionConfiguration configuration = new ConnectionConfiguration();
        configuration.setHost(host);
        configuration.setPassword(password);
        configuration.setPort(port);
        configuration.setSslPreferred(false);
        configuration.setTimeout(0);
        configuration.setUsername(username);

        try {
            Connection jdbcConnection =
                    DriverManager.getConnection(connections.connectionString,
                            configuration.getUsername(),configuration.getPassword());
            OlapWrapper wrapper = (OlapWrapper) jdbcConnection;
           connection = wrapper.unwrap(org.olap4j.OlapConnection.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        connections.getOlapConnection().getDatabase();
       // Statement statement = olapConnection.createStatement();


        // Statement statement = olapConnection.createStatement();

        // CellSet cellSet = statement.executeOlapQuery("SELECT [Measures].[Sales Amount] ON COLUMNS, [Product].[Category].Members ON ROWS FROM [Adventure Works]");


    }
}
