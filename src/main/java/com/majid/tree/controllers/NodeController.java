package com.majid.tree.controllers;

import com.majid.tree.domain.Node;
import com.majid.tree.services.NodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.IntBinaryOperator;

/**
 * Created by Majid Ghaffuri on 10/18/2018.
 */
@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeServiceImpl nodeService;

    @RequestMapping("/get/{nodeId}")
    public ResponseEntity<Node> changeParent(@PathVariable Long nodeId){

        Node node = nodeService.getById(nodeId);
        if ( node == null){
            // Node was not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(node);
    }


    @RequestMapping("/children/{nodeId}")
    public ResponseEntity<List<Node>> getChildren(@PathVariable Long nodeId){

        if (nodeService.getById(nodeId) == null){
            // Node not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nodeService.getChildren(nodeId));
    }


    @RequestMapping("/allchildren/{nodeId}")
    public ResponseEntity<List<Node>> getAllChildren(@PathVariable Long nodeId){

        if (nodeService.getById(nodeId) == null){
            // Node  not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nodeService.getAllChildren(nodeId));
    }


    @RequestMapping("/changeparent/{nodeId}/{parentId}")
    public ResponseEntity<Integer> changeParent(@PathVariable Long nodeId, @PathVariable Long parentId){

        if (nodeService.getById(parentId) == null || nodeService.getById(nodeId) == null){
            // At least one of this node not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nodeService.changeParent(nodeId, parentId));
    }


    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.PUT)
    public ResponseEntity<Node> add(@PathVariable("parentId") Long parentId, @Valid @RequestBody Node node){

        if (node.getId() != null || node.getPath() != null){
            // This properties can not be set by api
            return ResponseEntity.badRequest().build();
        }

        if (parentId != 0 && nodeService.getById(parentId) == null) {
            // Parent id not found
            return ResponseEntity.notFound().build();
        }

        if (parentId == 0 && nodeService.getRoot() != null){
            // You can not create 2 root node
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(nodeService.addNode(node, parentId));
    }
}
