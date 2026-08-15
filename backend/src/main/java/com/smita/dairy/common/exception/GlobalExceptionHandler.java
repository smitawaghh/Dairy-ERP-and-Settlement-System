package com.smita.dairy.common.exception;

import com.smita.dairy.auth.exception.InvalidCredentialsException;

import com.smita.dairy.cashadvance.exception.CashAdvanceNotFoundException;
import com.smita.dairy.cashadvance.exception.InvalidCashAdvanceException;

import com.smita.dairy.farmer.exception.DuplicateFarmerException;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.feedsale.exception.FeedSaleNotFoundException;
import com.smita.dairy.feedsale.exception.InvalidFeedSaleException;

import com.smita.dairy.ledger.exception.LedgerEntryNotFoundException;

import com.smita.dairy.milkentry.exception.InvalidMilkEntryException;
import com.smita.dairy.milkentry.exception.MilkEntryNotFoundException;

import com.smita.dairy.payment.exception.InvalidPaymentException;
import com.smita.dairy.payment.exception.PaymentNotFoundException;

import com.smita.dairy.ratecard.exception.InvalidRateCardException;
import com.smita.dairy.ratecard.exception.RateCardNotFoundException;

import com.smita.dairy.settlement.exception.InvalidSettlementException;
import com.smita.dairy.settlement.exception.SettlementNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------
    // 400 BAD REQUEST
    // -------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                validationErrors
        );
    }

    @ExceptionHandler({
            InvalidMilkEntryException.class,
            InvalidCashAdvanceException.class,
            InvalidFeedSaleException.class,
            InvalidPaymentException.class,
            InvalidSettlementException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(
            RuntimeException exception) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }

    // -------------------------
    // 401 UNAUTHORIZED
    // -------------------------

    @ExceptionHandler({
            InvalidCredentialsException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiError> handleUnauthorized(
            RuntimeException exception) {

        return buildError(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                null
        );
    }

    // -------------------------
    // 403 FORBIDDEN
    // -------------------------

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException exception) {

        return buildError(
                HttpStatus.FORBIDDEN,
                "Access denied",
                null
        );
    }

    // -------------------------
    // 404 NOT FOUND
    // -------------------------

    @ExceptionHandler({
            FarmerNotFoundException.class,
            RateCardNotFoundException.class,
            MilkEntryNotFoundException.class,
            LedgerEntryNotFoundException.class,
            CashAdvanceNotFoundException.class,
            FeedSaleNotFoundException.class,
            PaymentNotFoundException.class,
            SettlementNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(
            RuntimeException exception) {

        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );
    }

    // -------------------------
    // 409 CONFLICT
    // -------------------------

    @ExceptionHandler({
            DuplicateFarmerException.class,
            InvalidRateCardException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ApiError> handleConflict(
            RuntimeException exception) {

        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                null
        );
    }

    // -------------------------
    // 500 INTERNAL SERVER ERROR
    // -------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception exception) {

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                null
        );
    }

    // -------------------------
    // COMMON BUILDER
    // -------------------------

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            Map<String, String> validationErrors) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                validationErrors
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }
}