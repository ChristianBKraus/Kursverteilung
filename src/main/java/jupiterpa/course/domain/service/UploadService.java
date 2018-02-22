package jupiterpa.course.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jupiterpa.course.domain.model.*;
import jupiterpa.infrastructure.actuator.*;

@Service
public class UploadService {

	@Autowired
	StudentRepo repo;
	@Autowired 
	InterfaceHealth health;

	public void calculate() {
	}
}
