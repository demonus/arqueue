package io.github.arqueue.core.runners;

import io.github.arqueue.api.OpenStackConnection;
import io.github.arqueue.exception.OpenStackApiException;

/**
 * Created by root on 11/25/15.
 */
public interface IResponse
{
	public void checkStatus(OpenStackConnection connection) throws OpenStackApiException;
}
