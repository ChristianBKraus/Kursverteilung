package jupiterpa.course.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepo extends MongoRepository<Student,Long>{ }
