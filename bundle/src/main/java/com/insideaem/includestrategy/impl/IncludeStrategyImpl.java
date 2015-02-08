package com.insideaem.includestrategy.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.insideaem.includestrategy.api.IncludeStrategy;
import com.insideaem.includestrategy.api.IncludeStrategyConstants;

public class IncludeStrategyImpl implements IncludeStrategy {
	private final String type;
	private final boolean noCaching;

	public IncludeStrategyImpl(Resource resourceFromResourceType) {
		ValueMap values = resourceFromResourceType.adaptTo(ValueMap.class);
		this.type = values
				.get(IncludeStrategyConstants.PN_INCLUDE_STRATEGY_TYPE,
						String.class);
		Boolean noCaching = values.get(
				IncludeStrategyConstants.INCLUDE_STRATEGY_DISABLE_CACHING,
				Boolean.class);
		this.noCaching = noCaching != null && noCaching.booleanValue();
	}

	public String getType() {
		return type;
	}

	public boolean isCachingDisabled() {
		return noCaching;
	}

	@Override
	public String toString() {
		return String.format("[type=%s, disableCaching=%s]", this.type,
				this.noCaching);
	}

}
