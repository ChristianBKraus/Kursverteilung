package jupiterpa.course.domain.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class OptimizationService {
    private static final Marker CONTENT = MarkerFactory.getMarker("CONTENT");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	StudentRepo studentRepo;
	CourseRepo courseRepo;
	FixCourseRepo fixedCourseRepo;
	SameCourseRepo sameCourseRepo;
	InterfaceHealth health;
	
	public OptimizationService(StudentRepo studentRepo, CourseRepo courseRepo, InterfaceHealth health) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
		this.health = health;
	}

	public void run() throws MathIllegalStateException, FormatException {
		// Read Data
		Collection<Student> students = studentRepo.findAll();
		Collection<Course> courses = courseRepo.findAll();
		Collection<FixCourse> fixedCourses = fixedCourseRepo.findAll();
		Collection<SameCourse> sameCourses = sameCourseRepo.findAll();
		
		Collection<Student> opt_students =
				OptimizationService.optimize(students, courses, fixedCourses, sameCourses);
		
		for (Student student : opt_students) {
			logger.info(CONTENT,student.toString());
		}
		
		studentRepo.save(opt_students);
	}
	public static Collection<Student> optimize(Collection<Student> students, 
			                              Collection<Course> courses,
			                              Collection<FixCourse> fixedCourses,
			                              Collection<SameCourse> sameCourses) throws FormatException, MathIllegalStateException  {
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
		return students;
	}
	
	private static Collection<Student> updateStudents(double[] solution, Collection<Student> students, Collection<Course> courses) {
		int nS = students.size();
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
		return students;
	}
	
}
