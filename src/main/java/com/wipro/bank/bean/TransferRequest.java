package com.wipro.bank.bean;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransferRequest {
    @NotNull
    private int from;
    @NotNull
    private int to;
    @NotNull
    private double amount;
}
