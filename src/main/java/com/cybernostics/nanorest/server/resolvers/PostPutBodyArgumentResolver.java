package com.cybernostics.nanorest.server.resolvers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cybernostics.nanorest.lib.interfaceparsers.EntityRestService;

public class PostPutBodyArgumentResolver implements  HandlerMethodArgumentResolver {

	private final class RestHttpRequest implements HttpInputMessage {
		private final HttpServletRequest servletRequest;
		private HttpHeaders headers;

		private RestHttpRequest(HttpServletRequest servletRequest) {
			this.servletRequest = servletRequest;
			headers = new HttpHeaders();
			Enumeration<String> headerNames = servletRequest.getHeaderNames();
			if (headerNames.hasMoreElements()) {
				for (String eachHeader = headerNames.nextElement();headerNames.hasMoreElements(); eachHeader = headerNames.nextElement()) {
					headers.add(eachHeader, servletRequest.getHeader(eachHeader));
				}
			}
		}

		@Override
		public HttpHeaders getHeaders() {
			return headers;
		}

		@Override
		public InputStream getBody() throws IOException {
			return servletRequest.getInputStream();
		}
	}

	@Autowired
	private HttpMessageConverter<?>[] messageConverters;

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
		this.messageConverters = messageConverters;
	}

    protected void raiseMissingParameterException(String paramName,
                                                  Class<?> paramType) throws Exception {
        throw new IllegalStateException("Missing parameter '" + paramName
                                        + "' of type [" + paramType.getName() + "]");
    }

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		if (AnnotationUtils.findAnnotation(parameter.getDeclaringClass(), EntityRestService.class) == null)
		{
			return false;
		}
		String methodName = parameter.getMethod().getName();
		if(methodName.startsWith("post") || methodName.startsWith("put"))
		{
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		HttpInputMessage httpInputMessage = new RestHttpRequest(servletRequest);
		return messageBodyToObject(parameter, httpInputMessage, parameter.getParameterType());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object messageBodyToObject(MethodParameter methodParam, HttpInputMessage inputMessage, Class<?> paramType)
			throws Exception {

		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
			String paramName = methodParam.getParameterName();
			if (paramName != null) {
				builder.append(' ');
				builder.append(paramName);
			}
			throw new HttpMediaTypeNotSupportedException(
					"Cannot extract parameter (" + builder.toString() + "): no Content-Type found");
		}

		List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
		if (this.messageConverters != null) {
			for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
				allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
				if (messageConverter.canRead(paramType, contentType)) {
//					if (logger.isDebugEnabled()) {
//						logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType
//								+"\" using [" + messageConverter + "]");
//					}
					return messageConverter.read((Class) paramType, inputMessage);
				}
			}
		}
		throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
	}
}
