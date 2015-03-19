package cache;

public interface LruCache<T, V> {

	V get(T key);
	void put(T key, V value);
	int getMaxSize();
}
