package jupiterpa.course.domain.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepo extends MongoRepository<Student,Long>{ 
	List<Student> findAllByOrderByNameAsc();
}
