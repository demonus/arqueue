package io.github.arqueue.exception;

/**
 * Created by root on 11/11/15.
 */
public class OpenStackApiException extends Exception
{
	public OpenStackApiException(String message)
	{
		super(message);
	}

	public OpenStackApiException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
