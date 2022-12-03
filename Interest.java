
/**
 * Interest.java 
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */

public class Interest {

	private String name;
	private int id;

	private static int numInterests = 0;

	/** CONSTRUCTORS */

	/**
	 * One-argument constructor
	 * 
	 * @param name the name of the Interest
	 */
	public Interest(String name) {
		this.name = name;
		this.id = numInterests;
	}

	/** ACCESSORS */

	/**
	 * Accesses the name of the Interest.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Accesses the id of the Interest.
	 * 
	 * @return the id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Accesses the number of interests.
	 * 
	 * @return the number of interests
	 */
	public int getNumInterests() {
		return numInterests;
	}

	/**
	 * This method checks if both Interests are equal in name.
	 * 
	 * @param o the original Interest
	 * @return whether or not the interests are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Interest)) {
			return false;
		} else {
			Interest i = (Interest) o;
			if (!this.name.equals(i.name)) {
				return false;
			}
			return true;
		}
	}

	/*** MUTATORS ***/

	/**
	 * Increments numInterests by one and sets id equal to the old numInterests.
	 */
	public void incInterests() {
		this.id = numInterests++;
	}

	/** ADDITIONAL OPERATIONS */

	/**
	 * Creates a string of the Interest's name.
	 * 
	 * @return name of the Interest
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns a consistent hash code for each Interest by summing the Unicode
	 * values of each character in the key. Key = name
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		int code = 0;
		for (int i = 0; i < name.length(); i++) {
			code += (int) name.charAt(i);
		}
		return code;
	}
}
