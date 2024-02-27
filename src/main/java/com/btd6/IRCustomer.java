package com.btd6;

import java.util.UUID;

public interface IRCustomer {
    String getFirstname();

    void setFirstname(String firstName);

    UUID getUuid();

    void setUuid(UUID id);

    String getLastname();

    void setLastname(String lastName);
}
