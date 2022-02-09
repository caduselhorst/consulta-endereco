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
import br.com.lgss.domain.exception.ErroExternoException;
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
						
			String url = String.format("https://viacep.com.br/ws/%1s/%2s/%3s/json", 
					uf, formataValorParametro(cidade), formataValorParametro(logradouro));
						
			System.out.println(url);
			
			URI resourceUri = URI.create(url);

			EnderecoCorreio[] enderecos = restTemplate.getForObject(resourceUri, EnderecoCorreio[].class);
			
			return Arrays.asList(enderecos);
		} catch (RestClientResponseException e) {
			
			if(e.getRawStatusCode() == 400) {
				throw new ParametroLocalidadeInvalidoException("Parâmetro de localidade inválido");
			} else {
				throw new ErroExternoException("Ocorreu um erro ao consultar o cep", e);
			}
		} 
	}
	
	private String formataValorParametro(String parametro) {
		
		String novo = parametro.replace(" ", "%20");
		
		return novo.toLowerCase()
				.replace("ã", "a")
				.replace("õ", "o")
				.replace("á", "a")
				.replace("é", "e")
				.replace("ç", "c")
				.replace("ó", "o")
				.replace("ê", "e")
				.replace("â", "a");
		
	}

}
