package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class FabricaHuellasDigitales {

    /**
     * Identificador para fabricar la huella digital de Bob
     * Jenkins para cadenas.
     */
    public static final int BJ_STRING   = 0;
    /**
     * Identificador para fabricar la huella digital de GLib para
     * cadenas.
     */
    public static final int GLIB_STRING = 1;
    /**
     * Identificador para fabricar la huella digital de XOR para
     * cadenas.
     */
    public static final int XOR_STRING  = 2;

    /**
     * Regresa una instancia de {@link HuellaDigital} para cadenas.
     * @param identificador el identificador del tipo de huella
     *        digital que se desea.
     * @throws IllegalArgumentException si recibe un identificador
     *         no reconocido.
     */
    public static HuellaDigital<String> getInstanciaString(int identificador) {
        if(identificador == FabricaHuellasDigitales.BJ_STRING)
            return new BobJenkins();
        else if (identificador == FabricaHuellasDigitales.GLIB_STRING)
            return new GLibHash();
        else if (identificador == FabricaHuellasDigitales.XOR_STRING)
            return new XORHash();
        else
            throw new IllegalArgumentException();
    }
    /* compleatar la clase, no los metodos :) */

    private static class BobJenkins implements HuellaDigital<String> {
        @Override
        public int huellaDigital(String s) {
            int  h = (int) hash(s.getBytes());
            return h;
        }
         long a,b,c;

        private int hash(byte [] k) {
            int l = k.length;
            a = 0x000000009e3779b9L;
            b = a;
            c = 0xffffffff;
            int i = 0;

            while (l >= 12) {
                a += (k[i] + (k[i+1] << 8) + (k[i+2] << 16) + (k[i+3] << 24));
                b += (k[i+4] + (k[i+5] << 8) + (k[i+6] << 16) + (k[i+7] << 24));
                c += (k[i+8] + (k[i+9] << 8) + (k[i+10] << 16) + (k[i+11] << 24));
                mezcla();
                i += 12;
                l -= 12;
            }
            c += k.length;

            switch(l) {
            	case 11:
                c = suma(c, correIzq(byteLong(k[i + 10]), 24));
	            case 10:
	                c = suma(c, correIzq(byteLong(k[i + 9]), 16));
	            case 9:
	                c = suma(c, correIzq(byteLong(k[i + 8]), 8));
	                // the first byte of c is reserved for the length
	            case 8:
	                b = suma(b, correIzq(byteLong(k[i + 7]), 24));
	            case 7:
	                b = suma(b, correIzq(byteLong(k[i + 6]), 16));
	            case 6:
	                b = suma(b, correIzq(byteLong(k[i + 5]), 8));
	            case 5:
	                b = suma(b, (k[i + 4]));
	            case 4:
	                a = suma(a, correIzq(byteLong(k[i + 3]), 24));
	            case 3:
	                a = suma(a, correIzq(byteLong(k[i + 2]), 16));
	            case 2:
	                a = suma(a, correIzq(byteLong(k[i + 1]), 8));
	            case 1:
	                a = suma(a, (k[i + 0]));
            }
            mezcla();
            return (int) (c&0xffffffff);
        }

        private long byteLong(byte b) {
            long val = b & 0x7F;
             return val;
        }

        private void mezcla() {
           a = resta(a, b); a = resta(a, c); a = xor(a, c >> 13);
           b = resta(b, c); b = resta(b, a); b = xor(b, correIzq(a, 8));
           c = resta(c, a); c = resta(c, b); c = xor(c, (b >> 13));
           a = resta(a, b); a = resta(a, c); a = xor(a, (c >> 12));
           b = resta(b, c); b = resta(b, a); b = xor(b, correIzq(a, 16));
           c = resta(c, a); c = resta(c, b); c = xor(c, (b >> 5));
           a = resta(a, b); a = resta(a, c); a = xor(a, (c >> 3));
           b = resta(b, c); b = resta(b, a); b = xor(b, correIzq(a, 10));
           c = resta(c, a); c = resta(c, b); c = xor(c, (b >> 15));

        }

        private long correIzq(long n, int r) {
            return (n << r) & 0x00000000ffffffffL;
        }

        private long resta(long a, long b) {
            return (a - b) & 0x00000000ffffffffL;
        }

        private long xor(long a, long b) {
            return (a ^ b) & 0x00000000ffffffffL;
        }

        private long suma(long a, long b) {
        	return (a + b) & 0x00000000ffffffffL;
        }

  
    }

    private static class GLibHash implements HuellaDigital<String> {
        @Override
        public int huellaDigital(String s) {
            return hash(s.getBytes());
        }

        /* método auxiliar que hace el trabajo  rudo */
        private int hash (byte [] k) {
            int h = 5381;
            for (int i = 0; i < k.length; i++) {
                char c = (char) k[i];
                h = h * 33 + c;
            }
            return h;
        }
    }

    private static class XORHash implements HuellaDigital<String> {
        @Override
        public int huellaDigital(String s) {
            return hash(s.getBytes());
        }

        private int hash(byte[] k) {
            int m = k.length; 
            int n = k.length;
            if((n&3) != 0)
                m = n + 4 - (n&3);
            byte[] t = new byte[m];
            for(int i = m-n; i < m; i++)
                t[i] = k[i-(m-n)];
            int h = 0;
            for(int j = 0; j < m; j+=4) {
                int a = (int)t[j] << 24;
                int b = (int)t[j+1] << 16;
                int c = (int)t[j+2] << 8; 
                int d = (int)t[j+3]; 
                int abcd = a|b|c|d;
                h ^= abcd; 
            } 
            return h; 

        }
    }
}
