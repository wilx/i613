package wilx;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
    protected HuffmanTreeNode<ItemType> root;


    /**
     *  Konstruktor třídy HuffmanCodeTree.
     *
     *@param  probMap  Mapa pravděpodobností výskytu zdrojových jednotek.
     */
    public HuffmanCodeTree(final Map<ItemType, Integer> probMap) {
        final PriorityQueue<HuffmanTreeNode<ItemType>> q = new PriorityQueue<>(
            new Comparator<HuffmanTreeNode<ItemType>>() {
                @Override
                public int compare(final HuffmanTreeNode<ItemType> o1, final HuffmanTreeNode<ItemType> o2) {
                    if (o1.getWeight() < o2.getWeight()) {
                        return 1;
                    }
                    if (o1.getWeight() == o2.getWeight()) {
                        return 0;
                    }
                    else {
                        return -1;
                    }
                }
            });
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

