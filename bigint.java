package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		/* IMPLEMENT THIS METHOD */
		integer=integer.trim();
		BigInteger bigInt = new BigInteger();
		int stringLength = 0;
		if(integer != null) {
			stringLength = integer.length();
		}
		if (stringLength == 0) {
			throw new IllegalArgumentException();
		}
		if(integer.charAt(0)=='-') {
			integer = integer.substring(1);
			bigInt.negative=true;
		}
		else if(integer.charAt(0)=='+') {
			integer = integer.substring(1);
		}
		for(int i=0; i<integer.length();i++) {
			if(!Character.isDigit(integer.charAt(i))) {
				throw new IllegalArgumentException();
			}
		}
		while(integer.startsWith("0")) {
			if(integer.charAt(0)=='0' && integer.length()==1) {
				return new BigInteger();
			} else {
				integer = integer.substring(1);
			}
		}
		bigInt.numDigits=integer.length();
		for(int i=0; i<integer.length();i++) {
		bigInt.front = new DigitNode(Character.getNumericValue(integer.charAt(i)), bigInt.front);
			
		}
		return bigInt;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		//Preliminary checks if front exists for both first and second
		if (first.front==null) {
			return second;
		}
		if (second.front==null) {
			return first;
		}
		//Reorders the two BigInts so I don't need to worry about magnitude
		if(compareMag(first, second)==false) {
			BigInteger temp = second;
			second=first;
			first=temp;
		}
		//Initialize sum to manipulate
		BigInteger sum = new BigInteger();
		//Case 1: ++ or --, only difference is carry the sign to sum
		//Makes sure it is both positive or negative
		if(first.negative == second.negative) {
			//Carry sign to sum
			sum.negative = first.negative;
			//Create node for sum
			sum.front = new DigitNode(0, null);
			//Pointers for all my BigInts
			DigitNode firstPtr = first.front;
			DigitNode secondPtr = second.front;
			DigitNode sumPtr = sum.front;
			//carry for the borrowing in add/subtract
			int carryOver = 0;
			//If both lists don't end, sum them up, and move along the list
			while(firstPtr!=null && secondPtr!=null) {
				//Add two digits and carry
				int sumDigit = firstPtr.digit + secondPtr.digit + carryOver;
				//Mod to get the digit for sum
				sumPtr.digit = sumDigit % 10;
				//Divide to get 10ths place, since it truncates
				carryOver = sumDigit / 10;
				//If either list still has an upcoming node, create another node for sum and move pointer
				if(firstPtr.next != null || secondPtr.next != null) {
					sumPtr.next = new DigitNode(0,null);
					sumPtr = sumPtr.next;
				}
				firstPtr = firstPtr.next;
				secondPtr=secondPtr.next;
			}
			//If first list doesn't end and second does
			if(firstPtr!=null && secondPtr ==null) {
				while(firstPtr != null) {
					int sumDigit = firstPtr.digit + carryOver;
					sumPtr.digit = sumDigit % 10;
					carryOver = sumDigit / 10;
					//If there is still a next, make node and move pointer
					if(firstPtr.next!=null) {
						sumPtr.next = new DigitNode(0,null);
						sumPtr=sumPtr.next;
					}
					firstPtr=firstPtr.next;
				}
			}
			//Copy and replace with secondPtr, no need for if-statement since magnitude is implemented
			while(secondPtr != null) {
				int sumDigit = secondPtr.digit + carryOver;
				sumPtr.digit = sumDigit % 10;
				carryOver = sumDigit / 10;
				//If there is still a next, make node and move pointer
				if(secondPtr.next!=null) {
					sumPtr.next = new DigitNode(0,null);
					sumPtr=sumPtr.next;
				}
				secondPtr=secondPtr.next;
			}
			//If carry is over 0, we make sure to start off next node with carry
			if(carryOver > 0) {
					sumPtr.next = new DigitNode(carryOver, null);
			}
		} 
		//Case 2: If one of the signs are different, hence +- or -+
		//Can treat like addition, if magnitude is taken into account and carry correctly
		if(first.negative!=second.negative) {
			//The sum will have the sign of the largest magnitude, which should be first
			//Same initializations as prior
			sum.negative = first.negative;
			sum.front = new DigitNode(0, null);
			DigitNode firstPtr = first.front;
			DigitNode secondPtr = second.front;
			DigitNode sumPtr = sum.front;
			//Needs another boolean to know if carry is necessary for following nodes
			//Edge case = 1300-90 = 1210, otherwise it will be 210 if hasCarried is not implemented
			boolean carry=false;
			boolean hasCarried=false;
			int firstDigit=0;
			//While links exist, move all pointers along
			while(firstPtr!=null && secondPtr!=null) {
				firstDigit=firstPtr.digit;
				//Decrement if there is a carry
				if(carry==true) {
					firstDigit--;
				}
				//Links exist AND first digit < second, then act as subtracting with need to carry
				//EX: 2-10 = -8 => 2-20 = -18 => 1 is the carry, 8 is the digit in LL. Similar to prior formats
				if(firstPtr !=null && secondPtr!=null && firstDigit < secondPtr.digit) {
					firstDigit+= 10;
					hasCarried=true;
				}
				//Needs modified firstDigit to account for carries when looking at long subtraction
				int difference = firstDigit - secondPtr.digit;
				sumPtr.digit = difference;
				//Move pointer along if either list still exists to account for potential length issues
				if(firstPtr.next != null || secondPtr.next !=null && second.front!=null) {
					sumPtr.next = new DigitNode(0, null);
				}
				//Move pointers along
				firstPtr=firstPtr.next;
				secondPtr=secondPtr.next;
				sumPtr=sumPtr.next;
				//If it has carried, assign it to carry so the next loop will account for it
				//1300-90 = 1210, in this the carry needs to be in place for final digit place 
				carry = hasCarried;
				//Reset hasCarried
				hasCarried = false;
			}
			//Bring the rest of the numbers in first to sum
			//Same code as above, except second is gone because first will always be longer than second, never the reverse
			//Can't manipulate firstPtr.digit above a digit unless it's an int
			while(firstPtr!=null) {
				firstDigit=firstPtr.digit;
				if(carry==true) {
					firstDigit--;
				}
				if(firstDigit < 0) {
					firstDigit+= 10;
					hasCarried=true;
				}
				sumPtr.digit=firstDigit;
				
				if(firstPtr.next != null) {
					sumPtr.next = new DigitNode(0, null);
					sumPtr = sumPtr.next;
				}
				firstPtr=firstPtr.next;
				carry=hasCarried;
				hasCarried=false;
			}
		}
		//Turn sum into a String to parse for lingering zeroes
		return parse(sum.toString());
	}

	private static boolean compareMag(BigInteger first, BigInteger second) {
		if(first.numDigits > second.numDigits) {
			return true;
		} else if(first.numDigits==second.numDigits) {
			boolean isLarger = true;
			for(DigitNode firstPtr=first.front,secondPtr=second.front;
					firstPtr!=null && secondPtr!=null;
					firstPtr=firstPtr.next,secondPtr=secondPtr.next) {
				if(firstPtr.digit > secondPtr.digit) {
					isLarger=true;
				} else if (firstPtr.digit < secondPtr.digit) {
					isLarger=false;
				}
			}
			return isLarger;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		BigInteger product = new BigInteger();
		product.front = new DigitNode(0,null);
		if(first==null || second==null) {
			return product;
		}
		//Reorganize according to magnitude
		if(compareMag(first, second)==false) {
			BigInteger temp = second;
			second=first;
			first=temp; 
		}
		int zero = 0;
		DigitNode firstPtr = first.front;
		while(firstPtr != null) {
			//Create total for each loop to add with product
			DigitNode secondPtr = second.front;
			BigInteger total = new BigInteger();
			total.front = new DigitNode(0, null);
			DigitNode totalPtr = total.front;
			
			//Provide zeroes if needed, depending on what iteration the loop is on
			int i = 0;
			while(i < zero) {
				i++;
				totalPtr.digit = 0;
				totalPtr.next = new DigitNode(0,null);
				totalPtr = totalPtr.next;
			}
			int carryOver = 0;
			//Multiply each digit in first by one digit in second while including carryOver
			int thisProduct=carryOver;
			//Same implementation as while(secondPtr!=null) in add method, with a few edits
			//This can be done since second list will end before first, unless same length
			while(secondPtr != null) {
				//Multiply current pointer digits and include carryOver
				thisProduct = firstPtr.digit * secondPtr.digit + carryOver;
				//Same logic as add for digit replacement
				totalPtr.digit = thisProduct % 10;
				carryOver = thisProduct / 10;
				
				//Make another node and move pointer along if second still exists
				if(secondPtr.next != null) {
					totalPtr.next = new DigitNode(0,null);
					totalPtr = totalPtr.next;
				}
				//Move pointer along
				secondPtr = secondPtr.next;
			}
			if(carryOver > 0) {
				totalPtr.next = new DigitNode(carryOver,null);
			}
			//Continuously add total and product using add method
			product = add(total,product);
			//Move pointer and increment zero since another iteration will move up a digit's place
			firstPtr = firstPtr.next;
			zero++;
		}
		//Provide an update to the sign of product
		//++=+ / --=+
		//+-=- / -+=-
		product.negative=false;
		if(first.negative!=second.negative) {
			product.negative=true;
		}
		//Same as add method, turn BigInt to String and parse for zeros to receive a BigInt back
		return parse(product.toString());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
