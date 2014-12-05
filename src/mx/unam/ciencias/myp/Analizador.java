package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.Lista;
import java.util.StringTokenizer;
/**
* <p> Clase para analizar una cadena y asignarle una gramática </p>
* 
* <p> 	La clase proporciona un método estático que recibe una gramatica
* 		y una cadena, separa en objetos Muestra y devuelve una lista
* 		con los objetos muestra. 
* </p>
*/
public class Analizador<T> {
	
	/**
	* Analiza una cadena de caracteres, los separa uno a uno y les da un valor 
	* correspondiente de acuerdo a la gramatica que recibio, al final devuelve 
	* una lista con todas las muestras que separo.
	* @param cadena la cadena a analizar.
	* @param gramatica la gramatica que se va a usar para darle valores a las 
	*	     muestras generadas.
	* @return lista una lista con las muestras obtenidas del analísis.
	*/
	public static Lista<Muestra<String>> procesa(String cadena) throws MalFormedFunctionException {
		Lista<Muestra<String>> muestras = new Lista<Muestra<String>>();
		StringTokenizer tokens = new StringTokenizer(cadena, " ()",true);
		int pAbre = 0;
		int pCierra = 0;
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			/* Evitamos espacios en blanco */
			if (token.equals("("))
				pAbre++;
			if(token.equals(")"))
				pCierra++;

			char c = token.charAt(0);
			if (c != ' ') {
				Muestra<String> muestra = Analizador.muestrea(token);
				if (muestra != null) {
					muestras.agregaFinal(muestra);
				}
			}
		}
		if (pAbre == 0 )
			throw new MalFormedFunctionException("Usa parentesis");

		int i = 0;
		for(Muestra<String> m :muestras) {
			m.setPeso(i++);
		}

		return muestras;
	}

	/**
	* Genera una muestra a partir de un elemento <i>s</i> basado en una gramatica <i>g</i>
	* @param s un caracter para comparar contra las muestras
	*/
	private static Muestra<String> muestrea(String s) throws MalFormedFunctionException {
		Muestra<String> muestra = null;
		char c = s.charAt(0);
		/* simbolos terminales */
		if (s.length() == 1 && !Analizador.esNumero(s)) {
			switch (c) {
				case 'x':
					muestra = new Muestra<String>(s,Gramatica.VARIABLE);
				break;
				case '-':
					muestra = new Muestra<String>(s,Gramatica.OPERADOR,1);
				break;
				case '+':
					muestra = new Muestra<String>(s,Gramatica.OPERADOR,1);
				break;
				case '*':
					muestra = new Muestra<String>(s,Gramatica.OPERADOR,1);
				break;
				case '/':
					muestra = new Muestra<String>(s,Gramatica.OPERADOR,1);
				break;
				case '^':
					muestra = new Muestra<String>(s,Gramatica.OPERADOR,1);
				break;
				case '(':
					muestra = new Muestra<String>(s,Gramatica.P_I,2);
				break;
				case ')':
					muestra = new Muestra<String>(s,Gramatica.P_F,2);
				break;
				default:
					throw new MalFormedFunctionException("Expresion no reconocida");
								
			}
		/* funciones y numeros */
		} else if (esNumero(s)){
			muestra = new Muestra<String>(s,Gramatica.NUMERO);
		} else {
			s = s.toLowerCase();
			if (s.equals("sin") || s.equals("cos") || s.equals("tan") ||
				s.equals("csc") || s.equals("ctg") || s.equals("sec"))
				muestra = new Muestra<String>(s,Gramatica.FUNCION);
			else if (s.equals("-x"))
				muestra = new Muestra<String>(s,Gramatica.VARIABLE);
		}
		return muestra;
	}

	private static boolean esNumero(String c) {
		try {
			Double.parseDouble(c);
			return true;
		} catch(Exception e) {
			return false;
		}
	}


	/**
	* Convierte cualquier cadena valida a número
	* @param c la cadena a convertir
	* @return n el numero que equivale la cadena, infinito en caso
	* 			de no ser válida.
	*/
	public static double aNumero(String c) {
		double n = 0;
		try {
			n = Double.parseDouble(c);
		} catch(Exception e) {
			n = Double.NEGATIVE_INFINITY;
		}
		return n;
	}

}
