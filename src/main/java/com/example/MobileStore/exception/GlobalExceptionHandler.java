package com.example.MobileStore.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler: Xử lý các ngoại lệ trên toàn bộ ứng dụng.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Xử lý lỗi validation khi request có dữ liệu không hợp lệ.
     *
     * @param exception lỗi validation của Spring
     * @param headers   headers của request
     * @param status    HTTP status code
     * @param request   request hiện tại
     * @return ResponseEntity chứa thông tin lỗi
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());

        List<String> errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        errorResponse.put("errors", errorMessages);

        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Xử lý lỗi khi không tìm thấy tài nguyên.
     *
     * @param exception ngoại lệ NotFoundException
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Xử lý lỗi khi tài nguyên đã tồn tại.
     *
     * @param exception ngoại lệ AlreadyExistsException
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Xử lý lỗi vi phạm ràng buộc dữ liệu (ví dụ: khóa ngoại, khóa chính trùng lặp).
     *
     * @param exception ngoại lệ DataIntegrityViolationException
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        String rootCauseMessage = Optional.ofNullable(exception.getRootCause())
                .map(Throwable::getMessage)
                .filter(StringUtils::hasText)
                .orElse(exception.getMessage());

        return buildErrorResponse("Data integrity violation", rootCauseMessage, HttpStatus.CONFLICT);
    }

    /**
     * Xử lý lỗi khi thông tin đăng nhập không hợp lệ.
     *
     * @param exception ngoại lệ InvalidCredentialsException
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        return buildErrorResponse("Invalid credentials", exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Hàm tạo response lỗi chung.
     *
     * @param message   Nội dung lỗi
     * @param status    HTTP status code
     * @return ResponseEntity chứa thông tin lỗi
     */
    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("message", message);

        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Hàm tạo response lỗi với thông tin chi tiết.
     *
     * @param message   Nội dung lỗi
     * @param details   Chi tiết lỗi
     * @param status    HTTP status code
     * @return ResponseEntity chứa thông tin lỗi
     */
    private ResponseEntity<Object> buildErrorResponse(String message, String details, HttpStatus status) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("message", message);
        errorResponse.put("details", details);

        return ResponseEntity.status(status).body(errorResponse);
    }
}
