package io.github.arqueue.hibernate.beans;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 10/19/15.
 */
@Entity
@Table(name = "qbic_flow_actions", catalog = "queuebic")
public class Action
{
	private String id;

	private Flow flow;

	private int orderNumber;

	private int parallelMax;

	private Set<Task> tasks = new HashSet<>(0);

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
	public Flow getFlow()
	{
		return flow;
	}

	public void setFlow(Flow flow)
	{
		this.flow = flow;
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(precision = 0)
	public int getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber)
	{
		this.orderNumber = orderNumber;
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
}
