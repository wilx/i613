import java.util.*;

/**
 *  Třída reprezentující strom Huffmanova kódu.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class HuffmanCodeTree {
    /**
     *  Kořen stromu Huffmanova kódu.
     */
    protected HuffmanTreeNode root;


    /**
     *  Konstruktor třídy HuffmanCodeTree.
     *
     *@param  probMap  Mapa pravděpodobností výskytu zdrojových jednotek.
     */
    public HuffmanCodeTree(Map probMap) {
        PriorityQueue q = new PriorityQueue(
            new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((HuffmanTreeNode) o1).getWeight()
                             < ((HuffmanTreeNode) o2).getWeight()) {
                        return 1;
                    }
                    if (((HuffmanTreeNode) o1).getWeight()
                             == ((HuffmanTreeNode) o2).getWeight()) {
                        return 0;
                    }
                    else {
                        return -1;
                    }
                }
            });
        Set entries = probMap.entrySet();
        Iterator i = entries.iterator();
        Object o;

        /*
         *  inicializace prioritni frony
         */
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            q.put(new HuffmanTreeNode(new Double((double) ((Integer) e.getValue()).intValue()),
                    e.getKey()));
        }

        /*
         *  vytvoreni stromu
         */
        while (q.size() > 1) {
            HuffmanTreeNode l;
            HuffmanTreeNode r;
            HuffmanTreeNode p;
            r = (HuffmanTreeNode) q.get();
            l = (HuffmanTreeNode) q.get();
            p = new HuffmanTreeNode(r.getWeight() + l.getWeight(), null, l, r);
            q.put(p);
        }
        root = (HuffmanTreeNode) q.get();
    }


    /**
     *  Prochází strom reprezentující Huffmanův kód a konstruuje převodní mapu.
     *
     *@param  node  Procházený uzel stromu.
     *@param  str   Řetězec reprezentující prošlou část mapy.
     *@param  map   Převodní mapa.
     */
    protected void preorder(HuffmanTreeNode node, String str, Map map) {
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
    public Map getHuffmanCode() {
        Map map = new HashMap();
        preorder(root, "", map);
        return map;
    }
}

