package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.Lista;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.RenderingHints;

/**
* Clase que sirve para generar la interaz grafica con respecto a las funciones
* que se le pasan en el constructor, con ella se construye un panel que se 
* muestra en la venta principal
*/
public class DibujaGUI extends JPanel {
  /* la lista de poligonos que se va a trazar */
    private Lista<Polygon> poligonos;

    /**
    * Constrctor por defecto, recibe una {@link Lista} de {@link Polygon} 
    * donde estan contenidas las graficas por dibujar
    */
    public DibujaGUI(Lista<Polygon> graficas, int ancho, int alto) {
           super();
            setBackground(new Color(0,0,0,0));
            setSize(ancho, alto);
           poligonos = graficas;
    }

    /**
    * Se sobreescribe el metodo y aqui se dibujan todas las graficas dentro de la
    * lista de graficas.
    */
    @Override  public void paintComponent(Graphics g) {
               super.paintComponent(g);
             Graphics2D g2 = (Graphics2D) g;
              g2.setStroke(new BasicStroke(2));
              g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              int i = 0;
               for(Polygon grafica : poligonos) {
                    g2.setColor(Color.decode(randomColor(i++)));
                    g2.drawPolyline(grafica.xpoints, grafica.ypoints, grafica.npoints);
               }
          
        }

        /* Devuelve un color para generar una grafica colorida */
        private String randomColor(int i) {
            String [] colors = {"007AFF","FF1300","4CD964",
                                "FF2D55", "E0F8D8","81F3FD",
                                "FFCD02", "DBDDDE"};
            return "0x" + colors[i%colors.length];
        }

 

 }