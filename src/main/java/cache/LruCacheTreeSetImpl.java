package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 
 * @author bfach
 * 
 *         This implementation provides a LRU Cache using a java comparator,
 *         timestamps and a treeset to provide the item that is up for evict
 *         based on oldest timestamp. This solution is however less efficient as
 *         the treeset is constructed for every PUT if the cache is at capcity
 *         to determine what item is up for eviction
 * 
 * @param <T>
 *            - The datatype for key of cache
 * @param <V>
 *            - The datatype for value of cache
 */
public class LruCacheTreeSetImpl<T, V> implements LruCache<T, V> {

	private final int maxSize;
	private Map<T, Value<T, V>> map = new HashMap<T, Value<T, V>>();

	public LruCacheTreeSetImpl(int maxSize) {
		if (maxSize < 1) {
			throw new IllegalArgumentException(
					"Maximum size of Cache Must be greater than zero");
		}

		this.maxSize = maxSize;
	}

	/**
	 * Returns element in cache by key if it exists.
	 * 
	 * @return Element of type V if it exists in cache. null if not
	 */
	public V get(T key) {
		Value<T, V> value = map.get(key);

		if (value == null) {
			return null;
		}

		// update timestamp of last accessed
		value.updateTs();
		return value.content;
	}

	/**
	 * @param T
	 *            - key of cache
	 * @param V
	 *            - value to cache
	 */
	public void put(T key, V value) {

		// if this addition will cause the map to be larger than maxsize
		if (map.size() + 1 > maxSize) {
			// evict the oldest version of the cache
			final TreeSet<Value<T, V>> set = new TreeSet<Value<T, V>>(
					map.values());
			final Value<T, V> evicted = set.pollLast();

			System.out.println("Evicting cache of key " + evicted.key);
			map.remove(evicted.key);
		}

		// add the value to the map, which updates the ts for sorting
		map.put(key, new Value<T, V>(key, value));
	}

	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * 
	 * @author facher1
	 *
	 * @param <X> - The datatype for key
	 * @param <Y> - The datatype for value
	 */
	class Value<X, Y> implements Comparable<Value<X, Y>> {

		Long ts;
		X key;
		Y content;

		public Value(X key, Y value) {
			this.key = key;
			this.content = value;
			updateTs();
		}

		/**
		 * This naturally orders the nodes in ascending order (thus oldest at end)
		 */
		public int compareTo(Value<X, Y> o) {
			return o.ts.compareTo(ts);
		}

		public void updateTs() {
			ts = System.nanoTime();
		}

	}

}
