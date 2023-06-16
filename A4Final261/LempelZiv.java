
import java.util.*;

public class LempelZiv {
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string(Final).
	 */
	public static String compress(String input) {

	    // Handle obvious errors
	    if (input == null || input.length() == 0) {
	        throw new IllegalArgumentException("Empty / null string.");
	    }

	    StringBuilder cText = new StringBuilder(); // StringBuilder to build the compressed text
	    int cursor = 0; // Cursor to keep track of the current position in the input string
	    int windowSize = 100; // Size of the window for searching previous matches

	    while (cursor < input.length()) { // Iterate through the input string

	        int length = 1; // Length of the current match
	        int prevMatch = 0; // Position of the previous match

	        while (true) {
	            // Search for a match 
	            int match = KMP.search(input.substring(cursor, cursor + length),
	                    input.substring(Math.max(0, cursor - windowSize), cursor));

	            if (match != -1) { // If a match is found
	                prevMatch = cursor - (Math.max(cursor - windowSize, 0) + match);
	                length++; // Increment the match length
	            } else { // If no match is found
	                if (length > 1) { // If the length of the match is greater than 1
	                    // Append the compressed format of the match to the compressed text
	                    cText.append("[" + (prevMatch) + "|" + (length - 1) + "|" + input.charAt(cursor + length - 1) + "]");
	                } else { // If the length of the match is 1
	                    // Append the compressed format of a single character to the compressed text
	                    cText.append("[0|0|" + input.charAt(cursor) + "]");
	                }
	                break;
	            }

	            if (cursor + length >= input.length()) { // If the end of the input string is reached
	                // Append the compressed format of the remaining characters to the compressed text
	                cText.append("[" + (prevMatch) + "|" + (length - 1) + "|" + input.charAt(cursor + length - 1) + "]");
	                break;
	            }
	        }

	        cursor = cursor + length; // Move the cursor to the next position
	    }

	    return cText.toString(); // Return the compressed text
	}



	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public static String decompress(String compressed) {

		StringBuilder dText = new StringBuilder();

		int counter = 0;

		while (counter < compressed.length()) {
			
			/* PARSE THE TUPLES */
			
			StringBuilder prevMatch = new StringBuilder();
			StringBuilder length = new StringBuilder();
			String terminalChar = "";

			// If we have a [ then we read it and move on
			if (compressed.substring(counter, counter + 1).equals("[")) {
				counter++;
			} 

			// Parse the distance back where match found
			while (!compressed.substring(counter, counter + 1).equals("|")) {
				prevMatch.append(compressed.substring(counter, counter + 1));
				counter++;
			}

			// If we have the separator char --> Read it and move on
			if (compressed.substring(counter, counter + 1).equals("|")) {
				counter++;
			}

			// Parse the distance back where match found
			while (!compressed.substring(counter, counter + 1).equals("|")) {
				length.append(compressed.substring(counter, counter + 1));
				counter++;
			}

			// If we have the separator char --> Read it and move on
			if (compressed.substring(counter, counter + 1).equals("|")) {
				counter++;
			}

			// Read the terminal Char
			terminalChar = compressed.substring(counter, counter + 1);
			counter++;
			// Read the last ] in the tuple
			counter++;
			
			
			/* PROCESS TUPLES */
			
			// If there was no match --> Just append the terminal char
			if (prevMatch.toString().equals("0") && length.toString().equals("0")) {
				dText.append(terminalChar);
			}

			else {

				int prevMatchInt = Integer.parseInt(prevMatch.toString());
				int lengthInt = Integer.parseInt(length.toString());

				// Find the pattern in the decoded string so far
				String decodeSoFar = dText.toString();

				String prevMatchString = dText.substring(dText.length() - prevMatchInt,
						dText.length() - prevMatchInt + lengthInt);

				dText.append(prevMatchString + terminalChar);
			}
		}
		return dText.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
}
