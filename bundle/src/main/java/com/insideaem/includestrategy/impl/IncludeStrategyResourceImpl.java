package com.insideaem.includestrategy.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;

import com.insideaem.includestrategy.api.IncludeStrategy;
import com.insideaem.includestrategy.api.IncludeStrategyConstants;
import com.insideaem.includestrategy.api.IncludeStrategyResource;

public class IncludeStrategyResourceImpl extends ResourceWrapper implements
		IncludeStrategyResource {

	IncludeStrategy includeStrategy;

	public IncludeStrategyResourceImpl(Resource resource) {
		super(resource);
		includeStrategy = resource.adaptTo(IncludeStrategy.class);
	}

	@Override
	public String getResourceType() {
		return IncludeStrategyConstants.RT_INCLUDE_STRATEGY;
	}

	@Override
	public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
		// No adaptation possible
		return null;
	}

	@Override
	public String getResourceSuperType() {
		return null;
	}

	public IncludeStrategy getIncludeStrategy() {
		return this.includeStrategy;
	}

	@Override
	public String toString() {
		return super.toString() + " - " + this.includeStrategy;
	}
}