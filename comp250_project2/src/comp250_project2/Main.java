package comp250_project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

// Christopher Pham
// all steps are completed

public class Main {
	
	public static void main(String[] args) {
		new Main(); // main object holds most code
	}
	
	public Main() { 
		Scanner in = new Scanner(System.in);
		KeyWordList keyWords;
		// prompt user to find a file, and will ask until file is found 
		String fileName = null;
		boolean fileFound = false;
		while (fileFound == false) {
			System.out.println("What is the name of the file?");
			fileName = in.nextLine().strip();
			if ( new File(fileName).exists()) { // check if file exist
				fileFound = true;
			} else {
				System.out.println("File does not exist.");
			}
		}
		// prompt user for n 
		int nOrder = -1;
		while (nOrder == -1) {
			try {
				System.out.println("Type a n."); // protect program from invalid input
				nOrder = in.nextInt();
			} catch (InputMismatchException ex) {
				System.out.println("Sorry please enter valid input.");
				in.nextLine();
			}
		}
		// will call Markov function depending on # of n
		if (nOrder == 1) {
			keyWords = prepareMarkov(fileName);
		} else {
			keyWords = prepareMarkovNOrder(fileName, nOrder);
		}
		keyWords.print(); // find output, will be keywords with nextwords
		int wordCount = -1;
		// prompt for number of words for random walk
		while (wordCount == -1 ) {
			try {
				System.out.println("Type in how many words do you want in random walk.");
				wordCount = in.nextInt();
			} catch (InputMismatchException ex) {
				System.out.println("Sorry please enter valid input.");
				in.nextLine();
			}
		}
		// will call random walk function depending on user input
		if (nOrder == 1) {
			randomWalk(wordCount, keyWords.getRandomKeyword(), keyWords); // starts on random keyword then continues for word count times
		} else {
			randomWalkNOrder(wordCount, keyWords.getRandomKeyword(), keyWords); // starts on random keyword then continues for word count times
		}
		in.close();
	}

	// only used in case that n is 1
	public void randomWalk(int numWords, String firstWord, KeyWordList keyWordList) {
		String startWord = firstWord;
		int letterCount = 0; // keeps track of letters in line
		for (int i = 0 ; i < numWords; i++) {
			// find next word after the startword
			String nextWord = keyWordList.getRandomNextWord(startWord);
			letterCount += nextWord.length() + 1;
			System.out.print(nextWord + " ");
			// if the letters in long are a lot this will make a new line
			if (letterCount >= 100) {
				System.out.println();
				letterCount = 0;
			}
			startWord = nextWord; // set up for next startword for the getrandom next word function
		}
	}

	// this is step 4, unused in program but proof I did step 4
	public void randomWalkPairs(int numWords, String firstWord, KeyWordList keyWordList) {
		String startWord = firstWord;

		for (int i = 0 ; i < numWords; i++) {
			String nextWord = keyWordList.getRandomNextWord(startWord);
			System.out.print(nextWord + " ");
			startWord = startWord.substring(startWord.indexOf(" ")+1,startWord.length());
			startWord = startWord + " " + nextWord;
			// if this reaches the last word in the file then
			// we can't get a new pair therefore what we do is use the 1st keyword after making 
			// a print using the last possible pair 
			if (keyWordList.getBottomValue().equals(startWord)) {
				nextWord = keyWordList.getRandomNextWord(startWord);
				System.out.print( nextWord + " ");
				startWord = startWord.substring(startWord.indexOf(" ")+1,startWord.length());
				startWord = startWord + " " + nextWord;
				startWord = keyWordList.get(0);
			}
		}
	}

	public void randomWalkNOrder(int numWords, String firstWord, KeyWordList keyWordList) {
		String startWord = firstWord;
		int letterCount = 0; // keep track of size of line

		for (int i = 0 ; i < numWords; i++) {
			String nextWord = keyWordList.getRandomNextWord(startWord); // get next word
			letterCount += nextWord.length() + 1; // increase size of line
			System.out.print( nextWord + " ");
			startWord = startWord.substring(startWord.indexOf(" ")+1,startWord.length()); // removes the 1st word in this pair
			startWord = startWord + " " + nextWord; // adds a second word so its a pair again
			// if this reaches the last word in the file then
			// we can't get a new pair therefore what we do is use the 1st keyword after making 
			// a print using the last possible pair 
			if (letterCount >= 100) {
				System.out.println();
				letterCount = 0;
			}
			// if the keyword that will be used is the last pair in the file, it will print the nextword that was passed
			// for this iteration, then it will change the startword into the first keyword in the list
			if (keyWordList.getBottomValue().equals(startWord)) {
				nextWord = keyWordList.getRandomNextWord(startWord);
				System.out.print( nextWord + " ");
				startWord = startWord.substring(startWord.indexOf(" ")+1,startWord.length());
				startWord = startWord + " " + nextWord;
				startWord = keyWordList.get(0);
			}
		}
	}

	public KeyWordList prepareMarkovNOrder(String fileName, int nOrder) {
		KeyWordList wordCollection = new KeyWordList();
		try {
			FileReader fReader =  new FileReader(fileName); // open reader
			Scanner in = new Scanner(fReader);
			String startWord = "";

			for (int i = 0; i < nOrder; i++) {
				boolean found = false;
				// will go through file until it finds the amount of words that is nOrder
				while(in.hasNext() && found == false) {
					String current = in.next().toLowerCase();
					// will not store tokens that are all punctuation
					if (!current.replaceAll("\\p{Punct}", "").equals("")) {
						found = true;
						if (i < nOrder -1 ) {
							// will add the word and a space, so the words don't all combine to one word
							startWord += current.replaceAll("\\p{Punct}", "") + " ";
						} else if (i == nOrder - 1) {
							// if this is the last word to add it won't add a space after
							startWord += current.replaceAll("\\p{Punct}", "");
						}
					}
				}
			}
			String keyword;
			
			while (in.hasNext()) {
				keyword = startWord; 
				// finds nextword
				String nextWord = in.next().toLowerCase().replaceAll("\\p{Punct}", "");
				if (!nextWord.equals("")) {
					// if the nextword is not empty, it will add this to the keyword list with the nextword
					// if its already in it the keyword list will increment its nextword count
					wordCollection.foundWordSequence(keyword, nextWord);
					// will remove the first word anf space from startword
					startWord = startWord.substring(startWord.indexOf(" ")+1,startWord.length()) + " " + nextWord;
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		return wordCollection;
	}

	// this is to show I did step 4
	public KeyWordList prepareMarkovPair(String fileName) {
		KeyWordList wordCollection = new KeyWordList();
		try {

			FileReader fReader =  new FileReader(fileName); // open reader
			Scanner in = new Scanner(fReader);
			String word1 = null;
			String word2 = null;

			// go through file to receive one token that is not all punctuation
			boolean found = false;
			while(in.hasNext() && found == false) {
				String current = in.next().toLowerCase();
				if (!current.replaceAll("\\p{Punct}", "").equals("")) {
					found = true;
					word1 = current.replaceAll("\\p{Punct}", "");
				}
			}
			// go through file to receive one token that is not all punctuation
			boolean found2 = false;
			while(in.hasNext() && found2 == false) {
				String current = in.next().toLowerCase();
				if (!current.replaceAll("\\p{Punct}", "").equals("")) {
					found2 = true;
					word2 = current.replaceAll("\\p{Punct}", "");
				}
			}
			String keyword;

			while (in.hasNext()) {
				keyword = word1 + " " + word2; // makes pair
				String nextWord = in.next().toLowerCase().replaceAll("\\p{Punct}", ""); // gets next word
				if (!nextWord.equals("")) {
					// nextword is not all blank call found word sequence, will create new keyword if not in
					// keyword list, if not it will increase count for existing keyword
					wordCollection.foundWordSequence(keyword, nextWord);
					word1 = word2; // shift the words
					word2 = nextWord; // shift the words
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return wordCollection;
	}


	// happens in case n is 1
	public KeyWordList prepareMarkov(String fileName) {
		KeyWordList wordCollection = new KeyWordList();
		try {

			FileReader fReader =  new FileReader(fileName); // open reader
			Scanner in = new Scanner(fReader);
			String keyword = in.next().toLowerCase(); // pick up first word

			// will run until it goes through entire file
			while (in.hasNext()) {
				String currentWord = in.next().toLowerCase(); // receive token
				String processed = currentWord.replaceAll("\\p{Punct}", "");
				// will only add token word if its not all punctuation
				if (!processed.equals("")) {
					// add to keyword list
					wordCollection.foundWordSequence(keyword, currentWord.replaceAll("\\p{Punct}", ""));
					keyword = currentWord.replaceAll("\\p{Punct}", "");
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return wordCollection;
	}
}
