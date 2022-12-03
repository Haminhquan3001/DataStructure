
/**
 * UserTest.java
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class UserTest {
	public static void main(String[] args) throws IOException {
		final int userTotal;
		final int interestTotal;
		File inFile1 = new File("users.txt");
		Scanner input = new Scanner(inFile1);
		userTotal = input.nextInt();
		interestTotal = input.nextInt();
		input.nextLine(); // clear input

		HashTable<Interest> interestTable = new HashTable<>(interestTotal); // HashTable used for find interests
		HashTable<User> loginTable = new HashTable<>(userTotal); // HashTable used for users logging in
		HashTable<User> friendTable = new HashTable<>(userTotal); // HashTable used for finding friends
		ArrayList<BST<User>> commonInterests = new ArrayList<>(interestTotal);
		for (int i = 0; i < interestTotal; i++) { // fill commonInterests with empty BSTs for each interest
			commonInterests.add(new BST<User>());
		}
		NameComparator c = new NameComparator();
		ArrayList<User> users = new ArrayList<>();
		Graph graph = new Graph(userTotal + 1);

		while (input.hasNext()) {
			String name, username, password, city, friend, interest, id, friendId;
			BST<User> friends = new BST<>(); // user's friends list
			List<Interest> interests = new List<>(); // user's interests

			name = input.nextLine();
			id = input.nextLine();
			username = input.nextLine();
			password = input.nextLine();
			city = input.nextLine();
			int numFriends = input.nextInt();
			input.nextLine();
			for (int i = 0; i < numFriends; i++) { // add friends to User's friend list
				friend = input.nextLine();
				friendId = input.nextLine(); // u -> v
				graph.addDirectedEdge(Integer.parseInt(friendId), Integer.parseInt(id));
				friends.insert(new User(friend, Integer.parseInt(friendId)), c);
			}
			int numInterests = input.nextInt();
			input.nextLine();
			for (int i = 0; i < numInterests; i++) { // add interests to User
				interest = input.nextLine();
				Interest tempInter = new Interest(interest);
				if (!interestTable.contains(tempInter, tempInter.getName())) { // If the interest does not exist yet
					tempInter.incInterests(); // increase number of unique interests
					interestTable.put(tempInter, tempInter.getName()); // add new interest to interest HashTable
				} else { // If the interest already exists
					tempInter = interestTable.get(tempInter, tempInter.getName()); // set equal to existing interest
				}
				interests.addLast(tempInter); // add interest to user's list of interests
			}

			User u = new User(name, Integer.parseInt(id), username, password, city, friends, interests);
			String loginKey = u.getUserName() + u.getPassword();
			loginTable.put(u, loginKey);
			friendTable.put(u, u.getName());
			interests.placeIterator();
			for (int i = 0; i < u.getLengthInterests(); i++) { // add user to BST of users with common interest
				int interIndex = interests.getIterator().getID(); // get the interest id
				commonInterests.get(interIndex).insert(u, c); // inserts user into BST of users with common interest
				interests.advanceIterator(); // move to next interest
			}
			users.add(u);
			if (input.hasNextLine()) {
				input.nextLine();
			}
		}

		for (int i = 0; i < users.size(); i++) { // replace place holder friends with full User profiles
			User u = users.get(i);
			List<User> friends = u.getFriends().toList(); // get user's list of friends
			u.clearFriends(); // clear the user's BST of friends
			friends.placeIterator();
			for (int j = 0; j < friends.getLength(); j++) { // add the full User back to the friend's BST
				User friend = friends.getIterator(); // get friend in list
				List<User> foundFriends = friendTable.find(friend, friend.getName()); // find Users with same name in
																						// HashTable
				foundFriends.placeIterator();
				for (int k = 0; k < foundFriends.getLength(); k++) {
					if (friend.getID() == foundFriends.getIterator().getID()) {
						u.addFriend(foundFriends.getIterator());
					}
					foundFriends.advanceIterator();
				}
				friends.advanceIterator();
			}
		}

		input.close();

		input = new Scanner(System.in);

		User user = new User();
		String loginUser, loginPass, loginKey;
		System.out.print("Enter your username: ");
		loginUser = input.nextLine();
		System.out.print("Enter your password: ");
		loginPass = input.nextLine();
		loginKey = loginUser + loginPass;
		if (loginTable.contains(new User(loginUser, loginPass), loginKey)) { // If credentials match user in login
																				// HashTable
			user = loginTable.get(new User(loginUser, loginPass), loginKey); // log into user
		} else { // if user is not found in login HashTable (create a new user)
			String name, username = "", password, city, interest;
			int id;
			System.out.println("Username or password is incorrect. Let's make a new account!\n");
			System.out.print("Please enter your full name: ");
			name = input.nextLine();
			boolean newUsername = false;
			while (!newUsername) { // loop until a unique user name is given
				System.out.print("Please enter a user name: ");
				username = input.nextLine();
				newUsername = true;
				for (int i = 0; i < users.size(); i++) {
					if (username.equals(users.get(i).getUserName())) { // user name already exists
						newUsername = false;
						System.out.println("That username already exists!");
						break;
					}
				}
			}
			System.out.print("Please enter a password: ");
			password = input.nextLine();
			System.out.print("Please enter your city of residence: ");
			city = input.nextLine();
			id = users.size() + 1;
			user = new User(name, id, username, password, city);
			System.out.println("\nLet's look for a friend to add!");
			while (user.getLengthFriends() == 0) {
				addFriend(user, friendTable, input, graph);
			}
			System.out.println("\nLet's add a couple interests!");
			while (user.getLengthInterests() < 2) {
				System.out.print("\nEnter the name of the Interest you wish to add: ");
				interest = input.nextLine();
				Interest tempInter = new Interest(interest);
				if (!interestTable.contains(tempInter, tempInter.getName())) { // If the interest does not exist yet
					tempInter.incInterests(); // increase number of unique interests
					interestTable.put(tempInter, tempInter.getName()); // add new interest to interest HashTable
					commonInterests.add(new BST<User>());
				} else { // If the interest already exists
					tempInter = interestTable.get(tempInter, tempInter.getName()); // set equal to existing interest
				}
				user.addInterest(tempInter); // add interest to user's list of interests
			}

			loginKey = user.getUserName() + user.getPassword();
			loginTable.put(user, loginKey);
			friendTable.put(user, user.getName());
			List<Interest> interests = user.getInterests();
			interests.placeIterator();
			for (int i = 0; i < user.getLengthInterests(); i++) { // add user to BST of users with common interest
				int interIndex = interests.getIterator().getID(); // get the interest id
				commonInterests.get(interIndex).insert(user, c); // inserts user into BST of users with common interest
				interests.advanceIterator(); // move to next interest
			}
			users.add(user);
		}

		System.out.println("\nWelcome " + user.getName() + "!\n");

		boolean isRunning = true;
		while (isRunning) {
			char choice = promptUser(input); // get input from user
			System.out.println();

			if (choice == 'A') { // view friends list
				viewFriends(user, friendTable, input, graph);
			} else if (choice == 'B') { // search for new friend
				searchNewFriends(user, friendTable, interestTable, commonInterests, input, graph);
			} else if (choice == 'C') { // get friend recommendations
				getFriendRecommendations(user, users, friendTable, commonInterests, graph, input);
			} else if (choice == 'X') { // exit program
				System.out.println("Goodbye!");
				isRunning = false;
			} else { // invalid option
				System.out.println("Invalid menu option, Please select A-C or X to exit.\n");
			}

		}

		updateFile(users, commonInterests, inFile1); // **** COMMENTED OUT FOR TESTING ****
	}

	/**
	 * Displays a prompt that shows the user what they can do and asks for an input.
	 * 
	 * @param input keyboard Scanner
	 * @return user inputed char
	 */
	public static char promptUser(Scanner input) {
		System.out.println("\nPlease select from the following options:\n");
		System.out.println("A. View My Friends");
		System.out.println("B. Search for a New Friend");
		System.out.println("C. Get Friend Recommendations");
		System.out.println("X. Exit\n");
		System.out.print("Enter your choice: ");

		return input.nextLine().charAt(0);
	}

	/**
	 * Displays a prompt that allows the user to view their friends and remove
	 * friends.
	 * 
	 * @param user        the user
	 * @param friendTable table of users sorted by name
	 * @param input       keyboard Scanner
	 * @param g           graph of user relations
	 */
	public static void viewFriends(User user, HashTable<User> friendTable, Scanner input, Graph g) {
		System.out.println("\nPlease select from the following actions:\n");
		System.out.println("1. View My Friends List");
		System.out.println("2. View a Friend's Profile");
		System.out.println("3. Remove a Friend\n");
		System.out.print("Enter your choice: ");

		char choice = input.nextLine().charAt(0);

		if (choice == '1') { // view friends list
			System.out.println();
			user.printFriends();

		} else if (choice == '2') { // view friend's profile
			List<User> friends = user.getFriends().toList();
			System.out.println();
			friends.printNumberedList();
			System.out.print("\nEnter the number to the left of the friend whose profile you wish to view: ");
			int index = input.nextInt();
			input.nextLine(); // clear input
			if (index >= 0 && index < friends.getLength()) { // if index is within range
				friends.iteratorToIndex(index);
				System.out.println();
				friends.getIterator().printUserInfo();
			} else { // if index is out of bounds
				System.out.println("\nInvalid Choice!\n");
			}

		} else if (choice == '3') { // remove a friend
			if (user.getLengthFriends() > 0) { // if user has friends
				System.out.println();
				List<User> friends = user.getFriends().toList();
				friends.printNumberedList();
				System.out.print("\nEnter the number to the left of the friend you wish to remove: ");
				int index = input.nextInt();
				input.nextLine(); // clear keyboard input

				if (index >= 0 && index < friends.getLength()) { // if index is within range
					friends.iteratorToIndex(index);
					User friend = friends.getIterator();
					friend.removeFriend(user);
					user.removeFriend(friend);
					g.removeDirectedEdge(friend.getID(), user.getID());
					g.removeDirectedEdge(user.getID(), friend.getID());
					System.out.println("\n" + friend.getName() + "(ID:" + friend.getID()
							+ ") has been removed from your friends list\n");
				} else { // if index is out of bounds
					System.out.println("\nInvalid Choice!\n");
				}

			} else { // if user has no friends
				System.out.println("\nYou have no friends to remove at this time\n");
			}

		} else { // invalid option
			System.out.println("\nInvalid Choice!\n");
		}
	}

	/**
	 * Displays a prompt that allows the user to add a new friend by name or by
	 * common interest.
	 * 
	 * @param user            the user
	 * @param users           table of users
	 * @param interests       table of interests
	 * @param commonInterests list of BSTs of users with common interests
	 * @param input           keyboard Scanner
	 * @param gr              graph of user relations
	 */
	public static void searchNewFriends(User user, HashTable<User> users, HashTable<Interest> interests,
			ArrayList<BST<User>> commonInterests, Scanner input, Graph gr) {
		System.out.println("\nPlease select from the following actions:\n");
		System.out.println("1. Search for New Friend by Name");
		System.out.println("2. Search for New Friend by Interest\n");
		System.out.print("Enter your choice: ");

		char choice = input.nextLine().charAt(0);

		if (choice == '1') { // search for new friend by name
			addFriend(user, users, input, gr);
		} else if (choice == '2') { // search for new friend by interest
			System.out.print("\nEnter the name of the Interest you wish to search for: ");
			String interestName = input.nextLine();
			Interest interest = interests.get(new Interest(interestName), interestName);
			if (interest != null) { // interest exists
				List<User> friends = commonInterests.get(interest.getID()).toList();
				friends.printNumberedList();
				System.out.print("\nEnter the number to the left of the user you wish to add: ");
				int index = -1;
				if (input.hasNextInt()) {
					index = input.nextInt();
				}
				input.nextLine();
				if (index >= 0 && index < friends.getLength()) { // if index is within range
					friends.iteratorToIndex(index);
					User friend = friends.getIterator();
					if (!user.hasFriend(friend)) { // user is not already friend
						System.out.println("Would you like to add " + friend + " as a friend?\n");
						System.out.print("Please enter Yes or No (Y/N): ");
						choice = input.nextLine().charAt(0);
						if (choice == 'Y') { // add user as friend
							user.addFriend(friend);
							friend.addFriend(user);
							gr.addDirectedEdge(friend.getID(), user.getID());
							gr.addDirectedEdge(user.getID(), friend.getID());
							System.out.println("\n" + friend + " was added to your friends list!\n");
						} else if (choice == 'N') { // do not add user as friend
							System.out.println("\n" + friend + " was not added to your friends list.\n");
						} else { // invalid choice
							System.out.println("Invalid Choice. Not adding user as friend\n");
						}
					} else { // user is already friend
						System.out.println("\nThis user is already your friend!\n");
					}
				} else { // if index is out of bounds
					System.out.println("\nInvalid Choice!\n");
				}
			} else { // interest does not exist
				System.out.println("\nNo one has that interest\n");
			}
		} else { // invalid option
			System.out.println("\nInvalid Choice!\n");
		}
	}

	/**
	 * Displays a prompt to add a friend to the user's friends list.
	 * 
	 * @param user  the user
	 * @param users table of users
	 * @param input keyboard Scanner
	 * @param graph graph of user relations
	 */
	public static void addFriend(User user, HashTable<User> users, Scanner input, Graph graph) {
		char choice;
		System.out.print("\nEnter the name of the User you wish to add as a friend: ");
		String name = input.nextLine();
		List<User> friends = users.find(new User(name), name);
		if (friends.getLength() == 0) { // no users exist with that name
			System.out.println("\nNo user with that name exists.\n");
		} else if (friends.getLength() == 1) { // one user exists with that name
			User friend = friends.getFirst();
			if (!user.hasFriend(friend)) { // user does not have friend
				System.out.println("Would you like to add " + friend + " as a friend?\n");
				System.out.print("Please enter Yes or No (Y/N): ");
				choice = input.nextLine().charAt(0);
				if (choice == 'Y') { // add user as friend
					user.addFriend(friend);
					friend.addFriend(user);
					graph.addDirectedEdge(friend.getID(), user.getID());
					graph.addDirectedEdge(user.getID(), friend.getID());
					System.out.println("\n" + friend + " was added to your friends list!\n");
				} else if (choice == 'N') { // do not add user as friend
					System.out.println("\n" + friend + " was not added to your friends list.\n");
				} else { // invalid choice
					System.out.println("Invalid Choice. Not adding user as friend\n");
				}
			} else { // user already has friend
				System.out.println("\nThis user is already your friend!\n");
			}
		} else { // multiple users exist with that name
			System.out.println("\nThere are multiple users with that name:");
			friends.printNumberedList();
			System.out.print("\nEnter the number to the left of the user you wish to add: ");
			int index = -1;
			if (input.hasNextInt()) {
				index = input.nextInt();
			}
			input.nextLine();
			if (index >= 0 && index < friends.getLength()) { // if index is within range
				friends.iteratorToIndex(index);
				User friend = friends.getIterator();
				if (!user.hasFriend(friend)) { // user is not already friend
					System.out.println("Would you like to add " + friend + " as a friend?\n");
					System.out.print("Please enter Yes or No (Y/N): ");
					choice = input.nextLine().charAt(0);
					if (choice == 'Y') { // add user as friend
						user.addFriend(friend);
						friend.addFriend(user);
						graph.addDirectedEdge(friend.getID(), user.getID());
						graph.addDirectedEdge(user.getID(), friend.getID());
						System.out.println("\n" + friend + " was added to your friends list!\n");
					} else if (choice == 'N') { // do not add user as friend
						System.out.println("\n" + friend + " was not added to your friends list.\n");
					} else { // invalid choice
						System.out.println("Invalid Choice. Not adding user as friend\n");
					}
				} else { // user is already friend
					System.out.println("\nThis user is already your friend!\n");
				}
			} else { // if index is out of bounds
				System.out.println("\nInvalid Choice!\n");
			}
		}
	}

	/**
	 * Displays a prompt that allows the user to view friend recommendations and add
	 * new friends by name.
	 * 
	 * @param user            the user
	 * @param users           list of users
	 * @param friendTable     table of users by name
	 * @param commonInterests list of BSTs of users with common interests
	 * @param g               graph of user relations
	 * @param input           keyboard Scanner
	 */
	public static void getFriendRecommendations(User user, ArrayList<User> users, HashTable<User> friendTable,
			ArrayList<BST<User>> commonInterests, Graph g, Scanner input) {
		int choice = -1;
		boolean addingFriends = true;
		while (addingFriends) {
			System.out.println("\nPlease select from the following options:\n");
			System.out.println("1. Get Friend Recommendation by Mutual Friends");
			System.out.println("2. Add Friend");
			System.out.println("3. Return to Menu");
			System.out.print("\nEnter your choice: ");

			choice = input.nextLine().charAt(0);
			if (choice == '1') {
				System.out.println("\nRecommended friends by Mutual Friends: \n");
				g.BFS(user.getID());
				int count = 2;
				int numRecommendations = 1;
				for (int i = 1; i <= g.getNumVertices(); i++) {
					for (int j = 1; j <= g.getNumVertices(); j++) {
						if (g.getDistance(j).equals(count)) {
							NameComparator c = new NameComparator();
							User recommendedFriend = users.get(j - 1);
							List<Interest> userInterests = recommendedFriend.getInterests();
							userInterests.placeIterator();
							List<Interest> interests = new List<>();
							for (int k = 0; k < recommendedFriend.getLengthInterests(); k++) { // check suggested friend
																								// and user's common
																								// interests
								Interest temp = userInterests.getIterator();
								User found = commonInterests.get(temp.getID()).search(user, c);
								if (found != null) { // check if suggested friend and user have interest in common
									interests.addLast(temp);
								}
								userInterests.advanceIterator();
							}
							System.out.print(numRecommendations + ": " + recommendedFriend.getName() + "(ID:"
									+ recommendedFriend.getID() + ")\n\tCommon Interests: " + interests + "\n");
							numRecommendations++;
						}
					}
					count++;
				}
			} else if (choice == '2') {
				addFriend(user, friendTable, input, g);
			} else if (choice == '3') {
				addingFriends = false;
				System.out.println("\nReturning to Menu!\n");
			} else {
				System.out.println("\nInvalid choice!\n");
			}
		}
	}

	/**
	 * Updates input file with current state of users at end of program run.
	 * 
	 * @param users          list of users
	 * @param totalInterests list of BSTs of Users with common interests
	 * @param file           the input file
	 * @throws IOException
	 */
	public static void updateFile(ArrayList<User> users, ArrayList<BST<User>> totalInterests, File file)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append(users.size() + "\n"); // writes total number of users
		writer.append(totalInterests.size() + "\n"); // writes total number of interests
		for (int i = 0; i < users.size(); i++) { // write all users into file
			User u = users.get(i);
			writer.append(u.getName() + "\n"); // write user's full name
			writer.append(u.getID() + "\n"); // write user's ID
			writer.append(u.getUserName() + "\n"); // write user's user name
			writer.append(u.getPassword() + "\n"); // write user's password
			writer.append(u.getCity() + "\n"); // write user's city
			writer.append(u.getLengthFriends() + "\n"); // write number of friends user has
			List<User> friends = u.getFriends().toList();
			friends.placeIterator();
			for (int j = 0; j < friends.getLength(); j++) { // write user's friends
				writer.append(friends.getIterator().getName() + "\n"); // write friend's name
				writer.append(friends.getIterator().getID() + "\n"); // write friend's ID
				friends.advanceIterator();
			}
			writer.append(u.getLengthInterests() + "\n"); // write number of interests user has
			List<Interest> interests = u.getInterests();
			interests.placeIterator();
			for (int j = 0; j < interests.getLength(); j++) { // write user's interests
				writer.append(interests.getIterator().getName() + "\n");
				interests.advanceIterator();
			}
			if (i != users.size() - 1) { // don't skip a line at end of file
				writer.append("\n");
			}
		}
		writer.close();
	}

}
