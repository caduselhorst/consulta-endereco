package br.com.lgss.api.exceptionhandler;

import java.time.OffsetDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Problema {

	@ApiModelProperty(value = "Código do status HTTP", example = "400")
	private Integer status;
	
	@ApiModelProperty(value = "Tipo do problema", example = "https://lgss.com.br/dados-invalidos")
	private String tipo;
	
	@ApiModelProperty(value = "Título do problema", example = "Dados inválidos")
	private String titulo;
	
	@ApiModelProperty(value = "Detalhe do problema", example = "Informe a sigla de um estado da federação no parâmetro uf")
	private String detalhe;
	
	@ApiModelProperty(value = "Mensagem ao usuário final", example = "Informe a sigla de um estado da federação no parâmetro uf")
	private String mensagemUsuario;
	
	@ApiModelProperty(value = "Data e hora do problema (UTC)", example = "2022-02-09T01:44:28.221787Z")
	private OffsetDateTime dataHora;

}
