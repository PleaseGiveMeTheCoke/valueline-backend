package com.valueline.client.domain.common;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private boolean success;
    private String message;

    public static <R> Result<R> success(R data) {
        Result<R> result = new Result<>();
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static <R> Result<R> fail(String msg) {
        Result<R> result = new Result<>();
        result.setMessage(msg);
        result.setSuccess(false);
        return result;
    }

}
