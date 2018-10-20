package com.majid.tree.controllers;

import com.majid.tree.domain.Node;
import com.majid.tree.services.ServiceResponse;
import com.majid.tree.services.NodeServiceImpl;
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

    @RequestMapping("/get/{nodeId}")
    public ResponseEntity<ServiceResponse<Node>> changeParent(@PathVariable Long nodeId){
        return ResponseEntity.ok(nodeService.getById(nodeId));
    }

    @RequestMapping("/children/{nodeId}")
    public ResponseEntity<ServiceResponse<List<Node>>> getChildren(@PathVariable Long nodeId){
        return ResponseEntity.ok(nodeService.getChildren(nodeId));
    }

    @RequestMapping("/allchildren/{nodeId}")
    public ResponseEntity<ServiceResponse<List<Node>>> getAllChildren(@PathVariable Long nodeId){
        return ResponseEntity.ok(nodeService.getAllChildren(nodeId));
    }

    @RequestMapping("/changeparent/{nodeId}/{parentId}")
    public ResponseEntity<ServiceResponse> changeParent(@PathVariable Long nodeId, @PathVariable Long parentId){
        return ResponseEntity.ok(nodeService.changeParent(nodeId, parentId));
    }

    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.PUT)
    public ResponseEntity<ServiceResponse<Node>> add(@PathVariable("parentId") Long parentId, @Valid @RequestBody Node node){
        return ResponseEntity.ok(nodeService.addNode(node, parentId));
    }
}
