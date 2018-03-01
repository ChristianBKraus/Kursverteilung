package jupiterpa.course.intf.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.*;
import jupiterpa.course.domain.model.*;
import jupiterpa.course.domain.service.*;


@RequestMapping(path = Controller.PATH)
@RestController
@Api(value="course", description="course Controller")
public class Controller {
    public static final String PATH ="/api";
    
    @Autowired StudentRepo studentRepo;
    @Autowired CourseRepo courseRepo;
    @Autowired FixCourseRepo fixedCourseRepo;
    @Autowired SameCourseRepo sameCourseRepo;
    @Autowired ActionRepo actionRepo;
    
    @Autowired OptimizationService solver;
    @Autowired UploadService upload;
    
    @GetMapping("/courses")
    @ApiOperation(value = "GET courses", response = Course.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<Course> getCourses() {  
    	return courseRepo.findAllByOrderByNameAsc();
    }
    
    @GetMapping("/students")
    @ApiOperation(value = "GET students", response = Student.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<Student> getStudents() {  
    	return studentRepo.findAllByOrderByNameAsc();
    }
    
    @GetMapping("/fixedCourses")
    @ApiOperation(value = "GET fixed Courses", response = FixCourse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<FixCourse> getfixedCourses() {  
    	return fixedCourseRepo.findAllByOrderByStudentAsc();
    }

    @GetMapping("/sameCourses")
    @ApiOperation(value = "GET students wanting same course", response = SameCourse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<SameCourse> getSameCourses() {  
    	return sameCourseRepo.findAllByOrderByStudent1AscStudent2Asc();
    }

    @GetMapping("/actions")
    @ApiOperation(value = "GET Action Log", response = Action.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<Action> getActions() {  
    	return actionRepo.findAll();
    }
    
    
    @PutMapping("/optimize")
    @ApiOperation(value = "Opimize")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully calculated")
    })
    public void optimize() throws MathIllegalStateException, FormatException {  
    	solver.run();
    }
    
    @PostMapping("/upload/students")
    public ModelAndView uploadStudent(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FormatException {

		upload.uploadStudent(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/api/students");
    }
    @PostMapping("/upload/courses")
    public ModelAndView uploadCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FormatException {

        upload.uploadCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/api/courses");
    }
    @PostMapping("/upload/fixedCourses")
    public ModelAndView uploadFixCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FormatException {

		upload.uploadFixCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/api/fixedCourses");
    }
    @PostMapping("/upload/sameCourses")
    public ModelAndView uploadSameCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FormatException {

        upload.uploadSameCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/api/sameCourses");
    }

    @GetMapping("/download/students")
    public void downloadStudents(HttpServletResponse response) throws FormatException {
    	try {
    	response.setContentType("text/csv");
    	response.setHeader("Content-Disposition", "attachment; filename=\"student_result.csv\"");
    	upload.downloadStudents(response.getOutputStream());
    	} catch (IOException ex) {
    		throw new FormatException("Allgemeiner Ein-Ausgabe Fehler");
    	}
    }
    @GetMapping("/download/courses")
    public void downloadCourses(HttpServletResponse response) throws FormatException {
    	try {
    	response.setContentType("text/csv");
    	response.setHeader("Content-Disposition", "attachment; filename=\"course_result.csv\"");
    	upload.downloadCourses(response.getOutputStream());
    	} catch (IOException ex) {
    		throw new FormatException("Allgemeiner Ein-Ausgabe Fehler");
    	}
    }
    @GetMapping("/download/fixedCourses")
    public void downloadFixCourses(HttpServletResponse response) throws FormatException {
    	try {
    	response.setContentType("text/csv");
    	response.setHeader("Content-Disposition", "attachment; filename=\"fixcourse_result.csv\"");
    	upload.downloadFixCourses(response.getOutputStream());
    	} catch (IOException ex) {
    		throw new FormatException("Allgemeiner Ein-Ausgabe Fehler");
    	}
    }
    @GetMapping("/download/sameCourses")
    public void downloadSameCourses(HttpServletResponse response) throws FormatException {
    	try {
    	response.setContentType("text/csv");
    	response.setHeader("Content-Disposition", "attachment; filename=\"samecourse_result.csv\"");
    	upload.downloadSameCourses(response.getOutputStream());
    	} catch (IOException ex) {
    		throw new FormatException("Allgemeiner Ein-Ausgabe Fehler");
    	}
    }
    
    
    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Formatierungsfehler")
    @ExceptionHandler(FormatException.class) 
    public Collection<String> handleFormatException(FormatException ex){
    	return ex.getErrors();
    }

    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Problem nicht lösbar")
    @ExceptionHandler(MathIllegalStateException.class) 
    public void handleMathIllegalStateException(){}
}
