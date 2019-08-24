package wilx;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *  Třída reprezentující strom Huffmanova kódu.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class HuffmanCodeTree<ItemType extends Comparable<ItemType>> {
    /**
     *  Kořen stromu Huffmanova kódu.
     */
    protected final HuffmanTreeNode<ItemType> root;


    /**
     *  Konstruktor třídy HuffmanCodeTree.
     *
     *@param  probMap  Mapa pravděpodobností výskytu zdrojových jednotek.
     */
    public HuffmanCodeTree(final Map<ItemType, Integer> probMap) {
        final PriorityQueue<HuffmanTreeNode<ItemType>> q = new PriorityQueue<>(
            (o1, o2) -> Double.compare(o2.getWeight(), o1.getWeight()));
        final Set<Entry<ItemType, Integer>> entries = probMap.entrySet();
        /*
         *  inicializace prioritni frony
         */
        for (Entry<ItemType, Integer> e : entries) {
            q.put(new HuffmanTreeNode<>(e.getValue(),
                e.getKey()));
        }

        /*
         *  vytvoreni stromu
         */
        while (q.size() > 1) {
            HuffmanTreeNode<ItemType> l;
            HuffmanTreeNode<ItemType> r;
            HuffmanTreeNode<ItemType> p;
            r = q.get();
            l = q.get();
            p = new HuffmanTreeNode<>(r.getWeight() + l.getWeight(), null, l, r);
            q.put(p);
        }
        root = q.get();
    }


    /**
     *  Prochází strom reprezentující Huffmanův kód a konstruuje převodní mapu.
     *
     *@param  node  Procházený uzel stromu.
     *@param  str   Řetězec reprezentující prošlou část mapy.
     *@param  map   Převodní mapa.
     */
    protected void preorder(final HuffmanTreeNode<ItemType> node,
        final String str, final Map<ItemType, String> map) {
        if (node == null) {
            return;
        }
        if (node.getValue() != null) {
            map.put(node.getValue(), str);
        }
        if (node.getLeft() != null) {
            preorder(node.getLeft(), str + "0", map);
        }
        if (node.getRight() != null) {
            preorder(node.getRight(), str + "1", map);
        }
    }


    /**
     *  Vrací mapu mapující zdrojové jednotky na kódová slova v Huffmanově kódu.
     *
     *@return    Mapa Huffmanova kódu.
     */
    public Map<ItemType, String> getHuffmanCode() {
        final Map<ItemType, String> map = new HashMap<>();
        preorder(root, "", map);
        return map;
    }
}

