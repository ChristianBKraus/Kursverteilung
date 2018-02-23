package jupiterpa.course.domain.service;

import java.util.ArrayList;
import java.util.Collection;

public class FormatException extends Throwable {
	Collection<String> errors;
	public FormatException() {
		errors = new ArrayList<String>();
	}
	public FormatException(Collection<String> errors) {
		this.errors = errors;
	}

}
