package com.upsidle.web.payload.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private HttpStatus status;
  private String message;
  private LocalDateTime timestamp;

  @JsonInclude(Include.NON_NULL)
  private List<String> errors;

  public ApiError(HttpStatus status, String message, List<String> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
    this.timestamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
