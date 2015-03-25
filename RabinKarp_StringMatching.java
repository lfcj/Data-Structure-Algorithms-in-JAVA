import java.io.*;
import java.util.*;

public class RabinKarp {
	static List<String> text = new ArrayList<String>();

	public List<int[]> rabinKarp(List<String> text, String pattern) {
		List<int[]> positions = new ArrayList<int[]>();
		int row = 0;
		for (String strLine : text) {
			/*
			 * We take the 127 standard codes of the Ascii table as 
			 * basis d.
			 */
			int basis = 127;
			int tLen = strLine.length();
			int mLen = pattern.length();
			int hashT = 0;
			int hashM = 0;
			int asciiT = 0;
			int p = 31; // prime number
			int h = (int) Math.pow(basis, mLen - 1) % p;
			for (int i = 0; i < mLen; i++) {
				int asciiM = (int) pattern.charAt(i);
				hashM += (asciiM * Math.pow(basis, mLen - i - 1)) % p;
				asciiT = (int) strLine.charAt(i);
				hashT += (asciiT * Math.pow(basis, mLen - i - 1)) % p;
			}
			for (int s = 0; s < tLen - mLen + 1; s++) {
				if (hashT == hashM) {
					if (pattern == strLine.substring(s, s + mLen)) {
						int[] coordinates = new int[mLen + 1];// +1 for row.
						coordinates[0] = row;
						positions.add(coordinates);
					}
				} else {
					asciiT = (int) strLine.charAt(s);
					int nextAscii = (int) strLine.charAt(s + mLen);
					hashT = (basis *hashT - (asciiT*h) + nextAscii)%p;
				}

			}
			row++;
		}

		return positions;
	}

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

	public static void main(String[] args) throws IOException {
		RabinKarp rk = new RabinKarp();
		textReader("PATH_TO_FILE");
		rk.rabinKarp(text, "PATTERN_TO_FIND");
	}
}// End of RabinKarp class
