package io.github.arqueue.hibernate.beans;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by root on 10/19/15.
 */
@Entity
@Table(name = "qbic_flow_tasks", catalog = "queuebic")
public class Task
{
	private String id;

	private String data;

	private Flow.Status status;

	private Action action;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(length = 40)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Column(length = 2000)
	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	public Flow.Status getStatus()
	{
		return status;
	}

	public void setStatus(Flow.Status status)
	{
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id", nullable = false)
	public Action getAction()
	{
		return action;
	}

	public void setAction(Action action)
	{
		this.action = action;
	}
}
