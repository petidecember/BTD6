package com.btd6;

import org.jdbi.v3.core.Jdbi;

public class Main {
    public static void main(String[] args) {
        final Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/BTD6", "BTD6", "");
        System.out.println("connecting");

        jdbi.useHandle(handle -> {
        });

    }
}
