package com.mercadolibre.purchasecoupon.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}
