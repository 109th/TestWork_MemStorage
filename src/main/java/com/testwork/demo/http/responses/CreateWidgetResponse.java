package com.testwork.demo.http.responses;

import com.testwork.demo.http.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateWidgetResponse extends BaseResponse {
    private String uuid;

    public CreateWidgetResponse(String uuid) {
        this.uuid = uuid;
    }

    public CreateWidgetResponse(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public CreateWidgetResponse() {

    }
}
