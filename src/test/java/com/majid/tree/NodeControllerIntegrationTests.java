package com.majid.tree;

import com.majid.tree.services.NodeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test should be run on an empty database and order of the test methods is important
 *
 * Created by Majid Ghaffuri on 10/19/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NodeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NodeService nodeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void clearNodeTable(){
        entityManager.createNativeQuery("DELETE FROM node").executeUpdate();
    }

    @Test
    public void testAddRootNode() throws Exception{
        mockMvc.perform(put("/node/add/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.id").isNumber())
                .andExpect(jsonPath("$.responseObject.name").value("A"))
                .andExpect(jsonPath("$.responseObject.path").isEmpty())
                .andExpect(jsonPath("$.responseObject.parentId").isEmpty());
    }

    @Test
    public void testAddNodeWithParent() throws Exception{
        entityManager.createNativeQuery("INSERT INTO node (id, name) VALUES (10, 'A')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (11, 'B', '10,')").executeUpdate();

        mockMvc.perform(put("/node/add/11")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.id").isNumber())
                .andExpect(jsonPath("$.responseObject.name").value("A"))
                .andExpect(jsonPath("$.responseObject.path").value("10,11,"))
                .andExpect(jsonPath("$.responseObject.parentId").value(11))
                .andExpect(jsonPath("$.responseObject.rootId").value(10));
    }

    @Test
    public void testFindNodeById() throws Exception{
        entityManager.createNativeQuery("INSERT INTO node (id, name) VALUES (10, 'A')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (11, 'B', '10,')").executeUpdate();

        mockMvc.perform(get("/node/get/11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.id").value(11))
                .andExpect(jsonPath("$.responseObject.name").value("B"))
                .andExpect(jsonPath("$.responseObject.path").value("10,"))
                .andExpect(jsonPath("$.responseObject.parentId").value(10))
                .andExpect(jsonPath("$.responseObject.rootId").value(10));
    }

    @Test
    public void testGetChildren() throws Exception{
        entityManager.createNativeQuery("INSERT INTO node (id, name) VALUES (10, 'A')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (11, 'B1', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (12, 'B2', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (13, 'B3', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (14, 'B4', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (15, 'B5', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (16, 'C', '11,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (17, 'C', '11,')").executeUpdate();

        mockMvc.perform(get("/node/children/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject", hasSize(5)));
    }

    @Test
    public void testGetAllChildren() throws Exception{
        entityManager.createNativeQuery("INSERT INTO node (id, name) VALUES (10, 'A')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (11, 'B1', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (12, 'B2', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (13, 'B3', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (14, 'B4', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (15, 'B5', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (16, 'C', '10,11,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (17, 'C', '10,11,')").executeUpdate();

        mockMvc.perform(get("/node/allchildren/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject", hasSize(7)));
    }

    @Test
    public void testChangeParent() throws Exception{
        entityManager.createNativeQuery("INSERT INTO node (id, name) VALUES (10, 'A')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (11, 'AB1', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (12, 'AB2', '10,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (13, 'AB1C1', '10,11,')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO node (id, name, path) VALUES (14, 'AB1C2', '10,11,')").executeUpdate();

        mockMvc.perform(get("/node/changeparent/11/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(0))
                .andReturn();

        mockMvc.perform(get("/node/get/14"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.path").value("10,12,11,"));
    }
}
