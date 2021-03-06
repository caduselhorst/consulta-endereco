package br.com.lgss.api.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lgss.core.validation.Estado;
import br.com.lgss.domain.model.EnderecoCorreio;
import br.com.lgss.domain.service.ConsultaEnderecoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Endereços")
@Validated
@RestController
@RequestMapping(path = "enderecos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsultaEnderecoController {
	
	@Autowired
	private ConsultaEnderecoService consultaEnderecoService;
	
	@ApiOperation("Consulta o endereço pelo CEP")
	@GetMapping(path = "/cep")
	public ResponseEntity<EnderecoCorreio> porCep(
			@ApiParam(value = "Cep a ser consultado", example = "68902020") String cep) {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
				.body(consultaEnderecoService.consultaEnderecoPeloCep(cep));
	}
	
	@ApiOperation("Consulta endereços por uma Localidade")
	@GetMapping(path = "/localidade")
	public ResponseEntity<List<EnderecoCorreio>> porLocalidade(
			@ApiParam(value = "Sigla da unidade da federação",  example = "AP") @RequestParam @Estado String uf, 
			@ApiParam(value = "Cidade",  example = "Macapá") @RequestParam String cidade, 
			@ApiParam(value = "Parte do logradouro",  example = "Leopoldo") @RequestParam String logradouro) {		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
				.body(consultaEnderecoService.consultaEnderecosPorLocalidade(uf, cidade, logradouro));
	}

}
