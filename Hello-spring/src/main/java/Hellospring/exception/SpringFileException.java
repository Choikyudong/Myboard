package Hellospring.exception;

@SuppressWarnings("serial")
public class SpringFileException extends RuntimeException {

	public SpringFileException(String message) {
		super(message);
	}

	public SpringFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
