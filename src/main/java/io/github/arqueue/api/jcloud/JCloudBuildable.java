package io.github.arqueue.api.jcloud;

import io.github.arqueue.exception.ValidationException;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.Set;

/**
 * Created by root on 11/10/15.
 */
public interface JCloudBuildable
{
	Set<? extends NodeMetadata> build(InstanceGenerator instanceGenerator) throws RunNodesException,
			ValidationException;
}
