/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author aimilbc
 */
class connection {
    public connection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/CECS323_JDBC_Project", null, null);
        System.out.println("Connection created");
    }
}
