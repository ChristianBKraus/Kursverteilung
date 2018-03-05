package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import jupiterpa.course.domain.service.FormatException;

@Document
public class FixCourse {
	
	@Id
	private String student;
	private String course;
	private String bookedCourse;
	
	public FixCourse() {}
	public FixCourse(String student, String course) {
		this.student = student;
		this.course = course;
	}
	public static FixCourse read(List<String> args) throws FormatException {
		if (args.size() < 3) {
			int index = Integer.parseInt(args.get(args.size()-1)) + 2;
			throw new FormatException("Formatierungsfehler in Zeile " + String.valueOf(index) + " (nur " + args.size() + " Werte)");
		}
		return new FixCourse(args.get(0),args.get(1));
	}
	public String write() {
		return student + ";" + course + ";" + bookedCourse;
	}
		
	@Override
	public String toString() {
		return "Fix Course for " + student + " to " + course; 
	}
	public String getStudent() {
		return student;
	}
	public String getCourse() {
		return course;
	}
	public String getBookedCourse() {
		return bookedCourse;
	}
	public void setBookedCourse(String bookedCourse) {
		this.bookedCourse = bookedCourse;
	}
	
	
}
