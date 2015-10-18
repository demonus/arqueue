package io.github.arqueue.exception;

/**
 * Created by root on 10/16/15.
 */
public class CloseResourceException extends Exception
{
	public CloseResourceException(String message)
	{
		super(message);
	}

	public CloseResourceException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
