package com.majid.tree.services;

import com.majid.tree.domain.Node;

import java.util.List;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
public interface NodeService {

    ServiceResponse<Integer> changeParent(Long nodeId, Long newParentId);

    ServiceResponse<Node> addNode(Node node, Long parentId);

    ServiceResponse<List<Node>> getChildren(Long nodeId);

    ServiceResponse<List<Node>> getAllChildren(Long nodeId);

    ServiceResponse<Node> getById(Long nodeId);
}
