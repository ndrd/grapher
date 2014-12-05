package mx.unam.ciencias.myp;

import mx.unam.ciencias.edd.Lista;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*; 
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;


public class Graficador extends JFrame {

    /**
     * Creates new form Graficador
     */
    public Graficador() {
        initComponents();
    }

 
    @SuppressWarnings("unchecked")
    private void initComponents() {

        textoFuncion = new JTextField();
        botonGraficar = new JButton();
        botonLimpiar = new JButton();
        etiquetaFuncion = new JLabel();
        contenedorPanelGrafica = new JScrollPane();
        selectorAncho = new JSpinner();
        etiquetaAncho = new JLabel();
        etiquetaAlto = new JLabel();
        selectorAlto = new JSpinner();
        selectorX1 = new JSpinner(new SpinnerNumberModel(0, -10000, 10000, 0.1));
        etiquetaX2 = new JLabel();
        selectorX2 = new JSpinner(new SpinnerNumberModel(0, -10000, 10000, 0.1));
        etiquetaX1 = new JLabel();
        selectorY1 = new JSpinner(new SpinnerNumberModel(0, -10000, 10000, 0.1));
        etiquetaY2 = new JLabel();
        selectorY2 = new JSpinner(new SpinnerNumberModel(0, -10000, 10000, 0.1));
        etiquetaY1 = new JLabel();
        guardarPNG = new JButton();
        guardarSVG = new JButton();
        guardarPDF = new JButton();
        guardaArchivo =  new JFileChooser();
        ancho = 900;
        alto = 600;
        ploter = new Trazador(ancho+1, alto);
        panelGrafica = new DibujaGUI(ploter.listaPoligonos(),0,0);
        funciones = new Lista<>();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Graficador v 1.0");
        setBackground(java.awt.Color.darkGray);

        textoFuncion.setText("(* (* (- x 2) (+ x 2)) x)");

        /* obtiene las dimensiones de la grafica que se va a trazar y las devuelve */
        botonGraficar.setText("Graficar");
        botonGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    String f = textoFuncion.getText();
                    if(f.length() == 0)
                        throw new MalFormedFunctionException("Ingresa una expresion");
                    /* la funcion ya fue graficada */
                    if(funciones.indiceDe(f)  != -1)
                         throw new MalFormedFunctionException("Funcion ya graficada");
                     funciones.agregaFinal(f);

                    /* obtenemos el intervalo */
                    SpinnerNumberModel s1 = (SpinnerNumberModel) selectorX1.getModel();
                    SpinnerNumberModel s2 = (SpinnerNumberModel) selectorX2.getModel();
                    SpinnerNumberModel t1 = (SpinnerNumberModel) selectorY1.getModel();
                    SpinnerNumberModel t2 = (SpinnerNumberModel) selectorY2.getModel();
                    SpinnerNumberModel a1 = (SpinnerNumberModel) selectorAncho.getModel();
                    SpinnerNumberModel a2 = (SpinnerNumberModel) selectorAlto.getModel();
                    double x1,x2,y1,y2;
                    int ancho, alto;

                    ancho = a1.getNumber().intValue();
                    alto = a2.getNumber().intValue();
                    x1 = s1.getNumber().doubleValue();
                    x2 = s2.getNumber().doubleValue();
                    y1 = t1.getNumber().doubleValue();
                    y2 = t2.getNumber().doubleValue();
                  /* actualizamos las medidas por si se redimensiono  */
                    ploter.setMedidasYrango(ancho, alto,x1,x2,y1,y2);
                    ploter.agregaFuncion(f.toLowerCase());
                    panelGrafica = new DibujaGUI(ploter.listaPoligonos(),ancho, alto);
                    contenedorPanelGrafica.setViewportView(panelGrafica);
                    textoFuncion.setText("");

                } catch(MalFormedFunctionException mffe) {
                    String causa = mffe.getMessage();
                    JOptionPane.showMessageDialog(null, causa);                  
 
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(null, "Mi culpa :/"); 
   
                }
            }
        });

        /* limpia las lista de funciones */
        botonLimpiar.setText("Limpiar");
        botonLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ploter.limpiar();
                funciones.limpia();
                contenedorPanelGrafica.setViewportView(new DibujaGUI(new Lista<Polygon>(),300,300));
            }
        });

        etiquetaFuncion.setText("f(x):");

        panelGrafica.setBorder(null);
        contenedorPanelGrafica.setViewportView(panelGrafica);

        etiquetaAncho.setText("Ancho");

        etiquetaAlto.setText("Alto");

        etiquetaX2.setText("x₂");

        etiquetaX1.setText("x₁");

        etiquetaY2.setText("y₂");

        etiquetaY1.setText("y₁");

        selectorX1.setValue(new Double(-6.4));
        selectorX2.setValue(new Double(6.4));
        selectorY1.setValue(new Double(-4.5));
        selectorY2.setValue(new Double(4.5));
        selectorAncho.setValue(new Integer(ancho));
        selectorAlto.setValue(new Integer(alto));



        guardarPNG.setText("2 PNG");
        guardarPNG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                BufferedImage imagen = new BufferedImage(panelGrafica.getWidth(), panelGrafica.getWidth(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D cg = imagen.createGraphics();
                panelGrafica.paintAll(cg);
                try {
                        File salida = mostrarArchivador( new FileNameExtensionFilter("Portable Network Graphic (*.png)", "png"));
                        if (ImageIO.write(imagen, "png", salida))
                        {
                            JOptionPane.showMessageDialog(null,"imagen guardada");
                        }
                } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,"No se pudo guardar la imagen");

                }
            }
        });

        guardarSVG.setText("2 SVG");
        guardarSVG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                File archivo = mostrarArchivador( new FileNameExtensionFilter("ScalarVectorGraphic (*.svg)", "svg"));
                if(archivo == null)
                    return;
                try {
                    PrintWriter salida = new PrintWriter(archivo);
                    salida.println(ploter.getSVG());
                    salida.close();
                    JOptionPane.showMessageDialog(null, "Imagen guardada :)");

                } catch(Exception e) {
                    JOptionPane.showMessageDialog(null, "No se pudo guardar la imagen ");
                }
            }
        });

        guardarPDF.setText("2 PDF");
        guardarPDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                    JOptionPane.showMessageDialog(null, "Funcion disponible para la version 2.0 ");
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(contenedorPanelGrafica)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(etiquetaFuncion, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textoFuncion)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonGraficar, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonLimpiar, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(etiquetaAncho)
                                .addGap(1, 1, 1)
                                .addComponent(selectorAncho, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(etiquetaAlto)
                                .addGap(3, 3, 3)
                                .addComponent(selectorAlto, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiquetaX1)
                                .addGap(1, 1, 1)
                                .addComponent(selectorX1, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiquetaX2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectorX2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiquetaY1)
                                .addGap(1, 1, 1)
                                .addComponent(selectorY1, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etiquetaY2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectorY2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(guardarSVG)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(guardarPNG)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(guardarPDF)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contenedorPanelGrafica, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textoFuncion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonGraficar)
                            .addComponent(botonLimpiar)))
                    .addComponent(etiquetaFuncion))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(selectorAncho)
                        .addComponent(etiquetaAlto, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectorAlto)
                        .addComponent(selectorX1)
                        .addComponent(etiquetaX2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectorX2)
                        .addComponent(etiquetaX1)
                        .addComponent(selectorY1)
                        .addComponent(etiquetaY2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectorY2)
                        .addComponent(etiquetaY1)
                        .addComponent(guardarPNG)
                        .addComponent(guardarPDF)
                        .addComponent(guardarSVG))
                    .addComponent(etiquetaAncho, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );


        pack();
    }                        

                           
   /**
   * Funcion que muestra un selecctor de archivos para guardar la imagen :)
   */
    private File mostrarArchivador(FileFilter filtro) {
            JFileChooser selectorArchivo = new JFileChooser();
            selectorArchivo.setDialogTitle("Selecciona una ubicacion para guardar");
            selectorArchivo.setFileFilter(filtro);
            int eleccion = selectorArchivo.showSaveDialog(this);
          
            if (eleccion == JFileChooser.APPROVE_OPTION) {
                return selectorArchivo.getSelectedFile();
            }
            return null;
        }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
 

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Graficador g = new Graficador();
                g.setSize(new Dimension(900,580));
                g.setResizable(false);
                g.setBackground(Color.decode("0x121315"));
                g.setVisible(true);
            }
        });
    }


    private JButton botonGraficar;
    private JButton botonLimpiar;
    private JButton guardarPNG;
    private JButton guardarSVG;
    private JButton guardarPDF;
    private DibujaGUI panelGrafica;
    private JLabel etiquetaFuncion;
    private JLabel etiquetaAncho;
    private JLabel etiquetaAlto;
    private JLabel etiquetaX2;
    private JLabel etiquetaX1;
    private JLabel etiquetaY2;
    private JLabel etiquetaY1;
    private JScrollPane contenedorPanelGrafica;
    private JSpinner selectorAncho;
    private JSpinner selectorAlto;
    private JSpinner selectorX1;
    private JSpinner selectorX2;
    private JSpinner selectorY1;
    private JSpinner selectorY2;
    private JTextField textoFuncion;
    JFileChooser guardaArchivo;
    private Trazador ploter;
    private Lista<String> funciones;
    private int ancho;
    private int alto;
    private double x1,x2,y1,y2;
}
