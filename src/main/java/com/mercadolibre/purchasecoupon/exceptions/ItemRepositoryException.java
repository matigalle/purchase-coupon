package com.mercadolibre.purchasecoupon.exceptions;

public class ItemRepositoryException extends RuntimeException {

    private final int statusCode;

    public ItemRepositoryException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
