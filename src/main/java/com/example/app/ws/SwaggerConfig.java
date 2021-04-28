package com.example.app.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	Contact contact = new Contact("Pooja Pogul", "http://www.example.com", "test@test.com");
	
	List<VendorExtension> vendorExtensions = new ArrayList<>();
	
	
	ApiInfo apiInfo = new ApiInfo("Photo App RESTful Web Service documentation",
			"This pages documents Photo App RESTful  Web Service endpoints",
			"1.0", 
			"http://www.example.com/service.html", 
			contact, 
			"Apache 2.0",
			"http://www.apache.org/licenses/LICENSE-2.0",
			vendorExtensions);
	
	@Bean
	public Docket apiDocket() {
		
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<String>(Arrays.asList("HTTP", "HTTPS")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.app.ws"))
				.paths(PathSelectors.any())
				.build();
				
		return docket;
		
	}
	
	@Bean
	public LinkDiscoverers discoverers() {

		List<LinkDiscoverer> plugins = new ArrayList<LinkDiscoverer>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
		
	}

}
