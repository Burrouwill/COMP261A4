
/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */

import java.util.*;
import java.util.regex.Pattern;

public class HuffmanCoding {

	/**
	 * Inner Class to act as leaf
	 */
	record Node(double weight, int code, String symbol, Node leftChild, Node rightChild) implements Comparable<Node> {

		/**
		 * Compares Nodes based on weight
		 */
		public int compareTo(Node o) {

			// Compare lexicographically if dealing with two char nodes
			if (this.weight == o.weight && this.symbol != null && o.symbol != null) {
				return this.symbol.compareTo(o.symbol);
			}
			// Else compare by weight
			return Double.compare(this.weight, o.weight);
		}
	}

	// Fields for Huffman Class
	Node HuffmanTree;

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		// Get a collection of unique chars
		Set<String> symbolsUsed = new HashSet<>();

		// collection of chars in text
		List<String> textList = new ArrayList<>();

		// Populate list of text & list of symbols used
		for (int i = 0; i < text.length(); i++) {
			symbolsUsed.add(text.substring(i, i + 1));
			textList.add(text.substring(i, i + 1));
		}

		// Create queue of nodes for each symbol & their weights based on how common
		// they are in the text
		PriorityQueue<Node> nodes = new PriorityQueue<>();

		for (String letter : symbolsUsed) {
			double freq = textList.stream().filter((s) -> (s.equals(letter))).count();
			nodes.add(new Node(freq, -1, letter, null, null));
		}

		// Build the tree
		while (nodes.size() != 1) {
			// Get the two lowest weighted nodes off the front of the queue
			Node n1 = nodes.poll();
			Node n2 = nodes.poll();

			// Set binary values for left and right child
			Node leftChild = new Node(n1.weight(), 0, n1.symbol(), n1.leftChild(), n1.rightChild());
			Node rightChild = new Node(n2.weight(), 1, n2.symbol(), n2.leftChild(), n2.rightChild());

			// Set the two nodes as child nodes of a new node, return new node to queue
			double combinedWeight = leftChild.weight() + rightChild.weight();
			nodes.add(new Node(combinedWeight, -1, null, leftChild, rightChild));
		}

		// Set the Tree field to be the tree
		HuffmanTree = nodes.poll();
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should return
	 * the encoded text as a binary string, that is, a string containing only 1 and
	 * 0.
	 */
	public String encode(String text) {
		StringBuilder encodedString = new StringBuilder();
		String code = "";
		for (int i = 0; i < text.length(); i++) {
			encodedString.append(encodeHelper("" + text.charAt(i), HuffmanTree, code));
		}
		return encodedString.toString();
	}

	/**
	 * Helper method to traverse the binary tree and retrieve the code for a given
	 * character
	 *
	 * @param letter        The character to encode
	 * @param encodedString The StringBuilder to store the encoded result
	 * @param node          The current node in the binary tree
	 */
	public String encodeHelper(String letter, Node node, String code) {
		// Base case: Handle null node
		if (node == null) {
			return null;
		}

		// Base case: We found the symbol that we are looking for, return its code
		if (node.symbol() != null && node.symbol().equals(letter)) {
			code = code + node.code();
			return code;
		}

		// Check if the current node's code is -1 (indicating the first node)
		if (node.code() == -1) {
			// Skip appending the code and continue traversal
			String leftCodeFirst = encodeHelper(letter, node.leftChild(), code);
			if (leftCodeFirst != null) {
				return leftCodeFirst;
			}

			String rightCodeFirst = encodeHelper(letter, node.rightChild(), code);
			if (rightCodeFirst != null) {
				return rightCodeFirst;
			}

		}
		// Recurse on left child
		String leftCode = encodeHelper(letter, node.leftChild(), code);
		if (leftCode != null) {
			return node.code() + leftCode;
		}

		// Recurse on right child
		String rightCode = encodeHelper(letter, node.rightChild(), code);
		if (rightCode != null) {
			return node.code() + rightCode;
		}

		// If we reach this point, it means the current node and its descendants do not
		// contain the symbol we're looking for
		return null;

	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree, and
	 * return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		StringBuilder text = new StringBuilder();
		// Pointer Node for navigating the tree
		Node pointer = HuffmanTree;

		// For each 1/0 in the encoded text
		for (int i = 0; i < encoded.length(); i++) {

			// Get the 1/0
			String binCode = encoded.substring(i, i + 1);

			if (binCode.equals("0")) {
				pointer = pointer.leftChild();
			}
			else if (binCode.equals("1")) {
				pointer = pointer.rightChild();
			}
			// Once we find a letter, add it to the text and reset the pointer to the root
			if (pointer.symbol() != null) {
				text.append(pointer.symbol());
				pointer = HuffmanTree;
			}
		}
		return text.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print out
	 * the encoding tree.
	 */
	public String getInformation() {
		return "";
	}

	/**
	 * Prints the Binary Tree when passed the root
	 * 
	 * @param root
	 */
	public void printBinaryTree(Node root) {
		System.out.println("\n\t\t*Parent ------------> Leaf*\n");
		printBinaryTreeRecursive(root, 0);
	}

	/**
	 * Helper Method to recursively print the Binary Tree.
	 * 
	 * @param node
	 * @param level
	 */
	private void printBinaryTreeRecursive(Node node, int level) {
		if (node == null) {
			return;
		}

		// Print the right subtree
		printBinaryTreeRecursive(node.rightChild, level + 1);

		// Print indentation based on the level
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}

		// Print the node
		System.out.println(node.symbol + " " + node.code());

		// Print the left subtree
		printBinaryTreeRecursive(node.leftChild, level + 1);
	}

}
