package com.majid.tree;

import com.majid.tree.domain.Node;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Majid Ghaffuri on 10/20/2018.
 */
public class NodeUnitTest {

    @Test
    public void testGetHeight(){
        Assert.assertEquals(0 , new Node("A", null).getHeight());
        Assert.assertEquals(1 , new Node("A", "1,").getHeight());
        Assert.assertEquals(5 , new Node("A", "1,238476,321,2,23,").getHeight());
    }

    @Test
    public void testGetRootIdWithRootNode(){
        Node node1 = new Node("A", null);
        node1.setId(2L);
        Assert.assertEquals(2L , node1.getRootId().longValue());

        Node node2 = new Node("A", "");
        node2.setId(23L);
        Assert.assertEquals(23L , node2.getRootId().longValue());
    }

    @Test
    public void testGetRootIdWithChildrenNodes(){
        Assert.assertEquals(1L , new Node("A", "1,").getRootId().longValue());
        Assert.assertEquals(123L , new Node("A", "123,1223,345,987,").getRootId().longValue());
    }

    @Test
    public void testGetParentIdWithRootNode() {
        Assert.assertEquals(null , new Node("A", null).getParentId());
        Assert.assertEquals(null , new Node("A", "").getParentId());
    }

    @Test
    public void testGetParentWithChildrenNode() {
        Assert.assertEquals(223L , new Node("A", "223,").getParentId().longValue());
        Assert.assertEquals(55L , new Node("A", "345,234,54,223,55,").getParentId().longValue());
    }

    @Test
    public void testGetChildrenPath(){
        Node node1 = new Node("A", null);
        node1.setId(23L);
        Assert.assertEquals("23," , node1.getChildrenPath());

        Node node2 = new Node("A", "");
        node2.setId(232L);
        Assert.assertEquals("232," , node2.getChildrenPath());

        Node node3 = new Node("A", "34,");
        node3.setId(653L);
        Assert.assertEquals("34,653," , node3.getChildrenPath());

        Node node4 = new Node("A", "1,2,444444,5,12,3,");
        node4.setId(653L);
        Assert.assertEquals("1,2,444444,5,12,3,653," , node4.getChildrenPath());
    }
}
