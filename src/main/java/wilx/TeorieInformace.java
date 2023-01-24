package wilx;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.Serial;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Hlavní třída appletu Teorie informace.
 *
 * @author Václav Haisman
 * @created 25. leden 2003
 */
public class TeorieInformace extends JApplet {

    @Serial
    private static final long serialVersionUID = -6839232509270632651L;

    protected JPanel contentPane;

    protected JPanel panel;

    protected JLabel lblAvgEnt, lblMsgEnt, lblMsgLen, lblMsgHuffLen, lblMsgRed, lblMsgHuffRed, lblCWordLen,
            lblCWordHuffLen, lblAvgRedASCII, lblAvgRedHuff;

    protected CharArrayEntropyAnalyzer ea;

    protected MyTableModel model;

    private static final DecimalFormat df = new DecimalFormat("###.###");

    /**
     * Třída implementující DocumentListener pro vstupní pole.
     *
     * @author Václav Haisman
     * @created 25. leden 2003
     */
    class TextAreaListener implements DocumentListener {

        /**
         * Description of the Method
         *
         * @param e
         *          Description of the Parameter
         */
        @Override
        public void changedUpdate(final DocumentEvent e) {
            handler(e);
        }

        /**
         * Description of the Method
         *
         * @param e
         *          Description of the Parameter
         */
        @Override
        public void insertUpdate(final DocumentEvent e) {
            handler(e);
        }

        /**
         * Description of the Method
         *
         * @param e
         *          Description of the Parameter
         */
        @Override
        public void removeUpdate(final DocumentEvent e) {
            handler(e);
        }

        /**
         * Tato metoda vezme text ze vstupního textového pole a posle ho modelu
         * tabulky. Také provede přepočítání charakteristických hodnot.
         *
         * @param e
         *          DocumentEvent
         */
        private void handler(final DocumentEvent e) {
            int i;
            final Document doc = e.getDocument();
            String text;
            try {
                text = doc.getText(0, doc.getLength());
            } catch (final BadLocationException ex) {
                System.err.println("Exceprion: " + ex);
                return;
            }
            // update tabulky
            final char[] znaky = text.toUpperCase().toCharArray();
            Arrays.sort(znaky);
            ea = new CharArrayEntropyAnalyzer(znaky);
            model.update(znaky);
            // update ostatnich hodnot
            // prumenrna entropie
            lblAvgEnt.setText(df.format(ea.averageEntropy() == -0.0 ? 0.0 : ea.averageEntropy()) + " bitů");
            final char[] msg = text.toUpperCase().toCharArray();
            double msgEnt = ea.messageEntropy(msg);
            if (msgEnt == -0.0) {
                msgEnt = 0.0;
            }
            // entropie zpravy
            lblMsgEnt.setText(df.format(msgEnt) + " bitů");
            // delka zpravy
            lblMsgLen.setText((doc.getLength() * 8) + " bitů");
            int len = 0;
            final HuffmanCodeTree<Character> huff = new HuffmanCodeTree<>(ea.getFrequenciesMap());
            final Map<Character, String> huffkod = huff.getHuffmanCode();
            for (i = 0; i < msg.length; ++i) {
                final String s = huffkod.get(msg[i]);
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
            final Character[] klice = huffkod.keySet().toArray(new Character[huffkod.keySet().size()]);
            for (i = 0; i < klice.length; ++i) {
                avgwordlen += 8 * ea.probability(klice[i]);
            }
            lblCWordLen.setText(df.format(avgwordlen) + " bitů");
            // prumerna delka slova Huffmanova kodu zpravy
            double avgwordhufflen = 0;
            for (i = 0; i < klice.length; ++i) {
                avgwordhufflen += huffkod.get(klice[i]).length() * ea.probability(klice[i]);
            }
            lblCWordHuffLen.setText(df.format(avgwordhufflen));
            // prumerna redundance ASCII kodu
            lblAvgRedASCII.setText(df.format(avgwordlen - ea.averageEntropy()));
            // prumerna redundance Huffmanova kodu
            lblAvgRedHuff.setText(df.format(avgwordhufflen - ea.averageEntropy()));
        }
    }

    /**
     * Tato trida implementuje model tabulky zobrazující charakteristiké hodnoty
     * jednotlivých zdrojových jednotek.
     *
     * @author wilx
     * @created 25. leden 2003
     */
    class MyTableModel extends AbstractTableModel {

        @Serial
        private static final long serialVersionUID = 6050182162198136481L;

        List<String> columns = new ArrayList<>();

        List<List<String>> data = new ArrayList<>();

        /**
         * Constructor for the MyTableModel object
         */
        public MyTableModel() {
            super();
            columns.add("Default");
        }

        /**
         * Metoda update provádí prepočítání a zobrazení charakteristických hodnot
         * zdrojových jednotek z předaného bufferu.
         *
         * @param characters
         *          Buffer analyzované zprávy.
         */
        public void update(final char[] characters) {
            int i;

            data = new ArrayList<>();
            columns = new ArrayList<>();
            final Map<Character, Integer> freq = ea.getFrequenciesMap();
            final Character[] klice = freq.keySet().toArray(new Character[freq.keySet().size()]);
            Arrays.sort(klice);
            // zahlavi
            columns.add("Názvy");
            for (i = 0; i < klice.length; ++i) {
                columns.add(klice[i].toString());
            }
            // radek pravdepodobnosti
            List<String> row = new ArrayList<>();
            row.add("Pravděpodobnost");
            for (i = 0; i < klice.length; ++i) {
                final double p = ea.probability(klice[i]);
                row.add(df.format(p));
            }
            data.add(row);
            // radek entropie
            row = new ArrayList<>();
            row.add("Entropie");
            for (i = 0; i < klice.length; ++i) {
                double e = ea.entropy(klice[i]);
                e = e == -0.0 ? 0.0 : e;
                row.add(df.format(e));
            }
            data.add(row);
            // radek delky Huffmanova kodu pro dany znak
            row = new ArrayList<>();
            row.add("Délka Huffmanova kódu");
            final HuffmanCodeTree<Character> huff = new HuffmanCodeTree<>(ea.getFrequenciesMap());
            final Map<Character, String> huffkod = huff.getHuffmanCode();
            for (i = 0; i < klice.length; ++i) {
                final String s = huffkod.get(klice[i]);
                row.add(Integer.toString(s.length()));
            }
            data.add(row);
            // radek vlastniho Huffmanova kodu pro dany znak
            row = new ArrayList<>();
            row.add("Huffmanův kód");
            for (i = 0; i < klice.length; ++i) {
                row.add(huffkod.get(klice[i]));
            }
            data.add(row);

            fireTableStructureChanged();
        }

        /**
         * Gets the columnCount attribute of the MyTableModel object
         *
         * @return The columnCount value
         */
        @Override
        public int getColumnCount() {
            return columns.size();
        }

        /**
         * Gets the rowCount attribute of the MyTableModel object
         *
         * @return The rowCount value
         */
        @Override
        public int getRowCount() {
            return data.size();
        }

        /**
         * Gets the columnName attribute of the MyTableModel object
         *
         * @param col
         *          Description of the Parameter
         * @return The columnName value
         */
        @Override
        public String getColumnName(final int col) {
            return columns.get(col);
        }

        /**
         * Gets the valueAt attribute of the MyTableModel object
         *
         * @param row
         *          Description of the Parameter
         * @param col
         *          Description of the Parameter
         * @return The valueAt value
         */
        @Override
        public Object getValueAt(final int row, final int col) {
            final List<String> r = data.get(row);
            return r.get(col);
        }

        /**
         * Gets the columnClass attribute of the MyTableModel object
         *
         * @param c
         *          Description of the Parameter
         * @return The columnClass value
         */
        @Override
        public Class<?> getColumnClass(final int c) {
            return getValueAt(0, c).getClass();
        }
    }

    /**
     * Kostruktor tridy TeorieInformace
     */
    public TeorieInformace() {
        super();
        init();
    }

    /**
     * Incializační metoda appletu.
     */
    @Override
    public void init() {
        panel = new JPanel(new BorderLayout());
        contentPane = (JPanel) getContentPane();
        contentPane.add(panel);

        createCenter();
        createEast();
        createSouth();
    }

    /**
     * Vytvoří centrální část okna programu, tabulka charakteristik jednotlivých
     * písmen.
     */
    protected void createCenter() {
        final JPanel center = new JPanel(new GridLayout());
        model = new MyTableModel();
        final JTable table = new JTable(model);

        final JScrollPane sp = new JScrollPane(table);

        center.add(sp);
        center.setBorder(BorderFactory.createTitledBorder("Tabulka charackteristických hodnot"));
        panel.add(center);
    }

    /**
     * Vytvoří východní část hlavního okna programu, hodnoty a jejich názvy.
     */
    protected void createEast() {
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel east = new JPanel(gridbag);
        JComponent comp;
        final Border empty = BorderFactory.createEmptyBorder(1, 1, 1, 3);

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
     * Vytvoří obsah jižního panelu hlavního okna programu, textové pole.
     */
    protected void createSouth() {
        final JPanel south = new JPanel(new GridLayout());
        final JTextArea input = new JTextArea(10, 50);
        final JScrollPane sp = new JScrollPane(input);
        input.getDocument().addDocumentListener(new TextAreaListener());
        south.add(sp);
        south.setBorder(BorderFactory.createTitledBorder("Zpráva"));

        panel.add(south, BorderLayout.SOUTH);
    }

    /**
     * Funkce main pro spousteni appletu z příkazové řádky.
     *
     * @param argv
     *          The command line arguments
     */
    public static void main(final String[] argv)
            throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        final JFrame frame = new JFrame("Teorie informace");
        frame.setContentPane(new TeorieInformace());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
