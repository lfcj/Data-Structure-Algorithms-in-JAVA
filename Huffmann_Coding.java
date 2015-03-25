import java.io.*;
import java.util.*;

public class Huffmann {

	/****************** Start of class HuffmannNODE **********/
	public class HuffmannNode {
		public int weight;
		public int asciiLetter;
		public HuffmannNode leftChild;
		public HuffmannNode rightChild;
		boolean isLeft, isRight;
		public HuffmannNode father;

		public HuffmannNode(int[] letter) {
			this.weight = letter[1];
			this.asciiLetter = letter[0];
		}

		public HuffmannNode(HuffmannNode left, HuffmannNode right) {
			this.weight = left.weight + right.weight;
			this.leftChild = left;
			this.rightChild = right;
			this.leftChild.father = this;
			this.rightChild.father = this;
			this.leftChild.isLeft = true;
			this.rightChild.isRight = true;
		}

	}

	/******************* END OF CLASS HuffmannNODE ******************/

	/****************** START OF CODING METHODS ******************/
	/*
	 * only characters from the ascii table that are probably used in a text
	 * will be counted, specifically from code 32(space) to code 253(ý)
	 */
	static int[][] lettersFrequency = new int[254][2];
	static List<String> text = new ArrayList<String>();

	/* Step1. Count letters */

	public void textCounter(List<String> text) {
		// initialize lettersFrequency
		for (int i = 0; i < 254; i++)
			lettersFrequency[i][1] = 0;
		// count characters
		int ascii = 0;
		for (String strLine : text) {
			for (int i = 0; i < strLine.length(); i++) {
				ascii = (int) strLine.charAt(i);
				lettersFrequency[ascii - 32][0] = ascii;
				lettersFrequency[ascii - 32][1]++;
			}
		}
		// remove array of characters that are not in text.
		lettersFrequency = removeZeros(lettersFrequency);
	}

	static String printArray(int[] array) {
		String res = "[";
		if (array.length != 0) {
			for (int i = 0; i < array.length - 1; i++) {
				res += array[i];
				res += " ,";
			}
			res += array[array.length - 1];
			res += "]";
			return res;
		} else
			return "";
	}

	/* Build Huffmann tree */
	// Step1. Sort weight of letters in a queue, and create nodes of same weight
	// letters, or single nodes, if there are not 2 letters with the same weight

	public List<HuffmannNode> buildTree(int[][] lettersFreq) {
		List<HuffmannNode> buffer = new ArrayList<HuffmannNode>();
		// buffers has a node containing every leaf (single).
		for (int i = 0; i < lettersFreq.length; i++)
			buffer.add(new HuffmannNode(lettersFreq[i]));
		buffer = putNodesTogether(buffer);
		return buffer;
	}

	// Step3. Connect every two lightest nodes.
	public List<HuffmannNode> putNodesTogether(List<HuffmannNode> nodes) {
		while (nodes.size() > 1) {
			buildHeap(nodes);
			HuffmannNode leftChild = nodes.get(0);
			nodes.remove(0);
			buildHeap(nodes);
			HuffmannNode rightChild = nodes.get(0);
			nodes.remove(0);
			nodes.add(new HuffmannNode(leftChild, rightChild));
		}
		return nodes;
	}

	// this method combines findLeaves to get all leaves that contain the
	// letters, and method findCode, which finds the code for a single leaf.
	// It returns an array in which we can find code for letter (char)i at
	// position i-32.
	public int[][] arrayOfCodes(HuffmannNode tree) {
		int[][] codes = new int[254][0];
		List<HuffmannNode> emptyList = new ArrayList<HuffmannNode>();
		// find all leaves of list.
		List<HuffmannNode> leaves = findLeaves(tree, emptyList);
		// find code for every leaf.
		for (HuffmannNode leaf : leaves) {
			LinkedList<Integer> code = findCode(leaf);
			int index_ascii = code.pop() - 32;
			// copy code into temporary array.
			int[] temp = new int[code.size()];
			for (int i = 0; i < code.size(); i++) {
				temp[i] = code.get(i);
			}
			// place code at index with ascii code of original letter -32.
			codes[index_ascii] = temp;
		}
		return codes;

	}

	// starting down from the leaf, we go up and write a 0, if
	// currentNode is a leftChild, or 1 if it is a rightChild.
	public LinkedList<Integer> findCode(HuffmannNode leaf) {
		LinkedList<Integer> code = new LinkedList<Integer>();
		HuffmannNode currentNode = leaf;
		while (currentNode.father != null) {
			if (currentNode.isLeft) {
				code.addFirst(0);
			} else if (currentNode.isRight) {
				code.addFirst(1);
			}
			currentNode = currentNode.father;
		}
		// every code has its corresponding letter(ascii code) at the front.
		code.addFirst(leaf.asciiLetter);

		return code;
	}

	// returns all leaves of the tree in a list.
	public List<HuffmannNode> findLeaves(HuffmannNode tree,
			List<HuffmannNode> leaves) {
		if (tree != null) {
			if (tree.asciiLetter != 0)
				leaves.add(tree);
			if (tree.leftChild != null)
				findLeaves(tree.leftChild, leaves);
			if (tree.rightChild != null)
				findLeaves(tree.rightChild, leaves);
		}
		return leaves;
	}

	/* Final Step: Substitute every character in text with its code */
	public List<Integer> codeText(List<String> text, int[][] codes) {
		List<Integer> codedText = new ArrayList<Integer>();
		for (String strLine : text) {
			for (int i = 0; i < strLine.length(); i++) {
				int index = (int) strLine.charAt(i) - 32;
				// copy code into codedText
				for (int j = 0; j < codes[index].length; j++) {
					codedText.add(codes[index][j]);

				}
			}
		}
		return codedText;
	}

	/**************** END OF CODING METHODS ********************/

	/*************** START OF DECODING METHODS ******************/

	public String decode(HuffmannNode tree, List<Integer> codedText) {
		String text = "";
		int i = 0;
		int size = codedText.size();
		int direction = 0;
		HuffmannNode currentNode = tree;
		while (i < size) {
			direction = codedText.get(i);
			// go to the left
			if (direction == 0 && currentNode.leftChild != null)
				currentNode = currentNode.leftChild;
			// go the right
			else if (direction == 1 && currentNode.rightChild != null)
				currentNode = currentNode.rightChild;
			// leaf found, copy to text and go back to root
			else if (currentNode.asciiLetter != 0) {
				if ((char) currentNode.asciiLetter == '$')
					text += "\n";
				else {
					text += (char) currentNode.asciiLetter;
				}
				currentNode = tree;
				i--;
			}
			i++;
		}
		return text;
	}

	/**************** AUXILIAR FUNCTIONS ***********************/
	static void textReader(String fileName) throws IOException {
		BufferedReader in = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fileName);
			in = new BufferedReader(fr);
			String str;
			while ((str = in.readLine()) != null) {
				text.add(str);
				text.add("$");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			fr.close();
		}
	}

	// remove letters with weight zero.
	static int[][] removeZeros(int[][] array) {
		List<int[]> temp = new ArrayList<int[]>();
		for (int[] letter : array) {
			if (letter[1] != 0)
				temp.add(letter);
		}
		int[][] zeroLessArray = new int[temp.size()][2];
		for (int i = 0; i < temp.size(); i++)
			zeroLessArray[i] = temp.get(i);
		return zeroLessArray;
	}

	static void swap(int[][] array, int index1, int index2) {
		int[] temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	static void buildHeap(List<HuffmannNode> list) {
		int size = list.size();
		for (int i = (int) Math.floor(size / 2); i >= 0; i--) {
			heap(list, i);
		}
	}

	static void heap(List<HuffmannNode> list, int parentIndex) {
		int size = list.size();
		int left = parentIndex * 2 + 1;
		int right = parentIndex * 2 + 2;
		int smallest = parentIndex;
		if (size > left && list.get(left).weight < list.get(parentIndex).weight) {
			smallest = left;
		}

		if (size > right && list.get(right).weight < list.get(smallest).weight) {
			smallest = right;
		}
		if (smallest != parentIndex) {
			java.util.Collections.swap(list, parentIndex, smallest);
			heap(list, smallest);
		}
	}

	/****************** END OF AUXILIAR FUNCTIONS *********************/

	public static void main(String[] args) throws IOException {
		Huffmann code = new Huffmann();
		String filename = "PATH_TO_FILE";
		textReader(filename);
		code.textCounter(text);
		List<HuffmannNode> tree = code.buildTree(lettersFrequency);
		int[][] codes = code.arrayOfCodes(tree.get(0));
		List<Integer> codedText = code.codeText(text, codes);
		int i = 0;
		int bitsInCodedText = codedText.size();
		int size = bitsInCodedText;
		//Print text nicely:
		while (i < size) {
			int j = i + 50;
			if (j > size) {
				j = size - 1;
			}
			System.out.println(codedText.subList(i, j));
			i += 50;
		}
		i = 0;
		String decodedText = code.decode(tree.get(0), codedText);

		System.out.println(decodedText);

	}
}
