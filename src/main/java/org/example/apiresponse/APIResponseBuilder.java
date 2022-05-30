package org.example.apiresponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class APIResponseBuilder {

    public static <T> IResultDTO<T> ok(HttpStatus statusCode, T result, String message, Map<String, String> metaData) {
        ResponseData responseData = new ResponseData();
        responseData.setResponseCode(statusCode);
        responseData.setResponseMsg(message);

        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setResult(result);
        resultDTO.setResponseData(responseData);
        resultDTO.setMetaData(metaData);

        return resultDTO;
    }

    public static <T> IResultDTO<T> ok(T result, String message) {
        return ok(HttpStatus.OK, result, message, null);
    }

    public static <T> IResultDTO<T> ok(T result) {

        return ok(HttpStatus.OK, result, "Success", null
        );
    }

    public static <T> IResultDTO<T> ok(T result, Map<String, String> metaData) {
        return ok(HttpStatus.OK, result, "Success", metaData);
    }

    public static <T> IResultDTO<List<T>> ok(
            HttpStatus statusCode,
            Page<T> result, String message, Map<String, String> metaData
    ) {
        ResultDTO<List<T>> resultDTO = buildBaseListResponseData(statusCode, message, result.getContent());
        if (Objects.isNull(metaData)) {
            metaData = new HashMap<>();
        }

        metaData.put("pageCount", String.valueOf(result.getTotalPages()));
        metaData.put("totalData", String.valueOf(result.getTotalElements()));

        resultDTO.setMetaData(metaData);

        return resultDTO;
    }

    public static <T> IResultDTO<List<T>> ok(
            HttpStatus statusCode, Slice<T> result, String message,
            Map<String, String> metaData, Integer totalPages, Integer totalElements
    ) {
        ResultDTO<List<T>> resultDTO = buildBaseListResponseData(statusCode, message, result.getContent());
        if (Objects.isNull(metaData)) {
            metaData = new HashMap<>();
        }

        metaData.put("pageCount", String.valueOf(totalPages));
        metaData.put("totalData", String.valueOf(totalElements));

        resultDTO.setMetaData(metaData);

        return resultDTO;
    }

    public static <T> IResultDTO<List<T>> ok(Page<T> result) {
        return ok(HttpStatus.OK, result, "Success", null);
    }

    public static <T> IResultDTO<List<T>> ok(Slice<T> result, Integer totalPages, Integer totalElements) {
        return ok(HttpStatus.OK, result, "Success", null, totalPages, totalElements);
    }

    public static <T> IResultDTO<List<T>> ok(Page<T> result, Map<String, String> metaData) {
        return ok(HttpStatus.OK, result, "Success", metaData);
    }

    public static <T> IResultDTO<List<T>> ok(
            HttpStatus statusCode, List<T> result, String message, Map<String, String> metaData
    ) {
        ResultDTO<List<T>> resultDTO = buildBaseListResponseData(statusCode, message, result);
        if (Objects.isNull(metaData)) {
            metaData = new HashMap<>();
        }

        metaData.put("totalData", String.valueOf(result.size()));
        resultDTO.setMetaData(metaData);

        return resultDTO;
    }

    private static <T> ResultDTO<List<T>> buildBaseListResponseData(
            HttpStatus statusCode, String message,
            List<T> result
    ) {
        ResponseData responseData = new ResponseData();
        responseData.setResponseCode(statusCode);
        responseData.setResponseMsg(message);

        ResultDTO<List<T>> resultDTO = new ResultDTO<>();
        resultDTO.setResult(result);
        resultDTO.setResponseData(responseData);

        return resultDTO;
    }

    public static <T> IResultDTO<List<T>> ok(List<T> result) {
        return ok(HttpStatus.OK, result, "Success", null);
    }

    public static <T> IResultDTO<T> noContent(T result) {
        return ok(HttpStatus.NO_CONTENT, result, "No Content", null);
    }

    public static <T> IResultDTO<T> noContent(T result, String message) {
        return ok(HttpStatus.NO_CONTENT, result, message, null);
    }

    private static <T> IResultDTO<T> prepareErrorDTO(
            HttpStatus statusCode,
            String errorMessage,
            T result,
            Exception exception,
            String message,
            HttpServletRequest request
    ) {
        return prepareErrorDTO(
                statusCode,
                errorMessage,
                result,
                exception,
                message,
                request != null ? request.getMethod() + " " + request.getRequestURI() : null,
                null
        );
    }

    private static <T> IResultDTO<T> prepareErrorDTO(
            HttpStatus statusCode,
            String errorMessage,
            T result,
            Exception exception,
            String message,
            HttpServletRequest request,
            Map<String, String> metadata
    ) {
        return prepareErrorDTO(
                statusCode,
                errorMessage,
                result,
                exception,
                message,
                request.getMethod() + " " + request.getRequestURI(),
                metadata
        );
    }

    private static <T> IResultDTO<T> prepareErrorDTO(
            HttpStatus statusCode,
            String errorMessage,
            T result,
            Exception exception,
            String message,
            String path,
            Map<String, String> metadata
    ) {
        BaseResultDTO<T, ResponseAPIErrorDTO> resultDTO = new BaseResultDTO<>();
        resultDTO.setResult(result);
        resultDTO.setResponseData(new ResponseAPIErrorDTO(
                new Date(),
                statusCode,
                Objects.isNull(exception) ?
                        null :
                        exception.getClass()
                                .getCanonicalName() + " :: " + exception.getMessage(),
                Strings.isBlank(message) &&
                        Objects.nonNull(exception) ?
                        exception.getMessage() : message,
                path,
                errorMessage
        ));

        if (Objects.nonNull(metadata)) {
            resultDTO.setMetaData(metadata);
        }

        return resultDTO;
    }

    public static <T> IResultDTO<T> notFound(
            T result, Exception exception, String message, HttpServletRequest request
    ) {
        return prepareErrorDTO(HttpStatus.NOT_FOUND, "Not Found", result, exception, message, request);
    }

    public static <T> IResultDTO<T> conflict(
            T result, Exception exception, String message, HttpServletRequest request
    ) {
        return prepareErrorDTO(HttpStatus.CONFLICT, "Conflict", result, exception, message, request);
    }

    public static <T> IResultDTO<T> generic(
            HttpStatus httpStatus,
            Exception exception,
            String message,
            HttpServletRequest request
    ) {
        return prepareErrorDTO(httpStatus, httpStatus.getReasonPhrase(), null, exception, message, request);
    }

    public static <T> IResultDTO<T> tooManyRequest(
            T result,
            Exception exception,
            String message,
            HttpServletRequest request,
            Map<String, String> metadata
    ) {
        return prepareErrorDTO(
                HttpStatus.TOO_MANY_REQUESTS,
                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                result,
                exception,
                message,
                request,
                metadata
        );
    }

    public static <T> IResultDTO<T> internalServerError(
            T result,
            Exception exception,
            String message,
            HttpServletRequest request
    ) {
        return prepareErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                result,
                exception,
                message,
                request
        );
    }

    public static <T> IResultDTO<T> badRequest(
            T result,
            Exception exception,
            String message,
            HttpServletRequest request
    ) {
        return prepareErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                result,
                exception,
                message,
                request
        );
    }

    public static <T> IResultDTO<T> badRequest(
            T result, Exception exception, String message, String path
    ) {
        return prepareErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                result,
                exception,
                message,
                path,
                null
        );
    }

    public static <T> IResultDTO<T> gone(T result, Exception exception, String message, HttpServletRequest request) {
        return prepareErrorDTO(HttpStatus.GONE, "Gone", result, exception, message, request);
    }

    public static <T> IResultDTO<T> serviceUnavailable(
            T result, Exception exception, String message, HttpServletRequest request
    ) {
        return prepareErrorDTO(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service Unavailable",
                result,
                exception,
                message,
                request
        );
    }

    public static <T> IResultDTO<T> unsupportedOperation(
            T result, Exception exception, String message, HttpServletRequest request
    ) {
        return prepareErrorDTO(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Unsupported Operation",
                result,
                exception,
                message,
                request
        );
    }

    public static <T> IResultDTO<List<T>> okDefault(List<T> response, HttpServletRequest request) {
        try {
            if (CollectionUtils.sizeIsEmpty(response)) {
                return APIResponseBuilder.noContent(new ArrayList<>());
            }
            return APIResponseBuilder.ok(response);
        } catch (HttpClientErrorException e) {
            return APIResponseBuilder.badRequest(new ArrayList<>(), e, e.getMessage(), request);
        }
    }
}

