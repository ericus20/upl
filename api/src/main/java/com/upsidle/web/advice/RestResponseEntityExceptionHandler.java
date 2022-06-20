package com.upsidle.web.advice;

import com.upsidle.exception.InvalidServiceRequestException;
import com.upsidle.exception.user.InvalidTokenException;
import com.upsidle.exception.user.UserAlreadyExistsException;
import com.upsidle.web.payload.pojo.ApiError;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A global exception handler for REST API.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles IllegalArgumentException and IllegalStateException thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    var message = ex.getMessage();
    return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  /**
   * Handles generic exception thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
  @ExceptionHandler(value = {InvalidServiceRequestException.class})
  protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
    var message = ex.getMessage();
    return handleExceptionInternal(
        ex, message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  /**
   * Handles generic exception thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
  @ExceptionHandler(value = {UserAlreadyExistsException.class, InvalidTokenException.class})
  protected ResponseEntity<Object> handleUserExists(RuntimeException ex, WebRequest request) {
    var message = ex.getMessage();
    var error = new ApiError(HttpStatus.BAD_REQUEST, message);
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handles MethodArgumentNotValidException thrown by the REST API by @Valid annotation.
   *
   * @param ex the exception
   * @param headers the headers to be written to the response
   * @param status the selected response status
   * @param request the current request
   * @return the response entity
   */
  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request) {

    List<String> errors = new ArrayList<>();
    for (var error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }

    for (var error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    var error = new ApiError(status, "Method argument not valid", errors);
    return handleExceptionInternal(ex, error, headers, status, request);
  }
}
