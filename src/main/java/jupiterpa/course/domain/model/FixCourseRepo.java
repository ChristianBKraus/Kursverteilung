package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FixCourseRepo extends MongoRepository<FixCourse,String>{ 
	List<FixCourse> findAllByOrderByStudentAsc();
}
