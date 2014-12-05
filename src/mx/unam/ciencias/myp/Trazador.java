package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolDerivacion;
import java.awt.Polygon;
import java.awt.Color;


/**
* Clase que traza una grafica dentro de las coordenadas especificadas
* Se usan listas de puntos para poder tener control de donde se va a dibujar
* las funciones, funciona tanto como para generar el svg como para la interfaz
* grafica
*/
public class Trazador {

	private String graficasSVG;
	private Lista<Lista<Interprete.Punto>> graficas;
	private Lista<Polygon> listaPoligonos;
	private int ancho;
	private int alto;
	private double x0;
	private double x1;
	private double y0;
	private double y1;

	/** Constructor que recibe las dimensiones del trazador */
	public Trazador(int ancho, int alto) {
		this.graficas = new  Lista<Lista<Interprete.Punto>>();
		this.listaPoligonos = new Lista<Polygon>();
		this.ancho = ancho;
		this.alto = alto;
		this.graficasSVG = getPlano();

	}

	/** Constructor que recibe las dimensiones del trazador */
	public Trazador(int ancho, int alto, double x0, double x1, double y0, double y1) {
		this.graficasSVG = String.format("<svg width='%d' height='%d'>",ancho, alto);
		this.ancho = ancho;
		this.alto = alto;
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;

	}

	/**
	* el ploter toma una funcion y la compila
	*/
	public void agregaFuncion(String funcion) throws  MalFormedFunctionException{
		Lista<Muestra<String>> muestras = Analizador.procesa(funcion);
		ArbolDerivacion<Muestra<Funcion>> arbol = Compilador.compila(muestras);
	        	Interprete interprete = new Interprete(arbol);
	        	Lista<Interprete.Punto> puntos = interprete.evalua(x0, x1);
	       	listaPoligonos.agregaFinal(crearPoligono(puntos));
	        	graficas.agregaFinal(puntos);
	        	graficasSVG += bloqueSVG(puntos);
           	}

	/* genera colores aleatorios para que se vean lindas las graficas */
	private String randomColor() {
		String [] colors = {
			"#00FF00", "#00F8D8","#81F3FD",	
			"#007AFF","#0F1300","#4CD964",
			"#FFCD02", "#DBDDDE"
		};
		return colors[graficas.getLongitud()%colors.length];
	}

	/**
	* Reconsidera los argumentos  del ancho alto e intervalos, asi genera una grafica 
	* de nueva dimension
	*/
	protected void setMedidasYrango(int ancho, int alto, double x0, double x1, double y0, double y1) {
		if(this.ancho != ancho || this.alto != alto || this.x0 != x0 || this.x1 != x1 || this.y0 != y0 || this.y1 != y1) {
			/* redimensionar */
			this.ancho = ancho;
			this.alto = alto;
			this.x0 = x0;
			this.x1 = x1;
			this.y0 = y0;
			this.y1 = y1;
			graficasSVG = getPlano();
			for(Lista<Interprete.Punto> puntos  : graficas) {
				listaPoligonos.agregaFinal(crearPoligono(puntos));
	        			graficasSVG += bloqueSVG(puntos);
			}
		}
	}

	private String getPlano() {
		String plano =  String.format("<svg width='%d' height='%d'>",ancho, alto);
		return plano;
	}

	private Polygon crearPoligono(Lista<Interprete.Punto> puntos) {
		double deltaX = ancho / (x1-x0);
		double deltaY = alto / (y1-y0);
		double m1  =  ancho / 2;
		double m2 = ancho / 2;
		Polygon g = new Polygon();
		for(Interprete.Punto p: puntos) {
			int  x = (int)((ancho/2) + (p.getX() * deltaX));
			int y  = (int)((alto/2) +  (-1 * p.getY() * deltaY ));
			 g.addPoint(x,y);
		}
		return g;
	}

	/**
	* devuelve una lista de {@link Polygon} para poder generar una grafica en la interafaz grafica
	* @return listaPiligonos la lista de poligonos (grafica) que se genera
	*/
	public Lista<Polygon> listaPoligonos () {
		return listaPoligonos;
	}

           	/**
	* Método que genera una grafica en svg de las listas de puntos 
	* tomando las dimensiones correspondientes
	* @param puntos la lista de puntos que se quiere hacer
	* @return string una cadena con la representacion de la grafica dentro
	*         de los limites
	*/
	private String bloqueSVG(Lista<Interprete.Punto> puntos ) {
		String color = randomColor(puntos.getLongitud());
		double deltaX = ancho / (x1-x0);
		double deltaY = alto / (y1-y0);
		double nuevoX = x0 + ((x1 - x0) / 2);
		double nuevoY = y0 +  ((y1 - y0 ) / 2);

		double x = 0,y = 0;

		String svg = "<g>";
		svg += "<polyline fill='none' stroke='" + color + "' stroke-width='3'  points='";
		for(Interprete.Punto p: puntos) {
			x = p.getX();
			y = p.getY();
			if (x  < nuevoX) {
				x = Math.abs(x) - Math.abs(nuevoX);
			} else {
				x = Math.abs(nuevoX) - Math.abs(x);
			}
			if ( nuevoX < 0)
				x = -x;
			x = (ancho/2) + (deltaX * x);
			y =  (alto/2) + ( deltaY * y);
			svg += "" + x+ "," + y + " ";
		}
		return svg + "'/></g>";
	}

	/**
	* Método que limpia las listas de graficas tanto del SVG como de los poligonos
	*/
	public void limpiar() {
		graficas.limpia();
		listaPoligonos.limpia();
		graficasSVG = String.format("<svg width='%d' height='%d'>",ancho, alto);
	}

	/**
	* Devuelve una cadena que representa las graficas dentro del formato SVG
	* @return grafica una cadena con la representacion de las funciones en una cadena
		mediante el formato SVG
	*/
	public String getSVG() {
		return graficasSVG + "</svg>";
	}

	 private String randomColor(int i) {
	            String [] colors = {"#007AFF","#FF1300","#4CD964",
	                                "#FF2D55", "#81F3FD",
	                                "#FFCD02", };
	            return colors[i%colors.length];
	        }


 
}


