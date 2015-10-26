package io.github.arqueue.hibernate.beans;

import io.github.arqueue.hibernate.beans.helpers.CountableList;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by root on 10/19/15.
 */
@Entity
@Table(name = "qbic_flows", catalog = "queuebic")
public class Flow
{
	public enum Status
	{
		NEW,
		PENDING,
		IN_PROGRESS,
		ERROR,
		COMPLETE;
	}

	private String id;

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

	@Column(length = 200)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(length = 400)
	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	@OneToMany(mappedBy = "flow")
	public List<Action> getActions()
	{
		return actions;
	}

	public void setActions(List<Action> actions)
	{
		this.actions = actions;
	}

	private String name;

	private String token;

	private Status status;

	private List<Action> actions = new CountableList<>();
}
