package br.com.lgss.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum TipoProblema {
	
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema");
	
	private String title;
	private String uri;
	
	private TipoProblema(String path, String title) {
		this.uri = "https://lgss.com.br" + path;
		this.title = title;
	}
}
