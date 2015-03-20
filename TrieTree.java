

import java.io.*;
import java.util.*;

public class TrieTree {
	/* subclass TrieNode */
	public class TrieNode {
		private boolean isInTree;
		/*
		 * This tree recognizes only the 26 letters of the english alphabet, and
		 * the numbers 0-9. The 26th node is always empty to avoid an error while
		 * looking for a non-letter. find(symbol)->search in children[26]->false 
		 */
		TrieNode[] children = new TrieNode[37];
		int letterCounter;

		// Construktor
		public TrieNode() {
			this.isInTree = true;
		}

		public void insert(TrieNode node, char letter) {
			//Existiert schon der Knoten, wird nur der letterCounter inkrementiert.
			if (node.children[index(letter)] == null)
				node.children[index(letter)] = new TrieNode();
			node.children[index(letter)].letterCounter += 1;
		}

	}// End of class TrieNode



	static String textString = "";
	protected TrieNode root;
	static List<String> text = new ArrayList<String>();

	// Constructor
	public TrieTree() {
		this.root = new TrieNode();
	}

	public TrieTree(String word) {
		this.root = new TrieNode();
		TrieNode currentNode = root;
		for (int i = 0; i < word.length(); i++) {
			char currentLetter = toLowerCase(word.charAt(i));
			currentNode.insert(currentNode, currentLetter);
			currentNode = currentNode.children[index(currentLetter)];
		}
	}

	/* Auxiliar functions */
	static boolean isLetter(char x) {
		int ascii = (int) x;
		return ((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123));
	}

	static char toLowerCase(char x) {
		int ascii = (int) x;
		return (ascii > 64) && (ascii < 91) ? (char) (x + 32) : x;
	}

	static int index(char letter) {
		// ascii code from 'a' = 97. Index of 'a' in array Children = 0;
		if (isLetter(letter)) {
			return ((int) toLowerCase(letter) - 97);
		}
		// ascii('0') = 48. '0' has position 27 in children, so 48-21.
		else if (((int) letter > 47 & (int) letter < 58)) {
			return ((int) toLowerCase(letter) - 21);
		} else {
			System.err.println("'" + letter + "'" + " is not a letter.");
			return 37;
		}
	}

	static TrieNode nextLetterNode(TrieNode root, char letter) {
		return root.children[index(letter)];
	}
	static int suffixes(TrieNode node){
		int suffixes = 0;
		for (int i = 0; i < 36; i++) {
			if (node.children[i]!=null&&node.children[i].isInTree){
				suffixes += node.children[i].letterCounter;
			}
		}
		return suffixes;
	}
	
	static boolean isWord(TrieNode node) {
		return (node.letterCounter - suffixes(node))>0;
	}

	/*Read the file and returns String with the words in it*/
	static void textReader(String fileName) throws IOException {
		BufferedReader in = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fileName);
			in = new BufferedReader(fr);
			String str;
			while ((str = in.readLine()) != null) {
				textString += str;
				textString += "^";// Ende der Zeile.
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			fr.close();
		}
	}
	/* deletes all the not upper/lower letters/digits*/n */
	static void textCleaner() {
		String temp = "";
		for (int i = 0; i < textString.length(); i++) {
			int ascii = (int) textString.charAt(i);
			if ((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)
					|| (ascii > 47 & ascii < 58)) {
				temp += textString.charAt(i);
			/*Umläute und ß werden "romanisiert"*/
			} else if (textString.charAt(i) == 'ü') {
				temp += "ue";
			} else if (textString.charAt(i) == 'ä') {
				temp += "ae";
			} else if (textString.charAt(i) == 'ß') {
				temp += "ss";
			} else if (textString.charAt(i) == 'ö') {
				temp += "oe";
			} else if (temp != "") {
				text.add(temp);
				temp = "";
			}
		}
	}

	/* Dictionary Operations */
	public boolean find(String word) {
		boolean found = true;
		TrieNode currentRoot = root;
		// TrieNode prev = null;
		int counter = 0;
		while (found && counter < word.length()) {
			// next letter out of word
			char currentLetter = toLowerCase(word.charAt(counter));
			currentRoot = nextLetterNode(currentRoot, currentLetter);
			if (currentRoot != null && currentRoot.isInTree) {
				counter++;
			} else
				found = false;
		}// end of while loop.
		//return found || isWord(root); checks if word is part of word
		return isWord(currentRoot) &&found;
	}

	public void insert(String word) {
		TrieNode currentRoot = root;
		int counter = 0;
		while (counter < word.length() && currentRoot != null) {
			char currentLetter = toLowerCase(word.charAt(counter));
			currentRoot.insert(currentRoot, currentLetter);
			counter++;
			currentRoot = nextLetterNode(currentRoot, currentLetter);
		}
	}

	/* Count all apparition of a word in the text.
	   Add all words in the text into the TrieTree */
	public void setupTrie(List<String> text, TrieTree tree) throws IOException {
	/******************INSTERT YOUR FILE PATH!!!****************/
		textReader("YOURPATH/test.txt");
		textCleaner();
		for (String str : text) {
			tree.insert(str);
		}
	}

	public int wordCounter(TrieTree tree, String word) {
		if (tree.find(word)) {
		    // Go to the node of the last letter of the word
			// for example, go to 'o' from "hello"
			TrieNode currentNode = tree.root;
			for (int i = 0; i < word.length(); i++) {
				currentNode = currentNode.children[index(word.charAt(i))];
			}

			// word is a prefix from the prefixCounter words
			int prefixCounter = currentNode.letterCounter;

			// word is prefixCounter - suffixes times in text
			return prefixCounter - suffixes(currentNode);
		} else {
			System.out.println("'" + word + "'" + " is not in text");
			return 0;
		}
	}

	public static void main(String arg[]) throws IOException {
		TrieTree trie = new TrieTree();
		List<Integer> words = new ArrayList<Integer>();

		trie.setupTrie(text, trie);
		
		for (int i = 0; i < text.size(); i++) {
			words.add(trie.wordCounter(trie, text.get(i)));
		}
		for (int i = 0; i < 20; i++) {
			int max = Collections.max(words);
			int index = words.indexOf(max);
			String maxWord = text.get(index);
			System.out
					.println(maxWord + ": " + trie.wordCounter(trie, maxWord));
			Collections.replaceAll(words, max, 0);
		}
	}
}// End of class TrieTree
