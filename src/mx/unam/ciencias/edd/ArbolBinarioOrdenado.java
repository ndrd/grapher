package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son
 * genéricos, pero acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos
 *       sus descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos
 *       sus descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios
     * ordenados. */
    private class Iterador<T> implements Iterator<T> {

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

    /**
     * Constructor sin parámetros. Sencillamente ejecuta el
     * constructor sin parámetros de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de un árbol
     * binario. El árbol binario ordenado tiene los mismos elementos
     * que el árbol recibido, pero ordenados.
     * @param arbol el árbol binario a partir del cuál creamos el
     *        árbol binario ordenado.
     */
    public ArbolBinarioOrdenado(ArbolBinario<T> arbol) {
        Vertice<T> raiz = arbol.vertice(arbol.raiz());
        agregaVertice(raiz);
    }

    private void agregaVertice(Vertice<T> v) {
        if (v == null)
            return;
        agregaVertice(v.izquierdo);
        agrega(v.elemento);
        agregaVertice(v.derecho);
        
    }


    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden
     * in-order.
     * @param elemento el elemento a agregar.
     * @return un iterador que apunta al vértice del nuevo elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        elementos++;
        if (raiz == null) {
            raiz = new Vertice<T>(elemento);
            return raiz;
        } else 
            return agrega(raiz, elemento);
    }

    private VerticeArbolBinario<T> agrega(Vertice<T> v, T elemento ) {
        if (v == null)
            return null;
        else {
            /* Es mayor,nos vamos por la derecha */
            if (v.elemento.compareTo(elemento) < 0) {
                if (!v.hayDerecho()) {
                    v.derecho = new Vertice<T> (elemento);
                    v.derecho.padre = v;
                    return v.derecho;
                } else 
                    return agrega(v.derecho, elemento); 
            /* Menor o igual se va por la izquierda */
            } else {
                if(!v.hayIzquierdo()) {
                    v.izquierdo = new Vertice<T>(elemento);
                    v.izquierdo.padre = v;
                    return v.izquierdo;
                } else 
                    return agrega(v.izquierdo, elemento);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no
     * hace nada; si está varias veces, elimina el primero que
     * encuentre (in-order). El árbol conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {

        Vertice<T> vertice = _busca(raiz, elemento);
        /* no se encontro*/
        if (vertice == null)
            return;
        else {
           /* el vertice es una hoja, se va sin problemas  */
            if (esHoja(vertice)) {
                if (vertice.padre == null)
                    raiz = null;
                else
                   if (vertice.padre.izquierdo == vertice)
                     vertice.padre.izquierdo = null;
                    else
                        vertice.padre.derecho = null;
            /* El vertice a eliminar solo tiene izquierdo */
            } else if (vertice.izquierdo != null && vertice.derecho == null) {
                if (vertice.padre != null) {
                    if (vertice.padre.izquierdo == vertice) {
                        vertice.padre.izquierdo = vertice.izquierdo;
                        vertice.izquierdo.padre = vertice.padre;
                    } else {
                        vertice.padre.derecho = vertice.izquierdo;
                        vertice.izquierdo.padre = vertice.padre;
                    }
                } else {
                    /* el padre es nullo, entonces es la raiz*/
                    raiz = vertice.izquierdo;
                    vertice.izquierdo.padre = null;
                }
                /* Solo tiene derecho */
            } else if (vertice.izquierdo == null && vertice.derecho != null) {
                if (vertice.padre != null) {
                     if (vertice.padre.izquierdo == vertice) {
                        vertice.padre.izquierdo = vertice.derecho;
                        vertice.derecho.padre = vertice.padre;
                    } else {
                        vertice.padre.derecho = vertice.derecho;
                        vertice.derecho.padre = vertice.padre;
                    }
                } else {
                    /* el padre es nullo, entonces es la raiz*/
                    raiz = vertice.derecho;
                    vertice.derecho.padre = null;
                }
                /* se reemplaza por el anterior */
            } else {
                Vertice<T> anterior = buscaVerticeAnterior(vertice);
                /* el reemplazo una hoja no se hace nada mas que eliminarla*/
                vertice.elemento = anterior.elemento;
                if (esHoja(anterior)) {
                    if (anterior.padre.izquierdo == anterior)
                        anterior.padre.izquierdo = null;
                    else
                        anterior.padre.derecho = null;
                /* el reemplazo tiene hijo izquierdo */
                } else {
                    if (anterior.padre.derecho == anterior) {
                        anterior.padre.derecho = anterior.izquierdo;
                        anterior.izquierdo.padre = anterior.padre;
                    }
                    else {
                        anterior.padre.izquierdo = anterior.izquierdo;
                        anterior.izquierdo.padre = anterior.padre;
                    }
                }
        }
        --elementos;

        }

    }

    private void d(Object o) {
        System.out.println(o);
    }

    protected boolean esHoja(Vertice<T> vertice) {
        return vertice != null && vertice.izquierdo == null && vertice.derecho == null;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo
     * encuentra, regresa un iterador que apunta a dicho elemento;
     * si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un iterador que apunta al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return _busca(raiz, elemento);
    }

    protected Vertice<T> _busca(Vertice<T> vertice, T elemento) {
        if (vertice == null)
            return null;
        else 
            if (vertice.elemento.compareTo(elemento) > 0) 
                return _busca(vertice.izquierdo, elemento);
            else if (vertice.elemento.compareTo(elemento) < 0)
                return _busca(vertice.derecho, elemento);
            else 
                return vertice;
    }

    /**
     * Regresa el vertice anterior (en in-order) al vertice que recibe.
     * @param vertice el vertice del que queremos encontrar el anterior.
     * @return el vertice anterior (en in-order) al vertice que recibe.
     */
    protected Vertice<T> buscaVerticeAnterior(Vertice<T> vertice) {
        return anterior(vertice.izquierdo);
    }

    private Vertice<T> anterior(Vertice<T> vertice) {
        /* no tiene antecesor */
        if(vertice == null)
            return null;
        /* ya no tine mayores */
        if (vertice.derecho == null)
            return vertice;
        else
            return anterior(vertice.derecho);
    }

    private Vertice<T> maximo(Vertice<T> vertice) {
        /* La hoja mas lejana del vertice*/
        if (vertice.derecho == null && vertice.izquierdo == null)
            return vertice;
        else 
            if (vertice.derecho != null)
                return maximo(vertice.derecho);
            else
                return vertice;
        
    }

    private Vertice<T> minimo(Vertice<T> vertice) {
        if (vertice.derecho == null && vertice.izquierdo == null)
            return vertice;
        else
            if (vertice.izquierdo != null)
                return minimo(vertice.izquierdo);
            else
                return vertice;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera
     * en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(raiz);
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el
     * vértice no tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        Vertice<T> v = vertice(vertice);
        giraDerecha(v);

    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el
     * vértice no tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    protected void giraDerecha(Vertice<T> vertice) {
        if (vertice.izquierdo == null)
            return;
        Vertice<T> tmp = vertice.izquierdo;
        /* anclamos al padre el nuevo vértice */
        if (vertice.padre != null)
            if(vertice.padre.derecho == vertice)
                vertice.padre.derecho = tmp;
            else
                vertice.padre.izquierdo = tmp;
        else
            raiz = tmp;
        /* y súbimos... */
        tmp.padre = vertice.padre;
        vertice.izquierdo = tmp.derecho;
        if(vertice.izquierdo != null)
            vertice.izquierdo.padre = vertice;
        tmp.derecho = vertice;
        tmp.derecho.padre = tmp;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el
     * vértice no tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice<T> v = vertice(vertice);
        giraIzquierda(v);
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el
     * vértice no tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    protected void giraIzquierda(Vertice<T> vertice) {
        if (vertice.derecho == null)
            return;
        Vertice<T> tmp = vertice.derecho;
        tmp.padre = vertice.padre;
        /* Si tiene padre, anclamos el nuevo vértice a él */
        if (vertice.padre != null)
            if(vertice.padre.derecho == vertice)
                vertice.padre.derecho = tmp;
            else
                vertice.padre.izquierdo = tmp;
        else
            raiz = tmp;
        /* subimos al hijo izquierdo del vértice derecho*/       
        vertice.derecho = tmp.izquierdo;
        if (vertice.derecho != null)
            vertice.derecho.padre = vertice;
        tmp.izquierdo = vertice;
        vertice.padre = tmp;
    }

    protected Vertice<T> padre(Vertice<T> vertice) {
        return (vertice != null) ? vertice.padre : null;
    }
    /**
    * Devuelve el abuelo (padre del padre del vértice recibido)
    * @param vertice - el vértice del que queremos su abuelo
    * @return abuelo - el padre del padre del vértice recibido.
    */
    protected Vertice<T> abuelo(Vertice<T> vertice) {
        return (vertice != null && vertice.padre != null) ? vertice.padre.padre : null; 
    }

    /**
    * Devuelve el hermano del padre del vértice recibido
    * @param vertice - el vértice del queremos tener su tio
    * @return el hermano del padre (sea izquierdo o derecho)
    */
    protected Vertice<T> tio(Vertice<T> vertice) {
        Vertice<T> abuelo = abuelo(vertice);
        if (vertice == null || abuelo == null) 
            return null;
        else 
            if (vertice.padre == abuelo.derecho)
                return abuelo.izquierdo;
            else
                return abuelo.derecho;
    }

    protected Vertice<T> hermano(Vertice<T> vertice) {
        if (vertice == null || vertice.padre == null)
            return null;
        else 
            if (vertice == vertice.padre.derecho)
                return vertice.padre.izquierdo;
            else
                return vertice.padre.derecho;
    }

 


    protected void debug(Object o) {
        System.out.println(o);
    }

}
