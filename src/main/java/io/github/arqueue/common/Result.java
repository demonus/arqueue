package io.github.arqueue.common;

import io.github.arqueue.api.post.response.server.ServerResponse;

/**
 * Created by root on 10/16/15.
 */
public class Result
{
	private Object body;

	public Object get()
	{
		return body;
	}

	public Object getAsServerResponse()
	{
		return (ServerResponse) body;
	}

	public Result(Object body)
	{
		this.body = body;
	}
}
