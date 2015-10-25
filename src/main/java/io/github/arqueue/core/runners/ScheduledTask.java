package io.github.arqueue.core.runners;

import com.google.gson.Gson;
import io.github.arqueue.common.Result;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;

import java.util.concurrent.Callable;

/**
 * Created by root on 10/16/15.
 */
public class ScheduledTask implements Callable<Result>
{
	private Task task;

	private TaskData taskData;

	private static Gson gson = new Gson();

	public ScheduledTask(Task task)
	{
		this.task = task;

		this.taskData = gson.fromJson(task.getData(), TaskData.class);
	}

	@Override
	public Result call() throws Exception
	{
	   return null;
	}

	public String getId()
	{
		return task.getId();
	}

	public Flow.Status getStatus()
	{
		return task.getStatus();
	}

}
