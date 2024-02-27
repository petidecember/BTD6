package com.btd6;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RReading implements IRReading {
    private UUID uuid;
    @NotNull
    private String comment;
    @NotNull
    private RCustomer customer;
    @NotNull
    private LocalDate dateOfReading;
    @NotNull
    private Double metercount;
    @NotNull
    private String meterId;
    @NotNull
    private Boolean substitute;

    public RReading(String comment, RCustomer customer, LocalDate dateOfReading, double metercount, String meterid, boolean substitute) {
        setUuid(UUID.randomUUID());
        setComment(comment);
        setCustomer(customer);
        setDateOfReading(dateOfReading);
        setMeterId(meterid);
        setMetercount(metercount);
        setSubstitute(substitute);
    }
}
