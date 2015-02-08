package com.insideaem.includestrategy.api;

import org.apache.sling.api.resource.Resource;

public interface IncludeStrategyResource extends Resource {
	IncludeStrategy getIncludeStrategy();
}
