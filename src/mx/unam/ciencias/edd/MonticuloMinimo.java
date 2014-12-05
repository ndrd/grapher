package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear
 * un montículo mínimo con <em>n</em> elementos en tiempo
 * <em>O</em>(<em>n</em>), y podemos agregar y actualizar elementos
 * en tiempo <em>O</em>(log <em>n</em>). Eliminar el elemento mínimo
 * también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Iterable<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador<T extends ComparableIndexable<T>> implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;
        private MonticuloMinimo<T> monticulo;

        /* Construye un nuevo iterador, auxiliándose del montículo
         * mínimo. */
        public Iterador(MonticuloMinimo<T> monticulo) {
        this.indice = 0;
        this.monticulo = monticulo;
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return indice < monticulo.siguiente;
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            if(hasNext())
	      return monticulo.arbol[indice++];
	    return null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así
       por cómo Java implementa sus genéricos; de otra forma
       obtenemos advertencias del compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Lista)}, pero se ofrece este constructor por
     * completez.
     */
    public MonticuloMinimo() {
       this.arbol = creaArregloGenerico(1);
       siguiente = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una lista. Es
     * más barato construir un montículo con todos sus elementos de
     * antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     */
    public MonticuloMinimo(Lista<T> lista) {
        arbol = creaArregloGenerico(lista.getLongitud());
        siguiente = 0;  
        /* rellenamos el arbol s*/
        for (T t : lista) {
           t.setIndice(siguiente);
           arbol[siguiente++] = t;
        }
        /* Comenzamos a reordenar desde la mitad del arbol */
        ordena(siguiente / 2);
    }


    private void d(Object o) {
        System.out.println(o);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    public void agrega(T elemento) {
      if (siguiente >= arbol.length ) 
            arbol  = crecerArreglo();
        arbol[siguiente] = elemento;
        elemento.setIndice(siguiente++);
        reordena(elemento);
    }

    private T[] crecerArreglo() {
        T[] tmp = creaArregloGenerico(arbol.length * 2);
        for (int i = 0; i < arbol.length; i++)
            tmp[i] = arbol[i];
        return tmp;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        if(siguiente == 0)
            throw new IllegalStateException();
        T min = arbol[0];
        intercambia(0, --siguiente);
        ordena(0);
        return min;
        
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacio() {
      return siguiente == 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
   public void reordena(T elemento) {
        ordena(elemento.getIndice());
    }

   private void ordena(int i) {
        int minimo = minimo(i,2*i+1,2*i+2);
        if (minimo == i) {
            if (i-1 >= 0)
                ordena(i-1);
            return;
        } else {
            intercambia(i, minimo);
            ordena(minimo);
        }

   }

    /* retorna el elmento minimo del padre y sus hijos */
    private int minimo(int padre, int izq, int der) {
        if (izq < siguiente && der < siguiente) 
            if (arbol[padre].compareTo(arbol[izq]) <= 0 && arbol[padre].compareTo(arbol[der]) <= 0)
                return padre;
            else if (arbol[izq].compareTo(arbol[padre]) <= 0 && arbol[izq].compareTo(arbol[der]) <= 0)
                return izq;
            else
                return der;
        else
            if (izq  < siguiente)
                if (arbol[padre].compareTo(arbol[izq]) <= 0)
                    return padre;
                 else
                    return izq;
            else
                return padre;
    }


    private void intercambia(int a, int b){
        T temp = arbol[a];
        temp.setIndice(b);
        arbol[a] = arbol[b];
        arbol[a].setIndice(a);
        arbol[b]= temp;
    }
    

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    public int getElementos() {
      return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o
     *         mayor o igual que el número de elementos.
     */
    public T get(int i) {
      if (i < 0 || i > siguiente -1)
        throw new NoSuchElementException();
      return arbol[i]; 
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El
     * montículo se itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }
}
