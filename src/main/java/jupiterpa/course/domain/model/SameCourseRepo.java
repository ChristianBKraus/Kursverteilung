package jupiterpa.course.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SameCourseRepo extends MongoRepository<SameCourse,String>{ }
