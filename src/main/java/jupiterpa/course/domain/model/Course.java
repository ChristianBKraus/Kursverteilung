package jupiterpa.course.domain.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import jupiterpa.course.domain.service.FormatException;

@Document
public class Course {
	
	@Id
	private BigInteger _id;
	private int id;
	private String name;
	private int capacity;
	private int booked;
	
	public Course() {}
	public Course(int id, String name, int capacity) {
		this._id = BigInteger.valueOf(id);
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}
	public static Course read(List<String> args) throws FormatException {
		if (args.size() < 3) {
			int index = Integer.parseInt(args.get(args.size()-1)) + 2;
			throw new FormatException("Formatierungsfehler in Zeile " + String.valueOf(index) + " (nur " + args.size() + " Werte)");
		}
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
	public String getName() {
		return name;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getBooked() {
		return booked;
	}
	public void setBooked(int booked) {
		this.booked = booked;
	}
		
}
