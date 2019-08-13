package com.testwork.demo.http.responses;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class WidgetListDto {
    protected UUID uuid;
    protected Integer x;
    protected Integer y;
    protected Integer zIndex;
    protected Integer width;
    protected Integer height;
    protected Date lastModified;
}
