package com.majid.tree;

import com.majid.tree.domain.Node;
import com.majid.tree.repository.NodeRepository;
import com.majid.tree.services.NodeServiceImpl;
import com.majid.tree.services.ServiceResponse;
import com.majid.tree.util.Error;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.majid.tree.util.Error.NODE_NOT_FOUND;
import static com.majid.tree.util.Error.THIS_PROPERTIES_SHOULD_BE_NULL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by Majid Ghaffuri on 10/20/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class NodeServiceUnitTest {

    @Mock
    NodeRepository nodeRepositoryMock;

    @InjectMocks
    NodeServiceImpl nodeService;

    @Test
    public void testGetById(){
        when(nodeRepositoryMock.getNodeById(11L)).thenReturn(new Node("Test node", null));
        ServiceResponse<Node> serviceResponse1 = nodeService.getById(11L);
        Assert.assertEquals("Test node", serviceResponse1.getResponseObject().getName());
        Assert.assertEquals(0, serviceResponse1.getErrorCode());

        when(nodeRepositoryMock.getNodeById(234L)).thenReturn(null);
        ServiceResponse<Node> serviceResponse2 = nodeService.getById(234L);
        Assert.assertEquals(Error.NODE_NOT_FOUND.getCode(), serviceResponse2.getErrorCode());
    }

    @Test
    public void testAddNodeErrorSituations(){
        Node node1 = new Node("A", "1,");
        Assert.assertEquals(Error.THIS_PROPERTIES_SHOULD_BE_NULL.getCode(), nodeService.addNode(node1, 0L).getErrorCode());

        when(nodeRepositoryMock.getNodeById(anyLong())).thenReturn(null);
        Assert.assertEquals(Error.PARENT_NOT_FOUND.getCode(), nodeService.addNode(new Node(), 1L).getErrorCode());

        when(nodeRepositoryMock.getRoot()).thenReturn(new Node());
        Assert.assertEquals(Error.ROOT_ALREADY_EXIST.getCode(), nodeService.addNode(new Node("22", null), 0L).getErrorCode());
    }

    @Test
    public void testAddNodeCheckSetNodePath(){
        when(nodeRepositoryMock.getNodeById(0L)).thenReturn(null);
        when(nodeRepositoryMock.getRoot()).thenReturn(null);
        Assert.assertNull(nodeService.addNode(new Node(), 0L).getResponseObject().getPath());


        Node parentNode1 = new Node("P", "1,2,3,");
        parentNode1.setId(22L);
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(parentNode1);
        Assert.assertEquals("1,2,3,22,", nodeService.addNode(new Node(), 22L).getResponseObject().getPath());

        Node parentNode2 = new Node("P", null);
        parentNode2.setId(22L);
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(parentNode2);
        Assert.assertEquals("22,", nodeService.addNode(new Node(), 22L).getResponseObject().getPath());
    }

    @Test
    public void testAddNodeSuccessFirstRootNode(){
        when(nodeRepositoryMock.getRoot()).thenReturn(null);
        Assert.assertEquals(Error.SUCCESS.getCode(), nodeService.addNode(new Node(), 0L).getErrorCode());
    }

    @Test
    public void testChangeParentErrorSituations(){
        when(nodeRepositoryMock.getNodeById(1L)).thenReturn(new Node());
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(null);
        Assert.assertEquals(NODE_NOT_FOUND.getCode(), nodeService.changeParent(22L, 1L).getErrorCode());
        Assert.assertEquals(Error.PARENT_NOT_FOUND.getCode(), nodeService.changeParent(1L, 22L).getErrorCode());
    }

    @Test
    public void testGetChildrenErrorSituation(){
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(null);
        Assert.assertEquals(Error.NODE_NOT_FOUND.getCode(), nodeService.getChildren(22L).getErrorCode());
    }

    @Test
    public void testGetChildrenSuccess(){
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(new Node());
        when(nodeRepositoryMock.getNodesByPath(any())).thenReturn(null);
        Assert.assertEquals(0, nodeService.getChildren(22L).getResponseObject().size());

        List<Node> children = new ArrayList<>();
        children.add(new Node());
        children.add(new Node());
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(new Node());
        when(nodeRepositoryMock.getNodesByPath(any())).thenReturn(children);
        Assert.assertEquals(2, nodeService.getChildren(22L).getResponseObject().size());
    }

    @Test
    public void testGetAllChildrenSuccess(){
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(new Node());
        when(nodeRepositoryMock.getNodesAndChildrenByPath(any())).thenReturn(null);
        Assert.assertEquals(0, nodeService.getAllChildrenByPath(22L).getResponseObject().size());

        List<Node> children = new ArrayList<>();
        children.add(new Node());
        children.add(new Node());
        when(nodeRepositoryMock.getNodeById(22L)).thenReturn(new Node());
        when(nodeRepositoryMock.getNodesAndChildrenByPath(any())).thenReturn(children);
        Assert.assertEquals(2, nodeService.getAllChildrenByPath(22L).getResponseObject().size());
    }
}
