package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Clase para manipular arreglos genéricos.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
                     void quickSort(T[] a) 
    {
     	quickSort(a, 0, (a.length - 1));   
    }

    private static <T extends Comparable<T>> void quickSort(T[] a, int ini, int fin)
    {
    	if((fin - ini) < 1)
    		return;
    	int i = ini + 1;
    	int j = fin;
    	while(i < j)
    		if(a[i].compareTo(a[ini]) > 0 && a[j].compareTo(a[ini]) <= 0)
    			intercambia(a,i++,j--);
    		else if(a[i].compareTo(a[ini]) <= 0)
    			i++;
    		else
    			j--;
    	
    	if(a[i].compareTo(a[ini]) > 0)
    		i--;

    	intercambia(a,i,ini);
    	quickSort(a, ini,i-1);
    	quickSort(a,i+1, fin);

    }

    private static <T extends Comparable<T>> void intercambia(T[] a, int i, int j)
    {
    	T tmp = a[i];
    	a[i] = a[j];
    	a[j] = tmp;
    }

    /**
     * Ordena el arreglo recibido usando InsertionSort.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
                     void insertionSort(T[] a)
    {
     	for(int i = 1; i < a.length;i++)
     	{
            T p = a[i];
            int j = i - 1;
     		while(j >= 0 && a[j].compareTo(p) > 0)
            {
                a[j + 1] = a[j];
                j = j-1;
            }
            a[j+1] = p;
     	} 
     
       
    }

    /**
     * Hace quuna búsqueda binaria del elemento en el arreglo. Regresa
     * el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     */
    public static <T extends Comparable<T>>
                     int busquedaBinaria(T[] a, T e) 
    {
    	//quickSort(a);
        return busquedaBinaria(a, e, 0,a.length - 1);

    }

    private static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e, int ini, int fin)
    {
    	if(fin < ini)
    		return -1;
    	else
    	{
    		int mitad = (fin + ini) / 2;
	    	if(e.compareTo(a[mitad]) > 0)
	    		return busquedaBinaria(a, e, mitad + 1 , fin);
	    	else if(e.compareTo(a[mitad]) < 0)
	    		return busquedaBinaria(a, e, ini, mitad - 1);
            else
                return mitad;
	    }
	   
   	}

    private static void debug(Object o)
    {
    	System.out.println(o);
    }
}
