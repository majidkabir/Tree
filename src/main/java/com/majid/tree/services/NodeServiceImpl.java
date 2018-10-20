package com.majid.tree.services;

import com.majid.tree.domain.Node;
import com.majid.tree.repository.NodeRepository;
import com.majid.tree.util.ErrorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link NodeService} interface
 *
 * Created by Majid Ghaffuri on 10/19/2018.
 */
@Service
@Transactional
public class NodeServiceImpl implements NodeService {

    @Autowired
    NodeRepository nodeRepository;

    @Override
    public ServiceResponse changeParent(Long nodeId, Long newParentId) {
        Node node = nodeRepository.getNodeById(nodeId);
        Node newParentNode = nodeRepository.getNodeById(newParentId);

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

        nodeRepository.changeParent(oldPrefixPath, newPrefixPath);

        return new ServiceResponse<>();
    }


    @Override
    public ServiceResponse<Node> addNode(Node node, Long parentId) {
        if (node.getId() != null || node.getPath() != null){
            // This properties can not be set by api
            return new ServiceResponse<>().setErrorCode(ErrorHelper.THIS_PROPERTIES_SHOULD_BE_NULL);
        }

        if (parentId != 0 && nodeRepository.getNodeById(parentId) == null) {
            // Parent id not found
            return new ServiceResponse<>().setErrorCode(ErrorHelper.PARENT_NOT_FOUND);
        }

        if (parentId == 0 && nodeRepository.getRoot() != null){
            // You can not create 2 root node
            return new ServiceResponse<>().setErrorCode(ErrorHelper.ROOT_ALREADY_EXIST);
        }

        Node parentNode = nodeRepository.getNodeById(parentId);

        if (parentNode != null){
            node.setPath(parentNode.getChildrenPath());
        }

        nodeRepository.saveNode(node);

        return new ServiceResponse<>(node);
    }


    @Override
    public ServiceResponse<List<Node>> getChildren(Long nodeId) {
        Node node = nodeRepository.getNodeById(nodeId);

        if (node == null) {
            return new ServiceResponse<>().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        List<Node> childrenNodes = nodeRepository.getNodesByPath(node.getChildrenPath());

        if (childrenNodes == null) {
            childrenNodes = new ArrayList<>();
        }

        return new ServiceResponse<>(childrenNodes);
    }


    @Override
    public ServiceResponse<List<Node>> getAllChildrenByPath(Long nodeId) {
        Node node = nodeRepository.getNodeById(nodeId);

        if (node == null) {
            return new ServiceResponse<>().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        List<Node> childrenNodes = nodeRepository.getNodesAndChildrenByPath(node.getChildrenPath());

        if (childrenNodes == null) {
            childrenNodes = new ArrayList<>();
        }

        return new ServiceResponse<>(childrenNodes);
    }


    @Override
    public ServiceResponse<Node> getById(Long nodeId) {

        Node node = nodeRepository.getNodeById(nodeId);
        if (node == null) {
            return new ServiceResponse().setErrorCode(ErrorHelper.NODE_NOT_FOUND);
        }

        return new ServiceResponse<>(node);
    }
}
