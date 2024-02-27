package com.btd6;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWithReadingsDTO {
    private RCustomer customer;
    private List<RReading> readings;
}
