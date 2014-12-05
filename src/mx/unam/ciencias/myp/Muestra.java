package mx.unam.ciencias.myp;

/** 
* <p> Clase que modela una Muestra o <i>Token</i></p>
* <p> Sirve para generar una correspondencia entre determinador
* objeto y un tipo arbitrario asignado por una {@link Gramatica}
*/
public class Muestra<T> implements Comparable<Muestra<T>> {
	/** Objeto que va a almacenar la muestra */
	private T objeto;
	/** El tipo que representa dicho objeto dentro de la gramática */
	private Gramatica tipo;
	/** para orientar el arbol */
	private int peso;

	/**
	* <p> Constructor que recibe un objeto tipo T, y su tipo dentro de 
	* una gramatica
	* @param objeto el objeto que va a almacenar la muestra.
	* @param tipo el tipo dentro de la gramatica que representa el objeto.
	*/
	public Muestra(T objeto, Gramatica tipo) {
		this(objeto, tipo, 0);
	}	

	public Muestra(T objeto, Gramatica tipo, int peso ) {
		this.objeto = objeto;
		this.tipo = tipo;
		this.peso = peso;
	}

	/**
	* <p> Devuelve el tipo de la muestra </p>
	* @return objeto el tipo de la muestra actual
	*/
	public T get() {
		return objeto;
	}
	public void set(T objeto) {
		this.objeto = objeto;
	}

	public Gramatica tipo() {
		return tipo;
	}
 
	public void setPeso(int n) {
		peso = n;
	}

	public int peso() {
		return peso;
	}

	/* compara dos muestras por el peso, asi nos ayuda a balancear el arbol */
	@Override	
	public int compareTo(Muestra<T> m) {
		return this.peso - m.peso;
	}

	@Override
	public String toString() {
		return  objeto.toString();
	}

	@Override public boolean equals(Object o) {
		if (o ==  null) {
			return false;
		} else {
			 @SuppressWarnings("unchecked")
		        	Muestra<T> m = getClass().cast(o);
		        	if(m.tipo == this.tipo && m.peso ==  this.peso && m.objeto.equals(objeto)) 
						return true;
		        	else if(m.tipo == this.tipo && m.objeto.equals(objeto)) 
		        		return true;
		        	return false;
		} 
	}
}