package com.majid.tree.services;

import com.majid.tree.domain.Node;

import java.util.List;

/**
 * This class is a service class that manages all the operation on {@link Node} an entity.
 *
 * Created by Majid Ghaffuri on 10/18/2018.
 */
public interface NodeService {

    /**
     * Changing parent of a node, that means, moving a subtree of main tree from one node to another node,
     * this method changes the path of all nodes in the subtree.
     *
     * @param nodeId {@link Node} that we want to change the parent
     * @param newParentId New parent {@link Node}
     * @return {@link ServiceResponse} with errorCode zero for successful change
     */
    ServiceResponse<Integer> changeParent(Long nodeId, Long newParentId);

    /**
     * Create a new {@link Node} as a child of another node.
     *
     * @param node New {@link Node}, just has {@code name} property
     * @param parentId Id of parent node.
     * @return {@link ServiceResponse} that contains created {@link Node} with assigned {@code id} and {@code path}
     */
    ServiceResponse<Node> addNode(Node node, Long parentId);

    /**
     * Get first level children of a node.
     *
     * @param nodeId The ID of the parent node that we want to get the children
     * @return {@link ServiceResponse} that contains {@link List} of {@link Node} those are children of the node
     */
    ServiceResponse<List<Node>> getChildren(Long nodeId);

    /**
     * Get all children of a node, in all levels.
     *
     * @param nodeId The ID of the parent node that we want to get the children
     * @return {@link ServiceResponse} that contains {@link List} of {@link Node} those are children of the node
     */
    ServiceResponse<List<Node>> getAllChildrenByPath(Long nodeId);

    /**
     * Get a {@link Node} by its Id
     *
     * @param nodeId Id of the node
     * @return {@link ServiceResponse} that contains a {@link Node}
     */
    ServiceResponse<Node> getById(Long nodeId);
}
