import java.util.*;

/**
 *  Třída reprezentující analyzátor entropie pole charů.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class CharArrayEntropyAnalyzer extends CollectionEntropyAnalyzer {
    /**
     *  Reference na pole zprávy.
     */
    protected char[] buffer;
    /**
     *  Mapa frekvencí výskytů zdrojových jednotek.
     */
    protected Map freq;


    /**
     *  Constructor for the CharArrayEntropyAnalyzer object
     *
     *@param  b  Pole zprávy.
     */
    public CharArrayEntropyAnalyzer(char[] b) {
        buffer = b;
        freq = new HashMap();
        this.computeFrequencies();
        this.compute();
    }


    /**
     *  Constructor for the CharArrayEntropyAnalyzer object
     *
     *@param  b  Pole zprávy.
     *@param  f  Uživatelská mapa frekvence výskytů zdrojových jednotek.
     */
    public CharArrayEntropyAnalyzer(char[] b, Map f) {
        buffer = b;
        freq = f;
        this.compute();
    }


    /**
     *  Nastaví jiné pole se zprávou.
     *
     *@param  b  Pole zprávy.
     */
    public void setBuffer(char[] b) {
        buffer = b;
        freq.clear();
        this.computeFrequencies();
        this.compute();
    }


    /**
     *  Vrací entropii zdrojové jednotky.
     *
     *@param  o  Zrdojová jednotka.
     *@return    Entropie zdrojové jednotky.
     */
    public double entropy(Object o) {
        return -log2(probability(o));
    }


    /**
     *  Vrací entropii zprávy podle vnitřní tabulky frekvencí výskytů.
     *
     *@param  m  Zpráva
     *@return    Entropie zprávy.
     */
    public double messageEntropy(Object[] m) {
        double ent = 0;
        double prob;
        for (int i = 0; i < m.length; ++i) {
            prob = probability(m[i]);
            ent += prob * log2(prob);
        }
        return -ent;
    }


    /**
     *  Přepočítá průměrnou entropii a frekvence výskytů zdrojových jednotek.
     */
    public void compute() {
        Object o;
        avgent = 0;
        Iterator it = freq.keySet().iterator();
        while (it.hasNext()) {
            Character ch = (Character) it.next();
            double prob = probability(ch);
            avgent += prob * log2(prob);
        }
        avgent = -avgent;
    }


    /**
     *  Vrací pravděpodobnost zdrojové jednotky.
     *
     *@param  x  Zdrojová jednotka.
     *@return    Pravděpodobnost.
     */
    public double probability(Object x) {
        Object o;
        if ((o = freq.get(x)) == null) {
            return 0;
        }
        else {
            return (double) ((Integer) o).intValue() / buffer.length;
        }
    }


    /**
     *  Vrací pravděpodobnost zdrojové jednotky.
     *
     *@param  ch  Zdrojová jednotka.
     *@return     Pravděpodobnost.
     */
    public double probability(char ch) {
        return probability(new Character(ch));
    }


    /**
     *  Vrací velikost bufferu.
     *
     *@return    Velikost bufferu.
     */
    public int getBufferSize() {
        return buffer.length;
    }


    /**
     *  Vrací mapu frekvencí zdrojových jednotek.
     *
     *@return    Mapa frekvencí zdrojových jednotek.
     */
    public Map getFrequenciesMap() {
        return freq;
    }


    /**
     *  Spočítá frekvence zdrojových jednotek v bufferu.
     */
    public void computeFrequencies() {
        Object o;
        freq.clear();
        for (int i = 0; i < buffer.length; ++i) {
            if ((o = freq.get(new Character(buffer[i]))) == null) {
                freq.put(new Character(buffer[i]), new Integer(1));
            }
            else {
                freq.put(new Character(buffer[i]),
                        new Integer(((Integer) o).intValue() + 1));
            }
        }
    }
}

