package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import jupiterpa.course.domain.service.FormatException;

@Document
public class SameCourse {
	
	@Id
	private String id;
	private String student1;
	private String student2;
	private String course1;
	private String course2;
	
	public SameCourse() {}
	public SameCourse(String student1, String student2) {
		this.id = student1 + "/" + student2;
		this.student1 = student1;
		this.student2 = student2;
	}
	public static SameCourse read(List<String> args) throws FormatException {
		if (args.size() < 3) { 
			int index = Integer.parseInt(args.get(args.size()-1)) + 2;
			throw new FormatException("Formatierungsfehler in Zeile " + String.valueOf(index) + " (nur " + args.size() + " Werte)");
		}
		return new SameCourse(args.get(0), args.get(1));
	}
	public String write() {
		return student1 + ";" + student2 + ";" + course1 + ";" + course2;
	}

	@Override
	public String toString() {
		return "Student " + student1 + " wants to have the same course as student " + student2; 
	}
	public String getId() {
		return id;
	}
	public String getStudent1() {
		return student1;
	}
	public String getStudent2() {
		return student2;
	}
	public String getCourse1() {
		return course1;
	}
	public void setCourse1(String course1) {
		this.course1 = course1;
	}
	public String getCourse2() {
		return course2;
	}
	public void setCourse2(String course2) {
		this.course2 = course2;
	}
	
	
}
