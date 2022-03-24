 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
package un.kernel.util.rulecompiler;


import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpressionParser {

	 
	public static String doFormula(String equation) {
        
        Stack<Operator> operatorStack;
        Stack<String> operandStack;
        String valOne, valTwo, newVal;
        Operator temp;

         
        StringTokenizer tokenizer = new StringTokenizer(equation, " +-*/()^", true);
        String token = "";
        operandStack = new Stack();
        operatorStack = new Stack();
        try {
                while(tokenizer.hasMoreTokens()){   
                    token = tokenizer.nextToken();
                    if (token.equals(" ")) {  
                        continue;
                    }
                    else if (!Operator.isOperator(token)){  
                        operandStack.push(token);
                    }
                    else if (token.equals("(")) {
                        operatorStack.push(Operator.getOperator(token));
                    }
                    else if (token.equals(")")) {  
                        while (!((temp = operatorStack.pop()).isStartBrace())) {
                            valTwo = operandStack.pop();
                            valOne = operandStack.pop();
                            newVal = temp.doCalc(valOne, valTwo);
                            operandStack.push(newVal);
                        }
                    }
                    else {  
                        while (true) {  
                            if ((operatorStack.empty()) || (operatorStack.peek().isStartBrace()) || 
                                (operatorStack.peek().getPrecedenceLevel() < Operator.getOperator(token).getPrecedenceLevel())) {
                                    operatorStack.push(Operator.getOperator(token));
                                    break;  
                            }
                            temp = operatorStack.pop();
                            valTwo = operandStack.pop();
                            valOne = operandStack.pop();
                             
                            newVal = temp.doCalc(valOne, valTwo);
                            operandStack.push(newVal);
                        }
                    }
                }
                
                
                while(!operatorStack.isEmpty()) {
                	temp = operatorStack.pop();
                	valTwo = operandStack.pop();
                	valOne = operandStack.pop();
                	newVal = temp.doCalc(valOne, valTwo);
                	operandStack.push(newVal);
                }
                
                String formula = operandStack.pop();
               
                return  formula;
            }
            catch (Exception e) {
                e.printStackTrace();
                return equation;
            }

        }
	
	public static String getMathExpresion(String expression){
			Pattern pat= Pattern.compile("[(]");
	    	Matcher matcher=pat.matcher(expression);
	    	 
	    	int nbrBraceStart = 0;
	    	int nbrBraceEnd = 0; 
	    	while(matcher.find()) {
	    		nbrBraceStart ++;
	        }
			  pat= Pattern.compile("[)]");
			  matcher=pat.matcher(expression);
	    	   
			  while(matcher.find()) {
				  nbrBraceEnd ++;
		        }
	    	
	    	 int diff = nbrBraceStart-nbrBraceEnd;  
	    	 if (diff >0){
	    		 expression = expression.substring(diff);
	    		 
	    	 }else if (diff <0){
	    		 expression = expression.substring(0,expression.length()+diff);
	    	 }
	    	 return expression;

	}
//	public static String getMathExpresion_NO(String expression){
//    	 Stack<String> braceStack;
//         Stack<String> expStack;
//         String valOne, valTwo, newVal;
//         Operator temp;
//
//         //initalize
//         StringTokenizer tokenizer = new StringTokenizer(expression, " +-*/()^", true);
//         String token = "";
//         expStack = new Stack();
//         braceStack = new Stack();
//         String exp = "";
//         try {
//             while(tokenizer.hasMoreTokens()){ 
//                 token = tokenizer.nextToken();
//                 if (token.equals(" ")) {  
//                	 continue;
//                 }
//                 else if ("+-*/^".contains(token)) {
//                	 if (exp.equals("")){
//                		 expStack.push(token);
//                	 }else{
//                		 exp = exp+token;
//                	 }
//                	 
//                 }
//                 else if (!token.equals("(")&&!token.equals(")")){  
//                     exp = exp+token;
//                 }
//                 else if (token.equals("(")) {
//                	 braceStack.push(token);
//                      if (!exp.equals("")){
//                    	 expStack.push(exp);
//                    	 exp="";
//                     }
//                 }
//                 else if (token.equals(")")) { 
//                	 if (!braceStack.empty()){
//                		 if (!exp.equals("")){
//                			 exp = braceStack.pop()+exp + token;
//                			 expStack.push(exp);
//                			 exp="";
//                		 }else{
//                			 while (!expStack.isEmpty()){
//                            	 exp = expStack.pop()+exp;
//                             }
//                			 exp =  braceStack.pop()+exp + token;
//                			 expStack.push(exp);
//                			 exp="";
//                		 }
//                	 }
//                	 
//                 }
//             }
//             while (!expStack.isEmpty()){
//            	 exp = expStack.pop()+exp;
//             }
//             
//             return exp;
//         }catch (Exception ex){
//        	 return null;
//         }
//    }
	
	 
	
}
