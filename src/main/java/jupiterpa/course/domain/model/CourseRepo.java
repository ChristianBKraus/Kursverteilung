package jupiterpa.course.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepo extends MongoRepository<Course,Long>{ }
