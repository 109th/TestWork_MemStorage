package com.testwork.demo.controllers;

import com.testwork.demo.http.BaseResponse;
import com.testwork.demo.http.requests.ChangeWidgetRequest;
import com.testwork.demo.http.requests.CreateWidgetRequest;
import com.testwork.demo.http.responses.CreateWidgetResponse;
import com.testwork.demo.http.responses.WidgetListDto;
import com.testwork.demo.http.responses.WidgetListResponse;
import com.testwork.demo.http.responses.WidgetResponse;
import com.testwork.demo.models.Widget;
import com.testwork.demo.services.MemoryStorageInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class WidgetController {
    @Autowired
    private MemoryStorageInterface memoryStorage;

    @RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public BaseResponse Index() {
        return new BaseResponse("Hello there, general Kenobi");
    }

    @PostMapping(value = "/widget/create", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public BaseResponse WidgetCrate(@Valid @RequestBody CreateWidgetRequest request) {
        Widget widget = new Widget();
        BeanUtils.copyProperties(request, widget);
        widget.setLastModified(new Date(System.currentTimeMillis()));

        UUID uuid = memoryStorage.add(widget, request.getZIndex());

        return new CreateWidgetResponse(uuid);
    }

    @GetMapping(value = "/widget/get/{id}", produces = APPLICATION_JSON_VALUE)
    public BaseResponse WidgetGet(@PathVariable String id) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return new BaseResponse("UUID is incorrect", BaseResponse.ERROR_STATUS);
        }

        WidgetResponse response = new WidgetResponse();
        Widget widget = memoryStorage.getByUuid(uuid);
        if (widget != null) {
            BeanUtils.copyProperties(widget, response);
            response.setUuid(id);
        } else {
            return new BaseResponse("Widget not found", BaseResponse.ERROR_STATUS);
        }

        return response;
    }

    @PostMapping(value = "/widget/change/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public BaseResponse WidgetChange(@RequestBody ChangeWidgetRequest request, @PathVariable String id) {
        synchronized (id) {
            UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (Exception e) {
                return new BaseResponse("UUID is incorrect", BaseResponse.ERROR_STATUS);
            }

            Widget widget = memoryStorage.getByUuid(uuid);
            if (widget != null) {
                Widget changes = new Widget();
                BeanUtils.copyProperties(request, changes);
                widget.update(changes);
                if (request.getZIndex() != null) {
                    memoryStorage.updateZIndex(widget, request.getZIndex());
                }
            } else {
                return new BaseResponse("Widget not found", BaseResponse.ERROR_STATUS);
            }
        }
        return new BaseResponse("Widget changed");
    }

    @GetMapping(value = "/widget/list", produces = APPLICATION_JSON_VALUE)
    public WidgetListResponse WidgetList() {
        ArrayList<Widget> allWidgets = memoryStorage.getList();
        ArrayList<WidgetListDto> list = new ArrayList<>();

        for (Widget value : allWidgets) {
            WidgetListDto widgetListDto = new WidgetListDto();
            BeanUtils.copyProperties(value, widgetListDto);
            list.add(widgetListDto);
        }

        return new WidgetListResponse(list, allWidgets.size());
    }

    @DeleteMapping(value = "/widget/remove/{id}", produces = APPLICATION_JSON_VALUE)
    public BaseResponse WidgetRemove(@PathVariable String id) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return new BaseResponse("UUID is incorrect", BaseResponse.ERROR_STATUS);
        }

        memoryStorage.remove(uuid);

        return new BaseResponse("Widget was removed");
    }
}
