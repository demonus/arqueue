package io.github.arqueue.flow;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by root on 10/18/15.
 */

@Entity
@Table(name = "qbic_tasks", catalog = "queuebic")
public class Task
{
	public enum TaskStatus
	{
		PENDING("PENDING"),
		IN_PROGRESS("IN PROGRESS"),
		FAILED("FAILED"),
		COMPLETED("COMPLETED");

		String value;

		TaskStatus(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}

		public TaskStatus get(String value)
		{
			for (TaskStatus taskStatus : values())
			{
				if (taskStatus.getValue().equalsIgnoreCase(value))
				{
					return taskStatus;
				}
			}

			return null;
		}
	}

	String id;

	String flowId;

	String taskData;

	TaskStatus status;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "CHAR(40)")
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Column(nullable = false, length = 40)
	public String getFlowId()
	{
		return flowId;
	}

	public void setFlowId(String flowId)
	{
		this.flowId = flowId;
	}

	@Column(length = 2000)
	public String getTaskData()
	{
		return taskData;
	}

	public void setTaskData(String taskData)
	{
		this.taskData = taskData;
	}

	@Column(length = 100)
	public TaskStatus getStatus()
	{
		return status;
	}

	public void setStatus(TaskStatus status)
	{
		this.status = status;
	}
}
