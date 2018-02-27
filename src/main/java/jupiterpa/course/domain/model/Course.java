package jupiterpa.course.domain.model;

import java.io.IOException;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import jupiterpa.course.domain.service.FormatException;

@Document
public class Course {
	
	@Id
	private int id;
	private String name;
	private int capacity;
	private int booked;
	
	public Course() {}
	public Course(int id, String name, int capacity) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}
	public static Course read(List<String> args) throws FormatException {
		if (args.size() < 3) throw new FormatException();
		return new Course(Integer.parseInt(args.get(2)), args.get(0), Integer.parseInt(args.get(1)));
	}
	public String write() {
		return name + ";" + String.valueOf(capacity) + ";" + String.valueOf(booked);
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
	public int getBooked() {
		return booked;
	}
	public void setBooked(int booked) {
		this.booked = booked;
	}
		
}
