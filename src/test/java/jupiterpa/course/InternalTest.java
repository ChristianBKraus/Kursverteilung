package jupiterpa.course;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;
import jupiterpa.course.domain.model.*;
import jupiterpa.course.domain.service.ReadService;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class InternalTest { 
	final String workspace = "src/test/data/";
	
	private MultipartFile getMultipartFile(String filename) throws IOException {
		Path path = Paths.get(workspace+filename);
		byte[] file = null;
		file = Files.readAllBytes(path);
		MultipartFile multipart = new MockMultipartFile(filename,filename,"text/plain",file);
		return multipart;
	}
	
	@Test
    public void simpleTest() throws Exception {
		// Mock File
		MultipartFile student_file = getMultipartFile("student.csv");
		MultipartFile course_file = getMultipartFile("course.csv");
		MultipartFile fixedCourse_file = getMultipartFile("fixCourse.csv");
		MultipartFile sameCourse_file = getMultipartFile("sameCourse.csv");
		
		// Convert to Entities
		ReadService<Student> studentService = new ReadService<Student>();
		Collection<Student> students = studentService.read(student_file, args -> Student.read(args));
		
		ReadService<Course> courseService = new ReadService<Course>();
		Collection<Course> courses = courseService.read(course_file, args -> Course.read(args));
		
		ReadService<FixCourse> fixedService = new ReadService<FixCourse>();
		Collection<FixCourse> fixedCourses = fixedService.read(fixedCourse_file, args -> FixCourse.read(args));
		
		ReadService<SameCourse> sameCourseService = new ReadService<SameCourse>();
		Collection<SameCourse> sameCourses = sameCourseService.read(sameCourse_file, args -> SameCourse.read(args));
		
		// Ignore save and query from DB
		
		// Build Model
		Model model = new Model(students, courses, fixedCourses, sameCourses);			
		
		// Solve
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();
	   
	    // Convert
		Map<String,String> result = model.convert2String(solution);
		
		// Check
		assertThat( result.size(), is(3) );
		assertThat( result.get("S1"), is("C1"));
		assertThat( result.get("S2"), is("C1"));
		assertThat( result.get("S3"), is("C3"));

	}
}

