package app;

import java.io.*;
import java.util.*;

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
	    	/** COMPLETE THIS METHOD **/
	    	/** DO NOT create new vars and arrays - they are already created before being sent in
	    	 ** to this method - you just need to fill them in.
	    	 **/
public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		Stack<String> words = new Stack<String>();
		String tokens = null;
		expr = expr.replaceAll(" ", "");
		StringTokenizer std = new StringTokenizer(expr, delims, true);
		
		while(std.hasMoreTokens()) {
			tokens = std.nextToken();
			System.out.println(tokens);
			
			//add token to the stack if it has a letter/open bracket. 
			if(tokens.charAt(0) == '[' || Character.isLetter(tokens.charAt(0))) {
			        words.push(tokens);
				}
			 }
			//holds the string we pop temporarily. 
		String holder = null; 
		//pops the stack to see if the string popped is a variable or an array. 
		while(words.isEmpty() == false) { // or false
			holder = words.pop();
			System.out.println("The current popped character from the stack ---> " + holder);
			//pop takes one from size
			if(holder.equals("[")) {
				Array myarray = new Array(words.pop()); // arraylist holds the same value in myarray
				if(arrays.contains(myarray) == true) {
					continue;
				}
				else {
						arrays.add(myarray);
					}
				}
			else {
				Variable myvari = new Variable(holder);
				
				if(vars.contains(myvari) != false) {
					continue;
					}
				else {
					vars.add(myvari);
					}
				}
			 }
			
		  }
    
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
    	// following line just a placeholder for compilation
    	Stack<Float> floaties = new Stack<Float>();
    	Stack<Character> chars = new Stack<Character>();
    	Stack<String> stringarray = new Stack<String>();
    	expr = expr.replaceAll("\\s+", "");
    	char[] tokens = expr.toCharArray();
    	
    	for (int i = 0; i < tokens.length; i++) 
    	{
    		// Check for number
    		if (tokens[i] >= '0' && tokens[i] <= '9') 
    		{
    			String space = "";
    			int j = i;
    			
    			while (j < tokens.length && tokens[j] >= '0' && tokens[j] <= '9') 
    			{
    				space += tokens[j];
    				j++;
    			}
    			i = j - 1;
    			floaties.push((float)Integer.parseInt(space));
    		}
    		
    		// Check for variable
    		else if ((tokens[i] >= 'a' && tokens[i] <= 'z') || (tokens[i] >= 'A' && tokens[i] <= 'Z')) 
    		{
    			String varsname = "";
    			int j = i;
    			
    			while (j < tokens.length && ((tokens[j] >= 'a' && tokens[j] <= 'z') || (tokens[j] >= 'A' && tokens[j] <= 'Z'))) 
    			{
    				varsname += tokens[j];
    				j++;
    			}
    			i = j - 1;
    			
    			// If its the last index in the array it has to be a variable
    			if (i == tokens.length - 1) 
    			{
    				for (int k = 0; k < vars.size(); k++) 
        			{
        				if (varsname.equals(vars.get(k).name)) 
    					{
        					floaties.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			}
    			// If it doesn't have a [ after the variable its still a variable
    			else if (tokens[i + 1] != '[' && i <= tokens.length - 1) 
    			{
    				for (int k = 0; k < vars.size(); k++) 
        			{
        				if (varsname.equals(vars.get(k).name)) 
    					{
        					floaties.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			}
    			// If it has a [ after the variable it has to be an array
    			else if (tokens[i + 1] == '[' && i <= tokens.length - 1) 
    			{
    				for (int k = 0; k < arrays.size(); k++) 
    				{
    					if (varsname.equals(arrays.get(k).name)) 
    					{
    						stringarray.push(arrays.get(k).name);
    					}
    				}
    			}
    			
    		}
    		// Check for left parenthesis
    		else if (tokens[i] == '(') 
    		{
    			chars.push(tokens[i]);
    		}
    		
    		// Check for right parenthesis
    		else if (tokens[i] == ')') 
    		{
    			while (chars.peek() != '(') 
    			{
    				floaties.push(doOperation(chars.pop(), floaties.pop(), floaties.pop()));
    			}
    			chars.pop();
    		}
    		// Check for left bracket
    		else if (tokens[i] == '[') 
    		{
    			chars.push(tokens[i]);
    		}
    		
    		// Check for right bracket
    		else if (tokens[i] == ']') 
    		{
    			while (chars.peek() != '[') 
    			{
    				floaties.push(doOperation(chars.pop(), floaties.pop(), floaties.pop()));
    			}
    			float temp = floaties.pop();
				for (int k = 0; k < arrays.size(); k++) 
				{
					if (stringarray.peek().equals(arrays.get(k).name)) 
					{
						floaties.push((float)arrays.get(k).values[(int) temp]);
						stringarray.pop();
						break;
					}
				}
				chars.pop();
    		}
    		
    		// Check for operator
    		else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') 
    		{
    			while ((chars.isEmpty() == false) && hasPrecedence(tokens[i], chars.peek())) 
    			{
    				floaties.push(doOperation(chars.pop(), floaties.pop(), floaties.pop()));
    			}
    			chars.push(tokens[i]);
    		}
    	}
    	
    	while (chars.isEmpty() == false)
    	{
    		floaties.push(doOperation(chars.pop(), floaties.pop(), floaties.pop()));
    	}
    	
    	return floaties.pop();
    }
    private static boolean hasPrecedence(char current, char stack) 
    {
    	if ((stack == '+' || stack == '-') && (current == '*' || current == '/')) 
    	{
    		return false;
    	}
    	else if (stack == '(' || stack == ')') 
    	{
    		return false;
    	}
    	
    	else if (stack == '[' || stack == ']') 
    	{
    		return false;
    	}
    	else 
    	{
    		return true;
    	}
    }
    private static float doOperation(char charboi, float number2, float number1) 
    {
    	switch(charboi) 
    	{
    	case '-':
    		return (number1 - number2);
    	case '*': 
    		return (number1 * number2);
    	case '+':
    		return (number2 + number1);
    	case '/':
    		if (number2 == 0) 
    		{
    			throw new UnsupportedOperationException("Can't divide by zero");
    		}
    		return (number1 / number2);
    	}
    	return 0;
    }
}
