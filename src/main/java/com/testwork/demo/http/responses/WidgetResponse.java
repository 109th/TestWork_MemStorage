package com.testwork.demo.http.responses;

import com.testwork.demo.http.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class WidgetResponse extends BaseResponse {
    protected String uuid;
    protected Integer x;
    protected Integer y;
    protected Integer zIndex;
    protected Integer width;
    protected Integer height;
    protected Date lastModified;
}
