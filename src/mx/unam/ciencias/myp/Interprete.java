package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.ArbolDerivacion;
import mx.unam.ciencias.edd.AccionColapsaHojas;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.MeteSaca;

/**
* Clase que se crea a partir de un {@link ArbolDerivacion}
* de funciones, y devuelve los resultados de sus evaluaciones
* dentro de determinado rango, asume que el arbol ya esta 
* en un estado valido.
**/
public class Interprete{
	private ArbolDerivacion<Muestra<Funcion>> arbol;

	/* Clase local para tener una lista de puntos */	
	class Punto {
		private double x;
		private double y;

		Punto(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		@Override public String toString() {
			return x + ", " + y + " ";
		}


	}

	/**
	* Constructor por defecto que recibe un {@link ArbolDerivacion} y
	* lo utiliza para poder evaluar los valores de una  para todas la jeraquia de 
	* operadores.
	*/
	Interprete(ArbolDerivacion<Muestra<Funcion>> arbol){
		this.arbol = arbol;
	}

	/**
	* Evalua el árbol cargado dentro del rango dado.
	* @param x0 el inicio del intervalo
	* @param x1 final del intervalo
	* @return puntos una lista con las coordenadas de la evaluacion
	*/
	public  Lista<Punto> evalua(double x0, double x1) {
		return evalua(x0,x1,2);
	}

	/**
	* Evalua el árbol cargado dentro del rango dado.
	* @param x0 el inicio del intervalo
	* @param x1 final del intervalo
	* @param paso el número de espacios entre evaluaciones 
	* @return puntos una lista con las coordenadas de la evaluacion
	*/
	public  Lista<Punto> evalua(double x0, double x1, int paso) {
		int LIMITE_EVALUACION = 300;
		double delta = x1 - x0;
		double incremento = delta / LIMITE_EVALUACION;
		Lista<Punto> puntos = new Lista<>();
		for (int i = 0;i < LIMITE_EVALUACION ; i++ ) {
			puntos.agregaFinal(evalua(x0 + incremento*i));
			
		}
		return puntos;
	}

	/**
	* Evalua un solo valor y devuelve un punto con las coordenadas
	* resultantes
	* @param x el valor a evaluar dentro de la funcion
	* @return p el punto con las coordenadas resultantes
	 */
	public Punto evalua(double x) {
		final Muestra<Funcion> nuevo = new Muestra<Funcion>(new Funcion.Constante(x), Gramatica.NUMERO);
		Muestra<Funcion> y = arbol.colapsaHojas(new AccionColapsaHojas<Muestra<Funcion>>() {
			@Override public Muestra<Funcion> actua(MeteSaca<Muestra<Funcion>> cola) {
				return evalua(cola,nuevo,nuevo);
			}
		});
		Funcion.Constante yy = (Funcion.Constante) y.get();
		return new Punto(x, yy.evalua());
	}

	/**
	* Funcion que evalua recursivamente utilizando una pila de operadores, de esta manera  evalua respetando la 
	* jerarquia de operadores 
	*/
	private Muestra<Funcion> evalua(MeteSaca<Muestra<Funcion>> operadores, Muestra<Funcion> ultimo, Muestra<Funcion> valorX) {
		Pila<Muestra<Funcion>> pila = new Pila<>();
		if(operadores.esVacia())
			return ultimo;

		while (!operadores.esVacia()) {
			Muestra<Funcion> izquierdo = operadores.saca();
			Muestra<Funcion> padre = null;
			Muestra<Funcion> derecho = null;
			/* es un operador terminal */
			if (izquierdo.tipo() == Gramatica.VARIABLE) {
				Funcion.Constante val = (Funcion.Constante) izquierdo.get();
				Funcion.Constante vX = (Funcion.Constante) valorX.get();

				if(Double.compare(val.evalua(), Double.MIN_VALUE) == 0) {
					valorX.set(new Funcion.Constante(vX.evalua() * -1));
				} 
				izquierdo = valorX;
			}
			if (izquierdo.tipo() == Gramatica.NUMERO) {
				/* era el ultimo numero de la pila -> resultado */
				if (operadores.esVacia()) {
					if(pila.esVacia())
						return izquierdo;
					else {
						pila.mete(izquierdo);
						break;
					}
				} else {
					/*el padre deber ser una funcion, la gramatica y el tipo de recorrido lo indica */
					padre = operadores.saca();
					/* es un operador, entonces, debe tener un hijo derecho que es el siguiente elemento de la pila */ 
					if (padre.tipo() == Gramatica.OPERADOR) {
						derecho = operadores.saca();
						if (derecho.tipo() == Gramatica.VARIABLE )
							derecho = valorX;
					}
					padre = evalua(padre, izquierdo, derecho);
					ultimo = padre;
					pila.mete(padre);
				}
			/* si sale un operador, entonces, la pila no esta vacia */
			} else {
				/* si es una funcion trigonometrica entonces, evaluamos con la pila que debe tener
				a su hijo como elemento por sacar, pues fuel el ultimo en haber ingresado ya que este
				es su padre */
				if (izquierdo.tipo() == Gramatica.FUNCION) {
					izquierdo = evalua(izquierdo,pila.saca(),null);
				}
				ultimo = izquierdo;
				pila.mete(izquierdo);
			}
		}
		return  evalua(pila, ultimo,valorX);
	}

	/**
	* Evalua una funcion con respecto a su padre, si es una funcion binaria, toma a su hijo izquiero y derecho
	* para generar una nueva muestra con el resultado de sus expresiones 
	*/
	private Muestra<Funcion> evalua(Muestra<Funcion> padre, Muestra<Funcion> izquierdo, Muestra<Funcion> derecho) {
		/* sabemos que nuestro árbol es válido, por lo tanto podemos suponer que la izquierda es constante */
		Muestra<Funcion> f =  null;
		if (padre.tipo() == Gramatica.FUNCION) {
			Funcion.Trigonometrica trig = (Funcion.Trigonometrica) padre.get();
			Funcion.Constante val = (Funcion.Constante) izquierdo.get();
			f = new Muestra<Funcion>(new Funcion.Constante(trig.evalua(val.evalua())), Gramatica.NUMERO);
		/* ahora que es una funcion algebraica, el operador derecho no puede ser vacio */
		} else {
			Funcion.Constante x = (Funcion.Constante) izquierdo.get();
			Funcion.Constante y = (Funcion.Constante) derecho.get();
			Funcion.Algebraica fxy = (Funcion.Algebraica) padre.get();
			f = new Muestra<Funcion>(new Funcion.Constante(fxy.evalua(x.evalua(),y.evalua())), Gramatica.NUMERO);
		}

		return f;
	}

}