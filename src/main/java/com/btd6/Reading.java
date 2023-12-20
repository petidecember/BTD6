package com.btd6;

import java.beans.ConstructorProperties;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Reading implements IReading {
    private UUID uuid;
    private String comment;
    private ICustomer customer;
    private LocalDate dateofreading;
    private double metercount;
    private String meterid;
    private boolean substitute;

    @ConstructorProperties ({"uuid", "comment", "customer", "dateofreading", "metercount", "meterid", "substitute"})
    public Reading(UUID uuid, String comment, ICustomer customer, Date dateofreading, double metercount, String meterid, boolean substitute) {
        setUuid(uuid);
        setComment(comment);
        setCustomer(customer);
        setDateOfReading(dateofreading.toLocalDate());
        setMeterId(meterid);
        setMetercount(metercount);
        setSubstitute(substitute);
    }

    public Reading(String comment, ICustomer customer, LocalDate dateofreading, double metercount, String meterid, boolean substitute) {
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
    public ICustomer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(ICustomer customer) {
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
