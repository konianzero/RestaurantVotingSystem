package org.restaurant.voting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restaurant.voting.util.exception.*;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes;

    /**
     * Handle exception when the DispatcherServlet can't find a handler for a request
     * (when property {@code spring.mvc.throw-exception-if-no-handler-found: true}).
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("NoHandlerFoundException", ex);
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(), ex.getMessage()), status);
    }

    /**
     * Handle exception when validation on an argument annotated with @Valid fails.
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("MethodArgumentNotValidException", ex);
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("BindException", ex);
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    /**
     * Customize the response body of all exception types.
     * @return ResponseEntity with custom response body
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @NonNull HttpHeaders headers,@NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("Internal Exception", ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(), getRootCause(ex).getMessage()), status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> persistException(WebRequest request, NotFoundException ex) {
        log.error("NotFoundException: {}", ex.getMessage());
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({DataConflictException.class, VotingTimeOverException.class})
    public ResponseEntity<Object> dataConflictException(WebRequest request, RuntimeException ex) {
        log.error("Conflict Exception: {}", ex.getMessage());
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalRequestDataException.class)
    public ResponseEntity<Object> illegalRequestException(WebRequest request, IllegalRequestDataException ex) {
        log.error("IllegalRequestDataException: {}", ex.getMessage());
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(WebRequest request, RuntimeException ex) {
        log.error("Internal Error", ex);
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(WebRequest request, AppException ex) {
        log.error("AppException: {}", ex.getMessage());
        return createResponseEntity(getDefaultBody(request, ex.getOptions(), ex.getMessage()), ex.getStatus());
    }

    // *** Helper functions ***

    /**
     * Make message with name of invalid fields and their error messages.
     * @return ResponseEntity with custom response body.
     */
    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.defaults(), msg), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> getDefaultBody(WebRequest request, ErrorAttributeOptions options, String msg) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        return body;
    }

    private Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}
