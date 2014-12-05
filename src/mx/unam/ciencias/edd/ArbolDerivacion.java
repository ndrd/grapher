package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import mx.unam.ciencias.myp.Muestra;
import mx.unam.ciencias.myp.Funcion;

/**
 * Arbol de derivacion que sirve para interpretar gramaticas, permite 
 * agregar elementos a un nodo en perticular 
 */

public class ArbolDerivacion<T> extends ArbolBinario<T> {
     /* Clase privada para iteradores de árboles binarios
     * ordenados. */
    public class Iterador<T> implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice<T>> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador(ArbolBinario<T>.Vertice<T> vertice) {
            pila = new Pila<ArbolBinario<T>.Vertice<T>>();
            /* desde el princpicio cargamos la rama izquierda completa de la raiz */
            cargaIzquierdo(vertice);
        }

        private void cargaIzquierdo(ArbolBinario<T>.Vertice<T> vertice) {
            while (vertice != null) {
                pila.mete(vertice);
                vertice = vertice.izquierdo;
            }
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return pila.esVacia() != true;
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
            if (hasNext()) {
                ArbolBinario<T>.Vertice<T> v = pila.saca();
                cargaIzquierdo(v.derecho);
                return v.elemento;
            }
            else
                return null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

	public ArbolDerivacion() {
		super();
	}

            @Override public VerticeArbolBinario<T> agrega(T elemento) {
                return null;
            }

         /**
         * Agrega un nuevo elemento al árbol. El árbol conserva su orden
         * in-order.
         * @param elemento el elemento a agregar.
         */
        public T agrega(T elemento, T padre) {
                ArbolBinario<T>.Vertice<T> nuevo = new ArbolBinario<T>.Vertice<T>(elemento);
               if (raiz == null) {
                        raiz = nuevo;
                } else {
                        ArbolBinario<T>.Vertice<T> vertice = vertice(busca(padre));
                        if (vertice == null)
                            throw new NoSuchElementException();
                        if (vertice.izquierdo == null)
                            vertice.izquierdo = nuevo;
                        else
                            vertice.derecho = nuevo; 
                        nuevo.padre = vertice; 
                        nuevo.izquierdo =  nuevo.derecho = null; 
                        
               }
               elementos++;
               return elemento;

        }

            /**
            * Devuelve el padre de un elemento dentro del arbol
            * @param  elemento, el elemento a buscar
            * @return padre, el elemento que contiene el padre del elemento
            * @throws NoSuchElementException en caso de no encontrar al padre o de que
            *          no existe el elemento dentroo del arbol
            */ 
            public T getPadre(T elemento) {
            ArbolBinario<T>.Vertice<T> vertice = vertice(busca(elemento));
            if(vertice != null && vertice.hayPadre())
                return vertice.padre.elemento;
            throw new NoSuchElementException();

        }

        /**
        * elimina los elementos menos la raíz 
        */
         @Override public void elimina(T elemento) {

         }

         /**
         * Regresa un iterador para iterar el árbol. El árbol se itera
         * en orden BFS.
         * @return un iterador para iterar el árbol.    
         */
        @Override public Iterator<T> iterator() {
            return new Iterador<T>(raiz);
        }

        /**
        * genera un elemento a partir de la accion de {@link AccionColapsaHojas}
        * y lo devuelve.
        * @param accion como operaremos sobre el vertice
        */
        public T colapsaHojas(AccionColapsaHojas<T> accion) {
            /* recorremos el arbol para poder tener una pila con un recorrido en preorder */
            Cola<T> cola = new Cola<>();
            Iterador<T> it = new Iterador<T>(raiz);
            while(it.hasNext()) {
                cola.mete(it.next());
            }
           return accion.actua(cola);
        }

        private boolean esHoja(Vertice<T> v) {
            return (v.izquierdo == null && v.derecho == null);
        }
}