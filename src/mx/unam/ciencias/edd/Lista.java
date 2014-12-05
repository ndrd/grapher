package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de
 * la lista, eliminar elementos de la lista, comprobar si un
 * elemento está o no en la lista, y otras operaciones básicas.</p>
 *
 * <p>Las instancias de la clase Lista implementan la interfaz
 * {@link Iterator}, por lo que el recorrerlas es muy sencillo:</p>
 *
 * <pre>
 *   for (String s : l)
 *      System.out.println(s);
 * </pre>
 *
 * <p>Además, se le puede pedir a una lista una instancia de {@link
 * IteradorLista} para recorrerla en ambas direcciones.</p>
 */
public class Lista<T> implements Iterable<T> 
{

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo<T>
    {
        public T elemento;
        public Nodo<T> anterior;
        public Nodo<T> siguiente;

        public Nodo(T elemento)
	{
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador<T> implements IteradorLista<T> 
    {

        /* La lista a iterar. */
        Lista<T> lista;
        /* Elemento anterior. */
        private Lista<T>.Nodo<T> anterior;
        /* Elemento siguiente. */
        private Lista<T>.Nodo<T> siguiente;

        /* El constructor recibe una lista para inicializar su
         * siguiente con la cabeza. */
        public Iterador(Lista<T> lista) 
        {
	    	this.lista = lista; 
	    	this.siguiente = lista.cabeza;
	    }

        /* Existe un siguiente elemento, si el siguiente no es
         * nulo. */
        @Override public boolean hasNext() 
        {
	    	return (this.siguiente != null);
        }

        /* Regresa el elemento del siguiente, a menos que sea nulo,
         * en cuyo caso lanza la excepción
         * NoSuchElementException. */
        @Override public T next() 
        {
            if(this.hasNext()){
            	anterior = siguiente;
            	siguiente = siguiente.siguiente;
                return anterior.elemento;
            }
	    	else
				throw new NoSuchElementException();
        }

        /* Existe un elemento anterior, si el anterior no es
         * nulo. */
        @Override public boolean hasPrevious() 
        {
            return this.anterior != null;
        }

        /* Regresa el elemento del anterior, a menos que sea nulo,
         * en cuyo caso lanza la excepción
         * NoSuchElementException. */
        @Override public T previous() 
        {
        	if(hasPrevious())
            {
                siguiente = anterior;
        		anterior = anterior.anterior;
                return siguiente.elemento;
        	}
				
	   		 else
				throw new NoSuchElementException();
        }

        /* No implementamos el método remove(); sencillamente
         * lanzamos la excepción UnsupportedOperationException. */
        @Override public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /* Mueve el iterador al inicio de la lista; después de
         * llamar este método, y si la lista no es vacía, hasNext()
         * regresa verdadero y next() regresa el primer elemento. */
        @Override public void start() 
        {
            if(this.lista.longitud > 0)
            	siguiente = lista.cabeza;
        }

        /* Mueve el iterador al final de la lista; después de llamar
         * este método, y si la lista no es vacía, hasPrevious()
         * regresa verdadero y previous() regresa el último
         * elemento. */
        @Override public void end() 
        {
            if(this.lista.longitud > 0)
            	anterior = lista.rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo<T> cabeza;
    /* Último elemento de la lista. */
    private Nodo<T> rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que
     * contiene.
     */
    public int getLongitud() 
    {
        return longitud;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no
     * tiene elementos, el elemento a agregar será el primero y
     * último.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) 
    {
    	Nodo<T> tmp = new Nodo<T>(elemento);
	   	if(this.longitud == 0)
	   		this.cabeza = this.rabo = tmp;
	   	else
	   	{
	   		tmp.anterior = rabo;
            rabo.siguiente = tmp;
            rabo = tmp;
	   	}

	   	++this.longitud;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no
     * tiene elementos, el elemento a agregar será el primero y
     * último.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) 
    {
        Nodo<T> tmp = new Nodo<T>(elemento);
        if(this.longitud == 0)
        	this.cabeza = this.rabo = tmp;
        else
        {
        	tmp.siguiente = cabeza;
        	cabeza.anterior = tmp;
        	cabeza = tmp;
        }
        ++this.longitud;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está
     * contenido en la lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) 
    {   
        Nodo<T> nodo = buscaNodo(cabeza, elemento); 
        Nodo<T> anterior,siguiente; 
        if(nodo == null)
            return;

        if(nodo == cabeza && nodo == rabo)
            rabo = cabeza = null;

        else if(nodo == cabeza)
        {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }   
        else if(nodo == rabo)
        {
            rabo = rabo.anterior;
            rabo.siguiente  = null;
        } 
        else
        {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
        }  
        longitud--;
    }

    private static void debug(Object o)
    {
        System.out.println(o);
    }

    private Nodo<T> buscaNodo(Nodo<T> nodo, T elemento)
    {
        if(nodo == null)
            return null;
        return ((elemento.equals(nodo.elemento)) ? nodo : buscaNodo(nodo.siguiente, elemento));
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() 
    {
        if(this.longitud == 0)
        	throw new NoSuchElementException();
        else
        {
        	T elemento  = null;
        	elemento =  this.cabeza.elemento;
            cabeza = this.cabeza.siguiente;
            --this.longitud;
        	return elemento;
        }

    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() 
    {
       if(this.longitud == 0)
            throw new NoSuchElementException();
        
        T elemento  = null;
        elemento = this.rabo.elemento;
       
        if(rabo == cabeza)
            rabo = cabeza  = null;
        else
        {
            this.rabo.elemento = null;
            this.rabo = this.rabo.anterior;
            rabo.siguiente = null;
        }
         --this.longitud;
        return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la
     * lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) 
    {
    	if(this.longitud == 0)
    		return false;
    	return buscaNodo(cabeza,elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar
     *         el método.
     */
    public Lista<T> reversa() 
    {
        Lista<T> nueva = new Lista<T>();
        Nodo<T> tmp = this.rabo;
        while(tmp != null)
        {
        	nueva.agregaFinal(tmp.elemento);
        	tmp  = tmp.anterior;
        }
        return nueva;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos
     * elementos que la lista que manda llamar el método, en el
     * mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() 
    {
        Lista<T> nueva = new Lista<T>();
        Nodo<T> tmp = this.cabeza;
        while(tmp != null)
        {
            nueva.agregaFinal(tmp.elemento);
            tmp  = tmp.siguiente;
        }
        return nueva;	
      
    }

    /**
     * Limpia la lista de elementos. El llamar este método es
     * equivalente a eliminar todos los elementos de la lista.
     */
    public void limpia() 
    {
        this.cabeza = this.rabo = null;
        this.longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() 
    {
        if(this.cabeza == null || this.longitud == 0)
        	throw new NoSuchElementException();
        else
        	return this.cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() 
    {
        if(this.rabo == null || this.longitud == 0)
        	throw new NoSuchElementException();
        else
        	return this.rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si
     *         <em>i</em> es mayor o igual que cero y menor que el
     *         número de elementos en la lista.
     * @throws ExcepcionIndiceInvalido si el índice recibido es
     *         menor que cero, o mayor que el número de elementos en
     *         la lista menos uno.
     */
    public T get(int i) 
    {
		if(i < 0 || i >= this.longitud)
        	throw new ExcepcionIndiceInvalido();
        else
        {
        	int j = 0;
        	Nodo<T> tmp = this.cabeza;
        	while (tmp != null && (j++) < i)
        		tmp =  tmp.siguiente;
        	return tmp.elemento;
        }
 	}

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) 
    {
        if(this.longitud == 0)
        	return -1;
        else
        {
        	boolean encontrado = false;
        	int indiceDe = 0;
        	Nodo<T> tmp =  this.cabeza;
        	while(tmp != null && !encontrado)
        	{
        		if(tmp.elemento.equals(elemento))
        			return indiceDe;
        		else
        		{
        			tmp = tmp.siguiente;
        			indiceDe++;
        		}
        			
        	}
        	return -1;
        		
        }
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto
     *         recibido; <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) 
    {
    	if(o == null)
    		return false;
    	
    	if(getClass() != o.getClass())
            return false;

        @SuppressWarnings("unchecked")
        Lista<T> tmp = getClass().cast(o);

		if(tmp.longitud != this.longitud)
       			return false;

       		Nodo<T> aqui =  this.cabeza;
       		Nodo<T> alla = tmp.cabeza;	

       		while(aqui != null)
       		{
       			if(!aqui.elemento.equals(alla.elemento))
       				return false;
       			aqui =  aqui.siguiente;
       			alla = alla.siguiente;
       		}
       		return true;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() 
    {
    	String s = "[";
    	
    	if(this.longitud == 0)
    		return s += "]";

    	Nodo<T> tmp = this.cabeza;
    	while(tmp.siguiente != null)
    	{
    		 s += String.format("%s, ", tmp.elemento);
    		 tmp = tmp.siguiente;
    	}
        s += String.format("%s]", this.rabo.elemento);
    	return s;
    }
    /**
     * Regresa un iterador para recorrer la lista.
     * @return un iterador para recorrer la lista.
     */
    @Override public Iterator<T> iterator() 
    {
       	return iteradorLista();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas
     * direcciones.
     * @return un iterador para recorrer la lista en ambas
     * direcciones.
     */
    public IteradorLista<T> iteradorLista() 
    {
        return new Iterador<T>(this);
    }

     /**
     * Regresa una copia de la lista recibida, pero ordenada. La
     * lista recibida tiene que contener nada más elementos que
     * implementan la interfaz {@link Comparable}.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> l)
    {
    	if(l.longitud == 0 || l.longitud == 1)
    		return l.copia();
    	int mitad = l.longitud / 2;
    	Lista<T> izq = l.subLista(0,mitad);
    	Lista<T> der = l.subLista(mitad, l.longitud);
    	return mezcla(mergeSort(izq),mergeSort(der));
    }

    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> izquierda, Lista<T> derecha)
    {
 		Lista<T>.Nodo<T> i = izquierda.cabeza;
 		Lista<T>.Nodo<T> j = derecha.cabeza;
 		Lista<T> nueva = new Lista<T>();

 		while(i != null && j != null)
 		{
            // toma dos elementos, decide cual agregar primero
 			if(i.elemento.compareTo(j.elemento) < 0)
 			{
 				nueva.agregaFinal(i.elemento);
 				i = i.siguiente;
 			}
 			else
 			{
 				nueva.agregaFinal(j.elemento);
 				j = j.siguiente;
 			}
 		}

        // si j ya no tiene elementos, llenamos con i
        if(j == null)
        {
            while(i != null )
            {
                nueva.agregaFinal(i.elemento);
                i = i.siguiente;
            }
        }
        
        // analogamente...
        if(i == null)
        {
            while(j != null)
            {
                nueva.agregaFinal(j.elemento);
                j = j.siguiente;
            }
        }

 		return nueva;

    }


    public <T extends Comparable<T>> Lista<T> ordena(Lista<T> uno, Lista<T> dos)
    {
    	return mezcla(uno, dos);
    }

   

    public Lista<T> subLista(int inicio, int fin)
    {
    	Lista<T> nueva = new Lista<T>();
    	Nodo<T> tmp = cabeza;
        int i = 0;

         // nos desplazamos a la posicion en i
        while(tmp != null && (i++) < inicio)
            tmp = tmp.siguiente;
        for (int j = inicio; j < fin; j++ ) {
            nueva.agregaInicio(tmp.elemento);
            tmp = tmp.siguiente;
        }
    			
    	return nueva;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la
     * interfaz {@link Comparable}, y se da por hecho que está
     * ordenada.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
        boolean busquedaLineal(Lista<T> l, T e) 
    {
    	Lista<T>.Nodo<T> tmp = l.cabeza;
    	while(tmp != null)
    	{
            if(e.compareTo(tmp.elemento) == 0)
                return true;
    		if(e.compareTo(tmp.elemento) < 0)
                return false;
            tmp = tmp.siguiente;
    	}   
    	return false;
    }

   
}
