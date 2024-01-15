package com.btd6;

import java.time.LocalDate;
import java.util.UUID;

public interface IReading{

	void setUuid(UUID uuid);

	UUID getUuid();

	void setComment(String comment);

	String getComment();

	void setCustomer(Customer customer);

	Customer getCustomer();

	void setDateOfReading(LocalDate dateOfReading);

	LocalDate getDateOfReading();

	void setMetercount(Double meterCount);

	Double getMetercount();

	void setMeterId(String meterId);

	String getMeterId();

	void setSubstitute(Boolean substitute);

	Boolean getSubstitute();

}
