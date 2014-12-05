package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolDerivacion;
import mx.unam.ciencias.edd.Pila;
import java.util.NoSuchElementException;

/** 
* <p>Clase para generar un arbol de derivacion recibiendo
* una lista de {@link Muestra} que después
* será interpreado para generar el resultado de la 
* expresion analizada.</p>
* Si la expresión no esta bien formada, lanza una excepción
*/

public class Compilador {

	/**
	* Funcion que transforma los tokens en funciones de acuerdo a su 
	* valor, y posición.
	* @param lista una lista de {@link Muestra} basadas en una grámatica.
	* @return arbol un arbol de derivación listo para ser interpretado.
	* @throws MalFormedFunctionException en caso de estar mal formada la funcion.
	*/
	public static ArbolDerivacion<Muestra<Funcion>> compila(Lista<Muestra<String>> lista)
			throws MalFormedFunctionException {
		ArbolDerivacion<Muestra<Funcion>> arbol = crearArbol(lista);
		return arbol;
	}

	/**
	* Funcion que transforma los tokens en funciones de acuerdo a su 
	* valor, y posición.
	* @param <i>lista</i> una lista de {@link Muestra} basadas en una grámatica.
	* @return <i>arbol</i> un arbol de derivación listo para ser interpretado.
	* @throws MalFormedFunctionException en caso de estar mal formada la funcion.
	*/
	private static Muestra<Funcion> creaFuncion(Muestra<String> m) throws MalFormedFunctionException {
		Muestra<Funcion> mf = null;
		Funcion f;
		int peso = 0;
		switch(m.tipo()) {
			/* Aumentamos el contador de parentesis abiertos */
			case P_I:
				mf = new Muestra<Funcion>(new Funcion.P_I(), Gramatica.P_I);
			break;
			/* Contador de parentesís cerrados */
			case P_F:
				mf = new Muestra<Funcion>(new Funcion.P_F(), Gramatica.P_F);

			break;
			/* Se crea una funcion constante */
			case NUMERO:
				double n = Analizador.aNumero(m.get());
				mf = new Muestra<Funcion>(new Funcion.Constante(n), Gramatica.NUMERO);
			break;
			/* la funcion debe tomar  a sus hijos como argumentos, se deja al interprete */
			case OPERADOR:
				 mf = new Muestra<Funcion>(new Funcion.Algebraica(m.get().charAt(0)), Gramatica.OPERADOR);
			break;
			/* Mapea las funciones para solo obtener su valor */
			case FUNCION:
				int i = 0;
				String s = m.get();
				if(s.equals("sin"))
					i = 1;
				if(s.equals("cos"))
					i = 2;
				if(s.equals("tan"))
					i = 3;
				if(s.equals("sec"))
					i = 4;
				if(s.equals("csc"))
					i = 5;
				if(s.equals("ctg"))
					i = 6;
				mf = new Muestra<Funcion>(new Funcion.Trigonometrica(i),Gramatica.FUNCION);
			break;
			/* cosas raras que hace uno */
			case VARIABLE:
				double signo = Double.POSITIVE_INFINITY;
				if(m.get().equals("-x"))
					signo = Double.MIN_VALUE;
				mf = new Muestra<Funcion>(new Funcion.Constante(signo), Gramatica.VARIABLE);
			break;
			/* Si no era valido en la gramatica, aqui se va a quejar */
			case DESCONOCIDO:
				throw new MalFormedFunctionException("Verifica tu sintaxis, no generaste una expresion valida");

		}
		/* Una sola variable para todos */
		if (mf.tipo() == Gramatica.VARIABLE) 
			mf.setPeso(-100);
		else
			mf.setPeso(m.peso());
		return mf;
	}

	/**
	* Crea un arbol de derivacion en base a una lista de muestras
	* @throws NoSuchElementException si la lista es vacía.
	*/
	private static ArbolDerivacion<Muestra<Funcion>> crearArbol(Lista<Muestra<String>> lista)
			throws MalFormedFunctionException {
		if(lista.getLongitud() == 0)
			return null;

		ArbolDerivacion<Muestra<Funcion>> arbol = new ArbolDerivacion<Muestra<Funcion>>();
		/* Se transforma la lista de cadenas en lista de funciones para evaluarlas posteriormente */
		Lista<Muestra<Funcion>> lf = new Lista<>();
		for(Muestra<String> m : lista) {
			lf.agregaFinal(creaFuncion(m));
		}
		/* validamos la consistencia de la gramatica y construimos el arbol */
		if(esValida(lf)) {
			Muestra<Funcion> padre = lf.eliminaPrimero();
			crearArbol(lf, padre, arbol);
		} else {
			throw new MalFormedFunctionException("No es valida tu expresion!");
		}
		return arbol;	
	} 


	private static void crearArbol(Lista<Muestra<Funcion>> lista, 
							Muestra<Funcion> padre,
							ArbolDerivacion<Muestra<Funcion>> arbol) {
		if(lista.getLongitud() == 0)
			return;
		Muestra<Funcion> hijo = lista.eliminaPrimero();
		/* en este caso */
		if( padre.tipo() == Gramatica.P_I && hijo.tipo().esOperable()) {
			arbol.agrega(hijo,padre);
			padre = hijo;
		} else if (padre.tipo().esOperable()) {
			/* no se necesita actualizar la referencia al padre */
			if (hijo.tipo().esSimboloTerminal()) {
				arbol.agrega(hijo, padre);
			/* el hijo pude tener mas hijos */
			} else if(hijo.tipo().esOperable()) {
				arbol.agrega(hijo, padre);
				padre = hijo;
			/* se debe actualizar el padre */
			} else if (hijo.tipo() == Gramatica.P_F) {
				if (!arbol.raiz().get().equals(padre))
					padre = arbol.getPadre(padre);
			/* es un parentesis inicial, se consime despues el caso */
			} 	
		}
		crearArbol(lista, padre, arbol);
			
		
	}

	/**
	* Verifica la composicion de la lista y determina si tiene sentido dentro de la
	* gramatica establecida.
	* Se hace mediante la verificacion de una automata de pila aplicado a una gramatica 
	* independiente de contexto
	* https://www.youtube.com/watch?v=Q_b4c5kmV1Q
	*/
	private static boolean esValida(Lista<Muestra<Funcion>> lista) 
						throws MalFormedFunctionException {
		if (lista.getLongitud() == 0)
			throw new MalFormedFunctionException("expresion vacía");
		Lista<Muestra<Funcion>> copia = lista.copia();
		Pila<Gramatica> pila = new Pila<>();
		pila.mete(Gramatica.EXPRESION);
		int i = 0;
		try {
			for(Muestra<Funcion> m : copia) {
				++i;
				Gramatica g = pila.saca();
				if(g == Gramatica.EXPRESION && !m.tipo().esExpresion())
					throw new MalFormedFunctionException("Expresión invalida");
				llenaPila(pila,m.tipo());
			}
		/* se termino la pila antes ser valida */
		} catch (NoSuchElementException nsee) {
			throw new MalFormedFunctionException("Expresión invalida");
		}
		/* la pila esta vacia */
		if(!pila.esVacia()) {
			throw new MalFormedFunctionException("Expresión invalida");
		}
		return true;
	}

	private static void llenaPila(Pila<Gramatica> p, Gramatica g) throws MalFormedFunctionException {
		switch(g) {
			case P_I:
				p.mete(Gramatica.P_F);
				p.mete(Gramatica.EXPRESION);
			break;
			case OPERADOR:
				p.mete(Gramatica.EXPRESION);
				p.mete(Gramatica.EXPRESION);
			break;
			case FUNCION:
				p.mete(Gramatica.EXPRESION);
			break;
		}
	}	
}

