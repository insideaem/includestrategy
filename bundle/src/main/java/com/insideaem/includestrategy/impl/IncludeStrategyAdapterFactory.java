package com.insideaem.includestrategy.impl;

import javax.jcr.Node;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insideaem.includestrategy.api.IncludeStrategy;
import com.insideaem.includestrategy.api.IncludeStrategyConstants;

@Component(metatype = false)
@Service(value = org.apache.sling.api.adapter.AdapterFactory.class)
public class IncludeStrategyAdapterFactory implements AdapterFactory {
	@Property(name = "adapters")
	public static final String[] ADAPTER_CLASSES = { IncludeStrategy.class
			.getName() };

	@Property(name = "adaptables")
	public static final String[] ADAPTABLE_CLASSES = { Resource.class.getName() };

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public <AdapterType> AdapterType getAdapter(Object adaptable,
			Class<AdapterType> type) {
		if (adaptable instanceof Resource) {
			return getAdapter((Resource) adaptable, type);
		}
		return null;
	}

	/**
	 * Adapter
	 * 
	 * @param resource
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <AdapterType> AdapterType getAdapter(Resource resource,
			Class<AdapterType> type) {
		AdapterType result = null;
		if (resource != null && type == IncludeStrategy.class) {

			ResourceResolver adminResourceResolver = null;
			try {
				// Use of admin resource resolver to work on publish instances
				// also
				adminResourceResolver = resourceResolverFactory
						.getAdministrativeResourceResolver(null);
				Resource resourceTypeResource = adminResourceResolver
						.getResource(resource.getResourceType());
				if (resourceTypeResource != null) {
					Node node = resourceTypeResource.adaptTo(Node.class);
					if (node.isNodeType(IncludeStrategyConstants.NT_INCLUDE_STRATEGY)) {
						result = (AdapterType) new IncludeStrategyImpl(
								resourceTypeResource);
					}

				}
			} catch (Exception e) {
				log.error(
						String.format(
								"Could not check whether include strategy is enabled for resource %s",
								resource), e);
			} finally {
				if (adminResourceResolver != null) {
					adminResourceResolver.close();
				}
			}

		}

		return result;
	}
}
