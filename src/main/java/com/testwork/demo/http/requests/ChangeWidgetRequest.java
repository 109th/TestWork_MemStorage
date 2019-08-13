package com.testwork.demo.http.requests;

import lombok.Data;

@Data
public class ChangeWidgetRequest {
    private Integer x;

    private Integer y;

    private Integer zIndex;

    private Integer width;

    private Integer height;
}
