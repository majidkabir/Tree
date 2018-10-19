package com.majid.tree.services;

import com.majid.tree.domain.Node;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
public interface NodeService {

    Node getRoot();

    List<Node> changeParent(String nodeId, String newParentId);

    Node addNode(Node node, String parentId);

    List<Node> getChildren(String nodeId);

    Node getById(String nodeId);

//    List<Node> listAll();
//
//
//    Node saveOrUpdate(Node node);
}
