package jupiterpa.course.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class Course {
	
	@Id
	private int id;
	private String name;
	private int capacity;
	
	public Course() {}
	public Course(int id, String name, int capacity) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}
		
	@Override
	public String toString() {
		return "Course " + id + ":" + name + " / capacity=" + capacity; 
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
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
		
}
