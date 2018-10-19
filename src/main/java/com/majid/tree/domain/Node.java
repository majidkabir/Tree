package com.majid.tree.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
@Document(collection = "node")
public class Node {

    @Id
    private String  _id;
    private String name;
    private String path;
    private String parentId;

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHeight() {
        return StringUtils.countOccurrencesOf(path, ",");
    }

    public String getChildrenPath() {
        return (getPath() == null ? "": getPath()) + get_id() + ",";
    }
}
