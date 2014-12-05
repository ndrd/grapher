package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.MeteSaca;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link MeteSaca}.
 */
public class TestMeteSaca {

    /* Clase privada interna para poder hacer pruebas con instancias
     * de MeteSaca. Implementa de forma trivial (y errónea) los
     * métodos saca() y mira(), porque sólo nos interesa probar
     * mete() y esVacia(). */
    private class MSPrueba<T> extends MeteSaca<T> {
        public MSPrueba() { super(); }
        public T saca() { return null; }
        public T mira() { return null; }
    }

    private Random random;
    private int total;
    private MeteSaca<Integer> meteSaca;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra estructura, y una
     * instancia.
     */
    public TestMeteSaca() {
        random = new Random();
        total = 10 + random.nextInt(90);
        meteSaca = new MSPrueba<Integer>();
    }

    /**
     * Prueba unitaria para {@link MeteSaca#mete}.
     */
    @Test public void testMete() {
        Assert.assertTrue(meteSaca.esVacia());
        meteSaca.mete(1);
        Assert.assertFalse(meteSaca.esVacia());
    }

    /**
     * Prueba unitaria para {@link MeteSaca#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(meteSaca.esVacia());
        for (int i = 0; i < total; i++) {
            meteSaca.mete(random.nextInt(total));
            Assert.assertFalse(meteSaca.esVacia());
        }
    }
}
