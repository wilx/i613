/**
 *  Třída reprezentující uzel v stromě Huffmanova kódu.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class HuffmanTreeNode {


    /**
     *  Váha uzlu.
     */
    protected double weight;
    /**
     *  Obsah uzlu.
     */
    protected Object obj;
    /**
     *  Levý podstrom.
     */
    protected HuffmanTreeNode left;
    /**
     *  Pravý podstrom.
     */
    protected HuffmanTreeNode right;


    /**
     *  Constructor for the HuffmanTreeNode object
     *
     *@param  w  Váha uzlu.
     *@param  o  Obsah uzlu.
     *@param  l  Levý podstrom.
     *@param  r  Pravý podstrom.
     */
    public HuffmanTreeNode(double w, Object o, HuffmanTreeNode l, HuffmanTreeNode r) {
        weight = w;
        left = l;
        right = r;
        obj = o;
    }


    /**
     *  Constructor for the HuffmanTreeNode object
     *
     *@param  w  Váha uzlu.
     *@param  o  Obsah uzlu.
     *@param  l  Levý podstrom.
     *@param  r  Pravý podstrom.
     */
    public HuffmanTreeNode(Double w, Object o, HuffmanTreeNode l, HuffmanTreeNode r) {
        this(w.doubleValue(), o, l, r);
    }


    /**
     *  Constructor for the HuffmanTreeNode object
     *
     *@param  w  Váha uzlu.
     *@param  o  Obsah uzlu.
     */
    public HuffmanTreeNode(double w, Object o) {
        weight = w;
        left = right = null;
        obj = o;
    }


    /**
     *  Constructor for the HuffmanTreeNode object
     *
     *@param  w  Váha uzlu.
     *@param  o  Obsah uzlu.
     */
    public HuffmanTreeNode(Double w, Object o) {
        this(w.doubleValue(), o);
    }


    /**
     *  Vrací váhu uzlu.
     *
     *@return    Váha uzlu.
     */
    public double getWeight() {
        return weight;
    }


    /**
     *  Vrací levý podstrom.
     *
     *@return    Levý podstrom.
     */
    public HuffmanTreeNode getLeft() {
        return left;
    }


    /**
     *  Vrací pravý podstrom.
     *
     *@return    Pravý podstrom.
     */
    public HuffmanTreeNode getRight() {
        return right;
    }


    /**
     *  Vrací obsah uzlu.
     *
     *@return    Obsah uzlu.
     */
    public Object getValue() {
        return obj;
    }
}

