package com.CardMaster.common.exception;

import com.CardMaster.common.dto.ApiResponse;
import com.CardMaster.modules.iam.exception.InvalidCredentialsException;
import com.CardMaster.modules.iam.exception.UserNotFoundException;
import com.CardMaster.modules.paa.exception.*;
import com.CardMaster.modules.paa.exception.CustomExceptions.*;
import com.CardMaster.modules.bsp.exception.*;
import com.CardMaster.modules.cias.exception.*;
import com.CardMaster.modules.tap.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<String> res = new ApiResponse<>("User Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        ApiResponse<String> res = new ApiResponse<>("Login Failed", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            com.CardMaster.modules.cau.exception.UnauthorizedActionException.class,
            com.CardMaster.modules.cias.exception.UnauthorizedActionException.class
    })
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(RuntimeException ex) {
        ApiResponse<String> res = new ApiResponse<>("Access Denied", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    @ExceptionHandler({
            com.CardMaster.modules.paa.exception.CustomerNotFoundException.class,
            ApplicationNotFoundException.class,
            DocumentNotFoundException.class,
            ResourceNotFoundException.class,
            com.CardMaster.modules.cau.exception.EntityNotFoundException.class,
            com.CardMaster.modules.cias.exception.EntityNotFoundException.class,
            StatementNotFoundException.class,
            ProductNotFoundException.class,
            TransactionNotFoundException.class,
            com.CardMaster.modules.cias.exception.CustomerNotFoundException.class
    })
    public ResponseEntity<ApiResponse<String>> handleNotFound(RuntimeException ex) {
        ApiResponse<String> res = new ApiResponse<>("Resource Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler({
            DuplicateApplicationException.class,
            DuplicateDocumentException.class
    })
    public ResponseEntity<ApiResponse<String>> handleConflict(RuntimeException ex) {
        ApiResponse<String> res = new ApiResponse<>("Conflict Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler({
            InvalidStatusException.class,
            InvalidDocumentTypeException.class,
            LimitExceededException.class,
            IllegalArgumentException.class,
            com.CardMaster.modules.cau.exception.ValidationException.class,
            com.CardMaster.modules.cias.exception.ValidationException.class,
            PaymentFailedException.class,
            InsufficientLimitException.class
    })
    public ResponseEntity<ApiResponse<String>> handleBadRequest(RuntimeException ex) {
        ApiResponse<String> res = new ApiResponse<>("Invalid Input/Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler({
            CardIssuanceException.class,
            AccountSetupException.class
    })
    public ResponseEntity<ApiResponse<String>> handleUnprocessableEntity(RuntimeException ex) {
        ApiResponse<String> res = new ApiResponse<>("Unprocessable Entity", ex.getMessage());
        return ResponseEntity.status(422).body(res);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse<String>> handleFileError(FileStorageException ex) {
        ApiResponse<String> res = new ApiResponse<>("File Storage Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
        ApiResponse<String> res = new ApiResponse<>("Internal Server Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
