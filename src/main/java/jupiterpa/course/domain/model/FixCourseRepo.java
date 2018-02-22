package jupiterpa.course.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FixCourseRepo extends MongoRepository<FixCourse,String>{ }
