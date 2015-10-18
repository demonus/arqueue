package io.github.arqueue.core.tasks;

import io.github.arqueue.common.Result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by root on 10/16/15.
 */
public class Task implements Future<Result>
{
	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return false;
	}

	@Override
	public boolean isCancelled()
	{
		return false;
	}

	@Override
	public boolean isDone()
	{
		return false;
	}

	@Override
	public Result get() throws InterruptedException, ExecutionException
	{
		return null;
	}

	@Override
	public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return null;
	}
}
