package com.majid.tree.domain;

import org.springframework.util.StringUtils;

import javax.persistence.*;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */

@Entity
@Table(name="node")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String path;

    public Node() {
    }

    public Node(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Long getParentId() {
        if (path == null || path.isEmpty())
            return null;

        int index = path.lastIndexOf(',', path.length() - 2) + 1;

        return Long.valueOf(path.substring(index, path.length() - 1));
    }

    public Long getRootId() {
        if (path == null || path.isEmpty())
            return id; // This is the root node

        return Long.valueOf(path.substring(0, path.indexOf(',')));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return (getPath() == null ? "": getPath()) + getId() + ",";
    }
}
