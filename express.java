package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	Variable var;
    	Array arr;
    	String delim = " \t*+-/()]";
    	expr.replaceAll("\\s+","");
    	expr=expr.replace("[", "[ ");
//    	System.out.println(expr);
    	StringTokenizer str = new StringTokenizer(expr.trim(),delim); //delim->delims
    	//str.countTokens();
    	boolean isArr = false;
    	while(str.hasMoreTokens()==true) {
    		String tok = str.nextToken();
//    		System.out.println(tok);
    		for(int i=0;i<tok.length();i++) {
    			if(tok.charAt(i)=='[') { //Implement a way to detect duplicates in arraylist EX: A + A + A, don't add all 3
    				isArr=true;
    				break;
    			} else {
    				isArr=false;
    			}
    		}
    		if(tok.substring(0,1).matches("[0-9]")) {
    			continue;
    		}
    		if(isArr==true) {
		    		arr = new Array(tok.substring(0,tok.indexOf('[')));
//					System.out.println(arr + "Add this token to arr");
		    	if(!arrays.contains(arr)) {
					arrays.add(arr);
					isArr=false; //reset boolean
	//				break;
    			}
			} else {
					var = new Variable(tok);
				if(!vars.contains(var)) {
//		    		System.out.println(var + "Add this token to var");
		    		vars.add(var);
				}
			}
    	}
    }

  //a+b+A[0]+B[0]+d
    /*
     * a-b+10
     * a
     * b
     * A[2] + B[2]
     * A[
     * B[
     */
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	float ans = 0;
    	expr=removeSpace(expr);
    	expr=replaceVariables(expr,vars);
    	StringTokenizer full = new StringTokenizer(expr,delims,true);
    	Stack<String> order = new Stack<>();
		int pSize=0, bSize=0;
    	int run =0;
//    	Maybe count parentheses to see how many times I run through full?
//    	for(int i=0;i<expr.length();i++) {
//    		if(expr.charAt(i)=='[' || expr.charAt(i)=='(') {
//    			run++;
//    		}
//    	}
//    	
	    	while(full.hasMoreTokens()) {
	    		order.push(full.nextToken());
	//    		Arrays
	    		if(order.peek().equals("[")) {
	    			bSize=order.size();
	    		}
	    		if (order.peek().equals("]")) {
	    			String tempB="";
	    			//First ]
	    			order.pop();
	    			for(int i=order.size();i>bSize;i--) {
	    				tempB = order.pop() + tempB;
	    			}
	    			//Then [
	    			order.pop();
	    			System.out.println("Before Eval " + tempB);
	    			tempB=simpleEval(tempB);
	    			System.out.println("After Eval " +tempB);
	//    			Remove 22.0
	    			if(tempB.contains(".")) {
	    				tempB="["+tempB.substring(0, tempB.indexOf(".")) +"]";
	    			} else {
	    				tempB="["+tempB+"]";
	    			}
	    			System.out.println("Should be Array " + tempB);
	    			tempB=order.pop()+tempB;
	    			System.out.println("Final tempB is "+tempB);
	//    			StringTokenizer name = new StringTokenizer(tempB,delims,true);
	    			String arrVal = replaceArrays(tempB,arrays);
	    			order.push(arrVal);
	    		}
	//    		Parent
	    		if(order.peek().equals("(")) {
	    			pSize=order.size();
	    		}
	    		if (order.peek().equals(")")) {
	    			String tempP="";
	    			//First ]
	    			order.pop();
	    			for(int i=order.size();i>pSize;i--) {
	    				tempP = order.pop() + tempP;
	    				System.out.println(tempP);
	    			}
	    			//Then [
	    			order.pop();
	    			tempP=simpleEval(tempP);
	    			System.out.println("SimpEval "+tempP);
	    			order.push(tempP);
	    		}
    }
	    	if(!order.isEmpty()) {
	    		
	    	}
//    	
    	Stack<String>concate = new Stack<>();
    	while(!order.isEmpty()) {
    		concate.push(order.pop());
    	}
    	String eval = "";
    	while(!concate.isEmpty()) {
    		eval=eval+concate.pop();
    	}
    	StringTokenizer finalcheck = new StringTokenizer(eval,delims,true);
    	int multiValue = 0;
    	while(finalcheck.hasMoreTokens()) {
    		String firstL=finalcheck.nextToken();
    		char f = firstL.charAt(0);
    		if(Character.isDigit(f)) {
    			multiValue++;
    		}
    	}
//    	If more than 1 value, simplify it
    	if(multiValue>1) {
    		eval=simpleEval(eval);
    	}
    	
    	
    	System.out.println("Eval is " + eval);
    	
    	ans=Float.parseFloat(eval);
//		a+b*(A[a]+d)+A[B[b]] - test case
//		A[b-a] + B[2]
//		(a+b)+A[2]
//		a+b+A[2]+d+B[2] - SIMPLE variables, SIMPLE arrays
//    	A[b-2]
//    	A[(d-c)]
//    	A[3]+B[4]
//    	A[B[0]]
//    	d*(c-B[a+CARR[0]-A[1]+b])
//    	A[B[CARR[1]*2]]/2
    	return ans;
    	
    }
    private static float onlyNumber(String expression){
		return Float.parseFloat(expression);
	}
    private static String addSpace(String expr) {
    	return expr=expr.replace("[", "[ ");
    }
    private static String removeSpace(String expr) {
    	expr.trim();
    	expr=expr.replaceAll("\\s+","");
    	return expr;
    }
    private static String replaceVariables(String expr, ArrayList<Variable> vars) {
    	String temp = expr;
    	StringTokenizer str = new StringTokenizer(temp, delims);
    	while(str.hasMoreTokens()) {
    		String tok = str.nextToken();
    		for(Variable var : vars) {
    			if (var.name.equals(tok))
    				temp = temp.replaceAll(var.name, String.valueOf(var.value));
    		}
    	}
    	return temp;
    }
    private static String Math(String first, String second, char x) {
    	float ans=0;
    	float a = Float.parseFloat(first);
    	float b = Float.parseFloat(second);
    	switch (x) {
	    	case '+':
	    		ans = a + b;
	    		break;
	    	case '-':
	    		ans = a - b;
	    		break;
	    	case '*':
	    		ans = a * b;
	    		break;
	    	case '/':
	    		ans = a / b;
	    		break;
    	}
    	String result = Float.toString(ans);
    	return result;
    }
    private static String replaceArrays(String expr, ArrayList<Array> arrays) {
    	String temp = expr;
    	String noBrackets =  " \t*+-/()";
    	ArrayList<String> repArr = new ArrayList<>();
    	StringTokenizer str = new StringTokenizer(temp, noBrackets,true);
    	while(str.hasMoreTokens()) {
    		repArr.add(str.nextToken());
    	}
    	System.out.println("repArr is "+repArr.toString());
//    	Loop through array of string and check if it is an array
//    	anything that starts with a letter is guaranteed to be akin to A[1] or varB[2]
    	for(int i=0; i<repArr.size();i++) {
    		String isLetter = repArr.get(i);
    		char first = isLetter.charAt(0);
    		if(Character.isLetter(first)) {
//    			Find index of '[' and substring with string.length
    			String arrName = isLetter.substring(0, isLetter.indexOf('['));
//    			Leaving us with A from A[1], now we find array A in ArrayList and substring to get index
    			System.out.println("arrName is "+arrName);
//    			Can only solve SIMPLE arrays, A[B[1]] will NOT WORK
    			String innerIndex = isLetter.substring(isLetter.indexOf('[')+1, isLetter.indexOf(']'));
    			int inIndex = Integer.valueOf(innerIndex);
    			System.out.println(innerIndex);
    			for(Array arr : arrays) {
    				if (arr.name.equals(arrName))
    					repArr.set(i, String.valueOf(arr.values[inIndex]));
    			}
    		}
    	}
    	String concate = new String();
    	for(int i=0; i<repArr.size();i++) {
    		concate+=repArr.get(i);
    	}
    	
    	
    	return concate;
    }
    private static float makeFloat(String numVal) {
    	float result = 0;
    	if(numVal.matches("[0-9]*")) {
    		result = Float.parseFloat(numVal);
    		return result;
    	} else {
    		return result;
    	}
    }
    private static String simpleEval(String express) {
//    	3-4*5
//    	String into arraylist
    	String onlyOp =  " \t*+-/";
    	ArrayList<String> repArr = new ArrayList<>();
    	StringTokenizer str = new StringTokenizer(express, onlyOp,true);
    	while(str.hasMoreTokens()) {
    		repArr.add(str.nextToken());
    	}
	    while(repArr.contains("*") || repArr.contains("/")) {
	    	for(int i=0;i<repArr.size();i++) {
	    		if(repArr.get(i).equals("*")) {
	    			String res = Math(repArr.get(i-1),repArr.get(i+1),'*');
	    			repArr.set(i-1, res);
	    			repArr.remove(i);
	    			repArr.remove(i);
	    		}else
	    		if(repArr.get(i).equals("/")) {
	    			String res = Math(repArr.get(i-1),repArr.get(i+1),'/');
	    			repArr.set(i-1, res);
	    			repArr.remove(i);
	    			repArr.remove(i);
	    		}
	    	}
    	}
	    while(repArr.contains("+") || repArr.contains("-")) {
	    	for(int i=0;i<repArr.size();i++) {
	    		if(repArr.get(i).equals("+")) {
	    			String res = Math(repArr.get(i-1),repArr.get(i+1),'+');
	    			repArr.set(i-1, res);
	    			repArr.remove(i);
	    			repArr.remove(i);
	    		}else if(repArr.get(i).equals("-")) {
	    			String res = Math(repArr.get(i-1),repArr.get(i+1),'-');
	    			repArr.set(i-1, res);
	    			repArr.remove(i);
	    			repArr.remove(i);
	    		}
	    	}
	    }
    	String concate = new String();
    	for(int i=0; i<repArr.size();i++) {
    		concate+=repArr.get(i);
    	}
    	System.out.println(concate);
    	return concate;
    }
}
