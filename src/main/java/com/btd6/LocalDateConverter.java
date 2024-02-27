package com.btd6;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter implements ParamConverter<LocalDate> {
    @Override
    public LocalDate fromString(String s) {
        if(s == null) {
            return null;
        }
        try {
            return LocalDate.parse(s, DateTimeFormatter.ISO_DATE);
        } catch(DateTimeParseException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public String toString(LocalDate date) {
        if(date == null)
            return null;
        return DateTimeFormatter.ISO_DATE.format(date);
    }
}
