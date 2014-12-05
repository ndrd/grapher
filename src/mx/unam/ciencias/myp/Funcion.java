package mx.unam.ciencias.myp;

public class Funcion{

	private static double x;
	private static int tipo;

	/* Almacen de clases constantes*/
	protected static class Constante extends Funcion{
		private double x;
		Constante(double x) {
			this.x = x;
			super.x = x;
			super.tipo = 0;
		}

		public double evalua() {
			return this.x;
		}

		@Override public String toString() {
			return "" + this.x;
		}
		@Override public boolean equals(Object o) {
			if(o == null) 
				return false;
			 @SuppressWarnings("unchecked")
			 Funcion.Constante m = getClass().cast(o);
			 if (m.x == this.x)
			 	return true;
			 return false;
        	
		}
	}

	protected static class X extends Funcion {
		X() {
			super.x = Double.NEGATIVE_INFINITY;
			super.tipo = 2;

		} 

		@Override public String toString() {
			return "x";
		}
	}

	protected static class P_I extends Funcion {
		P_I() {
			super.x = Double.NEGATIVE_INFINITY;
			super.tipo = 3;

		} 

		@Override public String toString() {
			return "(";
		}
	}

	protected static class P_F extends Funcion {
		P_F() {
			super.tipo = 4;
			super.x = Double.NEGATIVE_INFINITY;
		} 

		@Override public String toString() {
			return ")";
		}
	}

	/** Subclase de Funciones para construir csas*/
	protected static class Trigonometrica extends Funcion {
		private int seleccion;

		Trigonometrica( int seleccion) {
			super.tipo = 10 + seleccion;
			this.seleccion = seleccion;
		}

		public double evalua(double x) {
			double r = 0;
			switch (seleccion) {
				/* Seno */
				case 1:
					r = Math.sin(x);
				break;
				/* Coseno */
				case 2:
					r = Math.cos(x);
				break;
				/* Tangente */
				case 3:
					r = Math.tan(x);
				break;
				/* Secante */
				case 4:
					if(Math.cos(x) != 0)
						r = 1 / Math.cos(x);
					else 
						r = Double.POSITIVE_INFINITY;
				break;
				/* Cosecante */
				case 5:
					if(Math.sin(x) != 0)
						r = 1 / Math.sin(x);
					else 
						r = Double.POSITIVE_INFINITY;
				break;
				/* Cotangente */
				case 6:
					if(Math.tan(x) != 0)
						r = 1 / Math.tan(x);
					else 
						r = Double.POSITIVE_INFINITY;
				break;
			}
			return r;
		}

		@Override public String toString() {
			String s[]  = {"","sen","cos","tan","sec","csc","ctg"};
			return s[seleccion];
		}
	}


	protected static class Algebraica extends Funcion{

		private double x;
		private char seleccion;

		Algebraica(char seleccion) {
			this.seleccion = seleccion;
			super.tipo = (int) seleccion;

		}

		public double evalua(double x, double y) {
			switch (seleccion) {
				case '*':
					return  x * y;
				
				case '-':
					return  x - y;
				
				case '+':
					return 	x + y;
				
				case '/':
					if(y != 0) 
						return x/y;
					else
						return Double.POSITIVE_INFINITY;
				
				case '^':
					return Math.pow(x,y);
			}
			d("No existe la funcion, te devuelvo 0");
			return 0;
		}

		@Override public String toString() {
			return "" + seleccion;
		}
	}



	public void d(Object o) {
		System.out.println(o);
	}


}