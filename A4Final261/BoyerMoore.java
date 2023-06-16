import java.util.*;

/**
 * Implementation for BoyerMoore String Search algorithm(Final).
 * 
 * Based on the implementation from: https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_string-search_algorithm
 * 
 * @author wgrbu
 *
 */
public class BoyerMoore {

	/**
	 * Primary method for BoyerMoore string search. 
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static int search(String pattern, String text) {

		// Handle Empty / null String
		if (pattern.length() == 0 || pattern == null) {
			return 0;
		}

		// Create char arrays for easier handling
		char p[] = pattern.toCharArray();
		char t[] = text.toCharArray();

		// Create tables
		int jumpTable[] = createJumpTable(p);
		int badCharTable[] = createBadCharTable(p);

		// Search loop
		for (int i = p.length - 1, j; i < t.length;) {
			for (j = p.length - 1; p[j] == t[i]; --i, --j) {
				if (j == 0) {
					// If inner loop completes and j == 0 --> We found a match, return start index
					return i;
				}
			}
			// If no match found, determine the max no of chars to jump ahead
			// Based on badChar rule & goodSuffix rule
			i += Math.max(badCharTable[p.length - 1 - j], jumpTable[t[i]]);
		}
		// We didn't find a match.
		return -1;
	}

	/**
	 * Generates the jump table used in the Boyer-Moore algorithm. 
	 * The jump table is used to determine the number of positions 
	 * to jump ahead in the text when a mismatch occurs between the 
	 * pattern and the text during the search.
	 * @param p
	 * @return
	 */
	private static int[] createJumpTable(char[] p) {
		// Max value of a char possible + 1
		final int alphabetSize = Character.MAX_VALUE + 1;
		// Array to store jump values
		int table[] = new int[alphabetSize];
		// Set each element in table to length of pattern
		// Ensures that if a char is encountered in text that not present
		// in pattern, jump value will be equal to the length of the pattern
		for (int i = 0; i < table.length; ++i) {
			table[i] = p.length;
		}
		// Determine the jump value for each char in p
		for (int i = 0; i < p.length; ++i) {
			table[p[i]] = p.length - 1 - i;
		}

		return table;
	}

	/**
	 * Generates the bad character table used in the Boyer-Moore algorithm. 
	 * The bad character table helps determine the maximum number of positions 
	 * to shift the pattern when a mismatch occurs during the search.
	 * @param p
	 * @return
	 */
	private static int[] createBadCharTable(char[] p) {

		int table[] = new int[p.length];

		int lastPrefixPos = p.length;

		for (int i = p.length; i > 0; --i) {
			if (isPrefix(p, i)) {
				lastPrefixPos = i;
			}
			table[p.length - i] = lastPrefixPos - i + p.length;
		}

		for (int i = 0; i < p.length - 1; ++i) {
			int suffixLen = suffixLen(p, i);
			table[suffixLen] = p.length - 1 - 1 + suffixLen;
		}
		return table;
	}

	/**
	 * Determines whether char starting from index x form a prefix of p
	 * @param p
	 * @param x
	 * @return
	 */
	private static boolean isPrefix(char[] p, int x) {
		for (int i = x, j = 0; i < p.length; ++i, ++j) {
			if (p[i] != p[j]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculates the length of the longest suffix (a palindrome) 
	 * in the given character array p, starting from index x.
	 * @param p
	 * @param x
	 * @return
	 */
	private static int suffixLen(char[] p, int x) {
		int len = 0;
		for (int i = x, j = p.length - 1; i >= 0 && p[i] == p[j]; --i, --j) {
			len += 1;
		}
		return len;

	}
}