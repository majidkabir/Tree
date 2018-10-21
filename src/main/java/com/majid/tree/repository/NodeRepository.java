package com.majid.tree.repository;

import com.majid.tree.domain.Node;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * The repository class for {@link Node} entity
 *
 * Created by Majid Ghaffuri on 10/20/2018.
 */
@Repository
public class NodeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find the root node of the tree and return it, path of the root node is null.
     *
     * @return Root {@link Node} of the tree
     */
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
     * Get a {@link Node} by its Ids
     *
     * @param nodeId Id of the node
     * @return {@link Node}
     */
    public Node getNodeById(Long nodeId){
        return entityManager.find(Node.class, nodeId);
    }

    /**
     * Changing all node with path prefix of {@code oldPrefixPath} to {@code newPrefixPath}
     *
     * @param oldPrefixPath Old prefix of path nodes
     * @param newPrefixPath New prefix of path nodes
     * @return Number of node that affected by this query
     */
    public int changeParent(String oldPrefixPath, String newPrefixPath) {
        return entityManager.createNativeQuery("UPDATE node SET path = :newPrefixPath || SUBSTRING(path,:oldPrefixLen) WHERE path LIKE :oldPrefixPath || '%'")
                .setParameter("newPrefixPath", newPrefixPath)
                .setParameter("oldPrefixLen", oldPrefixPath.length() + 1)
                .setParameter("oldPrefixPath", oldPrefixPath)
                .executeUpdate();
    }

    /**
     * Persisting the node to database
     *
     * @param node {@link Node} to persist in database
     */
    public void saveNode(Node node){
        entityManager.persist(node);
    }

    /**
     * Get all sibling nodes that exactly have this path
     *
     * @param path Path of the nodes
     * @return List of the nodes that matches
     */
    public List<Node> getNodesByPath(String path){
        return entityManager.createQuery("SELECT e FROM Node e WHERE e.path = :parentPath", Node.class)
                .setParameter("parentPath", path)
                .getResultList();
    }

    /**
     * Get all nodes that the prefix of those matches this path
     *
     * @param path Path of the nodes
     * @return List of the nodes that matches
     */
    public List<Node> getNodesAndChildrenByPath(String path){
        return   entityManager.createQuery("SELECT e FROM Node e WHERE e.path LIKE :path", Node.class)
                .setParameter("path", path + '%')
                .getResultList();
    }
}
