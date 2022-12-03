
/**
 * List.java
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */

import java.util.NoSuchElementException;

public class List<T> {
	private class Node {
		private T data;
		private Node next;
		private Node prev;

		public Node(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}

	private int length;
	private Node first;
	private Node last;
	private Node iterator;

	/**** CONSTRUCTOR ****/

	/**
	 * Instantiates a new List with default values
	 * 
	 * @postcondition A new empty list is created
	 */
	public List() {
		first = null;
		last = null;
		iterator = null;
		length = 0;
	}

	/**
	 * Makes a copy of a list
	 * 
	 * @param original the original list
	 * @postcondition A copy of the list is created
	 */
	public List(List<T> original) {
		if (original == null) {
			return;
		} else if (original.getLength() == 0) {
			this.first = null;
			this.last = null;
			this.iterator = null;
			length = 0;
		} else {
			Node temp = original.first;
			while (temp != null) {
				addLast(temp.data);
				temp = temp.next;
			}
			iterator = null;
		}
	}

	/**** ACCESSORS ****/

	/**
	 * Returns the value stored in the first node
	 * 
	 * @precondition !isEmpty()
	 * @return the value stored at node first
	 * @throws NoSuchElementException when precondition is violated
	 */
	public T getFirst() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("getFirst(): List is Empty. No data to access!");
		}
		return first.data;
	}

	/**
	 * Returns the value stored in the last node
	 * 
	 * @precondition !isEmpty()
	 * @return the value stored in the node last
	 * @throws NoSuchElementException when precondition is violated
	 */
	public T getLast() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("getLast(): List is Empty. No data to access!");
		}
		return last.data;
	}

	/**
	 * Returns the current length of the list
	 * 
	 * @return the length of the list from 0 to n
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns whether the list is currently empty
	 * 
	 * @return whether the list is empty
	 */
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * Returns the value the iterator is pointing at
	 * 
	 * @return the value iterator is pointing at
	 * @throws NullPointerException
	 */
	public T getIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("getIterator(): " + "The iterator is offEnd.");
		} else {
			return iterator.data;
		}
	}

	/**
	 * Checks to see if the iterator is pointing at the list or if its offEnd
	 * 
	 * @return if the iterator is pointing at the list or is offEnd
	 */
	public boolean offEnd() {
		return iterator == null;
	}

	/**
	 * Searches the List for the specified value using the linear search algorithm
	 * 
	 * @param value the value to search for
	 * @return the location of value in the List or -1 to indicate not found Note
	 *         that if the List is empty we will consider the element to be not
	 *         found post: position of the iterator remains unchanged
	 */
	public int linearSearch(T value) {

		if (isEmpty()) {
			return -1;
		}
		Node temp = first;
		for (int i = 0; i < length; i++) {
			if (temp.data.equals(value)) {
				return i;
			}
			temp = temp.next;
		}
		return -1;
	}

	/**
	 * This methods checks if both list are equal in length and order
	 * 
	 * @param o the original list
	 * @return whether or not the list are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof List)) {
			return false;
		} else {
			List<T> L = (List<T>) o;
			if (this.length != L.length) {
				return false;
			} else {
				Node temp1 = this.first;
				Node temp2 = L.first;
				while (temp1 != null) {
					if (!(temp1.data.equals(temp2.data))) {
						return false;
					}
					temp1 = temp1.next;
					temp2 = temp2.next;
				}
				return true;
			}
		}
	}

	/**** MUTATORS ****/

	/**
	 * Creates a new first element
	 * 
	 * @param data the data to insert at the front of the list
	 * @postcondition Creates a new first node
	 */
	public void addFirst(T data) {
		if (first == null) {
			first = last = new Node(data);
		} else {
			Node N = new Node(data); // new node created
			N.next = first; // new node next node is current first
			first.prev = N; // node before is new node
			first = N; // first is now new node
		}
		length++;
	}

	/**
	 * Creates a new last element
	 * 
	 * @param data the data to insert at the end of the list
	 * @postcondition Creates a new last node
	 */
	public void addLast(T data) {
		if (first == null) {
			first = last = new Node(data);
		} else {
			Node N = new Node(data); // new node created
			last.next = N; // current last node points to new node
			N.prev = last; // new node previous is current last
			last = N; // last is now new node
		}
		length++;
	}

	/**
	 * removes the element at the front of the list
	 * 
	 * @precondition !isEmpty()
	 * @postcondition removes head of list
	 * @throws NoSuchElementException when precondition is violated
	 */
	public void removeFirst() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("removeFirst(): Cannot remove from an empty List!");
		} else if (length == 1) {
			first = last = iterator = null;
		} else {
			if (iterator == first) {
				iterator = null;
			}
			first = first.next;
			first.prev = null;
		}
		length--;
	}

	/**
	 * removes the element at the end of the list
	 * 
	 * @precondition !isEmpty()
	 * @postcondition removes tail of list
	 * @throws NoSuchElementException when precondition is violated
	 */
	public void removeLast() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("removeLast(): list is empty. Nothing to remove.");
		} else if (length == 1) {
			first = last = iterator = null;
		} else {
			if (iterator == last) {
				iterator = null;
			}
			last = last.prev;
			last.next = null;
		}
		length--;
	}

	/**
	 * This method places the iterator at first
	 * 
	 * @postcondition iterator = first
	 * @throws NullPointerException
	 */
	public void placeIterator() throws NullPointerException {
		iterator = first;
	}

	/**
	 * This method removes the value the iterator is pointing at
	 * 
	 * @precondition The iterator can not be offEnd
	 * @postcondition The value the iterator is pointing at is removed
	 * @throws NullPointerException
	 */
	public void removeIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("removeIterator(): The iterator is pointing at null.");
		} else if (iterator == first) {
			removeFirst();
		} else if (iterator == last) {
			removeLast();
		} else {
			iterator.prev.next = iterator.next;
			iterator.next.prev = iterator.prev;
			iterator = null;
			length--;
		}
	}

	/**
	 * This method adds a new node after the iterator node
	 * 
	 * @precondition The iterator can not be offEnd
	 * @param data data the user wants in the iterator
	 * @postcondition A new node is added after the iterator node
	 * @throws NullPointerException
	 */
	public void addIterator(T data) throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("addIterator(): The iterator is pointing at null");
		} else if (iterator == last) {
			addLast(data);
		} else {
			Node N = new Node(data);
			N.next = iterator.next;
			N.prev = iterator;
			iterator.next = N;
			N.next.prev = N;
			length++;
		}
	}

	/**
	 * This method advances the iterator one node towards the last
	 * 
	 * @precondition The iterator can not be offEnd
	 * @postcondition iterator = iterator.next
	 * @throws NullPointerException
	 */
	public void advanceIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("advanceIterator(): The iterator is offEnd.");
		} else {
			iterator = iterator.next;
		}
	}

	/**
	 * This method reverses the iterator back one node
	 * 
	 * @precondition The iterator can not be offEnd
	 * @postcondition iterator = iterator.prev
	 * @throws NullPointerException
	 */
	public void reverseIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("reverseIterator(): The iterator is offEnd.");
		} else {
			iterator = iterator.prev;
		}
	}

	/**
	 * Points the iterator at first and then advances it to the specified index
	 * 
	 * @param index the index where the iterator should be placed
	 * @precondition 0 < index <= length
	 * @throws IndexOutOfBoundsException when precondition is violated
	 */
	public void iteratorToIndex(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > length) {
			throw new IndexOutOfBoundsException("iteratorToIndex(): index must be in range 0 < index <= length");
		}
		placeIterator();
		for (int i = 0; i < index; i++) {
			advanceIterator();
		}

	}

	/**** ADDITIONAL OPERATIONS ****/

	/**
	 * List with each value on its own line At the end of the List a new line
	 * 
	 * @return the List as a String for display
	 */
	@Override
	public String toString() {
		String result = "";
		Node temp = first;
		while (temp != null) {
			result += temp.data + ", ";
			temp = temp.next;
		}
		if (result.length() > 2) {
			return result.substring(0, result.length() - 2);
		}
		return result;
	}

	/**
	 * This function prints the contents of the linked list to the screen
	 */
	public void printNumberedList() {
		Node temp = first;
		int i = 0;
		while (temp != null) {
			System.out.println(i + ": " + temp.data);
			temp = temp.next;
			i++;
		}
	}

}