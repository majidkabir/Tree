package com.majid.tree.services;

import com.majid.tree.domain.Node;

import java.util.List;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
public interface NodeService {

    Node getRoot();

    int changeParent(Long nodeId, Long newParentId);

    Node addNode(Node node, Long parentId);

    List<Node> getChildren(Long nodeId);

    List<Node> getAllChildren(Long nodeId);

    Node getById(Long nodeId);
}
