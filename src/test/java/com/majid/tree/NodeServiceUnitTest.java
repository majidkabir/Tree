package com.majid.tree;

import com.majid.tree.domain.Node;
import com.majid.tree.services.NodeService;
import com.majid.tree.services.NodeServiceImpl;
import com.majid.tree.services.ServiceResponse;
import com.majid.tree.util.ErrorHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * Created by Majid Ghaffuri on 10/20/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class NodeServiceUnitTest {

    @Mock
    EntityManager entityManagerMock;

    @Mock
    TypedQuery typedQueryMock;

    @InjectMocks
    NodeServiceImpl nodeService;

    @Test
    public void testGetById(){
        when(entityManagerMock.find(any(), anyLong())).thenReturn(new Node("Test node", null));
        ServiceResponse<Node> serviceResponse1 = nodeService.getById(11L);
        Assert.assertEquals("Test node", serviceResponse1.getResponseObject().getName());
        Assert.assertEquals(0, serviceResponse1.getErrorCode());

        when(entityManagerMock.find(any(), anyLong())).thenReturn(null);
        ServiceResponse<Node> serviceResponse2 = nodeService.getById(234L);
        Assert.assertEquals(ErrorHelper.NODE_NOT_FOUND, serviceResponse2.getErrorCode());
    }

    @Test
    public void testAddNodeErrorSituations(){
        Node node1 = new Node("A", "1,");
        Assert.assertEquals(ErrorHelper.THIS_PROPERTIES_SHOULD_BE_NULL, nodeService.addNode(node1, 0L).getErrorCode());

        when(entityManagerMock.find(any(), anyLong())).thenReturn(null);
        Assert.assertEquals(ErrorHelper.PARENT_NOT_FOUND, nodeService.addNode(new Node(), 1L).getErrorCode());

        when(typedQueryMock.getSingleResult()).thenReturn(new Node());
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(ErrorHelper.ROOT_ALREADY_EXIST, nodeService.addNode(new Node("22", null), 0L).getErrorCode());
    }

    @Test
    public void testAddNodeCheckSetNodePath(){
        when(typedQueryMock.getSingleResult()).thenReturn(null);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);

        Assert.assertNull(nodeService.addNode(new Node(), 0L).getResponseObject().getPath());


        Node parentNode1 = new Node("P", "1,2,3,");
        parentNode1.setId(22L);
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(parentNode1);
        Assert.assertEquals("1,2,3,22,", nodeService.addNode(new Node(), 22L).getResponseObject().getPath());

        Node parentNode2 = new Node("P", null);
        parentNode2.setId(22L);
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(parentNode2);
        Assert.assertEquals("22,", nodeService.addNode(new Node(), 22L).getResponseObject().getPath());
    }

    @Test
    public void testAddNodeSuccessFirstRootNode(){
        when(typedQueryMock.getSingleResult()).thenReturn(null);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(ErrorHelper.SUCCESS, nodeService.addNode(new Node(), 0L).getErrorCode());
    }

    @Test
    public void testChangeParentErrorSituations(){
        when(entityManagerMock.find(Node.class, 1L)).thenReturn(new Node());
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(null);
        Assert.assertEquals(ErrorHelper.NODE_NOT_FOUND, nodeService.changeParent(22L, 1L).getErrorCode());
        Assert.assertEquals(ErrorHelper.PARENT_NOT_FOUND, nodeService.changeParent(1L, 22L).getErrorCode());
    }

    @Test
    public void testGetChildrenErrorSituation(){
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(null);
        Assert.assertEquals(ErrorHelper.NODE_NOT_FOUND, nodeService.getChildren(22L).getErrorCode());
    }

    @Test
    public void testGetChildrenSuccess(){
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(new Node());
        when(typedQueryMock.getResultList()).thenReturn(null);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(0, nodeService.getChildren(22L).getResponseObject().size());

        List<Node> children = new ArrayList<>();
        children.add(new Node());
        children.add(new Node());
        when(typedQueryMock.getResultList()).thenReturn(children);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(2, nodeService.getChildren(22L).getResponseObject().size());
    }

    @Test
    public void testGetAllChildrenSuccess(){
        when(entityManagerMock.find(Node.class, 22L)).thenReturn(new Node());
        when(typedQueryMock.getResultList()).thenReturn(null);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(0, nodeService.getAllChildren(22L).getResponseObject().size());

        List<Node> children = new ArrayList<>();
        children.add(new Node());
        children.add(new Node());
        when(typedQueryMock.getResultList()).thenReturn(children);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(anyString(), any())).thenReturn(typedQueryMock);
        Assert.assertEquals(2, nodeService.getAllChildren(22L).getResponseObject().size());
    }
}
