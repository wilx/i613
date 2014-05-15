package wilx;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

/**
 *  Třída prioritní fronty použitá při konstrukci stromu Huffmanova kódu.
 *
 *@author     Václav Haisman
 *@created    25. leden 2003
 */
public class PriorityQueue implements Serializable {
    /**
     *  Délka fronty.
     */
    protected int size;
    /**
     *  Úložistě obsahu fronty.
     */
    protected Vector buf;
    /**
     *  Komparátor objektů fronty.
     */
    protected Comparator cmp;
    /**
     *  Implicitní komparátor.
     */
    protected final static Comparator defaultCmp;
    static {
        defaultCmp =
            new Comparator() {
                public int compare(final Object o1, final Object o2) {
                    return ((Comparable) o1).compareTo(o2);
                }
            };
    }


    /**
     *  Constructor for the PriorityQueue object
     */
    public PriorityQueue() {
        cmp = defaultCmp;
        buf = new Vector();
        size = 0;
    }


    /**
     *  Constructor for the PriorityQueue object
     *
     *@param  arr  Pole objektů ze kterých bude vytvořena fronta.
     */
    public PriorityQueue(final Object[] arr) {
        cmp = defaultCmp;
        buf = new Vector(arr.length);
        size = arr.length;
        for (int i = 0; i < arr.length; ++i) {
            buf.set(i, arr[i]);
        }
        heapify(1);
    }


    /**
     *  Constructor for the PriorityQueue object
     *
     *@param  c  Uživatelský komparátor.
     */
    public PriorityQueue(final Comparator c) {
        cmp = c;
        buf = new Vector();
        size = 0;
    }


    /**
     *  Constructor for the PriorityQueue object
     *
     *@param  arr  Pole objektů ze kterých bude vytvořena fronta.
     *@param  c    Uživatelský komparátor.
     */
    public PriorityQueue(final Object[] arr, final Comparator c) {
        cmp = c;
        buf = new Vector(arr.length);
        size = arr.length;
        for (int i = 0; i < arr.length; ++i) {
            buf.set(i, arr[i]);
        }
        heapify(1);
    }


    /**
     *  Constructor for the PriorityQueue object
     *
     *@param  col  Kolekce ze které bude vytvořena fronta.
     */
    public PriorityQueue(final Collection col) {
        cmp = defaultCmp;
        buf = new Vector(col);
        size = col.size();
        heapify(1);
    }


    /**
     *  Constructor for the PriorityQueue object
     *
     *@param  col  Kolekce ze které bude vytvořena fronta.
     *@param  c    Uživatelský komparátor.
     */
    public PriorityQueue(final Collection col, final Comparator c) {
        cmp = c;
        buf = new Vector(col);
        size = col.size();
        heapify(1);
    }


    /**
     *  Vrací levého následníka v binární haldě.
     *
     *@param  i  Rodič.
     *@return    Levý následník.
     */
    protected int left(final int i) {
        return (2 * i);
    }


    /**
     *  Vrací pravého následníka v binární haldě.
     *
     *@param  i  Rodič.
     *@return    Pravý následník.
     */
    protected int right(final int i) {
        return (2 * i + 1);
    }


    /**
     *  Vrací rodiče prvku binární haldy.
     *
     *@param  i  Prvek.
     *@return    Rodič prvku.
     */
    protected int parent(final int i) {
        return (i / 2);
    }


    /**
     *  Prohodí dva prvky.
     *
     *@param  i  Prvek haldy.
     *@param  j  Prvek haldy.
     */
    protected void swap(final int i, final int j) {
        final Object tmp = buf.get(i);

        buf.set(i, buf.get(j));
        buf.set(j, tmp);
    }


    /**
     *  Kostrukce binární haldy směrem ke kořeni.
     *
     *@param  i  Prvek.
     */
    protected void up(final int i) {
        final int p = parent(i);

        if (p > 0) {
            if (cmp.compare(buf.get(i - 1), buf.get(p - 1)) > 0) {
                swap(i - 1, p - 1);
                up(p);
            }
        }
    }


    /**
     *  Konstrukce binární haldy.
     *
     *@param  i  Prvek.
     */
    protected void heapify(final int i) {
        final int l = left(i);
        final int r = right(i);
        int largest;

        if (l <= size && cmp.compare(buf.get(l - 1), buf.get(i - 1)) > 0) {
            largest = l;
        }
        else {
            largest = i;
        }
        if (r <= size && cmp.compare(buf.get(r - 1), buf.get(i - 1)) > 0
                 && cmp.compare(buf.get(r - 1), buf.get(largest - 1)) > 0) {
            largest = r;
        }
        if (largest != i) {
            swap(largest - 1, i - 1);
            heapify(largest);
        }
    }


    /**
     *  Vloží prvek do fronty.
     *
     *@param  o  Prvek.
     */
    public void put(final Object o) {
        if (buf.size() > size) {
            buf.set(size, o);
        }
        else {
            buf.add(o);
        }
        ++size;
        up(size);
    }


    /**
     *  Vybere první prvek z fronty.
     *
     *@return    Prvek.
     */
    public Object get() {
        Object ret;

        if (size > 0) {
            ret = buf.get(0);
        }
        else {
            return null;
        }

        buf.set(0, buf.get(size - 1));
        --size;
        if (size > 0) {
            heapify(1);
        }
        buf.remove(buf.size() - 1);
        return ret;
    }


    /**
     *  Predikát prázdnosti fronty.
     *
     *@return    true/false
     */
    public boolean isEmpty() {
        return size > 0 ? false : true;
    }


    /**
     *  Vrací počet prvků ve frontě.
     *
     *@return    Počet prvků.
     */
    public int size() {
        return size;
    }
}

