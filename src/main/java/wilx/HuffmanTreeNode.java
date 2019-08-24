package wilx;

/**
 *  Třída reprezentující uzel v stromě Huffmanova kódu.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class HuffmanTreeNode<ItemType extends Comparable<ItemType>> {


    /**
     *  Váha uzlu.
     */
    protected final double weight;
    /**
     *  Obsah uzlu.
     */
    protected final ItemType obj;
    /**
     *  Levý podstrom.
     */
    protected final HuffmanTreeNode<ItemType> left;
    /**
     *  Pravý podstrom.
     */
    protected final HuffmanTreeNode<ItemType> right;


    /**
     *  Constructor for the HuffmanTreeNode object
     *
     *@param  w  Váha uzlu.
     *@param  o  Obsah uzlu.
     *@param  l  Levý podstrom.
     *@param  r  Pravý podstrom.
     */
    public HuffmanTreeNode(final double w, final ItemType o,
        final HuffmanTreeNode<ItemType> l, final HuffmanTreeNode<ItemType> r) {
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
     */
    public HuffmanTreeNode(final double w, final ItemType o) {
        weight = w;
        left = right = null;
        obj = o;
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
    public HuffmanTreeNode<ItemType> getLeft() {
        return left;
    }


    /**
     *  Vrací pravý podstrom.
     *
     *@return    Pravý podstrom.
     */
    public HuffmanTreeNode<ItemType> getRight() {
        return right;
    }


    /**
     *  Vrací obsah uzlu.
     *
     *@return    Obsah uzlu.
     */
    public ItemType getValue() {
        return obj;
    }
}

