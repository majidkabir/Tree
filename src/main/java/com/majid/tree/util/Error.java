package com.majid.tree.util;

/**
 * enum class for handling error code and error messages
 * Created by Majid Ghaffuri on 10/21/2018.
 */
public enum  Error {
    SUCCESS(0, "Success"),
    NODE_NOT_FOUND(1, "Node not found!"),
    PARENT_NOT_FOUND(1, "Parent node not found!"),
    ROOT_ALREADY_EXIST(1, "One root node already exist, and you can not create new root node!"),
    THIS_PROPERTIES_SHOULD_BE_NULL(1, "This properties can not set by user!"),;

    private final int code;
    private final String description;

    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
