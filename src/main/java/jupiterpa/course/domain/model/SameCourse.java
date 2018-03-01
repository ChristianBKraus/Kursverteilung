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
	private String kurs1;
	private String kurs2;
	
	public SameCourse() {}
	public SameCourse(String student1, String student2) {
		this.id = student1 + "/" + student2;
		this.student1 = student1;
		this.student2 = student2;
	}
	public static SameCourse read(List<String> args) throws FormatException {
		if (args.size() < 2) { 
			int index = Integer.parseInt(args.get(args.size()-1)) + 2;
			throw new FormatException("Formatierungsfehler in Zeile " + String.valueOf(index) + " (nur " + args.size() + " Werte)");
		}
		return new SameCourse(args.get(0), args.get(1));
	}
	public String write() {
		return student1 + ";" + student2 + ";" + kurs1 + ";" + kurs2;
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
	public String getKurs1() {
		return kurs1;
	}
	public void setKurs1(String kurs1) {
		this.kurs1 = kurs1;
	}
	public String getKurs2() {
		return kurs2;
	}
	public void setKurs2(String kurs2) {
		this.kurs2 = kurs2;
	}
	
	
}
