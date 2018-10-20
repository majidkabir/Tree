package com.majid.tree.services;

import com.majid.tree.domain.Node;
import com.majid.tree.util.ErrorHelper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a service class that manage all the operation on {@link Node} entity.
 *
 * Created by Majid Ghaffuri on 10/19/2018.
 */
@Repository
@Transactional
public class NodeServiceImpl implements NodeService {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Find the root node of the tree and return it, path of the root node is null.
     *
     * @return Root {@link Node} of the tree
     */
    private Node getRoot() {
        Node node = null;

        try {
            node = entityManager.createQuery("SELECT e FROM Node e WHERE e.path is null", Node.class)
                    .getSingleResult();
        } catch (NoResultException e){
            // Root node not exist
        }

        return node;
    }


    /**
     * Get a {@link Node} by its Ids
     *
     * @param nodeId Id of the node
     * @return {@link Node}
     */
    private Node getNodeById(Long nodeId){
        return entityManager.find(Node.class, nodeId);
    }


    /**
     * Changing parent of a node, that means, moving a subtree of main tree from one node to another node,
     * this method change the path of all nodes in the subtree.
     *
     * @param nodeId {@link Node} that we want to change the parent
     * @param newParentId New parent {@link Node}
     * @return {@link ServiceResponse} with errorCode zero for successful change
     */
    @Override
    public ServiceResponse changeParent(Long nodeId, Long newParentId) {
        Node node = entityManager.find(Node.class, nodeId);
        Node newParentNode = entityManager.find(Node.class, newParentId);

        if (node == null){
            // Node not found
            return new ServiceResponse<>().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        if (newParentNode == null) {
            // Parent node not found
            return new ServiceResponse<>().setErrorCode(ErrorHelper.PARENT_NOT_FOUND);
        }

        String oldPrefixPath = node.getChildrenPath();

        node.setPath(newParentNode.getChildrenPath());

        String newPrefixPath = node.getChildrenPath();

        int i = entityManager.createNativeQuery("UPDATE node SET path = :newPrefixPath || SUBSTRING(path,:oldPrefixLen) WHERE path LIKE :oldPrefixPath || '%'")
                .setParameter("newPrefixPath", newPrefixPath)
                .setParameter("oldPrefixLen", oldPrefixPath.length() + 1)
                .setParameter("oldPrefixPath", oldPrefixPath)
                .executeUpdate();
        return new ServiceResponse<>();
    }


    /**
     * Create a new {@link Node} as a child of another node.
     *
     * @param node New {@link Node}, just has {@code name} property
     * @param parentId Id of parent node.
     * @return {@link ServiceResponse} that contains created {@link Node} with assigned {@code id} and {@code path}
     */
    @Override
    public ServiceResponse<Node> addNode(Node node, Long parentId) {
        if (node.getId() != null || node.getPath() != null){
            // This properties can not be set by api
            return new ServiceResponse<>().setErrorCode(ErrorHelper.THIS_PROPERTIES_SHOULD_BE_NULL);
        }

        if (parentId != 0 && getNodeById(parentId) == null) {
            // Parent id not found
            return new ServiceResponse<>().setErrorCode(ErrorHelper.PARENT_NOT_FOUND);
        }

        if (parentId == 0 && getRoot() != null){
            // You can not create 2 root node
            return new ServiceResponse<>().setErrorCode(ErrorHelper.ROOT_ALREADY_EXIST);
        }

        Node parentNode = getNodeById(parentId);

        if (parentNode != null){
            node.setPath(parentNode.getChildrenPath());
        }

        entityManager.persist(node);

        return new ServiceResponse<>(node);
    }


    /**
     * Get first level children of a node.
     *
     * @param nodeId The id of the node that we want to get the children
     * @return {@link ServiceResponse} that contains {@link List} of {@link Node} those are children of the node
     */
    @Override
    public ServiceResponse<List<Node>> getChildren(Long nodeId) {
        Node node = getNodeById(nodeId);

        if (node == null) {
            return new ServiceResponse<>().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        List<Node> childrenNodes = entityManager.createQuery("SELECT e FROM Node e WHERE e.path = :childrenPath", Node.class)
                .setParameter("childrenPath", node.getChildrenPath())
                .getResultList();

        if (childrenNodes == null) {
            childrenNodes = new ArrayList<>();
        }

        return new ServiceResponse<>(childrenNodes);
    }

    /**
     * Get all children of a node, in all levels.
     *
     * @param nodeId The id of the node that we want to get the children
     * @return {@link ServiceResponse} that contains {@link List} of {@link Node} those are children of the node
     */
    @Override
    public ServiceResponse<List<Node>> getAllChildren(Long nodeId) {
        Node node = getNodeById(nodeId);

        if (node == null) {
            return new ServiceResponse<>().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        List<Node> childrenNodes =  entityManager.createQuery("SELECT e FROM Node e WHERE e.path LIKE :childrenPath", Node.class)
                .setParameter("childrenPath", node.getChildrenPath() + '%')
                .getResultList();

        if (childrenNodes == null) {
            childrenNodes = new ArrayList<>();
        }

        return new ServiceResponse<>(childrenNodes);
    }


    /**
     * Get a {@link Node} by its Ids
     *
     * @param nodeId Id of the node
     * @return {@link ServiceResponse} that contains a {@link Node}
     */
    @Override
    public ServiceResponse<Node> getById(Long nodeId) {

        Node node = getNodeById(nodeId);
        if (node == null) {
            return new ServiceResponse().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        return new ServiceResponse<>(node);
    }
}
