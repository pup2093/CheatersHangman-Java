import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Set<String> dictSet = new HashSet<String>();
		List<String> dictList = new ArrayList<String>();

		// read txt file and store it in a hashset
		try {
			Scanner scan = new Scanner(new File("Dictionary.txt"));
			String line = "";

			while (scan.hasNextLine()) {

				line = scan.nextLine();

				String[] words = line.replaceAll("\\p{Punct}", "").toLowerCase().split("\\s+");
				for (String word : words) {
					dictSet.add(word);
					dictList.add(word);

				}

			}

			scan.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// System.out.println(dict);

		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to Hangman.\nPlease type the size of the hidden word you want to try to guess.");
		int wSize = scan.nextInt();
		boolean done = false;
		do {
			for (String w : dictList) {
				if (w.length() == wSize) {
					System.out.println("thanks");
					done = true;
					break;
				} else if ((dictList.indexOf(w) == dictList.size() - 1) && w.length() != wSize) {
					System.out.println("Sorry, there are no words of that length. Try again.");
					wSize = scan.nextInt();
					continue;
				}
			}
		} while (!(done));

		// Build string key with '_' for loop for the size of word
		// this is built outside of game while loop

		/*
		 * String key = ""; for(int i = 0; i < wSize; i++) { key+= "_ "; }
		 */
        while(true) {
		String newKey = "";

		System.out.println("Type the number of wrong guesses you are allowed to have before you lose.");
		int guessNum = scan.nextInt();

		Set<String> hiddenList = new HashSet<String>(); // initial list of words that match user's length
		for (String w : dictList) {
			if (w.length() == wSize) {
				hiddenList.add(w);
			}
		}
		System.out.println("Alright, I've chosen my word!");
		// System.out.println(hiddenList);

		// Now we'll need to implement the cheating algorithm and create a hashmap to
		// store word families
		Map<String, Set<String>> families;// = new HashMap<String, Set<String>>();
		// Set<String> fam = new HashSet<String>(); // value for HashMap which is the
		// list or set of words that belong to a
		// word family
		boolean isDone = false;
		int j = 0;
		while (j < guessNum) {
			families = new HashMap<String, Set<String>>();

			// First, we need to split up the initial hidden list into word families (use
			// single Map) based on user's letter
			// Second, we need to choose largest word family set and make that our new
			// hidden word list
			// Then, we'll simply reveal the key that maps to the largest word family to the
			// player
			System.out.println("Guess a letter.");
			String input = scan.next();
			char[] arr = input.toCharArray();
			List<Character> userInput = new ArrayList<Character>();
			for (char d : arr) {
				userInput.add(d);
			}
			// List<char []> ch = Arrays.asList(arr);
			// String charac = scan.next();
			// char letter = charac.charAt(0);
			// String key = "";
			for (String wordT : hiddenList) {
				String key = "";
				Set<String> fam = new HashSet<String>();
				for (char c : wordT.toCharArray()) {// this for loop just loops through each character in each word in
					// the hidden list to see what to add to Map

					// alternative key building method
					// if(userInput.contains(c)) {
					// create new key here and then set old key to newKey after the for loop. After
					// finding the new max hiddenList

					char[] nk = newKey.toCharArray();
					List<Character> newK = new ArrayList<Character>();
					for (char f : nk) {
						newK.add(f);
					}

					if (userInput.contains(c) || newK.contains(c)) {
						key += c;
					} else {
						key += "_ ";
					}

				}

				// fam.add(wordT);

				// String keyString = new String(key);
				// fam.add(wordT); //the set will contain the actual words that belong to a
				// specified word family
				// families.put(key, fam);
				// hiddenList = families.get(key);
				// fam.add(wordT);
				// families.put(key, fam)); //families.put(key, set of words in family of key);
				addWord(key, wordT, families);

			}
			// families.put(key, fam);

			j++;

			// Now we need to implement the part that chooses the largest word family

			// List<Integer> sizeOfSet = new ArrayList<Integer>();
			int max = Integer.MIN_VALUE;
			// String newKey = "";
			for (String k : families.keySet()) {
				if (families.get(k).size() > max) {
					max = families.get(k).size();
					hiddenList = families.get(k);
					// build newKey here
					newKey = k;

				}

			}

			/*
			 * String newKey = ""; for(String key : families.keySet()) {
			 * if(families.get(key).equals(hiddenList)) { newKey = key; } }
			 */

			System.out.println(families);
			System.out.println(hiddenList);

			System.out.println("Here's what you have " + newKey);

			// No we need to display the key of the largest set to the user (remember, when
			// the user is out of guesses, we'll just randomly choose a word in the present
			// hiddenList to display
			/*
			 * for (String k : families.keySet()) { if (families.get(k).equals(hiddenList))
			 * { //System.out.println("Here's what you have so far: " + k); } }
			 * //System.out.println("You have " + " guesses left");
			 */

		}
		
		//Now that the while loop ends: if all letters are revealed, then the player wins. If not, then the player loses and you must reveal a random word from hidden list to the user
		List<String> finHiddenList = new ArrayList<String>();
		finHiddenList.addAll(hiddenList);
		Random rand = new Random();
		int r = rand.nextInt(finHiddenList.size());
		String ran = finHiddenList.get(r);
		if(!(newKey.contains("_"))) {
			System.out.println("You win!");
		}else {
			System.out.println("Sorry, you're out of guesses");
			System.out.println("The hidden word was " + ran);
			
					
		}
		System.out.println("Would you like tp play again? Type yes or no.");
		String userPlayAgain = scan.next();
		userPlayAgain.toLowerCase();
		if(userPlayAgain.equals("yes")) {
			continue;
		}
		else {
			break;
		}
	}
		
	}

	public static void addWord(String key, String word, Map<String, Set<String>> families) {
		if (families.get(key) == null) {
			families.put(key, new HashSet<String>());
		}
		families.get(key).add(word);
	}

}
// }
