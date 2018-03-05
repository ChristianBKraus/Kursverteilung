package jupiterpa.course.domain.model;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepo extends MongoRepository<Course,Long>{ 
	List<Course> findAllByOrderByNameAsc();
}
