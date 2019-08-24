package wilx;

import java.util.HashMap;
import java.util.Map;

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
    protected final Map<Character, Integer> freq;


    /**
     *  Constructor for the CharArrayEntropyAnalyzer object
     *
     *@param  b  Pole zprávy.
     */
    public CharArrayEntropyAnalyzer(final char[] b) {
        buffer = b;
        freq = new HashMap<>();
        computeFrequencies();
        compute();
    }


    /**
     *  Constructor for the CharArrayEntropyAnalyzer object
     *
     *@param  b  Pole zprávy.
     *@param  f  Uživatelská mapa frekvence výskytů zdrojových jednotek.
     */
    public CharArrayEntropyAnalyzer(final char[] b, final Map<Character, Integer> f) {
        buffer = b;
        freq = f;
        compute();
    }


    /**
     *  Nastaví jiné pole se zprávou.
     *
     *@param  b  Pole zprávy.
     */
    public void setBuffer(final char[] b) {
        buffer = b;
        freq.clear();
        computeFrequencies();
        compute();
    }


    /**
     *  Vrací entropii zdrojové jednotky.
     *
     *@param  o  Zrdojová jednotka.
     *@return    Entropie zdrojové jednotky.
     */
    @Override
    public double entropy(final char o) {
        return -log2(probability(o));
    }


    /**
     *  Vrací entropii zprávy podle vnitřní tabulky frekvencí výskytů.
     *
     *@param  m  Zpráva
     *@return    Entropie zprávy.
     */
    @Override
    public double messageEntropy(final char[] m) {
        double ent = 0;
        double prob;
        for (Character aM : m) {
            prob = probability(aM);
            ent += prob * log2(prob);
        }
        return -ent;
    }


    /**
     *  Přepočítá průměrnou entropii a frekvence výskytů zdrojových jednotek.
     */
    @Override
    public void compute() {
        avgent = 0;
        for (Character ch : freq.keySet()) {
            final double prob = probability(ch);
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
    @Override
    public double probability(final char x) {
        Integer o;
        if ((o = freq.get(x)) == null) {
            return 0;
        }
        else {
            return (double) o / buffer.length;
        }
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
    public Map<Character, Integer> getFrequenciesMap() {
        return freq;
    }


    /**
     *  Spočítá frekvence zdrojových jednotek v bufferu.
     */
    @Override
    public void computeFrequencies() {
        Integer o;
        freq.clear();
        for (char aBuffer : buffer) {
            if ((o = freq.get(aBuffer)) == null) {
                freq.put(aBuffer, 1);
            } else {
                freq.put(aBuffer, o + 1);
            }
        }
    }
}

