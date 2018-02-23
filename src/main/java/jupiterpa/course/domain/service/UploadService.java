package jupiterpa.course.domain.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class UploadService {

	@Autowired StudentRepo studentRepo;
	@Autowired CourseRepo courseRepo;
	@Autowired FixCourseRepo fixedCourseRepo;
	@Autowired SameCourseRepo sameCourseRepo;
	
	@Autowired InterfaceHealth health;
	
	public void uploadStudent(MultipartFile file) throws IOException, FormatException {
		ReadUtility<Student> service = new ReadUtility<Student>();
		Collection<Student> list = service.read(file, args -> Student.read(args));
		studentRepo.save(list);
	}
	public void uploadCourse(MultipartFile file) throws IOException, FormatException {
		ReadUtility<Course> service = new ReadUtility<Course>();
		Collection<Course> list = service.read(file, args -> Course.read(args));
		courseRepo.save(list);
	}
	public void uploadFixCourse(MultipartFile file) throws IOException, FormatException {
		ReadUtility<FixCourse> service = new ReadUtility<FixCourse>();
		Collection<FixCourse> list = service.read(file, args -> FixCourse.read(args));
		fixedCourseRepo.save(list);
	}
	public void uploadSameCourse(MultipartFile file) throws IOException, FormatException {
		ReadUtility<SameCourse> service = new ReadUtility<SameCourse>();
		Collection<SameCourse> list = service.read(file, args -> SameCourse.read(args));
		sameCourseRepo.save(list);
	}
}
