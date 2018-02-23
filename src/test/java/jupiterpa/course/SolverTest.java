package jupiterpa.course;

import org.junit.Test;
import jupiterpa.course.domain.model.*;
import jupiterpa.course.domain.service.FormatException;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

public class SolverTest { 
	@Test
    public void simpleTest() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		courses.add(new Course(0,"C1",2));
		courses.add(new Course(1,"C2",2));
		courses.add(new Course(2,"C3",2));
		
		students.add(new Student(0, "S0", "C1", "C2", "C3"));
		students.add(new Student(1, "S1", "C2", "C1", "C3"));
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);	
		assertThat( model.getnConstraint(), equalTo(5));
		assertThat( model.getnVariable(), equalTo(6));
		
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

		assertThat( solution[0], is(1.0));
		assertThat( solution[1], is(0.0));
		
		assertThat( solution[2], is(0.0));
		assertThat( solution[3], is(1.0));
		
		assertThat( solution[4], is(0.0));
		assertThat( solution[5], is(0.0));
		
		Map<String,String> result = model.convert2String(solution);
		assertThat( result.size(), is(2) );
		assertThat( result.get("S0"), is("C1"));
		assertThat( result.get("S1"), is("C2"));
		
    }
	@Test
    public void simpleTest2() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		courses.add(new Course(0,"C1",2));
		courses.add(new Course(1,"C2",2));
		courses.add(new Course(2,"C3",2));
		
		students.add(new Student(0, "S0", "C1", "C2", "C3"));
		students.add(new Student(1, "S1", "C2", "C1", "C3"));
		students.add(new Student(2, "S2", "C2", "C1", "C3"));
		students.add(new Student(3, "S3", "C2", "C1", "C3"));
		students.add(new Student(4, "S4", "C2", "C1", "C3"));
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);			
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

		Map<String,String> result = model.convert2String(solution);
		assertThat( result.size(), is(5) );
		assertThat( result.get("S0"), is("C1"));
		assertThat( result.get("S1"), is("C3"));
		assertThat( result.get("S2"), is("C2"));
		assertThat( result.get("S3"), is("C2"));
		assertThat( result.get("S4"), is("C1"));
		
    }
	@Test(expected=NoFeasibleSolutionException.class)
    public void invalid() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		courses.add(new Course(0,"C1",2));
		courses.add(new Course(1,"C2",2));
		courses.add(new Course(2,"C3",2));
		
		students.add(new Student(0, "S0", "C1", "C2", "C3"));
		students.add(new Student(1, "S1", "C2", "C1", "C3"));
		students.add(new Student(2, "S2", "C2", "C1", "C3"));
		students.add(new Student(3, "S3", "C2", "C1", "C3"));
		students.add(new Student(4, "S4", "C2", "C1", "C3"));
		students.add(new Student(5, "S5", "C2", "C1", "C3"));
		students.add(new Student(6, "S6", "C2", "C1", "C3"));
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);			
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();
	    assertThat( 1, is(0) );
    }
	@Test
    public void fixCourse() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		courses.add(new Course(0,"C1",2));
		courses.add(new Course(1,"C2",2));
		courses.add(new Course(2,"C3",2));
		
		students.add(new Student(0, "S0", "C1", "C2", "C3"));
		students.add(new Student(1, "S1", "C2", "C1", "C3"));
		students.add(new Student(2, "S2", "C2", "C1", "C3"));
		students.add(new Student(3, "S3", "C2", "C1", "C3"));
		
		fixedCourses.add( new FixCourse("S3","C3"));
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);			
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

		Map<String,String> result = model.convert2String(solution);
		assertThat( result.get("S0"), is("C1"));
		assertThat( result.get("S1"), is("C2"));
		assertThat( result.get("S2"), is("C2"));
		assertThat( result.get("S3"), is("C3"));
    }
	@Test
    public void sameCourse() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		courses.add(new Course(0,"C1",2));
		courses.add(new Course(1,"C2",2));
		courses.add(new Course(2,"C3",2));
		
		students.add(new Student(0, "S0", "C1", "C2", "C3"));
		students.add(new Student(1, "S1", "C2", "C1", "C3"));
		students.add(new Student(2, "S2", "C2", "C1", "C3"));
		students.add(new Student(3, "S3", "C2", "C1", "C3"));
		
		fixedCourses.add( new FixCourse("S3","C3"));
		sameCourses.add( new SameCourse("S2","S3"));
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);			
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

		Map<String,String> result = model.convert2String(solution);
		assertThat( result.get("S0"), is("C1"));
		assertThat( result.get("S1"), is("C2"));
		assertThat( result.get("S2"), is("C3"));
		assertThat( result.get("S3"), is("C3"));
    }
	@Test
    public void performance10_10() throws Exception, FormatException {
		List<Student> students = new ArrayList<Student>();
		List<Course> courses = new ArrayList<Course>();
		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
		
		for (int i = 0; i < 10; i++) {
			String c = "C" + i; 
			courses.add(new Course(0,c,2));
		}
		
		for (int i = 0; i < 10; i++) {
			String s = "S" + i;
			String c1 = "C" + (int) Math.floor( Math.random() * 10.0 );
			String c2 = "C" + (int) Math.floor( Math.random() * 10.0 );
			String c3 = "C" + (int) Math.floor( Math.random() * 10.0 );
			System.out.println(s + "/" + c1 + "/" + c2 + "/" + c3);
			students.add(new Student(i, s, c1, c2, c3));
		}
		
		Model model = new Model(students,courses, fixedCourses, sameCourses);			
	    SimplexSolver solver = new SimplexSolver();
	    double[] solution = 
	    		solver.optimize(
	        		model.getObjectiveFunction(), 
	        		model.getConstraints(),
	                GoalType.MAXIMIZE, 
	                true).getPoint();

    }
//	@Test
//    public void performance150_40() throws Exception {
//		List<Student> students = new ArrayList<Student>();
//		List<Course> courses = new ArrayList<Course>();
//		List<SameCourse> sameCourses = new ArrayList<SameCourse>();
//		List<FixCourse> fixedCourses = new ArrayList<FixCourse>();
//		
//		for (int i = 0; i < 40; i++) {
//			String c = "C" + i; 
//			courses.add(new Course(0,c,8));
//		}
//		
//		for (int i = 0; i < 150; i++) {
//			String s = "S" + i;
//			String c1 = "C" + (int) Math.floor( Math.random() * 40.0 );
//			String c2 = "C" + (int) Math.floor( Math.random() * 40.0 );
//			String c3 = "C" + (int) Math.floor( Math.random() * 40.0 );
//			System.out.println(s + "/" + c1 + "/" + c2 + "/" + c3);
//			students.add(new Student(i, s, c1, c2, c3));
//		}
//		
//		Model model = new Model(students,courses, fixedCourses, sameCourses);			
//	    SimplexSolver solver = new SimplexSolver();
//	    solver.setMaxIterations(10000);
//	    double[] solution = 
//	    		solver.optimize(
//	        		model.getObjectiveFunction(), 
//	        		model.getConstraints(),
//	                GoalType.MAXIMIZE, 
//	                true).getPoint();
//
//    }
}