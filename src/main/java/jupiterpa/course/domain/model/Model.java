package jupiterpa.course.domain.model;

import java.util.*;
import org.apache.commons.math3.optimization.linear.*;
import org.slf4j.*;

import jupiterpa.course.domain.service.FormatException;

public class Model {
    private static final Marker CONTENT = MarkerFactory.getMarker("CONTENT");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	Collection<String> errors;
	
	int nStudent;
	int nCourse;
	int nVariable;
	int nConstraint;
	LinearObjectiveFunction objectiveFunction;
	Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
	
	Collection<Student> students;
	Collection<Course> courses;
	Collection<FixCourse> fixedCourses;
	Collection<SameCourse> sameCourses;
	final Map<String,Student> studentMap = new HashMap<String,Student>();
	final Map<String,Course> courseMap = new HashMap<String,Course>();
	
	public Model(Collection<Student> students, Collection<Course> courses,
			     Collection<FixCourse> fixedCourses, Collection<SameCourse> sameCourses) throws FormatException {
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
		} else 
			throw new FormatException(errors);
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
		
		// There must be students
		if (students.size() == 0) {
			errors.add("Keine Studenten");
		}
		
		// There must be courses
		if (courses.size() == 0) {
			errors.add("Keine Kurse");
		}
				
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
	public void update(double[] solution) {
		updateStudents(solution);
		updateCourses();
		updateFixedCourses();
		updateSameCourses();
	}
	private void updateStudents(double[] solution) {
		int nS = this.getnStudent();
		int s = 0;
		for (Student student: students) {
			int c = 0;
			for (Course course: courses) {
				if (solution[s + nS * c] > 0.0) {
					student.setCourse(course.getName());
				}
				c++;
			}
			s++;
		}
	}
	private void updateCourses() {
		Map<String,Integer> booked = new HashMap<String,Integer>();
		for (Student student : students) {
			String course = student.getCourse();
			if (booked.containsKey(course)) {
				Integer n = booked.get(course);
				n += 1;
				booked.put(course, n);
			} else {
				booked.put(course, 1);
			}
		}
		
		for (Course course : courses) {
			course.setBooked(booked.getOrDefault(course.getName(), 0));
		}
	}
	private void updateFixedCourses() {
		for (FixCourse course : fixedCourses) {
			course.setBookedCourse(getStudent(course.getStudent()).getCourse());
		}
	}
	private void updateSameCourses() {
		for (SameCourse course : sameCourses) {
			course.setCourse1(getStudent(course.getStudent1()).getCourse());
			course.setCourse2(getStudent(course.getStudent2()).getCourse());
		}
	}
	
	public void print() {
		logger.info(CONTENT, "Students:");
		for (Student student : students) {
			logger.info(CONTENT,student.toString());
		}

		logger.info(CONTENT, "Courses:");
		for (Course course : courses) {
			logger.info(CONTENT,course.toString());
		}

		logger.info(CONTENT, "Fixed Courses:");
		for (FixCourse course : fixedCourses) {
			logger.info(CONTENT,course.toString());
		}

		logger.info(CONTENT, "Same Courses:");
		for (SameCourse course : sameCourses) {
			logger.info(CONTENT,course.toString());
		}
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
	public Collection<Student> getStudents() {
		return students;
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public Collection<FixCourse> getFixedCourses() {
		return fixedCourses;
	}

	public Collection<SameCourse> getSameCourses() {
		return sameCourses;
	}
	
	
}
