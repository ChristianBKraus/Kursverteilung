package jupiterpa.course.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class UploadService {

	@Autowired StudentRepo studentRepo;
	@Autowired CourseRepo courseRepo;
	@Autowired FixCourseRepo fixedCourseRepo;
	@Autowired SameCourseRepo sameCourseRepo;
	
	@Autowired InterfaceHealth health;
	
    private static final Marker CONTENT = MarkerFactory.getMarker("CONTENT");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	public void uploadStudent(MultipartFile file) throws IOException {
		ReadService<Student> service = new ReadService<Student>();
		Collection<Student> list = service.read(file, args -> Student.read(args));
		studentRepo.save(list);
	}
	public void uploadCourse(MultipartFile file) throws IOException {
		ReadService<Course> service = new ReadService<Course>();
		Collection<Course> list = service.read(file, args -> Course.read(args));
		courseRepo.save(list);
	}
	public void uploadFixCourse(MultipartFile file) throws IOException {
		ReadService<FixCourse> service = new ReadService<FixCourse>();
		Collection<FixCourse> list = service.read(file, args -> FixCourse.read(args));
		fixedCourseRepo.save(list);
	}
	public void uploadSameCourse(MultipartFile file) throws IOException {
		ReadService<SameCourse> service = new ReadService<SameCourse>();
		Collection<SameCourse> list = service.read(file, args -> SameCourse.read(args));
		sameCourseRepo.save(list);
	}
}
