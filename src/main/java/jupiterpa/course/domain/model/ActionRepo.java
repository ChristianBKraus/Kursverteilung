package jupiterpa.course.domain.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepo extends MongoRepository<Action,String>{ 
	Action findById(String id);
}
