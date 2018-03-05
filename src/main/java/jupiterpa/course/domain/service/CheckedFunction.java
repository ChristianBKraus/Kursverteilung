package jupiterpa.course.domain.service;

@FunctionalInterface
public interface CheckedFunction<T,R> {
	R apply(T t) throws FormatException;
}
