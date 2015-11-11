package io.github.arqueue.exception;

/**
 * Created by root on 11/10/15.
 */
public class ValidationException extends Exception
{
	public ValidationException(String message)
	{
		super(message);
	}

	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
