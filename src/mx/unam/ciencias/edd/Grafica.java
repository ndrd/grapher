package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y
 * aristas, tales que las aristas son un subconjunto del producto
 * cruz de los vértices.
 */
public class Grafica<T> implements Iterable<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador<T> implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice<T>> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de
         * vértices. */
        public Iterador(Grafica<T> grafica) {
            iterador = grafica.vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            return iterador.next().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Aristas para gráficas; para poder guardar el peso de las
     * aristas. */
    private class Arista<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice<T> vecino;
        /* El peso de arista conectando al vértice con el vecino. */
        public double peso;

        public Arista(Grafica<T>.Vertice<T> vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }
    }

    /* Vertices para gráficas; implementan la interfaz
     * ComparableIndexable y VerticeGrafica */
    private class Vertice<T> implements ComparableIndexable<Vertice<T>>,
        VerticeGrafica<T> {

        /* Iterador para las vecinos del vértice. */
        private class IteradorVecinos implements Iterator<VerticeGrafica<T>> {

            /* Iterador auxiliar. */
            private Iterator<Grafica<T>.Arista<T>> iterador;
            
            /* Construye un nuevo iterador, auxiliándose de la lista
             * de vecinos. */
            public IteradorVecinos(Iterator<Grafica<T>.Arista<T>> iterador) {
                this.iterador = iterador;
            }

            /* Nos dice si hay un siguiente vecino. */
            public boolean hasNext() {
                return iterador.hasNext();
            }

            /* Regresa el siguiente vecino. La audición es
             * inevitable. */
            public VerticeGrafica<T> next() {
                Grafica<T>.Arista<T> arista = iterador.next();
                return (VerticeGrafica<T>)arista.vecino;
            }

            /* No lo implementamos: siempre lanza una excepción. */
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de aristas que conectan al vértice con sus
         * vecinos. */
        public Diccionario<T, Grafica<T>.Arista<T>> aristas;

        /* Crea un nuevo vértice a partir de un elemento. */
          public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            aristas = new Diccionario<>();
        }

        /* Regresa el elemento del vértice. */
        public T getElemento() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        public int getGrado() {
            return aristas.getTotal();
        }

        /* Regresa el color del vértice. */
        public Color getColor() {
            return color;
        }

        /* Define el color del vértice. */
        public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterador para los vecinos. */
        public Iterator<VerticeGrafica<T>> iterator() {
            return new IteradorVecinos(aristas.iterator());
        }

        /* Define el índice del vértice. */
        public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        public int compareTo(Vertice<T> vertice) {
            if (vertice.distancia > distancia)
                return -1;
            else if (vertice.distancia < distancia)
                return 1;
            else
                return 0;
        }
    }

    /* Vértices. */
    private Diccionario<T, Vertice<T>> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        aristas = 0;
        vertices = new Diccionario<T, Vertice<T>>();
    }

    /**
     * Regresa el número de vértices.
     * @return el número de vértices.
     */
    public int getVertices() {
        return vertices.getTotal();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }



    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido
     *         agregado a la gráfica.
     */
    public void agrega(T elemento) {
         if (!vertices.contiene(elemento))
            vertices.agrega(elemento, new Vertice<T>(elemento));
        else
            throw new IllegalArgumentException();
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica. El peso de la arista que conecte a los
     * elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b ya están
     *         conectados, o si a es igual a b.
     */
    public void conecta(T a, T b) {
           conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva arista.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b ya están
     *         conectados, o si a es igual a b.
     */
    public void conecta(T a, T b, double peso) {
        if (a == null || a.equals(b) || sonVecinos(a,b)) {
            throw new IllegalArgumentException();
         } else {
            Vertice<T> va = buscaVertice(a);
            Vertice<T> vb = buscaVertice(b);
            if (va == null || vb == null)
                throw new NoSuchElementException();
            va.aristas.agrega(b,new Arista<T>(vb, peso));
            vb.aristas.agrega(a, new Arista<T>(va, peso));
            aristas++;
        } 
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b no están
     *         conectados.
     */
    public void desconecta(T a, T b) {
        Vertice<T> va = buscaVertice(a);
        Vertice<T> vb = buscaVertice(b);
        if (va == null || vb == null)
            throw new NoSuchElementException();
        if (!sonVecinos(a,b))
            throw new IllegalArgumentException();
        va.aristas.elimina(b);
        vb.aristas.elimina(a);
        aristas--;
    }

    private void eliminaArista(Vertice<T> a, Vertice<T> b) {
        a.aristas.elimina(b.elemento);
        b.aristas.elimina(a.elemento);
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la
     *         gráfica, <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        try {
            return vertices.contiene(elemento);
        } catch(NoSuchElementException nsee) {
            return false;
        }
    }


    /**
     * Elimina un elemento de la gráfica. El elemento tiene que
     * estar contenido en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está
     *         contenido en la gráfica.
     */
    public void elimina(T elemento) {
        Vertice<T> v = buscaVertice(elemento);
        /* no existe el elemento en la grafica */
        if (v == null)
            throw new NoSuchElementException();
        for (Arista<T> tmp :  v.aristas) {
            tmp.vecino.aristas.elimina(elemento);
            aristas--;
        }
        vertices.elimina(elemento);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los
     * elementos deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en
     *         otro caso.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice<T> va = buscaVertice(a);
        Vertice<T> vb = buscaVertice(b);
        if (va == null || vb == null)
            throw new NoSuchElementException();
        else 
            return va.aristas.contiene(b);
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que
     * contienen a los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que
     *         contienen a los elementos recibidos, o -1 si los
     *         elementos no están conectados.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     */
    public double getPeso(T a, T b) {
        Vertice<T> va = buscaVertice(a);
        Vertice<T> vb = buscaVertice(b);
        if (va == null || vb == null)
            throw new NoSuchElementException();
        if (!va.aristas.contiene(b))
            return -1;
        return va.aristas.get(b).peso;

    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @throws NoSuchElementException si elemento no es elemento de
     *         la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice<T> v = buscaVertice(elemento);
        if (v == null)
            throw new NoSuchElementException();
        else
            return v;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la
     * gráfica, en el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
         for ( Vertice<T> v: vertices)
            accion.actua(v);    }

    /**
     * Realiza la acción recibida en todos los vértices de la
     * gráfica, en el orden determinado por BFS, comenzando por el
     * vértice correspondiente al elemento recibido. Al terminar el
     * método, todos los vértices tendrán color {@link
     * Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos
     *        comenzar el recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la
     *         gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice<T> v = buscaVertice(elemento);
        if (v == null)
            throw new NoSuchElementException();
        Cola<Vertice<T>> cola = new Cola<Vertice<T>>();
        quitaColor();
        cola.mete(v);
        xfs(cola, accion);
     }

     private void xfs(MeteSaca<Vertice<T>> m, AccionVerticeGrafica<T> accion) {
        if (m.esVacia())
            return;
        Vertice<T> v = m.saca();
        v.color = Color.ROJO;
        accion.actua(v);
        for (Arista<T> tmp : v.aristas) 
            if (tmp.vecino.color != Color.ROJO) {
                m.mete(tmp.vecino);
                tmp.vecino.color = Color.ROJO;
            } 
                
        xfs(m,accion);
    }


    private void quitaColor() {
        for (Vertice<T> v: vertices) {
            v.color = Color.NINGUNO;
            for (Arista<T> w : v.aristas)
                w.vecino.color = Color.NINGUNO;
        }
    }
    /**
     * Realiza la acción recibida en todos los vértices de la
     * gráfica, en el orden determinado por DFS, comenzando por el
     * vértice correspondiente al elemento recibido. Al terminar el
     * método, todos los vértices tendrán color {@link
     * Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos
     *        comenzar el recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la
     *         gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice<T> v = buscaVertice(elemento);
        if (v == null)
            throw new NoSuchElementException();
        Pila<Vertice<T>> pila = new Pila<Vertice<T>>();
        quitaColor();
        pila.mete(v);
        xfs(pila, accion);
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se
     * itera en el orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos
     * vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman
     *         una trayectoria de distancia mínima entre los
     *         vértices <tt>a</tt> y <tt>b</tt>. Si los elementos se
     *         encuentran en componentes conexos distintos, el
     *         algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos
     *         no está en la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Lista<VerticeGrafica<T>> lista = new Lista<>();
        /* nunca se sabe.... */
        if (origen.equals(destino)) 
            return lista;
        Vertice<T> vOrigen = buscaVertice(origen);
        Vertice<T> vDestino = buscaVertice(destino);
        if(vOrigen == null || vDestino == null)
            throw new NoSuchElementException();
       actualizarDistancia(vOrigen, vDestino);
       vDestino.distancia = 0;
       Lista<Vertice<T>> l = vertices.valores();
       MonticuloMinimo<Vertice<T>> heap = new MonticuloMinimo<Vertice<T>>(l);
       return generarTrayecto(vDestino, heap);
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y
     * el elemento de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice
     *         <tt>origen</tt> y el vértice <tt>destino</tt>. Si los
     *         vértices están en componentes conexas distintas,
     *         regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos
     *         no está en la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        Lista<VerticeGrafica<T>> lista = new Lista<>();
        /* nunca se sabe.... */
        if (origen.equals(destino)) 
            return lista;
        Vertice<T> vOrigen = buscaVertice(origen);
        Vertice<T> vDestino = buscaVertice(destino);
        if(vOrigen == null || vDestino == null)
            throw new NoSuchElementException();
        /* ponemos todos los vertices con distancia infinita excepto el origen */
        distanciaInfinita();
        vOrigen.distancia = 0;
        Lista<Vertice<T>> l = vertices.valores();
        MonticuloMinimo<Vertice<T>> heap = new MonticuloMinimo<Vertice<T>>(l);
        /* actualizamos los pesos, si la grafica no es conexa, alguno qeudo con distancia infinito*/
        if(actulizaPesos(heap)) 
            generarRuta(vDestino, lista);
        return lista;
            
    }

 /* metodo auxiliar que se encarga de generar los pesos de acuerdo al algoritmo
    da en base a los pesos de las aristas */
    private boolean actulizaPesos(MonticuloMinimo<Vertice<T>> heap) {
       while(!heap.esVacio()) {
            Vertice<T> m = heap.elimina();

            if (m.distancia == Double.POSITIVE_INFINITY)
                return false;

            for (Arista<T> a: m.aristas) {
                Vertice<T> v = a.vecino;
                if (m.distancia + a.peso < v.distancia) {
                    v.distancia = m.distancia + a.peso;
                    heap.reordena(v);
                }
            }
        }
        return true;
    }  



    /* Calcula la distancia entre cada uno de los vertices usando bfs */
    private void actualizarDistancia(Vertice<T> origen, Vertice<T> destino) {
        final Vertice<T> d = destino;
        bfs(origen.elemento, new AccionVerticeGrafica<T>() {
            public void actua(VerticeGrafica<T> v) {
                Vertice<T> a = (Vertice<T>) v;
                a.distancia = distancia(a, d);
            }
        });
    }

    /* Actualiza la distancia entre el vertice origen y el destino */
    private int distancia(Vertice<T> origen, Vertice<T> destino) {
        for (Arista<T> a: origen.aristas) 
            if (a.vecino.equals(destino))
                return 1;
            else
                origen = a.vecino;
 
       return 1 + distancia(origen,destino);
    }

    /* método que genera una ruta despues de haber sido actualizados los pesos
    tomaremos el destino y el origen */
    private void generarRuta(Vertice<T> destino, Lista<VerticeGrafica<T>> lista) {
        /* agregamos el vertice visitado*/
        lista.agregaInicio(destino);
        /* llegamos al origen */
        if (destino.distancia == 0)
            return;
             
        for (Arista<T> a : destino.aristas) 
            /* encontramos de donde viene la semilla */
            if (destino.distancia - a.peso == a.vecino.distancia) 
                generarRuta(a.vecino, lista);
             
    }
       
    /* Genera el trayecto de menor distancia, despues de haber actualizado los vertices */ 
    private Lista<VerticeGrafica<T>> generarTrayecto( Vertice<T> destino, MonticuloMinimo<Vertice<T>> heap) {
        Lista<VerticeGrafica<T>> lista =  new Lista<>();
        int ultimaDistancia = -1;
        while(!heap.esVacio()) {
            Vertice<T> v = heap.elimina();
            if(v.distancia > ultimaDistancia) {
                lista.agregaInicio(v);
                ultimaDistancia = (int) v.distancia;
            }
        }
        return lista;
    }

    /* Método auxiliar que pone la distancia en infinito a los vertices que usaremos */
    private void distanciaInfinita() {
        for (Vertice<T> v: vertices)
            v.distancia = Double.POSITIVE_INFINITY;
    }

    private void verDistancia() {
        for (Vertice<T> v: vertices)
            System.out.println(v.distancia);
    }

     /* Método auxiliar que pone la distancia en infinito a los vertices que usaremos */
    private void pesoInfinito() {
        for (Vertice<T> v: vertices)
            for (Arista<T> a: v.aristas)
                a.peso = Double.POSITIVE_INFINITY;
    }

    /* Método auxiliar para buscar vértices. */
    private Vertice<T> buscaVertice(T elemento) {
        if(!vertices.contiene(elemento))
            return null;
        else
            return vertices.get(elemento);
    }
}

