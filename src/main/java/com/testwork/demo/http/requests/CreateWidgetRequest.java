package com.testwork.demo.http.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateWidgetRequest {
    @NotNull(message = "Attribute \"X\" can't be a null")
    private Integer x;

    @NotNull(message = "Attribute \"Y\" can't be a null")
    private Integer y;

    private Integer zIndex;

    @NotNull(message = "Attribute \"Width\" can't be a null")
    private Integer width;

    @NotNull(message = "Attribute \"Height\" can't be a null")
    private Integer height;
}
