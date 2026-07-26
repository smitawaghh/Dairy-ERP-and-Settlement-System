package com.smita.dairy.feedsale.exception;

public class FeedSaleNotFoundException extends RuntimeException {

    public FeedSaleNotFoundException(Long id) {
        super("Feed sale not found with id: " + id);
    }
}