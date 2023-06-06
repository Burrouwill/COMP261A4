/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public static int search(String pattern, String text) {
		try {
			// Handle IllegalArgs
			if (pattern == null || text == null) {
				throw new IllegalArgumentException("Text and/or Pattern is null.");
			}
			if (pattern.length() > text.length()) {
				throw new IllegalArgumentException("Pattern longer than Text.");
			}
			if (pattern.equals("") || text.equals("")) {
				throw new IllegalArgumentException("Empty Pattern and/or Text.");
			}

			// Calc Jump Table
			int[] prefixLength = calcJumpTable(pattern);

			// Initialise variables to be used
			int tLength = text.length();
			int pLength = pattern.length();
			int t = 0;
			int p = 0;

			// Start search through text
			while (t < tLength) {
				if (pattern.charAt(p) == text.charAt(t)) {
					p++;
					t++;

					if (p == pLength) { // If we have matched entire pattern --> Return starting index
						return t - p;
					}

				} else { // If we fail to match a char
					p = prefixLength[p]; // Set p to jump value

					if (p < 0) { // Handles the first value from Jump Table (-1)
						t++;
						p++;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		return -1; // We couldnt find it
	}

	/**
	 * Calculates the Jump Table for the pattern, to be used in the KMP main
	 * algorithm.
	 * 
	 * @param pattern
	 * @return
	 * @author wgrbu
	 */
	public static int[] calcJumpTable(String pattern) {

		int patternLength = pattern.length();
		int[] jumpTable = new int[patternLength + 1];
		jumpTable[0] = -1; // Set special case 1st value of jump table

		int prefixLength = 0;
		int i = 1;

		while (i < patternLength) {
			if (pattern.charAt(prefixLength) == pattern.charAt(i)) {
				prefixLength++;
				i++;
				jumpTable[i] = prefixLength;
			} else if (prefixLength > 0) {//
				prefixLength = jumpTable[prefixLength];
			} else { // Prefix length 0 so set that in array and move on one char
				i++;
				jumpTable[i] = 0;
			}
		}
		return jumpTable;
	}

}
