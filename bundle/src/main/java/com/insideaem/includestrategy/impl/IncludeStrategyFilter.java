package com.insideaem.includestrategy.impl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insideaem.includestrategy.api.IncludeStrategy;
import com.insideaem.includestrategy.api.IncludeStrategyConstants;
import com.insideaem.includestrategy.api.IncludeStrategyResource;

@Component(immediate = true)
@Service
@Properties({
		@Property(name = "sling.filter.scope", value = "COMPONENT"),
		@Property(name = Constants.SERVICE_RANKING, intValue = -200000, propertyPrivate = false),
		@Property(name = "service.description", value = "Include Strategy Support Filter. This can be used to include a component using different strategies"),
		@Property(name = "service.vendor", value = "InsideAEM") })
public class IncludeStrategyFilter implements Filter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		boolean performIncludeStrategy = false;
		SlingHttpServletRequest slingRequest = null;
		IncludeStrategy includeStrategy = null;
		if (isHtmlExtension(servletRequest)) {

			includeStrategy = getIncludeStrategy(servletRequest);
			if (includeStrategy != null) {
				// Include Strategy defined
				slingRequest = (SlingHttpServletRequest) servletRequest;
				if (noIncludeStrategySelector(slingRequest)) {
					// No include strategy selector --> wrap resource and render
					performIncludeStrategy = true;
				} else {
					// Include strategy selector
					if (includeStrategy.isCachingDisabled()) {
						SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) servletResponse;
						slingResponse.setHeader("Dispatcher", "no-cache");
					}
				}
			}
		}

		if (performIncludeStrategy) {
			IncludeStrategyResource includeStrategyResource = new IncludeStrategyResourceImpl(
					slingRequest.getResource());
			String includeUrl = String.format(
					"%s.%s.html",
					includeStrategyResource.getResourceResolver().map(
							includeStrategyResource.getPath()),
					IncludeStrategyConstants.INCLUDE_STRATEGY_SELECTOR);
			slingRequest.setAttribute(
					IncludeStrategyConstants.REQ_ATTR_INCLUDE_STRATEGY_URL,
					includeUrl);

			slingRequest.getRequestDispatcher(includeStrategyResource).include(
					servletRequest, servletResponse);
			slingRequest
					.removeAttribute(IncludeStrategyConstants.REQ_ATTR_INCLUDE_STRATEGY_URL);
		} else {
			// normal processing
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	private boolean isHtmlExtension(ServletRequest servletRequest) {
		return ((HttpServletRequest) servletRequest).getRequestURI().endsWith(
				".html");
	}

	private boolean noIncludeStrategySelector(
			SlingHttpServletRequest servletRequest) {
		String selector = servletRequest.getRequestPathInfo()
				.getSelectorString();
		return !StringUtils.equals(selector,
				IncludeStrategyConstants.INCLUDE_STRATEGY_SELECTOR);
	}

	private IncludeStrategy getIncludeStrategy(ServletRequest servletRequest) {
		IncludeStrategy result = null;
		if (servletRequest instanceof SlingHttpServletRequest) {
			SlingHttpServletRequest slingHttpServletRequest = (SlingHttpServletRequest) servletRequest;
			result = slingHttpServletRequest.getResource().adaptTo(
					IncludeStrategy.class);
		}

		return result;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing

	}

	class InternalSlingHttpServletRequestWrapper extends
			SlingHttpServletRequestWrapper {

		public InternalSlingHttpServletRequestWrapper(
				SlingHttpServletRequest wrappedRequest) {
			super(wrappedRequest);
		}

		@Override
		public RequestDispatcher getRequestDispatcher(Resource resource) {
			Resource decoratedResource = this.decorate(resource);
			return super.getRequestDispatcher(decoratedResource);
		}

		@Override
		public RequestDispatcher getRequestDispatcher(Resource resource,
				RequestDispatcherOptions options) {
			Resource decoratedResource = this.decorate(resource);
			return super.getRequestDispatcher(decoratedResource, options);
		}

		public Resource decorate(Resource resource) {
			return new IncludeStrategyResourceImpl(resource);
		}
	}

}
