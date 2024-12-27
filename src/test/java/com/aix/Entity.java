package com.aix;

import com.aix.annotation.IgnoreField;

import java.util.List;

public class Entity {

    private int id;
    private String key;
    private String value;
    @IgnoreField
    private List<Entity> children;

    public int getId() {
        return id;
    }

    public Entity setId(int id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Entity setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Entity setValue(String value) {
        this.value = value;
        return this;
    }

    public List<Entity> getChildren() {
        return children;
    }

    public Entity setChildren(List<Entity> children) {
        this.children = children;
        return this;
    }

}
