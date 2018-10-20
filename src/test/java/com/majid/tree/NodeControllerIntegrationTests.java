package com.majid.tree;

import com.majid.tree.services.NodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test should be run on an empty database and order of the test methods is important
 *
 * Created by Majid Ghaffuri on 10/19/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NodeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NodeService nodeService;

    @Test
    public void test() throws Exception{
        mockMvc.perform(put("/node/add/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.name").value("A"))
                .andReturn();
    }
}
