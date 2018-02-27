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
	ActionRepo actionRepo;
	InterfaceHealth health;
	
	public OptimizationService(StudentRepo studentRepo, CourseRepo courseRepo,
			                   FixCourseRepo fixedCourseRepo, SameCourseRepo sameCourseRepo,
			                   ActionRepo actionRepo,
			                   InterfaceHealth health) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
		this.fixedCourseRepo = fixedCourseRepo;
		this.sameCourseRepo = sameCourseRepo;
		this.actionRepo = actionRepo;
		
		this.health = health;
	}

	public void run() throws MathIllegalStateException, FormatException {
		// Read Data
		Collection<Student> students = studentRepo.findAll();
		Collection<Course> courses = courseRepo.findAll();
		Collection<FixCourse> fixedCourses = fixedCourseRepo.findAll();
		Collection<SameCourse> sameCourses = sameCourseRepo.findAll();
		
		// Build Model
		Model model = new Model(students,courses, fixedCourses, sameCourses);		
		
		logger.info(CONTENT, "Initial Model:");
		model.print();

		model = OptimizationService.optimize(model);
		
		logger.info(CONTENT, "Resulting Model:");
		model.print();
		
		studentRepo.save(model.getStudents());
		courseRepo.save(model.getCourses());
		fixedCourseRepo.save(model.getFixedCourses());
		sameCourseRepo.save(model.getSameCourses());
		
		actionLog("Optimize");
	}
	public static Model optimize(Model model) throws FormatException, MathIllegalStateException  {
		
		// Solve
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

	    // Update 
	    model.update(solution);
	    return model;
	}
	private void actionLog(String id) {
		Action action = actionRepo.findById(id);
		if (action == null)
			action = new Action(id);
		else 
			action.update();
		actionRepo.save(action);
	}
	
	
}
