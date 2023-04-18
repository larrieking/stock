package com.acl.stock.Exceptions;


import com.acl.stock.domain.response.AppResponse;
import com.acl.stock.domain.response.ErrorModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
@Slf4j
@ControllerAdvice
public class ExceptionHandlers {


    @InitBinder
    private void activateDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    //AuthorizationResponse
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<?> handleUserBadRequestException(final BadRequestException ex) {
        return AppResponse.<String>builder().data("").status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage())
                .error(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public AppResponse<?> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {

        log.error("Error: {}; ex.getName() {}; ex.getValue() {}", ex.getMessage(), ex.getName(), ex.getValue());

        return AppResponse.<String>builder().data("").error(ErrorModel.builder().messageError("Invalid Value supplied").fieldName(ex.getName()).rejectedValue(ex.getValue()).build())
                .status(HttpStatus.BAD_REQUEST.value()).message("Input Validation Error").build();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public AppResponse<?> handleBindException(final BindException ex) {

        List<ErrorModel> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage())).distinct()
                .collect(Collectors.toList());

        Map<String, String> errorResponse = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.put(fieldError.getField(), fieldError.getDefaultMessage());

        }

        AppResponse<?> response = AppResponse.<String>builder().data("").error(errorResponse)
                .status(HttpStatus.BAD_REQUEST.value()).message("Input Validation Error").build();

        /* Added to fix class level validator exception **/
        if (errorMessages.isEmpty() && ex.getBindingResult().getAllErrors().size() > 0) {
            for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
                errorMessages.add(new ErrorModel("not_applicable", "Not Applicable", objectError.getDefaultMessage()));
            }
            response = AppResponse.<String>builder().data("").error(errorMessages)
                    .status(4000).message("Input Validation Error").build();
        }
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<?> methodArgumentNotValidException(final MethodArgumentNotValidException ex) {

        List<ErrorModel> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage())).distinct()
                .collect(Collectors.toList());

        Map<String, String> errorResponse = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.put(fieldError.getField(), fieldError.getDefaultMessage());

        }

        AppResponse<?> response = AppResponse.<String>builder().data("").error(errorResponse)
                .status(HttpStatus.BAD_REQUEST.value()).message("Input Validation Error").build();

        /* Added to fix class level validator exception **/
        if (errorMessages.isEmpty() && ex.getBindingResult().getAllErrors().size() > 0) {
            for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
                errorMessages.add(new ErrorModel("not_applicable", "Not Applicable", objectError.getDefaultMessage()));
            }
            response = AppResponse.<String>builder().data("").error(errorMessages)
                    .status(4000).message("Input Validation Error").build();
        }
        return response;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<?> constraintViolationException(final ConstraintViolationException ex) {

        List<ErrorModel> errorMessages = ex.getConstraintViolations().stream()
                .map(err -> new ErrorModel(err.getPropertyPath().toString(), err.getInvalidValue(), err.getMessage()))
                .distinct().collect(Collectors.toList());

        return AppResponse.builder().data("").status(HttpStatus.BAD_REQUEST.value())
                .message("Constraint Violations Error").error(errorMessages).build();
    }



}