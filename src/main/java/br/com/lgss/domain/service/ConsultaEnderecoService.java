package br.com.lgss.domain.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.lgss.domain.exception.CepMalFormadoException;
import br.com.lgss.domain.exception.CepNaoEncontradoException;
import br.com.lgss.domain.exception.ParametroLocalidadeInvalidoException;
import br.com.lgss.domain.model.EnderecoCorreio;

@Service
public class ConsultaEnderecoService {

	@Autowired
	private RestTemplate restTemplate;

	public EnderecoCorreio consultaEnderecoPeloCep(String cep) {

		try {
			URI resourceURI = URI.create("https://viacep.com.br/ws/" + cep + "/json");

			EnderecoCorreio endereco = restTemplate.getForObject(resourceURI, EnderecoCorreio.class);
			
			if(endereco.getErro() != null && endereco.getErro().booleanValue()) {
				throw new CepNaoEncontradoException("O cep informado não foi encontrado");
			}
			
			return endereco;
		} catch (RestClientResponseException e) {
			
			if(e.getRawStatusCode() == 400) {
				throw new CepMalFormadoException("O cep informado não está bem formado. Aceita somente números e comprimento de 8");
			} else {
				throw new RuntimeException("Ocorreu um erro ao consultar o cep", e);
			}
		}

	}
	
	public List<EnderecoCorreio> consultaEnderecosPorLocalidade(String uf, String cidade, String logradouro) {
		try {
			URI resourceURI = URI.create(String.format("https://viacep.com.br/ws/%1s/%2s/%3s/json", 
					uf, cidade, logradouro));

			EnderecoCorreio[] enderecos = restTemplate.getForObject(resourceURI, EnderecoCorreio[].class);
			
			return Arrays.asList(enderecos);
		} catch (RestClientResponseException e) {
			
			if(e.getRawStatusCode() == 400) {
				throw new ParametroLocalidadeInvalidoException("Parâmetro de localidade inválido");
			} else {
				throw new RuntimeException("Ocorreu um erro ao consultar o cep", e);
			}
		}
	}

}
