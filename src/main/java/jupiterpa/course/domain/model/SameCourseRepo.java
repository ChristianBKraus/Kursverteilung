package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SameCourseRepo extends MongoRepository<SameCourse,String>{ 
	List<SameCourse> findAllByOrderByStudent1AscStudent2Asc();
}
