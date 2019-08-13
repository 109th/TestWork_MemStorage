package com.testwork.demo.models;

import com.testwork.demo.services.MemoryStorage;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode
public class Widget {
    protected UUID uuid;
    protected Integer x;
    protected Integer y;
    protected Integer width;
    protected Integer height;
    protected Date lastModified;

    private final Object lock = new Object();

    public synchronized int getZIndex() {
        return MemoryStorage.getInstance().getWidgetPosition(this);
    }

    public synchronized UUID getUuid() {
        return uuid;
    }

    public synchronized void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public synchronized Integer getX() {
        return x;
    }

    public synchronized void setX(Integer x) {
        this.x = x;
    }

    public synchronized Integer getY() {
        return y;
    }

    public synchronized void setY(Integer y) {
        this.y = y;
    }

    public synchronized Integer getWidth() {
        return width;
    }

    public synchronized void setWidth(Integer width) {
        this.width = width;
    }

    public synchronized Integer getHeight() {
        return height;
    }

    public synchronized void setHeight(Integer height) {
        this.height = height;
    }

    public synchronized Date getLastModified() {
        return lastModified;
    }

    public synchronized void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public synchronized void update(Widget widget) {
        this.setX(widget.getX() != null ? widget.getX() : this.getX());
        this.setY(widget.getY() != null ? widget.getY() : this.getY());
        this.setWidth(widget.getWidth() != null ? widget.getWidth() : this.getWidth());
        this.setHeight(widget.getHeight() != null ? widget.getHeight() : this.getHeight());
        this.setLastModified(new Date(System.currentTimeMillis()));
    }
}
