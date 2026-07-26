package com.smita.dairy.common.exception;

import com.smita.dairy.farmer.exception.DuplicateFarmerException;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;
import com.smita.dairy.ratecard.exception.InvalidRateCardException;
import com.smita.dairy.ratecard.exception.RateCardNotFoundException;
import com.smita.dairy.milkentry.exception.InvalidMilkEntryException;
import com.smita.dairy.milkentry.exception.MilkEntryNotFoundException;
import com.smita.dairy.ledger.exception.LedgerEntryNotFoundException;
import com.smita.dairy.cashadvance.exception.CashAdvanceNotFoundException;
import com.smita.dairy.cashadvance.exception.InvalidCashAdvanceException;
import com.smita.dairy.feedsale.exception.FeedSaleNotFoundException;
import com.smita.dairy.feedsale.exception.InvalidFeedSaleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FarmerNotFoundException.class)
    public ResponseEntity<ApiError> handleFarmerNotFound(
            FarmerNotFoundException exception) {

        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(DuplicateFarmerException.class)
    public ResponseEntity<ApiError> handleDuplicateFarmer(
            DuplicateFarmerException exception) {

        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(RateCardNotFoundException.class)
    public ResponseEntity<ApiError> handleRateCardNotFound(
            RateCardNotFoundException exception) {

        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(InvalidRateCardException.class)
    public ResponseEntity<ApiError> handleInvalidRateCard(
            InvalidRateCardException exception) {

        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                null
        );
    }

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

    @ExceptionHandler(MilkEntryNotFoundException.class)
public ResponseEntity<ApiError> handleMilkEntryNotFound(
        MilkEntryNotFoundException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}

@ExceptionHandler(InvalidMilkEntryException.class)
public ResponseEntity<ApiError> handleInvalidMilkEntry(
        InvalidMilkEntryException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}

@ExceptionHandler(LedgerEntryNotFoundException.class)
public ResponseEntity<ApiError> handleLedgerEntryNotFound(
        LedgerEntryNotFoundException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}

@ExceptionHandler(CashAdvanceNotFoundException.class)
public ResponseEntity<ApiError> handleCashAdvanceNotFound(
        CashAdvanceNotFoundException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}

@ExceptionHandler(InvalidCashAdvanceException.class)
public ResponseEntity<ApiError> handleInvalidCashAdvance(
        InvalidCashAdvanceException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}

@ExceptionHandler(FeedSaleNotFoundException.class)
public ResponseEntity<ApiError> handleFeedSaleNotFound(
        FeedSaleNotFoundException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}

@ExceptionHandler(InvalidFeedSaleException.class)
public ResponseEntity<ApiError> handleInvalidFeedSale(
        InvalidFeedSaleException exception) {

    ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            null
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}
}