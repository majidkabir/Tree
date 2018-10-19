package com.majid.tree.controllers;

import com.majid.tree.domain.Node;
import com.majid.tree.services.NodeServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Node>> getChildren(@PathVariable String nodeId){

        if (nodeService.getById(nodeId) == null){
            // Node was not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nodeService.getChildren(nodeId));
    }

    @RequestMapping("/changeparent/{nodeId}/{parentId}")
    public ResponseEntity<List<Node>> changeParent(@PathVariable String nodeId, @PathVariable String parentId){

        if (nodeService.getById(parentId) == null || nodeService.getById(nodeId) == null){
            // At least one of this node was not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nodeService.changeParent(nodeId, parentId));
    }

    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.PUT)
    public ResponseEntity<Node> add(@PathVariable("parentId") String parentId, @Valid @RequestBody Node node){

        if (node.get_id() != null || node.getPath() != null){
            // This properties can not be set by api
            return ResponseEntity.badRequest().build();
        }

        if (!parentId.equals("0") && nodeService.getById(parentId) == null) {
            // Parent id not found
            return ResponseEntity.notFound().build();
        }

        if (parentId.equals("0") && nodeService.getRoot() != null){
            // You can not create 2 root node
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(nodeService.addNode(node, parentId));
    }
}
