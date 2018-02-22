package jupiterpa.course.intf.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    
    @Autowired Solver solver;
    @Autowired UploadService upload;
    
    @GetMapping("/course")
    @ApiOperation(value = "GET courses", response = Course.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<Course> getCourses() {  
    	return courseRepo.findAll();
    }
    
    @GetMapping("/student")
    @ApiOperation(value = "GET students", response = Student.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<Student> getStudents() {  
    	return studentRepo.findAll();
    }
    
    @GetMapping("/fixedCourses")
    @ApiOperation(value = "GET fixed Courses", response = FixCourse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<FixCourse> getfixedCourses() {  
    	return fixedCourseRepo.findAll();
    }

    @GetMapping("/sameCourses")
    @ApiOperation(value = "GET students wanting same course", response = SameCourse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Entity")
    })
    public List<SameCourse> getSameCourses() {  
    	return sameCourseRepo.findAll();
    }
    @PutMapping("/optimize")
    @ApiOperation(value = "Opimize")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully calculated")
    })
    public void optimize() {  
    	solver.optimize();
    }
    
    @PostMapping("/upload/students")
    public String uploadStudent(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
			upload.uploadStudent(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
    @PostMapping("/upload/courses")
    public String uploadCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        upload.uploadCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
    @PostMapping("/upload/fixcourse")
    public String uploadFixCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        upload.uploadFixCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
    @PostMapping("/upload/samecourse")
    public String uploadSameCourse(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        upload.uploadSameCourse(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
