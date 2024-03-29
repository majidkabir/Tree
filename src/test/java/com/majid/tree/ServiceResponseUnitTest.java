package com.majid.tree;

import com.majid.tree.domain.Node;
import com.majid.tree.services.ServiceResponse;
import com.majid.tree.util.Error;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Majid Ghaffuri on 10/21/2018.
 */
public class ServiceResponseUnitTest {

    @Test
    public void testResponseObjectType() {
        Assert.assertThat(new ServiceResponse<Node>(new Node()).getResponseObject(), Matchers.instanceOf(Node.class));
    }

    @Test
    public void testErrorCodeProperty() {
        Assert.assertEquals(ServiceResponse.Error(Error.NODE_NOT_FOUND), Error.NODE_NOT_FOUND);
    }

    @Test
    public void testErrorMessageProperty() {
        Assert.assertEquals(ServiceResponse.Error(Error.NODE_NOT_FOUND), Error.NODE_NOT_FOUND);
    }
}
