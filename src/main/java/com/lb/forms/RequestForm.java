package com.lb.forms;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.*;


public class RequestForm {
    @NotBlank(message = "Region is required")
    @Size(min = 3, max = 100, message = "Region name must be between 3 and 100 characters")
    private String region;

    @NotNull(message = "Number of labourers is required")
    @Min(value = 1, message = "At least 1 labourer must be requested")
    private Integer numLabourers;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotBlank(message = "Status is required")
    private String status;
}
