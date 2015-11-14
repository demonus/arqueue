package io.github.arqueue.core.runners;

import io.github.arqueue.api.OpenStackConnection;
import io.github.arqueue.common.Result;
import io.github.arqueue.exception.OpenStackApiException;
import io.github.arqueue.exception.ValidationException;

/**
 * Created by root on 11/10/15.
 */
public interface Buildable
{
	Result build(OpenStackConnection connect) throws
			ValidationException, OpenStackApiException;
}
