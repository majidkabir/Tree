package com.majid.tree.services;

import com.majid.tree.domain.Node;
import org.bson.BsonRegularExpression;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
@Service
public class NodeServiceImpl implements NodeService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NodeServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Node getRoot() {
        Query query = new Query();
        query.addCriteria(Criteria.where("path").exists(false));

        return mongoTemplate.findOne(query, Node.class);
    }

    @Override
    public List<Node> changeParent(String nodeId, String newParentId) {
        Node node = mongoTemplate.findById(nodeId, Node.class);
        Node newParentNode = mongoTemplate.findById(newParentId, Node.class);

        String oldPrefixPath = node.getChildrenPath();

        node.setParentId(newParentNode.get_id());
        node.setPath(newParentNode.getChildrenPath());

        String newPrefixPath = node.getChildrenPath();

        Query pathQuery = Query.query(new Criteria("path").regex(new BsonRegularExpression("^" + oldPrefixPath + ".*")));

        List<Node> nodes = new ArrayList<>();
        mongoTemplate.find(pathQuery, Node.class).forEach(new Consumer<Node>() {
            @Override
            public void accept(Node node) {
                node.setPath(node.getPath().replaceFirst(oldPrefixPath, newPrefixPath));
                mongoTemplate.save(node);
                nodes.add(node);
            }
        });

        mongoTemplate.save(node);
        nodes.add(0, node);
        return nodes;
    }

    @Override
    public Node addNode(Node node, String parentId) {
        Node parent = mongoTemplate.findById(parentId, Node.class);

        if (parent != null){
            if (parent.getPath() != null) {
                node.setPath(parent.getPath() + parent.get_id() + ",");
            } else {
                node.setPath(parent.get_id() + ",");
            }

            node.setParentId(parent.get_id());
        }

        mongoTemplate.save(node);

        return node;
    }

    @Override
    public List<Node> getChildren(String nodeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("parentId").is(nodeId));

        return mongoTemplate.find(query, Node.class);
    }

    @Override
    public Node getById(String nodeId) {
        return mongoTemplate.findById(nodeId, Node.class);
    }
}
