package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class Student {
	
	@Id
	private int id;
	private String name;
	private String course;
	private String course1;
	private String course2;
	private String course3;
	
	public Student() {}
	public Student(int id, String name, String course1, String course2, String course3) {
		this.id = id;
		this.name = name;
		this.course1 = course1;
		this.course2 = course2;
		this.course3 = course3;
	}
	public static Student read(List<String> args) {
		return new Student(Integer.parseInt(args.get(4)), args.get(0), args.get(1), args.get(2), args.get(3));
	}
		
	@Override
	public String toString() {
		return "Student " + id + ":" + name +" (" + course1 + "/" + course2 + "/" + course3 + ")"; 
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getCourse3() {
		return course3;
	}
	public void setCourse3(String course3) {
		this.course3 = course3;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
	
}
