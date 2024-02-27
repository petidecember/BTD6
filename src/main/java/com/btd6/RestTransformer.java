package com.btd6;

public class RestTransformer {
    public static Customer restCustomerToDomain(RCustomer rcustomer) {
        return new Customer(rcustomer.getUuid(), rcustomer.getFirstname(), rcustomer.getLastname());
    }

    public static Customer restCustomerToDomainRandomUUID(RCustomer rcustomer) {
        return new Customer(rcustomer.getFirstname(), rcustomer.getLastname());
    }

    public static RCustomer domainCustomerToRest(Customer customer) {
        return new RCustomer(customer.getUuid(), customer.getFirstname(), customer.getLastname());
    }

    public static Reading restReadingToDomain(RReading rreading) {
        return new Reading(rreading.getUuid(), rreading.getComment(), restCustomerToDomain(rreading.getCustomer()),
                rreading.getDateOfReading(), rreading.getMetercount(), rreading.getMeterId(), rreading.getSubstitute());
    }

    public static Reading restReadingToDomainRandomUUID(RReading rreading) {
        return new Reading(rreading.getComment(), restCustomerToDomain(rreading.getCustomer()),
                rreading.getDateOfReading(), rreading.getMetercount(), rreading.getMeterId(), rreading.getSubstitute());
    }

    public static RReading domainReadingToRest(Reading reading) {
        return new RReading(reading.getUuid(), reading.getComment(), domainCustomerToRest(reading.getCustomer()),
                reading.getDateOfReading(), reading.getMetercount(), reading.getMeterId(), reading.getSubstitute());
    }
}
