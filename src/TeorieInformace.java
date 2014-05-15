import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 *  Hlavní třída appletu Teorie informace.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class TeorieInformace extends JApplet {
    protected JPanel contentPane;
    protected JPanel panel;
    protected JLabel lblAvgEnt, lblMsgEnt, lblMsgLen, lblMsgHuffLen,
            lblMsgRed, lblMsgHuffRed, lblCWordLen, lblCWordHuffLen,
            lblAvgRedASCII, lblAvgRedHuff;
    protected CharArrayEntropyAnalyzer ea;
    protected MyTableModel model;
    private static DecimalFormat df = new DecimalFormat("###.###");


    /**
     *  Třída implementující DocumentListener pro vstupní pole.
     *
     *@author     Václav Haisman
     *@created    25. leden 2003
     */
    class TextAreaListener implements DocumentListener {
        /**
         *  Description of the Method
         *
         *@param  e  Description of the Parameter
         */
        public void changedUpdate(DocumentEvent e) {
            handler(e);
        }


        /**
         *  Description of the Method
         *
         *@param  e  Description of the Parameter
         */
        public void insertUpdate(DocumentEvent e) {
            handler(e);
        }


        /**
         *  Description of the Method
         *
         *@param  e  Description of the Parameter
         */
        public void removeUpdate(DocumentEvent e) {
            handler(e);
        }


        /**
         *  Tato metoda vezme text ze vstupního textového pole a posle ho
         *  modelu tabulky. Také provede přepočítání charakteristických hodnot.
         *
         *@param  e  DocumentEvent
         */
        private void handler(DocumentEvent e) {
            int i;
            Document doc = e.getDocument();
            String text;
            try {
                text = doc.getText(0, doc.getLength());
            } catch (BadLocationException ex) {
                System.err.println("Exceprion: " + ex);
                return;
            }
            // update tabulky
            char[] znaky = text.toUpperCase().toCharArray();
            Arrays.sort(znaky);
            ea = new CharArrayEntropyAnalyzer(znaky);
            model.update(znaky);
            // update ostatnich hodnot
            // prumenrna entropie
            lblAvgEnt.setText(df.format(ea.averageEntropy() == (double) -0.0
                     ? 0.0 : ea.averageEntropy()) + " bitů");
            char[] m = text.toUpperCase().toCharArray();
            Object[] msg = new Object[m.length];
            for (i = 0; i < m.length; ++i) {
                msg[i] = new Character(m[i]);
            }
            double msgEnt = ea.messageEntropy(msg);
            if (msgEnt == (double) -0.0) {
                msgEnt = 0.0;
            }
            // entropie zpravy
            lblMsgEnt.setText(df.format(msgEnt) + " bitů");
            // delka zpravy
            lblMsgLen.setText((doc.getLength() * 8) + " bitů");
            int len = 0;
            HuffmanCodeTree huff = new HuffmanCodeTree(ea.getFrequenciesMap());
            Map huffkod = huff.getHuffmanCode();
            for (i = 0; i < msg.length; ++i) {
                String s = (String) huffkod.get(msg[i]);
                len += s.length();
            }
            // delka zpravy v Huffmanove kodu
            lblMsgHuffLen.setText(len + " bitů");
            // redundance ASCII kodu zpravy
            lblMsgRed.setText(df.format(doc.getLength() * 8 - msgEnt) + " bitů");
            // redundance Huffmanova kodu zpravy
            lblMsgHuffRed.setText(df.format(len - msgEnt) + " bitů");
            // prumerna delka slova ASCII kodu zpravy
            double avgwordlen = 0;
            Object[] klice = huffkod.keySet().toArray();
            for (i = 0; i < klice.length; ++i) {
                avgwordlen += 8 * ea.probability(klice[i]);
            }
            lblCWordLen.setText(df.format(avgwordlen) + " bitů");
            // prumerna delka slova Huffmanova kodu zpravy
            double avgwordhufflen = 0;
            for (i = 0; i < klice.length; ++i) {
                avgwordhufflen += ((String)huffkod.get(klice[i])).length() 
                                  * ea.probability(klice[i]);
            }
            lblCWordHuffLen.setText(df.format(avgwordhufflen));
            // prumerna redundance ASCII kodu
            lblAvgRedASCII.setText(df.format(avgwordlen - ea.averageEntropy()));
            // prumerna redundance Huffmanova kodu
            lblAvgRedHuff.setText(df.format(avgwordhufflen - ea.averageEntropy()));
        }
    }


    /**
     *  Tato trida implementuje model tabulky zobrazující charakteristiké hodnoty
     *  jednotlivých zdrojových jednotek.
     *
     *@author     wilx
     *@created    25. leden 2003
     */
    class MyTableModel extends AbstractTableModel {
        Vector sloupce = new Vector();
        Vector data = new Vector();


        /**
         *  Constructor for the MyTableModel object
         */
        public MyTableModel() {
            super();
            sloupce.add("Default");
        }


        /**
         *  Metoda update provádí prepočítání a zobrazení charakteristických hodnot
         *  zdrojových jednotek z předaného bufferu.
         *
         *@param  znaky  Buffer analyzované zprávy.
         */
        public void update(char[] znaky) {
            int i;

            data = new Vector();
            sloupce = new Vector();
            Map freq = ea.getFrequenciesMap();
            Object[] klice = freq.keySet().toArray();
            Arrays.sort(klice);
            // zahlavi
            sloupce.add("Názvy");
            for (i = 0; i < klice.length; ++i) {
                sloupce.add(klice[i].toString());
            }
            // radek pravdepodobnosti
            Vector radek = new Vector();
            radek.add("Pravděpodobnost");
            for (i = 0; i < klice.length; ++i) {
                double p = ea.probability((Character) klice[i]);
                radek.add(df.format(p));
            }
            data.add(radek);
            // radek entropie
            radek = new Vector();
            radek.add("Entropie");
            for (i = 0; i < klice.length; ++i) {
                double e = ea.entropy(klice[i]);
                e = e == (double)-0.0 ? 0.0 : e;
                radek.add(df.format(e));
            }
            data.add(radek);
            // radek delky Huffmanova kodu pro dany znak
            radek = new Vector();
            radek.add("Délka Huffmanova kódu");
            HuffmanCodeTree huff = new HuffmanCodeTree(ea.getFrequenciesMap());
            Map huffkod = huff.getHuffmanCode();
            for (i = 0; i < klice.length; ++i) {
                String s = (String) huffkod.get(klice[i]);
                radek.add(Integer.toString(s.length()));
            }
            data.add(radek);
            // radek vlastniho Huffmanova kodu pro dany znak
            radek = new Vector();
            radek.add("Huffmanův kód");
            for (i = 0; i < klice.length; ++i) {
                radek.add(huffkod.get(klice[i]));
            }
            data.add(radek);

            fireTableStructureChanged();
        }


        /**
         *  Gets the columnCount attribute of the MyTableModel object
         *
         *@return    The columnCount value
         */
        public int getColumnCount() {
            return sloupce.size();
        }


        /**
         *  Gets the rowCount attribute of the MyTableModel object
         *
         *@return    The rowCount value
         */
        public int getRowCount() {
            return data.size();
        }


        /**
         *  Gets the columnName attribute of the MyTableModel object
         *
         *@param  col  Description of the Parameter
         *@return      The columnName value
         */
        public String getColumnName(int col) {
            return (String) sloupce.elementAt(col);
        }


        /**
         *  Gets the valueAt attribute of the MyTableModel object
         *
         *@param  row  Description of the Parameter
         *@param  col  Description of the Parameter
         *@return      The valueAt value
         */
        public Object getValueAt(int row, int col) {
            Vector r = (Vector) data.elementAt(row);
            Object o = r.elementAt(col);
            return o;
            //return ((Vector)data.elementAt(row)).elementAt(col);
        }


        /**
         *  Gets the columnClass attribute of the MyTableModel object
         *
         *@param  c  Description of the Parameter
         *@return    The columnClass value
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }


    /**
     *  Kostruktor tridy TeorieInformace
     */
    public TeorieInformace() {
        super();
        init();
    }


    /**
     *  Incializační metoda appletu.
     */
    public void init() {
        panel = new JPanel(new BorderLayout());
        contentPane = (JPanel) this.getContentPane();
        contentPane.add(panel);

        createCenter();
        createEast();
        createSouth();
    }


    /**
     *  Vytvoří centrální část okna programu, tabulka charakteristik jednotlivých písmen.
     */
    protected void createCenter() {
        JPanel center = new JPanel(new GridLayout());
        model = new MyTableModel();
        JTable table = new JTable(model);
        
        JScrollPane sp = new JScrollPane(table);

        center.add(sp);
        center.setBorder(BorderFactory.createTitledBorder
                         ("Tabulka charackteristických hodnot"));
        panel.add(center);
    }


    /**
     *  Vytvoří východní část hlavního okna programu, hodnoty a jejich názvy.
     */
    protected void createEast() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel east = new JPanel(gridbag);
        JComponent comp;
        Border empty = BorderFactory.createEmptyBorder(1, 1, 1, 3);

        //
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        comp = new JLabel("Průměrná entropie jednotky:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblAvgEnt = new JLabel("- bitů");
        gridbag.setConstraints(lblAvgEnt, c);
        east.add(lblAvgEnt);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Entropie zprávy:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblMsgEnt = new JLabel("- bitů");
        gridbag.setConstraints(lblMsgEnt, c);
        east.add(lblMsgEnt);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Délka zprávy v ASCII kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblMsgLen = new JLabel("- bitů");
        gridbag.setConstraints(lblMsgLen, c);
        east.add(lblMsgLen);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Délka zprávy v Huffmanově kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblMsgHuffLen = new JLabel("- bitů");
        gridbag.setConstraints(lblMsgHuffLen, c);
        east.add(lblMsgHuffLen);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Redundance ASCII kódu zprávy:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblMsgRed = new JLabel("- bitů");
        gridbag.setConstraints(lblMsgRed, c);
        east.add(lblMsgRed);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Redundance Huffmanova kódu zprávy:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);

        c.gridwidth = GridBagConstraints.REMAINDER;
        lblMsgHuffRed = new JLabel("- bitů");
        gridbag.setConstraints(lblMsgHuffRed, c);
        east.add(lblMsgHuffRed);

        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Průměrná délka slova ASCII kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        lblCWordLen = new JLabel("- bitů");
        gridbag.setConstraints(lblCWordLen, c);
        east.add(lblCWordLen);
        
        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Průměrná délka slova Huffmanova kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        lblCWordHuffLen = new JLabel("- bitů");
        gridbag.setConstraints(lblCWordHuffLen, c);
        east.add(lblCWordHuffLen);
        
        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Průměrná redundance ASCII kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        lblAvgRedASCII = new JLabel("- bitů");
        gridbag.setConstraints(lblAvgRedASCII, c);
        east.add(lblAvgRedASCII);
        
        //
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        comp = new JLabel("Průměrná redundance Huffmanova kódu:");
        comp.setBorder(empty);
        gridbag.setConstraints(comp, c);
        east.add(comp);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        lblAvgRedHuff = new JLabel("- bitů");
        gridbag.setConstraints(lblAvgRedHuff, c);
        east.add(lblAvgRedHuff);
        
        east.setBorder(BorderFactory.createTitledBorder("Analýza"));
        panel.add(east, BorderLayout.EAST);
    }


    /**
     *  Vytvoří obsah jižního panelu hlavního okna programu, textové pole.
     */
    protected void createSouth() {
        JPanel south = new JPanel(new GridLayout());
        JTextArea input = new JTextArea(10, 50);
        JScrollPane sp = new JScrollPane(input);
        input.getDocument().addDocumentListener(new TextAreaListener());
        south.add(sp);
        south.setBorder(BorderFactory.createTitledBorder("Zpráva"));

        panel.add(south, BorderLayout.SOUTH);
    }


    /**
     *  Funkce main pro spousteni appletu z příkazové řádky.
     *
     *@param  argv  The command line arguments
     */
    public static void main(String[] argv) {
        JFrame frame = new JFrame("Teorie informace");
        frame.setContentPane(new TeorieInformace());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

