package com.testwork.demo.services;

import com.testwork.demo.models.Widget;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MemoryStorage implements MemoryStorageInterface {
    private static MemoryStorage instance = null;

    private CopyOnWriteArrayList<Widget> mainStorage;
    private ConcurrentHashMap<String, Widget> secondaryStorage;

    public MemoryStorage() {
        mainStorage = new CopyOnWriteArrayList<Widget>();
        secondaryStorage = new ConcurrentHashMap<String, Widget>();
    }

    @PostConstruct
    private void PostConstruct() {
        MemoryStorage.instance = this;
    }

    public static MemoryStorageInterface getInstance() {
        return MemoryStorage.instance;
    }

    private UUID _add(Widget widget, Integer zIndex, boolean isUpdate) {
        if (!isUpdate) {
            UUID uuid = UUID.randomUUID();
            while (secondaryStorage.containsKey(uuid.toString())) {
                uuid = UUID.randomUUID();
            }
            widget.setUuid(uuid);
        }

        secondaryStorage.put(widget.getUuid().toString(), widget);
        mainStorage.add(zIndex, widget);

        return widget.getUuid();
    }

    @Override
    public UUID add(Widget widget) {
        return _add(widget, mainStorage.size(), false);
    }

    @Override
    public UUID add(Widget widget, Integer zIndex) {
        if (zIndex == null)
            zIndex = mainStorage.size();
        else if (zIndex < 0)
            zIndex = 0;
        else if (zIndex > mainStorage.size())
            zIndex = mainStorage.size();

        return _add(widget, zIndex, false);
    }

    @Override
    public void updateZIndex(Widget widget, Integer zIndex) {
        synchronized (widget) {
            int index = mainStorage.indexOf(widget);
            if (index != -1) {
                remove(widget);
                _add(widget, zIndex, true);
            }
        }
    }

    @Override
    public Integer count() {
        return mainStorage.size();
    }

    @Override
    public Widget getByUuid(UUID uuid) {
        return secondaryStorage.get(uuid.toString());
    }

    @Override
    public Integer getWidgetPosition(Widget widget) {
        return mainStorage.indexOf(widget);
    }

    @Override
    public ArrayList<Widget> getList() {
        return new ArrayList<>(mainStorage);
    }

    @Override
    public void remove(UUID uuid) {
        Widget widget = secondaryStorage.get(uuid.toString());
        if (widget != null) {
            mainStorage.remove(widget);
            secondaryStorage.remove(uuid.toString());
        }
    }

    @Override
    public void remove(Widget widget) {
        mainStorage.remove(widget);
        secondaryStorage.remove(widget.getUuid().toString());
    }
}
