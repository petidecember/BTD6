package com.btd6;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Required for serializing Customer correctly when using its UUID as foreign key
 */
public class CustomerAsArgumentFactory extends AbstractArgumentFactory<Customer> {

    public CustomerAsArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(Customer value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.getUuid().toString());
    }
}

