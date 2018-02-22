package jupiterpa.course.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class SameCourse {
	
	@Id
	private String id;
	private String student1;
	private String student2;
	
	public SameCourse() {}
	public SameCourse(String student1, String student2) {
		this.id = student1 + "/" + student2;
		this.student1 = student1;
		this.student2 = student2;
	}
		
	@Override
	public String toString() {
		return "Student " + student1 + " wants to have the same course as student " + student2; 
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudent1() {
		return student1;
	}
	public void setStudent1(String student1) {
		this.student1 = student1;
	}
	public String getStudent2() {
		return student2;
	}
	public void setStudent2(String student2) {
		this.student2 = student2;
	}
	
}