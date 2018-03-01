package jupiterpa.course;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;
import jupiterpa.course.domain.model.*;
import jupiterpa.course.domain.service.ReadUtility;
import jupiterpa.course.domain.service.FormatException;
import jupiterpa.course.domain.service.OptimizationService;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void File2Solution() throws Exception, FormatException {
		// Mock File
		MultipartFile student_file = getMultipartFile("student.csv");
		MultipartFile course_file = getMultipartFile("course.csv");
		MultipartFile fixedCourse_file = getMultipartFile("fixCourse.csv");
		MultipartFile sameCourse_file = getMultipartFile("sameCourse.csv");
		
		// Convert to Entities
		ReadUtility<Student> studentService = new ReadUtility<Student>();
		Collection<Student> students = studentService.read(student_file, args -> Student.read(args));
		
		ReadUtility<Course> courseService = new ReadUtility<Course>();
		Collection<Course> courses = courseService.read(course_file, args -> Course.read(args));
		
		ReadUtility<FixCourse> fixedService = new ReadUtility<FixCourse>();
		Collection<FixCourse> fixedCourses = fixedService.read(fixedCourse_file, args -> FixCourse.read(args));
		
		ReadUtility<SameCourse> sameCourseService = new ReadUtility<SameCourse>();
		Collection<SameCourse> sameCourses = sameCourseService.read(sameCourse_file, args -> SameCourse.read(args));
		
		// Ignore save and query from DB
		
		// Build Model
		Model model = new Model(students,courses,fixedCourses,sameCourses);
		
		// Solve
		model = OptimizationService.optimize(model);
		
		// Ignore save to DB

	    // Check Student
		assertThat( model.getnStudent(), is(3) );
		for (Student student : model.getStudents() ) {
			switch (student.getName()) {
				case "S1": assertThat( student.getCourse(), is("C1")); break;
				case "S2": assertThat( student.getCourse(), is("C1")); break;
				case "S3": assertThat( student.getCourse(), is("C3")); break;
			}
		}
		
		// Check Course
		assertThat( model.getnCourse(), is (3));
		for (Course course : model.getCourses()) {
			switch(course.getName()) {
			case "C1": assertThat( course.getBooked(), is(2) ); break;
			case "C2": assertThat( course.getBooked(), is(0) ); break;
			case "C3": assertThat( course.getBooked(), is(1) ); break;
			}
		}
		
		// Check FixCourse 
		assertThat( model.getFixedCourses().size(), is(1));
		for (FixCourse course : model.getFixedCourses()) {
			assertThat( course.getBookedCourse(), equalTo( course.getCourse() ) );
		}
		
		// CheckSameCourse
		assertThat( model.getSameCourses().size(), is(1));
		for (SameCourse course : model.getSameCourses()) {
			assertThat( course.getKurs1(), equalTo( course.getKurs2()));
		}
	}
	
}

