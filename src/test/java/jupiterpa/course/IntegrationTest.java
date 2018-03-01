package jupiterpa.course;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jupiterpa.course.domain.model.*;
import jupiterpa.course.intf.controller.Controller;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
@ActiveProfiles({"mock","test"})
public class IntegrationTest { 
	final String PATH = Controller.PATH; 

	@Autowired private MockMvc mockMvc;
	@Autowired private StudentRepo studentRepo;
	@Autowired private CourseRepo  courseRepo;
	@Autowired private FixCourseRepo fixedCourseRepo;
	@Autowired private SameCourseRepo sameCourseRepo;
	@Autowired private ActionRepo actionRepo;
	 @Autowired
	    private WebApplicationContext webApplicationContext;

	@Before
	public void ResetDB() {
		studentRepo.deleteAll();
		courseRepo.deleteAll();
		sameCourseRepo.deleteAll();
		fixedCourseRepo.deleteAll();
		actionRepo.deleteAll();
	}
	
	final String workspace = "src/test/data/";
	private MockMultipartFile getMultipartFile(String filename) throws IOException {
		Path path = Paths.get(workspace+filename);
		byte[] file = null;
		file = Files.readAllBytes(path);
		MockMultipartFile multipart = new MockMultipartFile("file",filename,"text/csv",file);
		return multipart;
	}

    @Test
    public void studentUpload() throws Exception {
    	MockMultipartFile file = getMultipartFile("student.csv");
		
        mockMvc.perform(fileUpload("/api/upload/students")
                        .file(file)
                        .param("filename", "student.csv"))
                    .andExpect(status().is(302));
     	
//      Get
       mockMvc.perform( get("/api/students") )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[*].name", hasItems("S1","S2","S3")))
        ;
    	
    }
    @Test
    public void courseUpload() throws Exception {
		MockMultipartFile file = getMultipartFile("course.csv");
		
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/upload/courses")
                        .file(file)
                        .param("filename", "course.csv"))
                    .andExpect(status().is(302));
     	
//      Get
    	mockMvc.perform( get("/api/courses") )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[*].name", hasItems("C1","C2","C3")));
        ;
    	
    }
    @Test
    public void sameCourseUpload() throws Exception {
		MockMultipartFile file = getMultipartFile("sameCourse.csv");
		
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/upload/sameCourses")
                        .file(file)
                        .param("filename", "sameCourse.csv"))
                    .andExpect(status().is(302));
     	
//      Get
    	mockMvc.perform( get("/api/sameCourses") )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].student1").value("S1"))
        .andExpect(jsonPath("$[0].student2").value("S2"))
        ;
    }
    @Test
    public void fixCourseUpload() throws Exception {
		MockMultipartFile file = getMultipartFile("fixCourse.csv");
		
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/upload/fixedCourses")
                        .file(file)
                        .param("filename", "fixCourse.csv"))
                    .andExpect(status().is(302));
     	
//      Get
    	mockMvc.perform( get("/api/fixedCourses") )
        .andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$[0].student").value("S3"))
		.andExpect(jsonPath("$[0].course").value("C3"))
        ;
    	
    }
    
    private void upload(String file, String path) throws Exception {
		MockMultipartFile file_mock = getMultipartFile(file);
		
		String base = "/api/upload/";
        mockMvc.perform(
        	MockMvcRequestBuilders.fileUpload(base+path)
                                  .file(file_mock)
                                  .param("filename", file))
                                  .andExpect(status().is(302)
                        );

    	mockMvc.perform( get("/api/" + path) )
               .andExpect(status().isOk())
		       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        ;
    }
    
    @Test 
    public void uploadAndOptimize() throws Exception {
    	upload("student.csv","students");
    	upload("course.csv","courses");
    	upload("fixCourse.csv","fixedCourses");
    	upload("sameCourse.csv","sameCourses");
    	
    	mockMvc.perform( put("/api/optimize") )
        	   .andExpect(status().isOk());
    	
    	mockMvc.perform( get("/api/students") )
               .andExpect(status().isOk())
	           .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	           .andExpect(jsonPath("$[0].name").value("S1"))
	           .andExpect(jsonPath("$[1].name").value("S2"))
	           .andExpect(jsonPath("$[2].name").value("S3"))
	           .andExpect(jsonPath("$[0].course").value("C1"))
	           .andExpect(jsonPath("$[1].course").value("C1"))
	           .andExpect(jsonPath("$[2].course").value("C3"))
	           ;
    	
    	mockMvc.perform( get("/api/download/students") )
    	   .andExpect( status().isOk() )
    	   .andExpect( content().contentType("text/csv"));                                      
    	mockMvc.perform( get("/api/download/courses") )
	       .andExpect( status().isOk() )
	       .andExpect( content().contentType("text/csv"));                                      
    	mockMvc.perform( get("/api/download/fixedCourses") )
	       .andExpect( status().isOk() )
	       .andExpect( content().contentType("text/csv"));                                      
    	mockMvc.perform( get("/api/download/sameCourses") )
	       .andExpect( status().isOk() )
	       .andExpect( content().contentType("text/csv"));                                      
    }
    
    private String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    
}