package com.example.test.util;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/***
 * Exception handler.
 * This class is responsible for returning the correct information for the user.
 * This will handle the mismatch information in the requests.
 * This will also handle the not informed field for the requests.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Field type mismatch
     * Example: Send a String and is expecting a Integer
     * It will return informing the user that the informed field is not compatible with the desired information.
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                "The field informed "
                        + ex.getValue().toString()
                        +" informed is not compatible with the required field. The type required is "
                        +ex.getRequiredType().getSimpleName()
                , ex.getMessage());
        return buildResponseEntity(apiError);
    }


    /***
     * This method will handle the exception.
     * It will also inform the field that is mandatory and is missing.
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
                "Field: "+ex.getBindingResult().getFieldErrors().get(0).getField());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
