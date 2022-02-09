package br.com.lgss.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.lgss.api.exceptionhandler.Problema;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
	
	private static final String MSG_INTERNAL_SERVER_ERROR = "Erro interno do Servidor";
	private static final String MSG_BAD_REQUEST = "Requisição inválida (erro do cliente)";

	@Bean
	public Docket apiDocket() {
		
		TypeResolver typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.SWAGGER_2 /*DocumentationType.OAS_30*/)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.lgss.api"))
				.paths(PathSelectors.any())
				.build()
			.useDefaultResponseMessages(false)
			.globalResponses(HttpMethod.GET, globalGetResponseMessages())
			.ignoredParameterTypes(ServletWebRequest.class)
			.additionalModels(typeResolver.resolve(Problema.class))
			.apiInfo(apiInfo())
			.tags(
				new Tag("Endereços", "Realiza as consultas de endereços por CEP ou Localidade")
				
			);
	}
	
	// corrigir serialização de data OpenAPI 3 com SpringFox 3
	@Bean
	public JacksonModuleRegistrar springFoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Consulta Endereço API")
				.description("API aberta para consulta de endeços utilizando o Web Service da ViaCEP (https://viacep.com.br)")
				.version("1.0.0")
				.contact(new Contact("LGSS Sistemas", "https://www.lgss.com.br", "suporte@lgss.com.br"))
				.build();
	}
	
	
	
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description(MSG_INTERNAL_SERVER_ERROR)
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description(MSG_BAD_REQUEST)
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build());
	}
	
	
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("br.com.lgss.api.exceptionhandler")))));
	}

}
