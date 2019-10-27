package com.wipro.bank.bean;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CustomerRequest {
    @NotNull
    private int customerId;
    @NotBlank
    private String name;

}
