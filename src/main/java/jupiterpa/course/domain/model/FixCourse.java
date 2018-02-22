package jupiterpa.course.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

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
