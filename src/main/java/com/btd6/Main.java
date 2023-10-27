package com.btd6;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/BTD6", "BTD6", "");
        System.out.println("connecting");

        final Handle handle = jdbi.open();
}
