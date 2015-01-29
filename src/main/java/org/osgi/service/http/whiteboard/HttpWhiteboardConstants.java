/*
 * Copyright (c) OSGi Alliance (2012, 2014). All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.service.http.whiteboard;

import org.osgi.framework.Filter;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.runtime.HttpServiceRuntimeConstants;

/**
 * Defines standard constants for the whiteboard services.
 * 
 * @author $Id$
 */
public final class HttpWhiteboardConstants {
	private HttpWhiteboardConstants() {
		// non-instantiable
	}

	/**
	 * Service property specifying the name of an {@link ServletContextHelper}
	 * service.
	 * 
	 * <p>
	 * For {@link ServletContextHelper} services, this service property must be
	 * specified. Context services without this service property are ignored.
	 * 
	 * <p>
	 * Servlet, listener, servlet filter, and resource services might refer to a
	 * specific {@link ServletContextHelper} service referencing the name with
	 * the {@link #HTTP_WHITEBOARD_CONTEXT_SELECT} property.
	 * 
	 * <p>
	 * For {@link ServletContextHelper} services, the value of this service
	 * property must be of type {@code String}. The value must follow the
	 * "symbolic-name" specification from Section 1.3.2 of the OSGi Core
	 * Specification.
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_PATH
	 * @see #HTTP_WHITEBOARD_CONTEXT_SELECT
	 * @see #HTTP_WHITEBOARD_DEFAULT_CONTEXT_NAME
	 */
	public static final String	HTTP_WHITEBOARD_CONTEXT_NAME			= "osgi.http.whiteboard.context.name";

	/**
	 * The name of the default {@link ServletContextHelper}. If a service is
	 * registered with this property, it is overriding the default context with
	 * a custom provided context.
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_NAME
	 */
	public static final String	HTTP_WHITEBOARD_DEFAULT_CONTEXT_NAME	= "default";

	/**
	 * Service property specifying the path of an {@link ServletContextHelper}
	 * service.
	 * 
	 * <p>
	 * For {@link ServletContextHelper} services this service property is
	 * required. Context services without this service property are ignored.
	 * 
	 * <p>
	 * This property defines a context path under which all whiteboard services
	 * associated with this context are registered. Having different contexts
	 * with different paths allows to separate the URL space.
	 * 
	 * <p>
	 * For {@link ServletContextHelper} services, the value of this service
	 * property must be of type {@code String}. The value is either a slash for
	 * the root or it must start with a slash but not end with a slash. Valid
	 * characters are defined in rfc3986#section-3.3. Contexts with an invalid
	 * path are ignored.
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_NAME
	 * @see #HTTP_WHITEBOARD_CONTEXT_SELECT
	 */
	public static final String	HTTP_WHITEBOARD_CONTEXT_PATH			= "osgi.http.whiteboard.context.path";

	/**
	 * Service property referencing a {@link ServletContextHelper} service.
	 * 
	 * <p>
	 * For servlet, listener, servlet filter, or resource services, this service
	 * property refers to the associated {@code ServletContextHelper} service.
	 * The value of this property is a filter expression which is matched
	 * against the service registration properties of the
	 * {@code ServletContextHelper} service. If this service property is not
	 * specified, the default context is used. If there is no context service
	 * matching, the servlet, listener, servlet filter, or resource service is
	 * ignored.
	 * <p>
	 * For example, if a whiteboard service wants to select a servlet context
	 * helper with the name &quot;Admin&quot; the expression would be
	 * &quot;(osgi.http.whiteboard.context.name=Admin)&quot;. Selecting all
	 * contexts could be done with
	 * &quot;(osgi.http.whiteboard.context.name=*)&quot;.
	 * <p>
	 * For servlet, listener, servlet filter, or resource services, the value of
	 * this service property must be of type {@code String}.
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_NAME
	 * @see #HTTP_WHITEBOARD_CONTEXT_PATH
	 */
	public static final String	HTTP_WHITEBOARD_CONTEXT_SELECT			= "osgi.http.whiteboard.context.select";

	/**
	 * Service property specifying the servlet name of a {@code Servlet}
	 * service.
	 * 
	 * <p>
	 * This name is used as the value for the
	 * {@code ServletConfig.getServletName()} method. If this service property
	 * is not specified, the fully qualified name of the service object's class
	 * is used as the servlet name. Filter services may refer to servlets by
	 * this name in their {@link #HTTP_WHITEBOARD_FILTER_SERVLET} service
	 * property to apply the filter to the servlet.
	 * 
	 * <p>
	 * Servlet names must be unique among all servlet services associated with a
	 * single {@link ServletContextHelper}. If multiple servlet services
	 * associated with the same HttpContext have the same servlet name, then all
	 * but the highest ranked servlet service are ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String}.
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_NAME			= "osgi.http.whiteboard.servlet.name";

	/**
	 * Service property specifying the request mappings for a {@code Servlet}
	 * service.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to the servlet. Servlet services without this service property or
	 * {@link #HTTP_WHITEBOARD_SERVLET_ERROR_PAGE} are ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 12.2 Specification of Mappings"
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_PATTERN			= "osgi.http.whiteboard.servlet.pattern";

	/**
	 * Service property specifying whether a {@code Servlet} service acts as an
	 * error page.
	 * 
	 * <p>
	 * The service property values may be the name of a fully qualified
	 * exception class, a three digit HTTP status code, the value "4xx" for all
	 * error codes in the 400 rage, or the value "5xx" for all error codes in
	 * the 500 rage. Any value that is not a three digit number, or one of the
	 * two special values is considered to be the name of a fully qualified
	 * exception class.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_ERROR_PAGE		= "osgi.http.whiteboard.servlet.errorPage";

	/**
	 * Service property specifying whether a {@code Servlet} service supports
	 * asynchronous processing.
	 * 
	 * <p>
	 * By default servlet services do not support asynchronous processing.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code Boolean}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 2.3.3.3 Asynchronous Processing"
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED	= "osgi.http.whiteboard.servlet.asyncSupported";

	/**
	 * Service property specifying the servlet filter name of a {@code Filter}
	 * service.
	 * 
	 * <p>
	 * This name is used as the value for the
	 * {@code FilterConfig.getFilterName()} method. If this service property is
	 * not specified, the fully qualified name of the service object's class is
	 * used as the servlet filter name.
	 * 
	 * <p>
	 * Servlet filter names must be unique among all servlet filter services
	 * associated with a single {@link ServletContextHelper}. If multiple
	 * servlet filter services associated with the same context have the same
	 * servlet filter name, then all but the highest ranked servlet filter
	 * service are ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String}.
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_NAME				= "osgi.http.whiteboard.filter.name";

	/**
	 * Service property specifying the request mappings for a {@code Filter}
	 * service.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to the servlet filter. Filter services without this service
	 * property or the {@link #HTTP_WHITEBOARD_FILTER_SERVLET} or the
	 * {@link #HTTP_WHITEBOARD_FILTER_REGEX} service property are ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 12.2 Specification of Mappings"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_PATTERN			= "osgi.http.whiteboard.filter.pattern";

	/**
	 * Service property specifying the {@link #HTTP_WHITEBOARD_SERVLET_NAME
	 * servlet names} for a servlet {@code Filter} service.
	 * 
	 * <p>
	 * The specified names are used to determine the servlets whose requests
	 * should be mapped to the servlet filter. Servlet filter services without
	 * this service property or the {@link #HTTP_WHITEBOARD_FILTER_PATTERN} or
	 * the {@link #HTTP_WHITEBOARD_FILTER_REGEX} service propertyare ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_SERVLET			= "osgi.http.whiteboard.filter.servlet";

	/**
	 * Service property specifying the request mappings for a servlet
	 * {@code Filter} service.
	 * 
	 * <p>
	 * The specified regular expressions are used to determine whether a request
	 * should be mapped to the servlet filter. The regular expressions must
	 * follow the syntax defined in {@code java.util.regex.Pattern}. Servlet
	 * filter services without this service property or the
	 * {@link #HTTP_WHITEBOARD_FILTER_SERVLET} or the
	 * {@link #HTTP_WHITEBOARD_FILTER_PATTERN} service property are ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "java.util.regex.Pattern"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_REGEX			= "osgi.http.whiteboard.filter.regex";

	/**
	 * Service property specifying whether a servlet {@code Filter} service
	 * supports asynchronous processing.
	 * 
	 * <p>
	 * By default servlet filters services do not support asynchronous
	 * processing.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code Boolean}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 2.3.3.3 Asynchronous Processing"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_ASYNC_SUPPORTED	= "osgi.http.whiteboard.filter.asyncSupported";

	/**
	 * Service property specifying the dispatcher handling of a servlet
	 * {@code Filter}.
	 * 
	 * <p>
	 * By default servlet filter services are associated with client requests
	 * only (see value {@link #DISPATCHER_REQUEST}).
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}. Allowed values are
	 * {@link #DISPATCHER_ASYNC}, {@link #DISPATCHER_ERROR},
	 * {@link #DISPATCHER_FORWARD}, {@link #DISPATCHER_INCLUDE},
	 * {@link #DISPATCHER_REQUEST}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_DISPATCHER		= "osgi.http.whiteboard.filter.dispatcher";

	/**
	 * Possible value for the {@link #HTTP_WHITEBOARD_FILTER_DISPATCHER}
	 * property indicating the servlet filter is applied to client requests.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	DISPATCHER_REQUEST						= "REQUEST";

	/**
	 * Possible value for the {@link #HTTP_WHITEBOARD_FILTER_DISPATCHER}
	 * property indicating the servlet filter is applied to include calls to the
	 * dispatcher.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	DISPATCHER_INCLUDE						= "INCLUDE";

	/**
	 * Possible value for the {@link #HTTP_WHITEBOARD_FILTER_DISPATCHER}
	 * property indicating the servlet filter is applied to forward calls to the
	 * dispatcher.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	DISPATCHER_FORWARD						= "FORWARD";

	/**
	 * Possible value for the {@link #HTTP_WHITEBOARD_FILTER_DISPATCHER}
	 * property indicating the servlet filter is applied in the asynchronous
	 * context.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	DISPATCHER_ASYNC						= "ASYNC";

	/**
	 * Possible value for the {@link #HTTP_WHITEBOARD_FILTER_DISPATCHER}
	 * property indicating the servlet filter is applied when an error page is
	 * called.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 6.2.5 Filters and the RequestDispatcher"
	 */
	public static final String	DISPATCHER_ERROR						= "ERROR";

	/**
	 * Service property specifying the request mappings for resources.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to resources. Resource services without this service property are
	 * ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 12.2 Specification of Mappings"
	 * @see #HTTP_WHITEBOARD_RESOURCE_PREFIX
	 */
	public static final String	HTTP_WHITEBOARD_RESOURCE_PATTERN		= "osgi.http.whiteboard.resource.pattern";

	/**
	 * Service property specifying the resource entry prefix for a resource
	 * service.
	 * 
	 * <p>
	 * If a resource service is registered with this property, requests are
	 * served with bundle resources.
	 * 
	 * <p>
	 * This prefix is used to map a requested resource to the bundle's entries.
	 * The value must not end with slash (&quot;/&quot;) with the exception that
	 * a name of the form &quot;/&quot; is used to denote the root of the
	 * bundle. See the specification text for details on how HTTP requests are
	 * mapped.
	 *
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see #HTTP_WHITEBOARD_RESOURCE_PATTERN
	 */
	public static final String	HTTP_WHITEBOARD_RESOURCE_PREFIX			= "osgi.http.whiteboard.resource.prefix";

	/**
	 * Service property specifying the target filter to select the Http
	 * Whiteboard Implementation to process the service.
	 * 
	 * <p>
	 * An Http Whiteboard Implementation can define any number of attributes
	 * which can be referenced by the target filter. The attributes should
	 * always include the
	 * {@link HttpServiceRuntimeConstants#HTTP_SERVICE_ENDPOINT_ATTRIBUTE
	 * osgi.http.endpoint} attribute if the endpoint information is known.
	 * 
	 * <p>
	 * If this service property is not specified, then all Http Whiteboard
	 * Implementations can process the service.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String} and be
	 * a valid {@link Filter filter string}.
	 */
	public static final String	HTTP_WHITEBOARD_TARGET					= "osgi.http.whiteboard.target";
}
