package com.phamanh.commonservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  public RestExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }


  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error("handleMethodArgumentNotValid: ",ex);
    var response = new ExceptionApiResponse();

    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setMessage("Validation failed");
    response.setHttpStatus(HttpStatus.BAD_REQUEST);
    response.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error("handleNoHandlerFoundException: ",ex);
    var response = new ExceptionApiResponse();

    response.setCode(HttpStatus.NOT_FOUND.value());
    response.setMessage("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
    response.setHttpStatus(HttpStatus.NOT_FOUND);
    response.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  // Các lỗi ràng buộc không thành công

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error("handleBindException: ",ex);
    var response = new ExceptionApiResponse();

    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setMessage(
        ex.getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce("", (s1, s2) -> s1 + "\n" + s2));
    response.setHttpStatus(HttpStatus.BAD_REQUEST);
    response.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


  @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error("handleHttpRequestMethodNotSupported: ",ex);
    var response = new ExceptionApiResponse();
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    response.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    response.setMessage(builder.toString());
    response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
    response.setPath(((ServletWebRequest) request).getRequest().getRequestURI().toString());
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }
}
