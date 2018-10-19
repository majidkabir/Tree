package com.majid.tree.controllers;

import com.majid.tree.domain.Node;
import com.majid.tree.services.NodeServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeServiceImpl nodeService;

    @RequestMapping("/children/{nodeId}")
    public List<Node> getChildren(@PathVariable String nodeId){
        return nodeService.getChildren(nodeId);
    }

    @RequestMapping("/changeparent/{nodeId}/{parentId}")
    public List<Node> changeParent(@PathVariable String nodeId, @PathVariable String parentId){
        return nodeService.changeParent(nodeId, parentId);
    }

    @RequestMapping(value = "/add/{id}", method = RequestMethod.PUT)
    public Node add(@PathVariable("id") String parentId, @Valid @RequestBody Node node){
        return nodeService.addNode(node, parentId);
    }
}
