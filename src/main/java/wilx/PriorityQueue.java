package wilx;

import java.io.Serial;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Třída prioritní fronty použitá při konstrukci stromu Huffmanova kódu.
 *
 * @author Václav Haisman
 * @created 25. leden 2003
 */
public class PriorityQueue<ItemType> extends AbstractQueue<ItemType> {

	@Serial
	private static final long serialVersionUID = 4003912131969360871L;

	/**
	 * Délka fronty.
	 */
	protected int size;

	/**
	 * Úložistě obsahu fronty.
	 */
	protected final ArrayList<ItemType> buf;

	/**
	 * Komparátor objektů fronty.
	 */
	protected final Comparator<? super ItemType> cmp;

	/**
	 * Constructor for the PriorityQueue object
	 */
	public PriorityQueue() {
		this((Comparator<? super ItemType>) Comparator.naturalOrder());
	}

	/**
	 * Constructor for the PriorityQueue object
	 *
	 * @param arr
	 *          Pole objektů ze kterých bude vytvořena fronta.
	 */
	public <T extends ItemType> PriorityQueue(final T[] arr) {
		cmp = (Comparator<? super ItemType>) Comparator.naturalOrder();
		buf = new ArrayList<>(Arrays.asList(arr));
		size = arr.length;
		heapify(1);
	}

	/**
	 * Constructor for the PriorityQueue object
	 *
	 * @param c
	 *          Uživatelský komparátor.
	 */
	public PriorityQueue(final Comparator<? super ItemType> c) {
		cmp = c;
		buf = new ArrayList<>();
		size = 0;
	}

	/**
	 * Constructor for the PriorityQueue object
	 *
	 * @param arr
	 *          Pole objektů ze kterých bude vytvořena fronta.
	 * @param c
	 *          Uživatelský komparátor.
	 */
	public PriorityQueue(final ItemType[] arr, final Comparator<ItemType> c) {
		cmp = c;
		buf = new ArrayList<>(Arrays.asList(arr));
		size = arr.length;
		heapify(1);
	}

	/**
	 * Constructor for the PriorityQueue object
	 *
	 * @param col
	 *          Kolekce ze které bude vytvořena fronta.
	 */
	public <T extends ItemType> PriorityQueue(final Collection<T> col) {
		cmp = (Comparator<? super ItemType>) Comparator.naturalOrder();
		buf = new ArrayList<>(col);
		size = col.size();
		heapify(1);
	}

	/**
	 * Constructor for the PriorityQueue object
	 *
	 * @param col
	 *          Kolekce ze které bude vytvořena fronta.
	 * @param c
	 *          Uživatelský komparátor.
	 */
	public <T extends ItemType> PriorityQueue(final Collection<T> col, final Comparator<? super ItemType> c) {
		cmp = c;
		buf = new ArrayList<>(col);
		size = col.size();
		heapify(1);
	}

	/**
	 * Vrací levého následníka v binární haldě.
	 *
	 * @param i
	 *          Rodič.
	 * @return Levý následník.
	 */
	protected int left(final int i) {
		return (2 * i);
	}

	/**
	 * Vrací pravého následníka v binární haldě.
	 *
	 * @param i
	 *          Rodič.
	 * @return Pravý následník.
	 */
	protected static int right(final int i) {
		return (2 * i + 1);
	}

	/**
	 * Vrací rodiče prvku binární haldy.
	 *
	 * @param i
	 *          Prvek.
	 * @return Rodič prvku.
	 */
	protected static int parent(final int i) {
		return (i / 2);
	}

	/**
	 * Prohodí dva prvky.
	 *
	 * @param i
	 *          Prvek haldy.
	 * @param j
	 *          Prvek haldy.
	 */
	protected void swap(final int i, final int j) {
		Collections.swap(buf, i, j);
	}

	/**
	 * Kostrukce binární haldy směrem ke kořeni.
	 *
	 * @param i
	 *          Prvek.
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
	 * Konstrukce binární haldy.
	 *
	 * @param i
	 *          Prvek.
	 */
	protected void heapify(final int i) {
		final int l = left(i);
		final int r = right(i);
		int largest;

		if (l <= size && cmp.compare(buf.get(l - 1), buf.get(i - 1)) > 0) {
			largest = l;
		} else {
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
	 * Vloží prvek do fronty.
	 *
	 * @param o
	 *          Prvek.
	 */
	public void put(final ItemType o) {
		if (buf.size() > size) {
			buf.set(size, o);
		} else {
			buf.add(o);
		}
		++size;
		up(size);
	}

	/**
	 * Vybere první prvek z fronty.
	 *
	 * @return Prvek.
	 */
	public ItemType get() {
		ItemType ret;

		if (size > 0) {
			ret = buf.get(0);
		} else {
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
	 * Predikát prázdnosti fronty.
	 *
	 * @return true/false
	 */
	@Override
	public boolean isEmpty() {
		return size <= 0;
	}

	/**
	 * Vrací počet prvků ve frontě.
	 *
	 * @return Počet prvků.
	 */
	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<ItemType> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean offer(ItemType item) {
		put(item);
		return true;
	}

	@Override
	public ItemType poll() {
		return this.get();
	}

	@Override
	public ItemType peek() {
		ItemType ret = null;
		if (size > 0) {
			ret = buf.get(0);
		}
		return ret;
	}
}
