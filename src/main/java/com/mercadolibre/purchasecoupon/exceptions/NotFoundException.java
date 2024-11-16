package com.mercadolibre.purchasecoupon.exceptions;

public class NotFoundException extends HttpClientException {

    public NotFoundException(String message) {
        super(message);
    }

}
