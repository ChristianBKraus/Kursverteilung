package jupiterpa.course.domain.service;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class UploadService {

	@Autowired
	StudentRepo studentRepo;
	@Autowired
	CourseRepo courseRepo;
	@Autowired
	FixCourseRepo fixedCourseRepo;
	@Autowired
	SameCourseRepo sameCourseRepo;
	@Autowired
	ActionRepo actionRepo;

	@Autowired
	InterfaceHealth health;

	public void uploadStudent(MultipartFile file) throws FormatException {
		try {
			ReadUtility<Student> service = new ReadUtility<Student>();
			Collection<Student> list = service.read(file, args -> Student.read(args));
			studentRepo.deleteAll();
			studentRepo.save(list);

			actionLog("Upload Student");
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public void uploadCourse(MultipartFile file) throws FormatException {
		try {
			ReadUtility<Course> service = new ReadUtility<Course>();
			Collection<Course> list = service.read(file, args -> Course.read(args));
			courseRepo.deleteAll();
			courseRepo.save(list);
			
			actionLog("Upload Course");
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public void uploadFixCourse(MultipartFile file) throws FormatException {
		try {
			ReadUtility<FixCourse> service = new ReadUtility<FixCourse>();
			Collection<FixCourse> list = service.read(file, args -> FixCourse.read(args));
			fixedCourseRepo.deleteAll();
			fixedCourseRepo.save(list);
			
			actionLog("Upload FixCourse");
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public void uploadSameCourse(MultipartFile file) throws FormatException {
		try {
			ReadUtility<SameCourse> service = new ReadUtility<SameCourse>();
			Collection<SameCourse> list = service.read(file, args -> SameCourse.read(args));
			sameCourseRepo.deleteAll();
			sameCourseRepo.save(list);
			
			actionLog("Upload SameCourse");
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}
	private void actionLog(String id) {
		Action action = actionRepo.findById(id);
		if (action == null)
			action = new Action(id);
		else 
			action.update();
		actionRepo.save(action);
	}


	public OutputStream downloadStudents(OutputStream stream) throws FormatException {
		try {
			actionLog("Download Student");
			
			String content = "Student;Kurs;Kurs1;Kurs2;Kurs3\n";
			for (Student student : studentRepo.findAll()) {
				content = content + student.write() + "\n";
			}
			stream.write(content.getBytes());
			stream.flush();
			stream.close();
			return stream;
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public OutputStream downloadCourses(OutputStream stream) throws FormatException {
		try {
			actionLog("Download Course");
			
			String content = "Kurs;Kapazität;Gebucht\n";
			for (Course course : courseRepo.findAll()) {
				content = content + course.write() + "\n";
			}
			stream.write(content.getBytes());
			stream.flush();
			stream.close();
			return stream;
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public OutputStream downloadFixCourses(OutputStream stream) throws FormatException {
		try {
			actionLog("Download FixCourse");
			
			String content = "Student;Wunschkurs;GebuchterKurs\n";
			for (FixCourse course : fixedCourseRepo.findAll()) {
				content = content + course.write() + "\n";
			}
			stream.write(content.getBytes());
			stream.flush();
			stream.close();
			return stream;
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}

	public OutputStream downloadSameCourses(OutputStream stream) throws FormatException {
		try {
			actionLog("Download SameCourse");
			
			String content = "Student1;Student2;Kurs1;Kurs2";
			for (SameCourse course : sameCourseRepo.findAll()) {
				content = content + course.write() + "\n";
			}
			stream.write(content.getBytes());
			stream.flush();
			stream.close();
			return stream;
		} catch (IOException ex) {
			throw new FormatException("Allgemeiner Ein-/Ausgabe Fehler");
		}
	}
}
