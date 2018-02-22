package jupiterpa.course.domain.service;

import java.util.List;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.*;

import org.springframework.stereotype.Service;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class Solver {

	StudentRepo studentRepo;
	CourseRepo courseRepo;
	FixCourseRepo fixedCourseRepo;
	SameCourseRepo sameCourseRepo;
	InterfaceHealth health;
	
	public Solver(StudentRepo studentRepo, CourseRepo courseRepo, InterfaceHealth health) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
		this.health = health;
	}

	public void optimize() {
		// Read Data
		List<Student> students = studentRepo.findAll();
		List<Course> courses = courseRepo.findAll();
		List<FixCourse> fixedCourses = fixedCourseRepo.findAll();
		List<SameCourse> sameCourses = sameCourseRepo.findAll();
		
		// Build Model
		Model model = new Model(students,courses, fixedCourses, sameCourses);		
		
		// Solve
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

	    // Update and Save
		students = updateStudents(solution, students, courses);
		studentRepo.save(students);
	}
	
	List<Student> updateStudents(double[] solution, List<Student> students, List<Course> courses) {
		int nS = students.size();
		int s = 0;
		for (Student student: students) {
			int c = 0;
			for (Course course: courses) {
				if (solution[s + nS * c] > 1.0) {
					student.setCourse(course.getName());
				}
				c++;
			}
			s++;
		}
		return students;
	}
	
}
