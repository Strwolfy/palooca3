package org.palooca.data;

import org.palooca.olap4j.Olap4jConnection;

import java.sql.SQLException;

public class OlapConnection2 {

    static String host = "http://192.168.169.3:7080/mdx/xmla/learn_kylin";
    static String port = "7080";
    static String username = "ADMIN";
    static String password = "KYLIN";
    static String database = "sales";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Olap4jConnection olap4jConnection =
                new Olap4jConnection(host, port, username, password, database);

        // Вызываем метод open() для открытия соединения
        olap4jConnection.open();

        olap4jConnection.getOlapConnection().getDatabase();
       // Statement statement = olapConnection.createStatement();

        //System.out.println("getDatabase " + );
        //System.out.println("getMetaData " + olap4jConnection.getOlapConnection().getMetaData());

       // CellSet cellSet = statement.executeOlapQuery("SELECT [Measures].[Sales Amount] ON COLUMNS, [Product].[Category].Members ON ROWS FROM [Adventure Works]");


    }
}
