package ueb8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BruteForce {

	static List<String> text = new ArrayList<String>();

	/*
	 * List contains Arrays with position coordinates from pattern. For example: 
	 * Muster = "word". Output: {[3,19,20,21,22], [10,2,3,4,5]}, meaning "word" 
	 * is in rows 3 and 10, in positions 19-22 and 2-5.
	 */

	public List<int[]> bruteForce(List<String> text, String muster) {
		List<int[]> positions = new ArrayList<int[]>();
		int zeile = 0;
		int isIn = 0;
		int verschiebung = 0;
		int m = muster.length();
		for (String strLine : text) {
			int n = strLine.length();
			while (verschiebung < m+1 ) {
				for (int i = verschiebung; i < n; i++) {
					int musterIndex = (i - verschiebung) % m;
					char textChar = strLine.charAt(i);
					char musterChar = muster.charAt(musterIndex);
					if (textChar == musterChar) {
						isIn++;
						// if muster is complete in text, create array with
						// position of muster in text
						if (isIn == m
								&& strLine.charAt(Math.abs(i - m + 1)) == muster
										.charAt(0)) {
							int[] wordPosition = new int[m + 1];
							wordPosition[0] = zeile;
							for (int j = 1; j < m + 1; j++)
								wordPosition[j] = i - m + j;
							int posSize = positions.size();
							if (posSize != 0) {
								if (isNotEqual(positions, wordPosition))
									positions.add(wordPosition);
							} else {
								positions.add(wordPosition);
							}
							isIn = 0;
						}
						
					} else {
						isIn = 0;
					}
				}
				verschiebung++;
				isIn =0;
			}
			zeile++;
			verschiebung = 0;
		}
		return positions;
	}

	/*
	 * Document is saved in Liste, every row is an element of the
	 * static list text
	 */
	static void textReader(String fileName) throws IOException {
		BufferedReader in = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fileName);
			in = new BufferedReader(fr);
			String str;
			while ((str = in.readLine()) != null) {
				text.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			fr.close();
		}
	}
	/*Hilfsfunktionen*/
	static String printArray(int[] array) {
		String res = "[";
		for (int i = 0; i < array.length - 1; i++) {
			res += array[i];
			res += " ,";
		}
		res += array[array.length - 1];
		res += "]";
		return res;
	}
	
	static boolean isNotEqual(List<int[]> positions, int[] pos){
		int row = pos[0];
		int index = positions.size() -1;
		boolean different = true;
		while(index>=0){
			if(positions.get(index)[0]==row){
				if(positions.get(index)[1]==pos[1])
					different = false;
			}
			else
				break;
			index--;
		}
		return different;
	}
	

	public static void main(String[] args) throws IOException {
		BruteForce bF = new BruteForce();
		String fileName = "PATH_TO_YOUR_FILE";
		textReader(fileName);
		bF.bruteForce(text, "PATTERN_TO_FIND");
		List<int[]> listMobys = bF.bruteForce(text, "Moby");
		List<int[]> listCaps = bF.bruteForce(text, "captain");
		System.out.println("PATTERN_TO_FIND_REPETITIONS: " + listMobys.size());
	}

}
