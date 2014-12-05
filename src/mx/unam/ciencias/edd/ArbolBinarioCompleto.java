package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import mx.unam.ciencias.edd.Cola;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal
 * forma que el árbol siempre es lo más cercano posible a estar
 * lleno.<p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios
     * completos. */
    private class Iterador<T> implements Iterator<T> {

        Cola<ArbolBinario<T>.Vertice<T>> cola = new Cola<ArbolBinario<T>.Vertice<T>>();

        /* Constructor que recibe la raíz del árbol. */
        public Iterador(ArbolBinario<T>.Vertice<T> raiz) {
            cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return cola.esVacia() != true;
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            if (hasNext()) {
                ArbolBinario<T>.Vertice<T> tmp = cola.saca();
                if (tmp.hayIzquierdo())
                    cola.mete(tmp.izquierdo);
                if(tmp.hayDerecho())
                    cola.mete(tmp.derecho);

                return tmp.get();
            } else
                return null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Sencillamente ejecuta el
     * constructor sin parámetros de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo
     * elemento se coloca a la derecha del último nivel, o a la
     * izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @return un iterador que apunta al vértice del árbol que
     *         contiene el elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        Cola<Vertice<T>> cola =  new Cola<Vertice<T>>();
        elementos++;
        if (raiz == null) {
            raiz = new Vertice<T> (elemento);
            return raiz;
        }
        else {
            cola.mete(raiz);
            return agrega(cola, elemento);
        }
    }

    /* Método privado que coloca el elemento en el primer espacio disponible */
    private VerticeArbolBinario<T> agrega(Cola<Vertice<T>> cola, T elemento) {
        /* Sacamos el vértice de la cola, al menos tiene la raiz (caso base) */
        ArbolBinario<T>.Vertice<T> vertice = new ArbolBinario<T>.Vertice<T>(elemento);
        if(!cola.esVacia())
            vertice = cola.saca();
        else
            return null;
        /* El vértice izquierdo esta vacio, entonces lo anclamos */
        if (vertice.izquierdo == null) {
            vertice.izquierdo = new Vertice<T> (elemento);
            vertice.izquierdo.padre =  vertice;
            return vertice.izquierdo;
        /* El vértice derecho esta libre, se ancla a este */
        } else if (vertice.derecho == null) {
            vertice.derecho = new Vertice<T> (elemento);
            vertice.derecho.padre = vertice;
            return vertice.derecho;
        /* no tuvo espacio disponible, entonces metemos esos vértices a la cola*/
        } else {
            cola.mete(vertice.izquierdo);
            cola.mete(vertice.derecho);
        /* llamada recursiva con la nueva cola */
            return agrega(cola, elemento);
        }
    }

    private void debug(Object o) {
        System.out.println(o);
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia
     * lugares con el último elemento del árbol al recorrerlo por
     * BFS, y entonces es eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(elementos == 0)
            return;

        /* buscamos el vertice que necesitamos eliminar */
        Vertice<T> vertice = busca(raiz, elemento);

        /* No existe el elemento que debemos eliminar */
        if(vertice == null)
            return;

         /* el elemento que queremos eliminar existe, procedemos a eliminar */
         else {
            Cola<Vertice<T>> cola = new Cola<>();
            cola.mete(raiz);
            Vertice<T> reemplazo = ultimo(cola);
            if(reemplazo == null)
                return;

            /* Se tratan los diversos casos que pueden ocurrir con la raíz */
            if (vertice == raiz) {
                if (elementos == 1)
                    raiz = null;
                else {
                    raiz.elemento = reemplazo.elemento;
                    if(reemplazo.padre.derecho == reemplazo)
                        reemplazo.padre.derecho = null;
                    else
                        reemplazo.padre.izquierdo = null;
                }
            /* es una hoja, nadie llorará su perdida */
            } else if (vertice.derecho == null && vertice.izquierdo == null) {
                /* Es la ultima hoja */
                if(reemplazo == vertice)
                    if (vertice.padre.izquierdo == vertice)
                        vertice.padre.izquierdo = null;
                    else
                        vertice.padre.derecho = null;
                else
                    vertice.elemento = reemplazo.elemento;
                    if(reemplazo.padre.izquierdo == reemplazo)
                        reemplazo.padre.izquierdo = null;
                    else
                        reemplazo.padre.derecho = null;
            /* es un vértice interno */
            } else {
                vertice.elemento = reemplazo.elemento;
                /* después de sustituir, lo eliminamos */
                if(reemplazo.padre.izquierdo == reemplazo)
                    reemplazo.padre.izquierdo = null;
                else
                    reemplazo.padre.derecho = null;
            }
            elementos--;
        }

    }

    private Vertice<T> ultimo(Cola<Vertice<T>> cola) {
        Vertice<T> vertice = null;
        if(!cola.esVacia())
            vertice = cola.saca();
        else
            return null;
        /* Es una hoja, y es la ultima que entro */
        if (vertice.derecho == null && 
            vertice.izquierdo == null && cola.esVacia())
            return vertice;
        else {
            if (vertice.izquierdo != null)
                cola.mete(vertice.izquierdo);
            if(vertice.derecho != null)
                cola.mete(vertice.derecho);
            return ultimo(cola);
        }

    }
      /**
     * Regresa un iterador para iterar el árbol. El árbol se itera
     * en orden BFS.
     * @return un iterador para iterar el árbol.    
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(raiz);
    }
        
}
