package jupiterpa.course;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jupiterpa.course.domain.model.*;
import jupiterpa.course.intf.controller.Controller;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
@ActiveProfiles({"mock","test"})
public class IntegrationTest { 
	final String PATH = Controller.PATH; 

	@Autowired private MockMvc mockMvc;
	@Autowired private StudentRepo repo;
	
	@Before
	public void ResetDB() {
		repo.deleteAll();
	}
	
    
    @Test
    public void test() throws Exception {
//        ClientMocking mock = (ClientMocking) client;
//        mock.inject(new ArrayList<Student>());
//
//        Student entity = new Student("Testing");
//
////      Post
//    	mockMvc.perform( post(PATH).content(toJson(entity)).contentType(APPLICATION_JSON_UTF8) )
//        .andExpect(status().isOk())
//		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
//        .andExpect(jsonPath("$.value").value("TestingT"));
//    	
////      Get
//    	mockMvc.perform( get(PATH) )
//        .andExpect(status().isOk())
//		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
//        .andExpect(jsonPath("$.value").value("TestingT"));
//    	
    }
    private String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    
}