package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las
 * siguientes propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la
 *      raíz).
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguno de sus descendientes tiene
 *      el mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las
 * operaciones de inserción, eliminación y búsqueda pueden
 * realizarse en <i>O</i>(log <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método
     * {@link ArbolBinarioOrdenado#agrega}, y después balancea el
     * árbol recoloreando vértices y girando el árbol como sea
     * necesario.
     * @param elemento el elemento a agregar.
     * @return un vértice que contiene al nuevo elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        VerticeArbolBinario<T> tmp = super.agrega(elemento);
        Vertice<T> v = vertice(tmp);
        /* Al agregar cualquier vértice nuevo se colorea de rojo */
        v.color = Color.ROJO;
        balanceaAgregar(v);
        return v;
    }

    /**
    * Balancea el vertice que se agrego al arbol.
    */
    protected void balanceaAgregar(Vertice<T> v) {
        /* Es la raiz, pintamos de negro y salimos */
        if (v.padre == null) {
            v.color = Color.NEGRO;
            return;
        /* el padre es negro, el hijo rojo, entonces esta balanceado */
        } else if (esNegro(v.padre)) {
            return;
        /* en este caso el padre, es rojo, y tiene abuelo, pues el padre no
        puede ser la raiz, ademas de que tiene tio, puede ser negro (null) */
        } else if (!esNegro(tio(v))) {
            v.padre.color = Color.NEGRO;
            tio(v).color = Color.NEGRO;
            abuelo(v).color = Color.ROJO;
        /* se puede violar alguna condicion al cambiar el color del abuelo 
         hacemos el balanceo recursivo para llegar a la raíz */
            balanceaAgregar(abuelo(v));
        /* En este caso, el padre es rojo, el tio es negro */
        } else {
            /* vemos si estan cruzados, los enderezamos */
            if (!esHijoIzquierdo(v) && esHijoIzquierdo(v.padre)) {
                /* Los enderezamos y actualizamos la referencia de v */
                giraIzquierda(v.padre);
                v = v.izquierdo;
            } else if (esHijoIzquierdo(v) && !esHijoIzquierdo(v.padre)) {
                giraDerecha(v.padre);
                v = v.derecho;
            }
            /* Ahora que esta "derechos" seguimos balanceando */
            v.padre.color = Color.NEGRO;
            abuelo(v).color = Color.ROJO;
            /* Por último, rotamos en direcccion contraria */
            if (esHijoIzquierdo(v) && esHijoIzquierdo(v.padre))
                giraDerecha(abuelo(v));
            else if (!esHijoIzquierdo(v) && !esHijoIzquierdo(v.padre))
                giraIzquierda(abuelo(v));
        }

    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que
     * contiene el elemento, y recolorea y gira el árbol como sea
     * necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        if (elemento == null)
            return;
        /* primero buscamos el nodo que queremos eliminar */
        VerticeArbolBinario<T> v = busca(elemento); 
        if (v == null)
            return;
        else
            elimina(vertice(v));
    }

    private void elimina(Vertice<T> v) {
        /* seguro eliminamos algo */
        --elementos;
        /* Sabemos que existe, entonces buscamos el anterior */
        Vertice<T> a = buscaVerticeAnterior(v);
        Vertice<T> h = new Vertice<T>(null);
        h.color = Color.NEGRO;
        /* Si tiene anterior, intercambiamos los elementos */
        if (a != null) {
            T e = v.elemento;
            v.elemento = a.elemento;
            a.elemento = e;
            v = a;
        }
        /* vemos que sucede con los hijos (tiene a lo más uno)*/
        if (esHoja(v)) {
            /* no tiene hijos, agregamos una fantasma (negro) */
            v.izquierdo = h;
            h.padre = v;
            
        } else {
            /* si tiene hijo, entonces se lo asignamos */
            if (v.izquierdo != null)
                h = v.izquierdo;
            else
                h = v.derecho;
        }
        /* h no nullo, anclaje seguro */
        h.padre = v.padre;  
        
        /* Caso 1 , v, no tiene padre, h es el nuevo hijo*/
        if (h.padre == null)
            raiz = h;
        else
            /* subimos a h*/
            if (esHijoIzquierdo(v))
               v.padre.izquierdo = h;
            else
                v.padre.derecho = h;
        /* Si h es rojo, lo pintamos de negro */
        if (!esNegro(h)) {
            h.color = Color.NEGRO;
            return;
        }
        /* h era negro, rebalanceamos y eliminamos fantasmas */
        if (esNegro(v) && esNegro(h) ) {
            rebalanceaElimina(h);
            eliminaVerticeFantasma(h);
            return;
        }
        eliminaVerticeFantasma(h);
        
    }

    /**
    * Rebalancea el metodo elimina 
    */
    private void rebalanceaElimina(Vertice<T> v) {
        /* caso 1: padre nulo (es la raíz), lo pintamos de negro */
        if(v.padre == null) {
            v.color = Color.NEGRO;
            raiz = v;
            return;
        }
        Vertice<T> h = hermano(v);
        /* caso 2: el hermano de v es rojo */
        if (!esNegro(h)) {
            /* recoloreamos al padre y al hermano */
            v.padre.color = Color.ROJO;
            h.color = Color.NEGRO;
            /* giramos al padre en direccion de v */
            if (esHijoIzquierdo(v)) 
                giraIzquierda(v.padre);
            
            else 
                giraDerecha(v.padre);
        }
        /* El padre, hermano y sobrinos negros */
        h = hermano(v);

        if (esNegro(v.padre) && esNegro(h) &&
            esNegro(h.izquierdo) && esNegro(h.derecho)) {
            /* pintamos al hermano de rojo y rebalanceamos con el padre */
            h.color = Color.ROJO;
            rebalanceaElimina(v.padre);
            return;
        /* El padre es rojo, hermano y sobrinos negros */
        }
        if (!esNegro(v.padre) && esNegro(h) &&
             esNegro(h.izquierdo) && esNegro(h.derecho)) {
            /* Coloreamos al padre de negro, al hermano de rojo */
            v.padre.color = Color.NEGRO;
            h.color = Color.ROJO;
            return;
            /* hermano negro,sobrinos bicolores*/
        } 
        /* Caso */
        if (esNegro(h)) {
            if (esHijoIzquierdo(v)) {
                if (esNegro(h.derecho) && !esNegro(h.izquierdo)) {
                    /* Coloreamos al hermano de rojo */
                    h.color = Color.ROJO;
                    h.izquierdo.color = Color.NEGRO;
                    giraDerecha(h);
                }
            } else {
                if (!esHijoIzquierdo(v) && esNegro(h.izquierdo) && !esNegro(h.derecho)) {
                    h.color = Color.ROJO;
                    h.derecho.color = Color.NEGRO;
                    giraIzquierda(h);
                }
            }
        }
        h = hermano(v);
        /* Los sbrinos son del mismo color*/
        h.color = v.padre.color;
        /* el padre de v se pinta de negro */
        v.padre.color = Color.NEGRO;
        /* giramos al padre en direccion de v */
        if (esHijoIzquierdo(v)) {
            h.derecho.color =  Color.NEGRO;
            giraIzquierda(v.padre);
        }
        else {
            h.izquierdo.color =  Color.NEGRO;
            giraDerecha(v.padre);
        }
    }

    private void eliminaVerticeFantasma(Vertice<T> v) {
        if (v != null && v.elemento == null) {
            if(v.padre != null)
                if (esHijoIzquierdo(v)) 
                    v.padre.izquierdo = null;
                else
                    v.padre.derecho = null;
            else
                raiz = null;
            
            v = null;
        }
    }


    private boolean esHijoIzquierdo(Vertice<T> vertice) {
        return (vertice != null && vertice.padre != null && 
                vertice.padre.izquierdo == vertice);
    }

    private boolean esNegro(Vertice<T> vertice) {
        return (vertice == null || vertice.color == Color.NEGRO);
    }

    
}
