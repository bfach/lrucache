package cache;

import static org.junit.Assert.*;

import org.junit.Test;

public class LruCacheDoubleLinkedListImplTest {

	/**
	 * Tests an exception is thrown if maximum size is zero
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testZeroSize() {
		new LruCacheDoubleLinkedListImpl<String, String>(0);
	}

	/**
	 * Tests an exception is thrown if maximum size is lower than zero
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegSize() {
		new LruCacheDoubleLinkedListImpl<String, String>(-1);
	}

	/**
	 * Tests that items are auto evicted as new items are added to the cache
	 */
	@Test
	public void testAddPastCapacity() {
		LruCacheDoubleLinkedListImpl<String, String> cache = new LruCacheDoubleLinkedListImpl<String, String>(2);
		cache.put("a", "val1");
		cache.put("b", "val2");
		cache.put("c", "val3");
		cache.put("d", "val4");

		assertNull(cache.get("a"));
		assertNull(cache.get("b"));
		assertEquals("val3", (cache.get("c")));
		assertEquals("val4", (cache.get("d")));
	}

	/**
	 * Tests that items are auto evicted as new items are added to the cache,
	 * and that retrieving an item slated for eviction causes the item to be
	 * given a new lease
	 */
	@Test
	public void testAddPastCapacityWithGet() {
		LruCacheDoubleLinkedListImpl<String, String> cache = new LruCacheDoubleLinkedListImpl<String, String>(3);
		cache.put("a", "val1");
		cache.put("b", "val2");
		cache.put("c", "val3");
		// cause a to be accessed more recently, thus bringing it back to the
		// top as far as eviction is concerned
		cache.get("a");
		cache.put("d", "val4");

		assertNull(cache.get("b"));
		assertNotNull(cache.get("a"));
		assertNotNull(cache.get("c"));
		assertNotNull(cache.get("d"));
	}

}
