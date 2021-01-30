package com.e.scrolltabdemo.bean;

import java.io.Serializable;

/**
 * Created by weioule
 * on 2021/1/2.
 */
public class SelectItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private Object object;
    private boolean isSelected = false;
    private int type;
    private float alpha;
    private float y;
    private float verticalOffse;

    public SelectItem(String id, String title, Object object, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.object = object;
        this.isSelected = isSelected;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getY() {
        return y;
    }

    public float getVerticalOffse() {
        return verticalOffse;
    }

    public void setVerticalOffse(float verticalOffse) {
        this.verticalOffse = verticalOffse;
    }
}
