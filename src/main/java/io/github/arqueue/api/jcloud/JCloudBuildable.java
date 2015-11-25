package io.github.arqueue.api.jcloud;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.common.Result;
import io.github.arqueue.exception.ValidationException;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.Set;

/**
 * Created by root on 11/10/15.
 */
public interface JCloudBuildable
{
	Result build(RackspaceConnect connect) throws RunNodesException,
			ValidationException;
}
