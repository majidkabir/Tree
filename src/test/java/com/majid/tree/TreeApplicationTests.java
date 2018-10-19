package com.majid.tree;

import com.majid.tree.controllers.NodeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeApplicationTests {

    @Autowired
    private NodeController controller;

	@Test
	public void contextLoads() {
        Assert.notNull(controller, "Node ");
	}

}
