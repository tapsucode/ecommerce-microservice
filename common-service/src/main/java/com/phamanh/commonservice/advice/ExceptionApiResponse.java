package com.phamanh.commonservice.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionApiResponse {
  private Integer code;
  private String message;
  private HttpStatus httpStatus;
  private String path;
}
