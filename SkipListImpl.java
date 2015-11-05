import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * Class representing a Skip List
 * 
 * @author ADK
 *
 * @param <T>
 *            represents a generic class definition
 */
public class SkipListImpl<T extends Comparable<? super T>> implements
		SkipList<T> {

	// Dummy head node for skip list
	Node<T> head;

	// Maximum node level for skip list
	int maxLevel;

	// Length of skip list
	int size;
	
	/**
	 * Constructor. Head node is given negative infinity value.
	 */
	SkipListImpl() {
		maxLevel = 0;
		size = 0;
		head = new Node(Long.MIN_VALUE, maxLevel);

	}

	/**
	 * 
	 * @author ADK
	 *
	 * @param <T>
	 *            represents a generic class definition
	 */

	private class Node<T> {

		// Stores the data
		T data;

		// Array of node pointers. One array entry for each level.
		Node<T> next[];

		/**
		 * Constructor
		 * 
		 * @param x
		 *            Data value for the node
		 * @param level
		 *            Number of levels of the node
		 */

		Node(T x, int level) {
			data = x;
			next = (Node<T>[]) new Node[level + 1];

		}
	}

	public static void main(String[] args) {
		
		 Scanner sc = null;
		
		 if (args.length > 0) {
		 File file = new File(args[0]);
		 try {
		 sc = new Scanner(file);
		 } catch (FileNotFoundException e) {
		 e.printStackTrace();
		 }
		 } else {
		 sc = new Scanner(System.in);
		 }
		 String operation = "";
		 long operand = 0;
		 int modValue = 997;
		 long result = 0;
		 Long returnValue = null;
		 SkipListImpl<Long> skipList = new SkipListImpl<Long>();
		 // Initialize the timer
		 long startTime = System.currentTimeMillis();
		
		 while (!((operation = sc.next()).equals("End"))) {
		 switch (operation) {
		 case "Add": {
		 operand = sc.nextLong();
		 skipList.add(operand);
		 result = (result + 1) % modValue;
		 break;
		 }
		 case "Ceiling": {
		 operand = sc.nextLong();
		 returnValue = skipList.ceiling(operand);
		 if (returnValue != null) {
		 result = (result + returnValue) % modValue;
		 }
		 break;
		 }
		 case "FindIndex": {
		 int intOperand = sc.nextInt();
		 returnValue = skipList.findIndex(intOperand);
		 if (returnValue != null) {
		 result = (result + returnValue) % modValue;
		 }
		 break;
		 }
		 case "First": {
		 returnValue = skipList.first();
		 if (returnValue != null) {
		 result = (result + returnValue) % modValue;
		 }
		 break;
		 }
		 case "Last": {
		 returnValue = skipList.last();
		 if (returnValue != null) {
		 result = (result + returnValue) % modValue;
		 }
		 break;
		 }
		 case "Floor": {
		 operand = sc.nextLong();
		 returnValue = skipList.floor(operand);
		 if (returnValue != null) {
		 result = (result + returnValue) % modValue;
		 }
		 break;
		 }
		 case "Remove": {
		 operand = sc.nextLong();
		 if (skipList.remove(operand)) {
		 result = (result + 1) % modValue;
		 }
		 break;
		 }
		 case "Rebuild": {
		 skipList.rebuild();
		 break;
		 }
		 case "Disp": {
		 skipList.disp();
		 break;
		 }
		 case "Contains": {
		 operand = sc.nextLong();
		 if (skipList.contains(operand))
		 result = (result + 1) % modValue;
		 break;
		 }
		 }
		 }
		
		 // End Time
		 long endTime = System.currentTimeMillis();
		 long elapsedTime = endTime - startTime;
		
		 System.out.println(result + " " + elapsedTime);

	}

	/**
	 * Adds a new node to the skip list
	 * 
	 * @param x
	 *            data for new node to be added
	 * 
	 */
	@Override
	public void add(T x) {
		if(contains(x))
			return;
		size++;
		
		Random rand = new Random();
		int levelCount = 0, nextToss = 0, i;
		nextToss = rand.nextInt(2);
		Node<T> newNext[];

		// Coin toss like event for determining number of levels for new node
		while (nextToss != 0) {
			levelCount++;
			nextToss = rand.nextInt(2);
		}

		// Increasing maxLevel and size of head if required
		if (levelCount > maxLevel) {
			newNext = (Node<T>[]) new Node[levelCount + 1];
			for (i = 0; i <= maxLevel; i++) {
				newNext[i] = head.next[i];
			}
			

			head.next = newNext;
			maxLevel = levelCount;

		}

		Node<T> scannerNode = head;

		// Allocating new node
		Node<T> newNode = new Node<>(x, levelCount);

		// Finding correct position for node at each level and connecting
		for (i = maxLevel; i >= 0; i--) {
			while (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) < 0) {
				scannerNode = scannerNode.next[i];
			}
			if (i <= levelCount) {
				newNode.next[i] = scannerNode.next[i];
				scannerNode.next[i] = newNode;
			}
		}

	}

	/**
	 * @return Returns an iterator for the skip list
	 */
	@Override
	public Iterator<T> iterator() {
		return null;
	}

	/**
	 * Function to display skip list
	 */
	public void disp() {
		Node<T> temp = head;

		for (int i = 0; i <= maxLevel; i++) {
			temp = head.next[i];
			while (temp != null) {
				System.out.print(temp.data + " ");
				temp = temp.next[i];
			}
			System.out.println("");
		}

	}

	/**
	 * Finds the previous node of the given node/data in the skip list
	 * 
	 * @param x
	 *            Data of node whose previous node is to be found
	 * @return Previous node
	 */
	Node<T> getPrevNode(T x) {
		Node<T> scannerNode = head;
		for (int i = maxLevel; i >= 0; i--) {
			while (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) < 0) {
				scannerNode = scannerNode.next[i];
			}
		}
		return scannerNode;
	}

	/**
	 * @return Returns size of skip list
	 */
	@Override
	public int size() {

		return size;
	}

	/**
	 * @return Returns data of the first node in the skip list
	 */
	@Override
	public T first() {
		if (head.next[0] == null)
			return null;
		return head.next[0].data;
	}

	/**
	 * @return Returns data of the last node in the skip list
	 */
	@Override
	public T last() {
		Node<T> scannerNode = head;
		for (int i = maxLevel; i >= 0; i--) {
			while (scannerNode.next[i] != null)
				scannerNode = scannerNode.next[i];
		}
		if (scannerNode == head)
			return null;
		return scannerNode.data;
	}

	/**
	 * @return True : Skip List is empty
	 */
	@Override
	public boolean isEmpty() {

		return head.next[0] == null;
	}

	/**
	 * @return Returns least skip list element(data) which is greater than or
	 *         equal to x
	 */
	@Override
	public T ceiling(T x) {
		Node<T> scannerNode = getPrevNode(x);

		if (scannerNode.next[0] == null)
			return null;
		return scannerNode.next[0].data;

	}

	/**
	 * @return Returns greatest skip list element(data) which is less than or
	 *         equal to x
	 */
	@Override
	public T floor(T x) {
		Node<T> scannerNode = getPrevNode(x);

		if (scannerNode.next[0] != null
				&& scannerNode.next[0].data.compareTo(x) == 0)
			return scannerNode.next[0].data;
		else if (scannerNode == head)
			return null;
		else
			return scannerNode.data;

	}

	/**
	 * @return True : If skip list contains x
	 */
	@Override
	public boolean contains(T x) {
		
		Node<T> scannerNode = head;

		for (int i = maxLevel; i >= 0; i--) {
			while (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) < 0) {

				scannerNode = scannerNode.next[i];
			}
			if (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) == 0)
				return true;
		}
		
		return false;
	}

	/**
	 * Removes the given element from skip list
	 * 
	 * @return True : If element is deleted. False : Element not found
	 */
	@Override
	public boolean remove(T x) {
        if(!contains(x))
        	return false;
		Node<T> scannerNode = head;
		
		for (int i = maxLevel; i >= 0; i--) {
			while (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) < 0) {
				scannerNode = scannerNode.next[i];
			}
			if (scannerNode.next[i] != null
					&& scannerNode.next[i].data.compareTo(x) == 0) {
				scannerNode.next[i] = scannerNode.next[i].next[i];
				
			}

		}
		
			size--;		
		
		return true;
	}

	/**
	 * Rebuilds the given skip list into a perfect skip list
	 */
	@Override
	public void rebuild() {

		// Calculating number of levels needed based on size(length) of skip list
		
		int newMaxLevel = ((int) (Math.log10(new Double(size)) / Math
				.log10(new Double(2))) - 1);

		

		// Temporary array to store next pointers for the node
		Node<T> newNext[];

		// Temporary array to maintain the most recent nodes at each level
		Node<T> prevNodes[];
		newNext = (Node<T>[]) new Node[newMaxLevel + 1];
		prevNodes = (Node<T>[]) new Node[newMaxLevel + 1];

		// Setting initial values for newNext and prevNodes
		newNext[0] = head.next[0];
		prevNodes[0] = head;
		for (int i = 1; i <= newMaxLevel; i++) {
			newNext[i] = null;
			prevNodes[i] = head;
		}
		head.next = newNext;
		maxLevel = newMaxLevel;

		Node<T> scannerNode = head.next[0];

		// levelCheckFactor is used to determine the number of levels for a node
		int levelCheckFactor = (int) Math.pow(new Double(2), new Double(
				maxLevel));

		// for loop for each node in the base level of list
		for (int i = 1; i <= size; i++) {
			int currentNodeLevel = maxLevel;
			int currentLevelCheck = levelCheckFactor;

			// Calulating number of levels for the node
			while (i % currentLevelCheck != 0 && currentNodeLevel != 0) {
				currentLevelCheck = currentLevelCheck / 2;
				currentNodeLevel--;
			}

			// Allocating new next array for the node with above calculated level
			
			
			newNext = (Node<T>[]) new Node[currentNodeLevel + 1];

			// 0th level next is simply the next node
			newNext[0] = scannerNode.next[0];

			// Connecting the node to previous nodes at all relevant levels
			for (int j = 0; j <= currentNodeLevel; j++) {
				prevNodes[j].next[j] = scannerNode;
				prevNodes[j] = scannerNode;
			}

			
			// updating the next array for the node
			scannerNode.next = newNext;
			

			scannerNode = scannerNode.next[0];
		}

	}

	/**
	 * @param n
	 *            index of node
	 * @return Returns the data of the node at index n
	 */
	@Override
	public T findIndex(int n) {

		Node<T> scannerNode = head;
		int counter = 0;
		if (n > size || n < 1)
			return null;
		while (counter < n) {
			scannerNode = scannerNode.next[0];
			counter++;
		}

		return scannerNode.data;

	}

}