package ueb7;

import java.io.*;
import java.util.*;

public class TrieTree {
	/* subclass TrieNode */
	public class TrieNode {
		private boolean isInTree;
		/*
		 * Dieser Baum kann nur die 26 Buchstaben des englischen Alphabets. und
		 * die Zahlen von 0 bis 9 Die 26-te Stelle ist immer leer. So vermeiden
		 * wir Fehler bei der Suche von nicht Buchstaben. find(zeichen)->in
		 * children[26] suchen->false
		 */
		TrieNode[] children = new TrieNode[37];
		int letterCounter;

		// Konstruktor
		public TrieNode() {
			this.isInTree = true;
		}

		public void einfuege(TrieNode node, char letter) {
			//Existiert schon der Knoten, wird nur der letterCounter inkrementiert.
			if (node.children[index(letter)] == null)
				node.children[index(letter)] = new TrieNode();
			node.children[index(letter)].letterCounter += 1;
		}

	}// End of class TrieNode

	static String textString = "";
	protected TrieNode root;
	static List<String> text = new ArrayList<String>();

	// Konstruktoren
	public TrieTree() {
		this.root = new TrieNode();
	}

	public TrieTree(String word) {
		this.root = new TrieNode();
		TrieNode currentNode = root;
		for (int i = 0; i < word.length(); i++) {
			char currentLetter = toLowerCase(word.charAt(i));
			currentNode.einfuege(currentNode, currentLetter);
			currentNode = currentNode.children[index(currentLetter)];
		}
	}

	/* Hilfsfunktionen */
	static boolean isLetter(char x) {
		int ascii = (int) x;
		return ((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123));
	}

	static char toLowerCase(char x) {
		int ascii = (int) x;
		return (ascii > 64) && (ascii < 91) ? (char) (x + 32) : x;
	}

	static int index(char letter) {
		// ascii code von 'a' = 97. Index von 'a' in array Children = 0;
		if (isLetter(letter)) {
			return ((int) toLowerCase(letter) - 97);
		}
		// ascii('0') = 48. '0' hat Stelle 27 in children, also 48-21.
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

	/* liest Datei und gibt String mit ihren Woertern aus */
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

	/* loescht alle nicht gross/klein Buchstaben/Ziffern */
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

	/* Worterbuchoperationen */
	public boolean find(String word) {
		boolean found = true;
		TrieNode currentRoot = root;
		// TrieNode prev = null;
		int counter = 0;
		while (found && counter < word.length()) {
			// naechstes Buchstabe aus Wort
			char currentLetter = toLowerCase(word.charAt(counter));
			currentRoot = nextLetterNode(currentRoot, currentLetter);
			if (currentRoot != null && currentRoot.isInTree) {
				counter++;
			} else
				found = false;
		}// end of while loop.
		//return found || isWord(root); findet, ob Wort mind. Teilwort ist.
		return isWord(currentRoot) &&found;
	}

	public void einfuege(String word) {
		TrieNode currentRoot = root;
		int counter = 0;
		while (counter < word.length() && currentRoot != null) {
			char currentLetter = toLowerCase(word.charAt(counter));
			currentRoot.einfuege(currentRoot, currentLetter);
			counter++;
			currentRoot = nextLetterNode(currentRoot, currentLetter);
		}
	}

	/* Alle Vorkommen eines Worts wort in einen Text zaehlen */
	// fuege alle Worter vom Text in TrieTree ein
	public void setupTrie(List<String> text, TrieTree tree) throws IOException {
		textReader("/home/lfcj/workspace/ALP3/test.txt");
		textCleaner();
		for (String str : text) {
			tree.einfuege(str);
		}
	}

	public int wordCounter(TrieTree tree, String word) {
		if (tree.find(word)) {
			// geht zum Node vom letzten Buchstabe aus Wort.
			// zB, aus "hallo", geht zu 'o'
			TrieNode currentNode = tree.root;
			for (int i = 0; i < word.length(); i++) {
				currentNode = currentNode.children[index(word.charAt(i))];
			}

			// word ist Prefix von prefixCounter Woertern
			int prefixCounter = currentNode.letterCounter;

			// word ist prefixCounter - suffixes Mal im Text
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
