package com.majid.tree.services;

import com.majid.tree.domain.Node;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    @Override
    public Node getRoot() {
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
     * Changing parent of a node, that means, moving a subtree of main tree from one node to another node,
     * this method change the path of all nodes in the subtree.
     *
     * @param nodeId {@link Node} that we want to change the parent
     * @param newParentId New parent {@link Node}
     * @return Number of nodes that affected by this change and there path was changed.
     */
    @Override
    public int changeParent(Long nodeId, Long newParentId) {
        Node node = entityManager.find(Node.class, nodeId);
        Node newParentNode = entityManager.find(Node.class, newParentId);

        String oldPrefixPath = node.getChildrenPath();

        node.setPath(newParentNode.getChildrenPath());

        String newPrefixPath = node.getChildrenPath();

        return entityManager.createNativeQuery("UPDATE node SET path = :newPrefixPath || SUBSTRING(path,:oldPrefixLen) WHERE path LIKE :oldPrefixPath || '%'")
                .setParameter("newPrefixPath", newPrefixPath)
                .setParameter("oldPrefixLen", oldPrefixPath.length() + 1)
                .setParameter("oldPrefixPath", oldPrefixPath)
                .executeUpdate();
    }

    /**
     * Create a new {@link Node} as a child of another node.
     *
     * @param node New {@link Node}, just has {@code name} property
     * @param parentId Id of parent node.
     * @return Created {@link Node} with assigned {@code id} and {@code path}
     */
    @Override
    public Node addNode(Node node, Long parentId) {
        Node parentNode = entityManager.find(Node.class, parentId);

        if (parentNode != null){
            node.setPath(parentNode.getChildrenPath());
        }

        entityManager.persist(node);
        return node;
    }

    /**
     * Get first level children of a node.
     *
     * @param nodeId The id of the node that we want to get the children
     * @return {@link List} of {@link Node} that are the children of the node
     */
    @Override
    public List<Node> getChildren(Long nodeId) {
        Node node = entityManager.find(Node.class, nodeId);

        return entityManager.createQuery("SELECT e FROM Node e WHERE e.path = :childrenPath", Node.class)
                .setParameter("childrenPath", node.getChildrenPath())
                .getResultList();
    }

    /**
     * Get all children of a node, in all levels.
     *
     * @param nodeId The id of the node that we want to get the children
     * @return {@link List} of {@link Node} that are the children of the node
     */
    @Override
    public List<Node> getAllChildren(Long nodeId) {
        Node node = entityManager.find(Node.class, nodeId);

        return entityManager.createNativeQuery("SELECT * FROM Node WHERE path LIKE :childrenPath || '%'", Node.class)
                .setParameter("childrenPath", node.getChildrenPath())
                .getResultList();
    }

    /**
     * Get a {@link Node} by its Ids
     *
     * @param nodeId Id of the node
     * @return {@link Node}
     */
    @Override
    public Node getById(Long nodeId) {
        return entityManager.find(Node.class, nodeId);
    }
}
