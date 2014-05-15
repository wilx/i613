package wilx;

/**
 *  Tato třída definuje abstraktní metody pro analyzátor kolekce.
 *
 *@author     Václav Haisman
 *@created    3. únor 2003
 */
abstract class CollectionEntropyAnalyzer extends EntropyAnalyzer {
    /**
     *  Vrací pravděpodobnost zdrojové jednotky v kolekci.
     *
     *@param  o  Description of the Parameter
     *@return    Description of the Return Value
     */
    public abstract double probability(Object o);


    /**
     *  Spočítá frekvence výskytu zdrojových jednotek.
     */
    public abstract void computeFrequencies();
}

