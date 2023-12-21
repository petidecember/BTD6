package com.btd6;

import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.UUID;

@EqualsAndHashCode
public class Customer implements ICustomer {
    private UUID uuid;
    private String firstname;
    private String lastname;

    @ConstructorProperties({"uuid", "firstname", "lastname"})
    public Customer(UUID uuid, String firstname, String lastname) {
        setUuid(uuid);
        setFirstname(firstname);
        setLastname(lastname);
    }



    public Customer(String firstname, String lastname) {
        setUuid(UUID.randomUUID());
        setFirstname(firstname);
        setLastname(lastname);
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID id) {
        this.uuid = id;
    }

    @Override
    public String getLastname() {
        return this.lastname;
    }

    @Override
    public void setLastname(String lastName) {
        this.lastname = lastName;
    }
}
