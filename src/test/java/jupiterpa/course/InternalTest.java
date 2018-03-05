package jupiterpa.course;

import org.junit.*;
import jupiterpa.course.domain.model.*;
import jupiterpa.course.domain.service.ReadUtility;
import jupiterpa.course.domain.service.FormatException;
import jupiterpa.course.domain.service.OptimizationService;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class InternalTest {
	final String workspace = "src/test/data/";

	Collection<Student> students = new ArrayList<>();
	Collection<Course> courses = new ArrayList<Course>();
	Collection<SameCourse> sameCourses = new ArrayList<SameCourse>();
	Collection<FixCourse> fixedCourses = new ArrayList<FixCourse>();
	Collection<String> messages = new ArrayList<>();
	
	@Before
	public void refresh() {
		students.clear();
		courses.clear();
		sameCourses.clear();
		fixedCourses.clear();
		messages.clear();
	}

	private MultipartFile getMultipartFile(String filename) throws IOException {
		Path path = Paths.get(workspace + filename);
		byte[] file = null;
		file = Files.readAllBytes(path);
		MultipartFile multipart = new MockMultipartFile(filename, filename, "text/plain", file);
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
		Model model = new Model(students, courses, fixedCourses, sameCourses);

		// Solve
		model = OptimizationService.optimize(model);

		// Ignore save to DB

		// Check Student
		assertThat(model.getnStudent(), is(3));
		for (Student student : model.getStudents()) {
			switch (student.getName()) {
			case "S1":
				assertThat(student.getCourse(), is("C1"));
				break;
			case "S2":
				assertThat(student.getCourse(), is("C1"));
				break;
			case "S3":
				assertThat(student.getCourse(), is("C3"));
				break;
			}
		}

		// Check Course
		assertThat(model.getnCourse(), is(3));
		for (Course course : model.getCourses()) {
			switch (course.getName()) {
			case "C1":
				assertThat(course.getBooked(), is(2));
				break;
			case "C2":
				assertThat(course.getBooked(), is(0));
				break;
			case "C3":
				assertThat(course.getBooked(), is(1));
				break;
			}
		}

		// Check FixCourse
		assertThat(model.getFixedCourses().size(), is(1));
		for (FixCourse course : model.getFixedCourses()) {
			assertThat(course.getBookedCourse(), equalTo(course.getCourse()));
		}

		// CheckSameCourse
		assertThat(model.getSameCourses().size(), is(1));
		for (SameCourse course : model.getSameCourses()) {
			assertThat(course.getCourse1(), equalTo(course.getCourse2()));
		}
	}

	@Test(expected = FormatException.class)
	public void uploadStudentException() throws IOException, FormatException {
		MultipartFile file = getMultipartFile("student_error.csv");

		// Convert to Entities
		ReadUtility<Student> service = new ReadUtility<Student>();
		Collection<Student> entries = service.read(file, args -> Student.read(args));
	}

	@Test(expected = FormatException.class)
	public void uploadCoursetException() throws IOException, FormatException {
		MultipartFile file = getMultipartFile("course_error.csv");

		// Convert to Entities
		ReadUtility<Course> service = new ReadUtility<Course>();
		Collection<Course> entries = service.read(file, args -> Course.read(args));
	}

	@Test(expected = FormatException.class)
	public void uploadFixedCourseException() throws IOException, FormatException {
		MultipartFile file = getMultipartFile("fixCourse_error.csv");

		// Convert to Entities
		ReadUtility<FixCourse> service = new ReadUtility<FixCourse>();
		Collection<FixCourse> entries = service.read(file, args -> FixCourse.read(args));
	}

	@Test(expected = FormatException.class)
	public void uploadSameCourseException() throws IOException, FormatException {
		MultipartFile file = getMultipartFile("sameCourse_error.csv");

		// Convert to Entities
		ReadUtility<SameCourse> service = new ReadUtility<SameCourse>();
		Collection<SameCourse> entries = service.read(file, args -> SameCourse.read(args));
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void check() {
		
		boolean exception = false;
		try {
			Model model = new Model(students, courses, fixedCourses, sameCourses);
		} catch (FormatException ex) {
			exception = true;
			
			assertThat(ex.getErrors().size(),is(messages.size()));
			assertThat(ex.getErrors(),anyOf(equalTo(messages)));
		}
		;
		assertThat(exception, is(true));

	}

	@Test
	public void optimizeWithoutData() throws IOException, FormatException {
		
		messages.add("Keine Studenten");
		messages.add("Keine Kurse");

		check();
	}

	@Test
	public void optimizeWithoutStudents() throws IOException, FormatException {
		
		students.add(new Student(0,"S1","C1","C1","C1"));
		
		messages.add("Keine Kurse");
		messages.add("Kurs C1 (Kurs 1) von S1 existiert nicht");
		messages.add("Kurs C1 (Kurs 2) von S1 existiert nicht");
		messages.add("Kurs C1 (Kurs 3) von S1 existiert nicht");

		check();
	}
	
//	@Test
//	public void multipleActions() {
//		assert(false);
//	}
}
