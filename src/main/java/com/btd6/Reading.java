package com.btd6;

import lombok.EqualsAndHashCode;
import org.jdbi.v3.core.mapper.Nested;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode
public class Reading implements IReading {
    private UUID uuid;
    private String comment;
    @Nested
    private Customer customer;
    private LocalDate dateofreading;
    private double metercount;
    private String meterid;
    private boolean substitute;

    @ConstructorProperties ({"uuid", "comment", "customer", "dateofreading", "metercount", "meterid", "substitute"})
    public Reading(UUID uuid, String comment, Customer customer, LocalDate dateofreading, double metercount, String meterid, boolean substitute) {
        setUuid(uuid);
        setComment(comment);
        setCustomer(customer);
        setDateOfReading(dateofreading);
        setMeterId(meterid);
        setMetercount(metercount);
        setSubstitute(substitute);
    }

    public Reading(String comment, Customer customer, LocalDate dateofreading, double metercount, String meterid, boolean substitute) {
        setUuid(UUID.randomUUID());
        setComment(comment);
        setCustomer(customer);
        setDateOfReading(dateofreading);
        setMeterId(meterid);
        setMetercount(metercount);
        setSubstitute(substitute);
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public LocalDate getDateOfReading() {
        return dateofreading;
    }

    @Override
    public void setDateOfReading(LocalDate dateofreading) {
        this.dateofreading = dateofreading;
    }

    @Override
    public Double getMetercount() {
        return metercount;
    }

    @Override
    public void setMetercount(Double metercount) {
        this.metercount = metercount;
    }

    @Override
    public String getMeterId() {
        return meterid;
    }

    @Override
    public void setMeterId(String meterid) {
        this.meterid = meterid;
    }

    @Override
    public Boolean getSubstitute() {
        return this.substitute;
    }

    @Override
    public void setSubstitute(Boolean substitute) {
        this.substitute = substitute;
    }

}
