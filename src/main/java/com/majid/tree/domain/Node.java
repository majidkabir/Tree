package com.majid.tree.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * This class is represent the entity of node.
 *
 * Created by Majid Ghaffuri on 10/18/2018.
 */

@Entity
@Table(name="node")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    /* Path is a string that contains all ancestors ids from root to parent of this node */
    @Column(columnDefinition = "TEXT")
    private String path;

    public Node() {
    }

    public Node(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Long getParentId() {
        /* The last id in the path string is the parent id */

        if (path == null || path.isEmpty()) /* This is the root node and the root node don't has parent */
            return null;

        int index = path.lastIndexOf(',', path.length() - 2) + 1;

        return Long.valueOf(path.substring(index, path.length() - 1));
    }

    public Long getRootId() {
        /* The first id in the path string is the root id */

        if (path == null || path.isEmpty())
            return id; /* This is the root node */

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
        /* In the path property each id is separated by ',' character and an extra ',' character at the end of it,
           and root node path is null, with this definition height of a node is number of ',' character in the path.
           */
        return StringUtils.countOccurrencesOf(path, ",");
    }

    public String getChildrenPath() {
        /* Path of children of this node is path this node plus id of this node with an extra ',' character at the end */
        return (getPath() == null ? "": getPath()) + getId() + ",";
    }
}
