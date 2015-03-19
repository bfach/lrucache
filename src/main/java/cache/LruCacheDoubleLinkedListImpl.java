package cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author bfach
 * 
 *         Implementation of a Least Recently Used cache using a hashmap and
 *         double linked list. Using the two references, one can determine the
 *         most current nodes in the list
 * 
 * @param <T>
 *            - The key for the cache
 * @param <V>
 *            - The value of the cache
 */
public class LruCacheDoubleLinkedListImpl<T, V> implements LruCache<T, V> {

	private Node<T, V> head;
	private Node<T, V> tail;
	private Map<T, Node<T, V>> nodeMap = new HashMap<>();
	private final int maxSize;

	public LruCacheDoubleLinkedListImpl(int maxSize) {
		if (maxSize < 1) {
			throw new IllegalArgumentException(
					"Max size of cache must be greater than zero");
		}

		// Set the max size of the cache
		this.maxSize = maxSize;
	}

	public V get(T key) {

		// check if map contains the node
		if (nodeMap.containsKey(key)) {
			Node<T, V> node = shiftNodeToHead(key);
			return node.value;
		}

		// not found
		return null;
	}

	/**
	 * This method removes the node from its intermediate position and makes it
	 * the new head of the double linked list
	 * 
	 * @param key
	 *            The key of the node to shift to head
	 */
	private Node<T, V> shiftNodeToHead(T key) {
		Node<T, V> node = nodeMap.get(key);
		System.out.println("Shifting node in cache for key " + node.key);
		// remove from node from the double linked list, as it will be added
		// back at head
		deleteNode(node);
		// set to head
		makeHead(node);
		return node;
	}

	private void makeHead(Node<T, V> node) {
		System.out.println("Making node in cache HEAD, for key " + node.key);
		// make current head the post in this node
		node.post = head;
		// head means nothing before
		node.pre = null;
		// set old head pre to new head (node) if not null
		if (head != null) {
			head.pre = node;
		}

		head = node;

		// if tail is undefined, then set tail same as head
		if (tail == null) {
			tail = node;
		}
	}

	public void put(T key, V value) {

		// if the map already contains the info, need to remove the old node and
		// make new one head
		if (nodeMap.containsKey(key)) {
			// no need to delete the node, since the only thing changing is
			// possibly the value
			Node<T, V> old = shiftNodeToHead(key);
			//set value of node to new value, done
			old.value = value;
			return;
		}

		// if here the item is new
		// if insertion will cause the map to grow
		if (nodeMap.size() + 1 > maxSize) {
			// remove the tail node
			nodeMap.remove(tail.key);
			System.out.println("Evicting cache of key " + tail.key);
			// set new tail, even if null
			tail = tail.pre;
		}

		// add the new node to the map and list
		Node<T, V> node = new Node<T, V>(key, value);
		nodeMap.put(key, node);
		makeHead(node);
	}

	/**
	 * Deletes the node from the double linked list
	 * 
	 * @param node
	 */
	private void deleteNode(Node<T, V> node) {
		System.out.println("Deleting node within cache for key " + node.key);
		// If not head, need to connect the nodes
		if (node.pre != null) {
			node.pre.post = node.post;
		}
		// pre is null, thus next is head
		else {
			head = node.post;
		}
		// if not the tail, need to connect the nodes
		if (node.post != null) {
			// have the post point to the one before current
			node.post.pre = node.pre;
		} else {
			tail = node.pre;
		}
	}

	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * 
	 * @author bfach
	 * 
	 * @param <X>
	 *            - The key for the node
	 * @param <Y>
	 *            - The value for the node
	 */
	class Node<X, Y> {

		Node<X, Y> pre;
		Node<X, Y> post;
		X key;
		Y value;

		public Node(X key, Y value) {
			this.key = key;
			this.value = value;
		}

	}

}
