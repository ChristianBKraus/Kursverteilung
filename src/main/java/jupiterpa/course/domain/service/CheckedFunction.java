package jupiterpa.course.domain.service;

import java.io.IOException;

@FunctionalInterface
public interface CheckedFunction<T,R> {
	R apply(T t) throws FormatException;
}
