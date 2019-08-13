package com.testwork.demo.http;

import lombok.Data;

@Data
public class BaseResponse {
    public static String SUCCESS_STATUS = "success";
    public static String ERROR_STATUS = "error";

    protected String status = SUCCESS_STATUS;

    protected String message;

    public BaseResponse() {
    }

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
