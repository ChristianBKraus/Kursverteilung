package jupiterpa.course.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import jupiterpa.course.domain.service.FormatException;

@Document
public class FixCourse {
	
	@Id
	private String student;
	private String course;
	
	public FixCourse() {}
	public FixCourse(String student, String course) {
		this.student = student;
		this.course = course;
	}
	public static FixCourse read(List<String> args) throws FormatException {
		if (args.size() < 2) throw new FormatException();
		return new FixCourse(args.get(0),args.get(1));
	}
		
	@Override
	public String toString() {
		return "Fix Course for " + student + " to " + course; 
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
}
