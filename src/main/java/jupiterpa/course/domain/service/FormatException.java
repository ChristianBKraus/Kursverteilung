package jupiterpa.course.domain.service;

import java.util.ArrayList;
import java.util.Collection;

public class FormatException extends Throwable {
	private static final long serialVersionUID = 2194107903058592654L;

	Collection<String> errors;
	public FormatException() {
		errors = new ArrayList<String>();
	}
	public FormatException(String error) {
		errors = new ArrayList<String>();
		errors.add(error);
	}
	public FormatException(Collection<String> errors) {
		this.errors = errors;
	}
	public Collection<String> getErrors() {
		return errors;
	}
}
