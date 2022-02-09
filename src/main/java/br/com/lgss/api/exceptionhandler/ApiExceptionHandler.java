package br.com.lgss.api.exceptionhandler;

import java.time.OffsetDateTime;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.lgss.domain.exception.CepMalFormadoException;
import br.com.lgss.domain.exception.CepNaoEncontradoException;
import br.com.lgss.domain.exception.ErroExternoException;
import br.com.lgss.domain.exception.ParametroLocalidadeInvalidoException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String MENSAGEM_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
			+ "Tente novamente. Se o problema persistir entre em contato com o Administrador";
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		
		
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		String detail = "O parâmetro [" + ex.getParameterName() + "] é obrigatório e não foi informado. Verifique e tente novamente.";
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(ex, problema, 
				new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null) {
			body = Problema.builder()
					.titulo(status.getReasonPhrase())
					.status(status.value())
					.dataHora(OffsetDateTime.now())
					.mensagemUsuario(MENSAGEM_ERRO_GENERICO_USUARIO_FINAL)
					.build();
		} else if (body instanceof String) {
			body = Problema.builder() 
					.titulo((String) body)
					.status(status.value())
					.dataHora(OffsetDateTime.now())
					.mensagemUsuario(MENSAGEM_ERRO_GENERICO_USUARIO_FINAL)
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
				
		String detail = "Informe a sigla de um estado da federação no parâmetro uf";
		
		Problema problem = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problem, 
				new HttpHeaders(), status, request);
	}

	
	@ExceptionHandler(CepMalFormadoException.class)
	public ResponseEntity<?> handleConstraintViolationException(CepMalFormadoException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		
		
		String detail = "O CEP informado é inválido. Utilize somente números, com comprimento de 8 dígitos.";
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problema, 
				new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ParametroLocalidadeInvalidoException.class)
	public ResponseEntity<?> handleConstraintViolationException(ParametroLocalidadeInvalidoException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		
		
		String detail = "Algum parâmetro de localidade é inválido. Verifique e tente novamente";
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problema, 
				new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(CepNaoEncontradoException.class)
	public ResponseEntity<?> handleConstraintViolationException(CepNaoEncontradoException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		TipoProblema tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		
		
		String detail = "Cep não encontrado na base dos correios";
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problema, 
				new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ErroExternoException.class)
	public ResponseEntity<?> handleErroExternoException(ErroExternoException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		TipoProblema tipoProblema = TipoProblema.ERRO_DE_SISTEMA;
		
		
		String detail = "Ocorreu um erro externo ao serviço. Tente novamente mais tarde.";
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problema, 
				new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleConstraintViolationException(RuntimeException e, 
			WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		TipoProblema tipoProblema = TipoProblema.ERRO_DE_SISTEMA;
		
		
		String detail = MENSAGEM_ERRO_GENERICO_USUARIO_FINAL;
		
		Problema problema = createProblemaBuilder(status, tipoProblema, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(e, problema, 
				new HttpHeaders(), status, request);
	}
	
	private Problema.ProblemaBuilder createProblemaBuilder(HttpStatus status, TipoProblema tipoProblema, String detalhe) {
		return Problema.builder()
				.detalhe(detalhe)
				.status(status.value())
				.tipo(tipoProblema.getUri())
				.titulo(tipoProblema.getTitle())
				.dataHora(OffsetDateTime.now());

	}
	
}
