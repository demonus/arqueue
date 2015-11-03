package io.github.arqueue.hibernate.beans;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/26/15.
 */

@Entity
@Table(name = "qbic_users", catalog = "queuebic")
public class User
{
	private String id;

	private String username;

	private String apiKey;

	private List<Flow> flows = new ArrayList<>();

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

	@Column(length = 300)
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@Column
	@Convert(converter = io.github.arqueue.hibernate.beans.helpers.CryptoConverter.class)
	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	@OneToMany(mappedBy = "user")
	public List<Flow> getFlows()
	{
		return flows;
	}

	public void setFlows(List<Flow> flows)
	{
		this.flows = flows;
	}
}
