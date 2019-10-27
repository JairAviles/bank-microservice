package com.wipro.bank.bean;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AccountRequest {
    @NotNull
    private int accountId;
    @NotNull
    private int customerId;
    @NotNull
    private Double balance;
}
