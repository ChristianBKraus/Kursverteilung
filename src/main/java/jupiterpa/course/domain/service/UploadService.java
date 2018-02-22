package jupiterpa.course.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

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

	public void uploadStudent(MultipartFile file) throws IOException {
		
		InputStream stream = file.getInputStream();
		Reader reader = new InputStreamReader(stream);
		CSVReader csvReader = new CSVReader(reader, ';');
		
		Collection<Student> students = new ArrayList<Student>();
		String[] nextRecord;
		int i = 0;
		csvReader.readNext(); // Header line
		while ((nextRecord = csvReader.readNext()) != null) {
			Student student = new Student(i,nextRecord[0],nextRecord[1],nextRecord[2],nextRecord[3]);
			System.out.println(student);
			students.add(student);
			i++;
		}
		csvReader.close();
	}
	public void uploadCourse(MultipartFile file) {
	}
	public void uploadFixCourse(MultipartFile file) {
	}
	public void uploadSameCourse(MultipartFile file) {
	}
}
