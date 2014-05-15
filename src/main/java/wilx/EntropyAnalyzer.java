package wilx;

/**
 *  Trída definující interface analyzátoru entropie.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public abstract class EntropyAnalyzer {
    /**
     *  Průměrná entropie.
     */
    protected double avgent;


    /**
     *  Vrací entripii zdrojové jednotky.
     *
     *@param  o  Zdrojová jednotka.
     *@return    Entropie.
     */
    public abstract double entropy(Object o);


    /**
     *  Vrací entropii zprávy složené z pole zdrojových jednotek.
     *
     *@param  o  Zpráva.
     *@return    Entropie zprávy.
     */
    public abstract double messageEntropy(Object[] o);


    /**
     *  Provede přepočítání hodnot průměrné entropie a frekvence výskitů zdrojových jednotek.
     */
    public abstract void compute();


    /**
     *  Vrací průměrnou entropii.
     *
     *@return    Průměrná entropie.
     */
    public double averageEntropy() {
        return avgent;
    }


    /**
     *  Vrací logarimus x o základu 2.
     *
     *@param  x  Hodnota x.
     *@return    Logaritmus x o základu 2.
     */
    public static double log2(final double x) {
        final double l = Math.log(x) / Math.log(2);
        return l == -0.0 ? 0.0 : l;
    }
}

