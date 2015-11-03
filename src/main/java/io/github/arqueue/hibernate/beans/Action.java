package io.github.arqueue.hibernate.beans;

import io.github.arqueue.hibernate.beans.helpers.Countable;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 10/19/15.
 */
@Entity
@Table(name = "qbic_flow_actions", catalog = "queuebic")
public class Action implements Countable
{
	private String id;

	private Flow flow;

	private int parallelMax;

	private Set<Task> tasks = new HashSet<>(0);

	private Integer orderNumber;

	private Flow.Status status;

	@Column(precision = 0, nullable = true, name = "order_number")
	public Integer getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber)
	{
		this.orderNumber = orderNumber;
	}

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flow_id", nullable = false)
	@OrderBy("order_number")
	public Flow getFlow()
	{
		return flow;
	}

	public void setFlow(Flow flow)
	{
		this.flow = flow;
	}

	@Column(precision = 0)
	public int getParallelMax()
	{
		return parallelMax;
	}

	public void setParallelMax(int parallelMax)
	{
		this.parallelMax = parallelMax;
	}

	@OneToMany(mappedBy = "action")
	public Set<Task> getTasks()
	{
		return tasks;
	}

	public void setTasks(Set<Task> tasks)
	{
		this.tasks = tasks;
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
}
