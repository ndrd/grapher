package mx.unam.ciencias.edd;

/**
* Interface publica que permite colapsar el {@link ArbolDerivacion}
* Tomando un vertice con hojas y permitiendo reasignar el resultado al padre.
* Se ideo como una manera de mantener la generalidad de los arboles
* pero poder tener manipulacion de los objetos del tipo {@link Muestra}
* y asi se pudiera operar con elementos de la clase {@link Funcion}
*/

public interface AccionColapsaHojas<T> {
	/**
	* Funcion que permite colapsar los vertices de determinada manera
	* Toma los vértices padre e hijo, opera las funciones del padre
	* con respecto a los hijos
	* @param operadores una cola con los elementos del arbol
	*  @return resultado el resultado de la evaluacion que puede ser 
	* 		  reasigndo al padre
	*/
    public T actua(MeteSaca<T> operadores);
}