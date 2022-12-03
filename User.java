
/**
 * User.java
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */

import java.util.Comparator;

public class User {
	private String fullName;
	private String userName;
	private String password;
	private String city;
	private BST<User> friends;
	private List<Interest> interests;
	private int userId;

	/** CONSTRUCTORS */

	/**
	 * Creates a new User with no information.
	 */
	public User() {
	}

	/**
	 * Creates a new User with only their name.
	 * 
	 * @param fullName user's full name
	 */
	public User(String fullName) {
		this.fullName = fullName;
		this.userName = "";
		this.password = "";
		this.city = "";
		this.friends = new BST<User>();
		this.interests = new List<Interest>();
	}

	/**
	 * Creates a new User with their name and id.
	 * 
	 * @param fullName user's full name
	 * @param userId   user's id
	 */
	public User(String fullName, int userId) {
		this.fullName = fullName;
		this.userName = "";
		this.password = "";
		this.city = "";
		this.friends = new BST<User>();
		this.interests = new List<Interest>();
		this.userId = userId;
	}

	/**
	 * Creates a new User with userName and password
	 * 
	 * @param userName user's user name
	 * @param password user's password
	 */
	public User(String userName, String password) {
		this.fullName = "";
		this.userName = userName;
		this.password = password;
		this.city = "";
		this.friends = new BST<User>();
		this.interests = new List<Interest>();
	}

	/**
	 * Creates a new User without friends or interests
	 * 
	 * @param fullName user's full name
	 * @param userName user's user name
	 * @param password user's password
	 * @param city     user's city of residence
	 */
	public User(String fullName, int userId, String userName, String password, String city) {
		this.fullName = fullName;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.city = city;
		this.friends = new BST<User>();
		this.interests = new List<Interest>();
	}

	/**
	 * Creates a new User with all information
	 * 
	 * @param fullName  user's full name
	 * @param userName  user's user name
	 * @param password  user's password
	 * @param city      user's city of residence
	 * @param friends   user's friend list
	 * @param interests user's interest list
	 */
	public User(String fullName, int userId, String userName, String password, String city, BST<User> friends,
			List<Interest> interests) {
		this.fullName = fullName;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.city = city;
		NameComparator c = new NameComparator();
		this.friends = new BST<User>(friends, c);
		this.interests = new List<Interest>(interests);
	}

	/** ACCESSORS */

	/**
	 * Accesses the full name of the User.
	 * 
	 * @return full name of User
	 */
	public String getName() {
		return this.fullName;
	}

	/**
	 * Accesses the user name of the User.
	 * 
	 * @return userName of User
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Accesses the password of the User.
	 * 
	 * @return password of User
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Accesses the city of the User.
	 * 
	 * @return city of User
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Accesses the id of the User.
	 * 
	 * @return userId of User
	 */
	public int getID() {
		return this.userId;
	}

	/**
	 * Accesses the number of interests of the User.
	 * 
	 * @return length of interests List
	 */
	public int getLengthInterests() {
		return this.interests.getLength();
	}

	/**
	 * Accesses the number of friends of the User.
	 * 
	 * @return length of friends BST
	 */
	public int getLengthFriends() {
		return this.friends.getSize();
	}

	/**
	 * Determines whether a friend is on the User's friends list.
	 * 
	 * @param friend the friend's user
	 * @return whether the friend is in the friends BST
	 */
	public boolean hasFriend(User friend) {
		NameComparator c = new NameComparator();
		User temp = friends.search(friend, c);
		if (temp == null) {
			return false;
		}
		return true;
	}

	/**
	 * Accesses the User's friends list.
	 * 
	 * @return BST of friends
	 */
	public BST<User> getFriends() {
		return friends;
	}

	/**
	 * Accesses the User's interests.
	 * 
	 * @return List of interests
	 */
	public List<Interest> getInterests() {
		return interests;
	}

	/**
	 * Checks if both User's are equal in user name and password
	 * 
	 * @param o object to be checked
	 * @return whether or not the objects are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof User)) {
			return false;
		} else {
			User u = (User) o;
			String key1 = this.userName + this.password;
			String key2 = u.userName + u.password;
			if (key1.equals(key2)) {
				return true;
			}
			return false;
		}
	}

	/*** MUTATORS ***/

	/**
	 * Removes all friends from friends BST.
	 */
	public void clearFriends() {
		friends.clear();
	}

	/**
	 * Adds a new friend to the friends BST.
	 * 
	 * @param friend user to be added
	 */
	public void addFriend(User friend) {
		NameComparator c = new NameComparator();
		friends.insert(friend, c);
	}

	/**
	 * Removes a friend from the friends BST.
	 * 
	 * @param friend user to be removed
	 */
	public void removeFriend(User friend) {
		NameComparator c = new NameComparator();
		friends.remove(friend, c);
	}

	/**
	 * Adds a new interest to the interests List.
	 * 
	 * @param interest the interest to add
	 */
	public void addInterest(Interest interest) {
		interests.addLast(interest);
	}

	/**
	 * Removes an interest from the interests List.
	 * 
	 * @param interest the interest to remove
	 */
	public void removeInterest(Interest interest) {
		int index = interests.linearSearch(interest);
		interests.iteratorToIndex(index);
		interests.removeIterator();
	}

	/** ADDITIONAL OPERATIONS */

	/**
	 * Prints all of the user's information.
	 */
	public void printUserInfo() {
		System.out.println(fullName + " @" + userName + "\n(ID:" + userId + ")\nCity: " + city + "\nFriends: ");
		friends.inOrderPrint();
		System.out.println("Interests: ");
		System.out.println(interests + "\n");
	}

	/**
	 * Prints the user's friends list.
	 */
	public void printFriends() {
		System.out.println("Your Friends: ");
		friends.inOrderPrint();
	}

	/**
	 * Creates a string of the User's fullName, userId and userName if one exists.
	 * 
	 * @return fullName, userId and userName of the User
	 */
	@Override
	public String toString() {
		String str = fullName + "(ID:" + userId + ")";
		if (userName != "") {
			str += " @" + userName;
		}
		return str;
	}

	/**
	 * Returns a consistent hash code for each User by summing the Unicode values of
	 * each character in the key. Key = fullName
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		String key = fullName;
		int code = 0;
		for (int i = 0; i < key.length(); i++) {
			code += (int) key.charAt(i);
		}
		return code;
	}
}

class NameComparator implements Comparator<User> {

	/**
	 * Compares the two Users by their full name and ID using the String compareTo
	 * method.
	 * 
	 * @param user0 the first user
	 * @param user1 the second user
	 */
	@Override
	public int compare(User user0, User user1) {
		String name0 = user0.getName() + user0.getID();
		String name1 = user1.getName() + user1.getID();
		return name0.compareTo(name1);
	}

}