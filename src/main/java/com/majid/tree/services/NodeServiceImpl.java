package com.majid.tree.services;

import com.majid.tree.domain.Node;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Majid Ghaffuri on 10/19/2018.
 */
@Repository
@Transactional
public class NodeServiceImpl implements NodeService {

    @PersistenceContext
    private EntityManager entityManager;


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

    @Override
    public Node addNode(Node node, Long parentId) {
        Node parentNode = entityManager.find(Node.class, parentId);

        if (parentNode != null){
            node.setPath(parentNode.getChildrenPath());
        }

        entityManager.persist(node);
        return node;
    }

    @Override
    public List<Node> getChildren(Long nodeId) {
        Node node = entityManager.find(Node.class, nodeId);

        return entityManager.createQuery("SELECT e FROM Node e WHERE e.path = :childrenPath", Node.class)
                .setParameter("childrenPath", node.getChildrenPath())
                .getResultList();
    }

    @Override
    public List<Node> getAllChildren(Long nodeId) {
        Node node = entityManager.find(Node.class, nodeId);

        return entityManager.createNativeQuery("SELECT * FROM Node WHERE path LIKE :childrenPath || '%'", Node.class)
                .setParameter("childrenPath", node.getChildrenPath())
                .getResultList();
    }

    @Override
    public Node getById(Long nodeId) {
        return entityManager.find(Node.class, nodeId);
    }
}
