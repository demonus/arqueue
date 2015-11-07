package io.github.arqueue.exception;

/**
 * Created by root on 11/6/15.
 */
public class CacheException extends Exception
{
	public CacheException(String message)
	{
		super(message);
	}

	public CacheException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
