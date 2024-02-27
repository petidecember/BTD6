package com.btd6;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        Server server = new Server("http://localhost:8080",
                new DatabaseConnection().openConnection("jdbc:mariadb://localhost:3306/BTD6", "BTD6", "")
//                new DatabaseConnection().openConnection("jdbc:h2:mem:test", "", "")
        );
        server.startServer();
    }
}