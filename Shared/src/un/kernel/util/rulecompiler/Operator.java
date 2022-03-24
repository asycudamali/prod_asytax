 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
package un.kernel.util.rulecompiler;


	
	
	
	//enum for Operator "objects"
     
	
	public enum Operator {

	    ADD("+", 1)
	    {
	        String doCalc(String d1, String d2) {
	            return RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d1+RuleUtils.BRACEEND+".add"+RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d2+RuleUtils.BRACEEND+RuleUtils.BRACEEND+RuleUtils.BRACEEND+".doubleValue"+RuleUtils.BRACEST+RuleUtils.BRACEEND;
	        }
	    },
	    SUBTRACT("-",1)
	    {
	        String doCalc(String d1, String d2) {
	            //return d1-d2; 
	           // return (BigDecimal.valueOf(d1).subtract(BigDecimal.valueOf(d2))).doubleValue();
	            return RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d1+RuleUtils.BRACEEND+".subtract"+RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d2+RuleUtils.BRACEEND+RuleUtils.BRACEEND+RuleUtils.BRACEEND+".doubleValue"+RuleUtils.BRACEST+RuleUtils.BRACEEND;
	        }
	    },
	    MULTIPLY("*", 2)
	    {
	        String doCalc(String d1, String d2) {
	           // return d1*d2;
	           // return (BigDecimal.valueOf(d1).multiply(BigDecimal.valueOf(d2))).doubleValue();
	            return RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d1+RuleUtils.BRACEEND+".multiply"+RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d2+RuleUtils.BRACEEND+RuleUtils.BRACEEND+RuleUtils.BRACEEND+".doubleValue"+RuleUtils.BRACEST+RuleUtils.BRACEEND;
	            

	        }
	    },
	    DIVIDE("/",2)
	    {
	        String doCalc(String d1, String d2) {
	            //return d1/d2;
	            //return (BigDecimal.valueOf(d1).divide(BigDecimal.valueOf(d2))).doubleValue();
	            return RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d1+RuleUtils.BRACEEND+".divide"+RuleUtils.BRACEST+"BigDecimal.valueOf"+RuleUtils.BRACEST+d2+RuleUtils.BRACEEND+RuleUtils.BRACEEND+RuleUtils.BRACEEND+".doubleValue"+RuleUtils.BRACEST+RuleUtils.BRACEEND;
	        }
	    },
	    STARTBRACE("(", 0)
	    {
	        String doCalc(String d1, String d2) {
	            return  "";
	        }
	    },
	    ENDBRACE(")",0)
	    {
	        String doCalc(String d1, String d2) {
	            return " ";
	        }
	    },
	    EXP("^", 3)
	    {
	        String doCalc(String d1, String d2) {
	            return "" ;// Math.pow(d1,d2);
	            
	        }
	    };

	    private String operator;
	    private int precedence;
	   
		

	    private Operator(String operator, int precedence) {
	        this.operator = operator;
	        this.precedence = precedence;
	    }

	    public int getPrecedenceLevel() {
	        return precedence;
	    }

	    public String getSymbol() {
	        return operator;
	    }

	    public static boolean isOperator(String s) {
	        for(Operator op : Operator.values()) { //iterate through enum values
	            if (op.getSymbol().equals(s))
	                return true;
	        }
	        return false;
	    }

	    public static Operator getOperator(String s) 
	        throws InvalidOperatorException {
	            for(Operator op : Operator.values()) { //iterate through enum values
	                if (op.getSymbol().equals(s))
	                    return op;
	            }
	            throw new InvalidOperatorException(s + " Is not a valid operator!");
	    }

	    public boolean isStartBrace() {
	        return (operator.equals("("));
	    }
	    //overriding calculation provided by each enum part
	    abstract String doCalc(String d1, String d2);
	}
	//error to be thrown/caught in ProjectOne.java
	class InvalidOperatorException extends Exception {
	    public InvalidOperatorException() {
	    }

	    public InvalidOperatorException(String s) {
	        super(s);
	    }
	}



	
	


