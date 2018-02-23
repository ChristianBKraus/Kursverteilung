package jupiterpa.course.domain.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.optimization.linear.*;

public class Model {
	Collection<String> errors;
	
	int nStudent;
	int nCourse;
	int nVariable;
	int nConstraint;
	LinearObjectiveFunction objectiveFunction;
	Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
	
	final Collection<Student> students;
	final Collection<Course> courses;
	final Collection<FixCourse> fixedCourses;
	final Collection<SameCourse> sameCourses;
	final Map<String,Student> studentMap = new HashMap<String,Student>();
	final Map<String,Course> courseMap = new HashMap<String,Course>();
	
	public Model(Collection<Student> students, Collection<Course> courses,
			     Collection<FixCourse> fixedCourses, Collection<SameCourse> sameCourses) {
		this.students = students;
		this.courses = courses;
		this.fixedCourses = fixedCourses;
		this.sameCourses = sameCourses;
		
		buildMaps();
		errors = check();
		if (errors.size() == 0) {
	
		// Dimensions
		nStudent = students.size();
		nCourse = courses.size();
		nVariable = students.size() * courses.size();
		nConstraint = students.size() + courses.size() + 
				      fixedCourses.size() + 
				      sameCourses.size() * nCourse;
		
		// only one course per student
		for (int i = 0; i < nStudent; i++) {
			constraints.add( onlyOneCourse(i) );
		}
		int index = 0;
		for (Course course : courses) {
			constraints.add( courseCapacity(index,course.getCapacity()) );
			index++;
		}
		for (FixCourse course: fixedCourses) {
			constraints.add( fixCourse(course) );
		}
		for (SameCourse course: sameCourses) {
			constraints.addAll( sameCourse(course) );
		}
		
		objectiveFunction = new LinearObjectiveFunction( objective( students, courses) ,0);
		}
	}
	
	public Map<String,String> convert2String(double[] solution) { 
		Map<String,String> result = new HashMap<String,String>();
		
		int c = 0;
		int s = 0;
		for (Course course : courses) {
			s = 0;
			for (Student student: students) {
				if ( solution[s + c * nStudent] == 1.0) {
					result.put(student.getName(),course.getName());
				}	
			    s++;
			}
			c++;
		}
		
		return result;
	}
	public Map<Student,Course> convert(double[] solution) { 
		Map<Student,Course>  result = new HashMap<Student,Course>();
		
		int c = 0;
		int s = 0;
		for (Course course : courses) {
			s = 0;
			for (Student student: students) {
				if ( solution[s + c * nStudent] == 1.0) {
					result.put(student,course);
				}	
			    s++;
			}
			c++;
		}
		
		return result;
	}
	public Student getStudent(String name) {
		return studentMap.getOrDefault(name, null);
	}
	public Course getCourse(String name) {
		return courseMap.getOrDefault(name,null);
	}
	
	private double[] Vector() {
		double A[] = new double[nVariable];
		for (int i = 0; i < nVariable; i++) { A[i] = 0.0; }
		return A;
	}
	
	private void buildMaps() {
		for (Student student: students) {
			studentMap.put(student.getName(), student);
		}
		for (Course course: courses) {
			courseMap.put(course.getName(), course);
		}
	}
	private Collection<String> check() {
		Collection<String> errors = new ArrayList<String>();
				
		// No duplicate Course Names
		if (studentMap.size() != students.size()) {
			for (Student student : students) {
				if (getStudent(student.getName()).getId() != student.getId()) {
					errors.add("Student " + student.getName() + " existiert doppelt");
				}
			}
		}
		
		// No duplicate Student Names
		if (courseMap.size() != courses.size()) {
			for (Course course : courses) {
				if (getCourse(course.getName()).getId() != course.getId()) {
					errors.add("Kurs " + course.getName() + " existiert doppelt");
				}
			}
		}

		// All Courses booked by students exist
		for (Student student : students) {
			if (getCourse(student.getCourse1()) == null) {
				errors.add("Kurs " + student.getCourse1() + " (Kurs 1) von " + student.getName() + " existiert nicht"); 
			}
			if (getCourse(student.getCourse2()) == null) {
				errors.add("Kurs " + student.getCourse2() + " (Kurs 2) von " + student.getName() + " existiert nicht"); 
			}
			if (getCourse(student.getCourse3()) == null) {
				errors.add("Kurs " + student.getCourse3() + " (Kurs 3) von " + student.getName() + " existiert nicht"); 
			}
		}
		
		// Student and Course referenced by FixedCourse exist
		for (FixCourse course : fixedCourses) {
			if (getStudent(course.getStudent()) == null) {
				errors.add("Student " + course.getStudent() + ", der einen Kurs fixieren will, existiert nicht");
			}
			if (getCourse(course.getCourse()) == null) {
				errors.add("Kurs " + course.getCourse() + " von " + course.getStudent() + ", der einen Kurs fixieren will, existiert nicht");
			}
		}
		
		// Students referenced by SameCourse exist
		for (SameCourse course : sameCourses) {
			if (getStudent(course.getStudent1()) == null) {
				errors.add("Student1 " + course.getStudent1() + ", der mit " + course.getStudent2() + " einen Kurs besuchen will, existiert nicht");
			}
			if (getStudent(course.getStudent2()) == null) {
				errors.add("Student2 " + course.getStudent2() + ", der mit " + course.getStudent1() + " einen Kurs besuchen will, existiert nicht");
			}
		}
		
		// Students of SameCourse do not share course
		for (SameCourse course : sameCourses) {
			Student student1 = getStudent(course.getStudent1());
			Student student2 = getStudent(course.getStudent2());
			
			if ( !student1.getCourse1().equals(student2.getCourse1()) &&
				 !student1.getCourse1().equals(student2.getCourse2()) &&	
				 !student1.getCourse1().equals(student2.getCourse3()) &&	
				 !student1.getCourse2().equals(student2.getCourse1()) &&	
				 !student1.getCourse2().equals(student2.getCourse2()) &&	
				 !student1.getCourse2().equals(student2.getCourse3()) &&	
				 !student1.getCourse3().equals(student2.getCourse1()) &&	
				 !student1.getCourse3().equals(student2.getCourse2()) &&	
				 !student1.getCourse3().equals(student2.getCourse3()) 	
				 ) {
				errors.add("Student " + student1.getName() + " und Student " + student2.getName() +" haben keine gemeinsame Wahl");
			}
		}
		
		return errors;
	}
	
	private LinearConstraint fixCourse(FixCourse fixCourse) {
		double A[] = Vector();
		int student = studentMap.get(fixCourse.getStudent()).getId();
		int course = courseMap.get(fixCourse.getCourse()).getId();
		A[ student + course * nStudent ] = 1.0;
		return new LinearConstraint(A, Relationship.EQ, 1.0); 
	}
	private Collection<LinearConstraint> sameCourse(SameCourse course) {
		int student1 = studentMap.get(course.getStudent1()).getId();
		int student2 = studentMap.get(course.getStudent2()).getId();
		
		Collection<LinearConstraint> list = new ArrayList<LinearConstraint>();
		for (int i = 0; i < nCourse; i++) {
			double A[] = Vector();
			A[ student1 + i * nStudent] = 1.0;
			A[ student2 + i * nStudent] = -1.0;
			list.add(new LinearConstraint(A, Relationship.EQ, 0.0));
		}
		
		return list;
	}
	
	private LinearConstraint onlyOneCourse(int studentIndex) {
		double A[] = Vector();
		
		for (int c = 0; c < nCourse; c++) { 
			A[studentIndex + c*nStudent] = 1.0; 
		}
		
		return new LinearConstraint(A,Relationship.EQ,1.0);
	}
	private LinearConstraint courseCapacity(int courseIndex, int capacity ) {
		double A[] = Vector();
		
		for (int s = 0; s < nStudent; s++) { 
			A[s + courseIndex*nStudent] = 1.0; 
		}
		
		return new LinearConstraint(A,Relationship.LEQ,(double) capacity);
	}
	private int getCourseIndex(String name, Collection<Course> courses) {
		Course course = courses.stream().filter( i -> i.getName().equals(name) ).findFirst().get();
		int index = course.getId();
		return index;
	}
	
	private double[] objective(Collection<Student> students, Collection<Course> courses) {
		double c[] = Vector();
		
		for (Student student : students ) {
			int index;
			index = getCourseIndex(student.getCourse1(), courses);			
			c[student.getId() + index * nStudent] = 3.0;
			
			index = getCourseIndex(student.getCourse2(), courses);			
			c[student.getId() + index * nStudent] = 2.0;
			
			index = getCourseIndex(student.getCourse3(), courses);			
			c[student.getId() + index * nStudent] = 1.0;			
		}
		return c;
	}
	
	public LinearObjectiveFunction getObjectiveFunction() {
		return objectiveFunction;
	}
	public Collection<LinearConstraint> getConstraints() {
		return constraints;
	}
	public int getnStudent() {
		return nStudent;
	}

	public int getnCourse() {
		return nCourse;
	}

	public int getnVariable() {
		return nVariable;
	}

	public int getnConstraint() {
		return nConstraint;
	}
	public Collection<String> getErrors() {
		return errors;
	}

	
}
