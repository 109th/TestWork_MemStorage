package com.testwork.demo.services;

import com.testwork.demo.models.Widget;

import java.util.ArrayList;
import java.util.UUID;

public interface MemoryStorageInterface {
    static MemoryStorageInterface getInstance() {
        return null;
    }

    UUID add(Widget widget);

    UUID add(Widget widget, Integer zIndex);

    void updateZIndex(Widget widget, Integer zIndex);

    Integer count();

    Widget getByUuid(UUID uuid);

    ArrayList<Widget> getList();

    Integer getWidgetPosition(Widget widget);

    void remove(UUID uuid);

    void remove(Widget widget);
}
