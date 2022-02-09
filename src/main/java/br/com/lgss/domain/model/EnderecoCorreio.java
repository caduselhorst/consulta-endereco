package br.com.lgss.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoCorreio {
	
	@ApiModelProperty(value = "CEP da localidade", example = "68902-020")
	private String cep;
	
	@ApiModelProperty(value = "Logradouro", example = "Rua Leopoldo Machado")
	private String logradouro;
	
	@ApiModelProperty(value = "Complemento do endereço na base dos Correios", example = "de 3147/3148 ao fim")
	private String complemento;
	
	@ApiModelProperty(value = "Bairro do endereço", example = "Beirol")
	private String bairro;
	
	@ApiModelProperty(value = "Localidade do endereço", example = "Macapá")
	private String localidade;
	
	@ApiModelProperty(value = "Sigla do estado (unidade da federação)", example = "AP")
	private String uf;
	
	@ApiModelProperty(value = "Código da localidade no IBGE", example = "1600303")
	private String ibge;
	
	@ApiModelProperty(value = "Código da localidade para Guia de Informação e Apuração de ICMS (somente SP)", example = "1004")
	private String gia;
	
	@ApiModelProperty(value = "Código de Dicagem Direta a Distância da localidade", example = "96")
	private String ddd;
	
	@ApiModelProperty(value = "Código da Localidade no SIAFI", example = "0605")
	private String siafi;
	
	@ApiModelProperty(value = "Indicativo de erro. Valor TRUE quando não encontra a localidade", example = "")
	private Boolean erro;

}
