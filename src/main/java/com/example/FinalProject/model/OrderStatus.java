package com.example.FinalProject.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PROCESSING("In processing"),
    CONFIRM("Confirm"),
    COOKING("Cooking"),
    READY("Ready");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
