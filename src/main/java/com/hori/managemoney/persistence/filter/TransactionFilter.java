package com.hori.managemoney.persistence.filter;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionFilter extends Filter {
    private String id;
    private String accountCode;
    private String note;
    private BigDecimal amount;
    private BigDecimal amountLowest;
    private BigDecimal amountHighest;
    private Timestamp dateBefore;
    private Timestamp dateAfter;
    private String username;
}
