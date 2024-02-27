package com.btd6;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RCustomer implements IRCustomer {
    private UUID uuid;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;

    public RCustomer(String firstname, String lastname) {
        setUuid(UUID.randomUUID());
        setFirstname(firstname);
        setLastname(lastname);
    }
}
