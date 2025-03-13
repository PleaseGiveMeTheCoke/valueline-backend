package com.valueline.client.common;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private boolean success;
    private String message;
}
