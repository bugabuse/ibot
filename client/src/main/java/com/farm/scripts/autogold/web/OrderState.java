package com.farm.scripts.autogold.web;

public enum OrderState {
    WAIT_FOR_PAYMENT,
    READY_FOR_DELIVERY,
    IN_TRADE,
    DELIVERED,
    CANCELED,
    MANUAL_TRADE,
    MANUAL_TRADE_PENALTY,
    WAIT_FOR_REFUND,
    REFUNDED;
}
