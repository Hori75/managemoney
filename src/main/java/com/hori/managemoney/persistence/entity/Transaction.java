package com.hori.managemoney.persistence.entity;

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
public class Transaction extends BaseEntity {
    private String accountCode;
    private String note;
    private BigDecimal amount;
    private Timestamp datetime;
    private String username;
}
