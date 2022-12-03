
/**
 * HashTable.java
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */
import java.util.ArrayList;

public class HashTable<T> {

	private int numElements;
	private ArrayList<List<T>> Table;

	/**
	 * Constructor for the hash table. Initializes the Table to be sized according
	 * to value passed in as a parameter Inserts size empty Lists into the table.
	 * Sets numElements to 0
	 * 
	 * @param size the table size
	 */
	public HashTable(int size) {
		this.numElements = 0;
		Table = new ArrayList<List<T>>(size);
		for (int i = 0; i < size; i++) {
			Table.add(new List<T>());
		}
	}

	/** Accessors */

	/**
	 * returns the hash value in the Table for a given key
	 * 
	 * @param key the key
	 * @return the index in the Table
	 */
	private int hash(String key) {
		int code = 0;
		for (int i = 0; i < key.length(); i++) {
			code += (int) key.charAt(i);
		}
		return (code % Table.size());
	}

	/**
	 * counts the number of keys at this index
	 * 
	 * @param index the index in the Table
	 * @precondition 0 <= index < Table.length
	 * @return the count of keys at this index
	 * @throws IndexOutOfBoundsException
	 */
	public int countBucket(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= Table.size()) {
			throw new IndexOutOfBoundsException("countBucket(): index must be within 0 <= index < Table.length");
		}
		return Table.get(index).getLength();
	}

	/**
	 * returns total number of keys in the Table
	 * 
	 * @return total number of keys
	 */
	public int getNumElements() {
		return numElements;
	}

	/**
	 * Accesses a specified key in the Table
	 * 
	 * @param t   the object to search for
	 * @param key the key of the object
	 * @return the value to which the specified key is mapped, or null if this table
	 *         contains no mapping for the key.
	 * @precondition t != null
	 * @throws NullPointerException if the specified key is null
	 */
	public T get(T t, String key) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("get(): a key cannot be null");
		}
		int bucket = hash(key);
		List<T> temp = Table.get(bucket);
		int index = temp.linearSearch(t);
		if (index == -1) {
			return null;
		}
		temp.iteratorToIndex(index);
		return temp.getIterator();
	}

	/**
	 * Accesses several objects in the Table based on a key and their hashCode
	 * values.
	 * 
	 * @param t   the object to search for
	 * @param key the key of the object
	 * @return list of all objects found at the given key whose hashCode values
	 *         match that of the given object
	 * @precondition t != null
	 * @throws NullPointerException if the specified object is null
	 */
	public List<T> find(T t, String key) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("find(): a key cannot be null");
		}
		int bucket = hash(key);
		List<T> temp = Table.get(bucket);
		List<T> found = new List<T>();
		temp.placeIterator();
		for (int i = 0; i < temp.getLength(); i++) {
			if (t.hashCode() == temp.getIterator().hashCode()) {
				found.addLast(temp.getIterator());
			}
			temp.advanceIterator();
		}
		return found;
	}

	/**
	 * Determines whether a specified key is in the Table
	 * 
	 * @param t   the object to search for
	 * @param key the key of the object
	 * @return whether the key is in the Table
	 * @precondition t != null
	 * @throws NullPointerException if the specified key is null
	 */
	public boolean contains(T t, String key) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("contains(): a key cannot be null");
		}
		int bucket = hash(key);
		int index = Table.get(bucket).linearSearch(t);
		if (index == -1) {
			return false;
		}
		return true;
	}

	/** Mutators */

	/**
	 * Inserts a new element in the Table at the end of the chain in the bucket to
	 * which the key is mapped
	 * 
	 * @param t   the object to insert
	 * @param key the key of the object
	 * @precondition t != null
	 * @throws NullPointerException for a null key
	 */
	public void put(T t, String key) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("put(): Cannot put a null into the hash table.");
		}
		int bucket = hash(key);
		Table.get(bucket).addLast(t);
		numElements++;
	}

	/**
	 * removes the key t from the Table calls the hash method on the key to
	 * determine correct placement has no effect if t is not in the Table or for a
	 * null argument
	 * 
	 * @param t   the object to remove
	 * @param key the key of the object
	 * @throws NullPointerException if the key is null
	 */
	public void remove(T t, String key) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("remove(): a key cannot be null");
		}
		int bucket = hash(key);
		List<T> temp = Table.get(bucket);
		int index = temp.linearSearch(t);
		if (index != -1) {
			temp.iteratorToIndex(index);
			temp.removeIterator();
			numElements--;
		}
	}

	/**
	 * Clears this hash table so that it contains no keys.
	 */
	public void clear() {
		for (int i = 0; i < Table.size(); i++) {
			Table.set(i, new List<T>());
		}
	}

	/** Additional Methods */

	/**
	 * Prints all the keys at a specified bucket in the Table. Each key displayed on
	 * its own line, with a blank line separating each key Above the keys, prints
	 * the message "Printing bucket #<bucket>:" Note that there is no <> in the
	 * output
	 * 
	 * @param bucket the index in the Table
	 */
	public void printBucket(int bucket) {
		System.out.println("Printing bucket #" + bucket);
		List<T> l = Table.get(bucket);
		if (l.getLength() != 0) {
			l.placeIterator();
		}
		for (int i = 0; i < l.getLength(); i++) {
			System.out.println(l.getIterator() + "\n");
			l.advanceIterator();
		}

	}

	/**
	 * Prints the first key at each bucket along with a count of the total keys with
	 * the message "+ <count> -1 more at this bucket." Each bucket separated with
	 * two blank lines. When the bucket is empty, prints the message "This bucket is
	 * empty." followed by two blank lines
	 */
	public void printTable() {
		for (int i = 0; i < Table.size(); i++) {
			List<T> temp = Table.get(i);
			if (temp.isEmpty()) {
				System.out.println("This bucket is empty");
			} else {
				int count = temp.getLength() - 1;
				System.out.println(temp.getFirst() + " " + count + " more at this bucket");
			}
			System.out.println("\n");
		}
	}

	/**
	 * Starting at the first bucket, and continuing in order until the last bucket,
	 * concatenates all elements at all buckets into one String
	 */
	@Override
	public String toString() {
		String buffer = "";
		for (int i = 0; i < Table.size(); i++) {
			buffer += Table.get(i).toString();
		}
		return buffer;
	}

}