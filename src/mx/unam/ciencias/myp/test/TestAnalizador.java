package mx.unam.ciencias.myp.test;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.myp.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

/**
* Clase para las pruebas unitarias de la clase {@link Analizador}
*/
public class TestAnalizador {

	private Random semilla;
	private Lista<Muestra<String>> lista;
	private int n;
	private int lim;

	public TestAnalizador() {
		lista = new Lista<Muestra<String>>();
		semilla = new Random();
		n = semilla.nextInt(100);
		lim  = 500;
	}

	/* genera una cadena de n tokens, se inyectan espacios en blanco */
	private String generaCadena(int n) {
		String s = "";
		String fuente = "(/   -* + 1 3 2  4 6   5 7 8 9 3               )";
		while(n > 0 && lim > 0 ) {
			char tmp = fuente.charAt(semilla.nextInt(fuente.length()));
			/* Si debe ser marcado como token, lo descontamos */
			if(tmp != ' ')
				--n;
			lim--;
			s += tmp;
		}
		if(n > 0) 
			for(int k = 0; k < n; k++)
				s+= "1";
		return s;
	}

	@Test public void testNumeroTokens(){
		String cadena = this.generaCadena(n);
		 Assert.assertTrue(lista.getLongitud() == n);
	}
}