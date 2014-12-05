package mx.unam.ciencias.myp;

/**
* <p>Enumeracion que sirve para modelar una grámatica formal
* libre de contexto </p>
*/

public enum Gramatica {
		NUMERO(true),
		OPERADOR(false),
		VARIABLE(true),
		FUNCION(false),
		P_I(false),
		P_F(false),
            EXPRESION(false),
		DESCONOCIDO(false);

	private final boolean terminal;

  	Gramatica(boolean terminal) { 
  		this.terminal = terminal; 
 	}

  public boolean esSimboloTerminal() { 
  	return this.terminal; 
  }

  public boolean esOperable() { 
  	if(this == Gramatica.OPERADOR || this == Gramatica.FUNCION || this == Gramatica.VARIABLE)
  		return true;
  	return false;
  }

  public boolean esExpresion() {
    if(this == Gramatica.OPERADOR || this == Gramatica.NUMERO || 
      this == Gramatica.VARIABLE  || this == Gramatica.FUNCION ||
      this == Gramatica.P_I) 
      return true;
    return false;
  }
	
}