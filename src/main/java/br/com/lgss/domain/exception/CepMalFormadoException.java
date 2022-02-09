package br.com.lgss.domain.exception;

public class CepMalFormadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2923049872761414503L;

	public CepMalFormadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CepMalFormadoException(String message) {
		super(message);
	}
	
	

}
