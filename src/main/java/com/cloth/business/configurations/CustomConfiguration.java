package com.cloth.business.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfiguration implements WebMvcConfigurer {

	
	//USED BECAUSE:
	//When using jasper, it return xml files instead of JSON. Configured it so that URL will return JSON, NOT XML
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
				.defaultContentType(MediaType.APPLICATION_JSON);
	}
}
