package com.testwork.demo.http.responses;

import com.testwork.demo.http.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class WidgetListResponse extends BaseResponse {
    protected Integer size;
    protected ArrayList<WidgetListDto> list;

    public WidgetListResponse(ArrayList<WidgetListDto> list, int size) {
        this.list = list;
        this.size = size;
    }

    public WidgetListResponse() {

    }
}
