package pl.put.poznan.jsontools.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException e){
        HttpStatus invalidInputStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiException apiException = new ApiException(
                e.getMessage(),
                invalidInputStatus
        );
        return new ResponseEntity<>(apiException, invalidInputStatus);
    }

}
