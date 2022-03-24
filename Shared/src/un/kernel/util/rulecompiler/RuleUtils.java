 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
package un.kernel.util.rulecompiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import so.util.DebugOutput;
import un.kernel.util.AbstractParserEnvironment;
import un.kernel.util.LegacyCharset;
import un.kernel.util.RuleSyntaxClasses;
import un.kernel.util.parser.RuleParser;
//import un.kernel.util.RuleSyntaxDocument;

public class RuleUtils implements  RuleSyntaxClasses, Cloneable {
	
	private final AbstractParserEnvironment environment;

    private int indentLevel = 0;
    private final LegacyCharset lc;
    private final static String TAXATIONPARSER = "taxationParseInterface";
    private final static String STRING_TYPE  ="$";
    private final static String DOUBLE_TYPE  ="%";
    private final static String READONLYMODE ="0";
    private final static String SPECIALCHAR  ="@";
    public static final String BRACEST = "[[";
    public static final String BRACEEND = "]]";
    public static final String FNC = "[F]";
    public static final String COMPILERVERSION="4.2.2_3";
    public static final String ALL ="ALL";
    public RuleUtils() {
        
    	super();
    	
        if (RuleParser.ruleParser == null) {
            RuleParser.ruleParser = new RuleParser(new ByteArrayInputStream(new byte[0]));
        } else {
            RuleParser.ruleParser.ReInit(System.in);
            
        }
        this.environment = new TaxationParserEnvironmentWithoutLng();
        lc = new LegacyCharset();
        
    }
    
    
    
    public final  ClassRule decodeRule(byte[] b,String ruleName ) {
    	//Empty rules
     	StringBuffer rule;
    	if (b == null || b.length == 0) {
    		rule = new StringBuffer();
    	} else {
    		ArrayList tokenList = environment.getTokenList();
    		ByteBuffer buffer = ByteBuffer.wrap(b);
 
    		boolean error = false;
    		Collections.sort(tokenList, new sortByCode());
    		rule = new StringBuffer();
    		buffer.position(0);
    		while (buffer.hasRemaining() && !error) {
    			if (notConstant(buffer, rule)) {
    				if (notString(buffer, rule)) {
    					if (!notKeywords(buffer, rule)) {
    						error = true;
    					}
    				}
    			}
    		}
    	}
    	//Empty rules
      
        String chaine = new String(rule);
        chaine = replaceSpecialChar(chaine,ruleName);
        chaine = CheckCondition(chaine,ruleName);
        chaine = chaine.replace(BRACEST,"(");
        chaine = chaine.replace(BRACEEND,")");
        
     // GVA <patch ID="#617 - rules with NUMERIC names do not compile" version="4.2.2" type="modification" date="June 11, 2014" author="JD">
        String className =  getClassName(ruleName);
     // GVA <patch ID="#617 - rules with NUMERIC names do not compile"/>        
        String classSource = header1 + className + header2 + className + header3 + chaine  + footer;
        return new ClassRule(className, classSource);
    }
    // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
    private String CheckCondition(String chaine, String ruleName){

    	 
    		String findChaine = "if";
    		String endFfindChaine = "{";
    		int pos = 0;
    		while(pos>=0){
    			pos = chaine.indexOf(findChaine,pos);
    			if (pos >= 0){
    				int endPos = chaine.indexOf(endFfindChaine, pos+findChaine.length());
    				if (endPos>=0){
    					String condition = chaine.substring(pos+findChaine.length(), endPos) ;
    					
    					String[] spl = condition.split("&&|\\|\\|");
    					for (int i=0;i<spl.length;i++){
    						String ssCondition = spl[i];
    						String[] ssCndSpl = ssCondition.split("!=|==|<=|>=|[<][^=]|[>][^=]");
    						for (int j=0;j<ssCndSpl.length;j++){
    							String expression = ssCndSpl[j];
    							if ((expression.contains("*")||expression.contains("/")||expression.contains("+")||expression.contains("-"))){  
    								 
    								Pattern pat= Pattern.compile("[\\\"][^\\\"].*?[\\\"]"); 
    						    	Matcher mat=pat.matcher(expression);
    						    	 HashMap<String, String> subMap= new HashMap();
    						    	 int k = 0;
    						    	while(mat.find()  ){
    						    		      
    						    		        String value =  mat.group();
    						    		        String key = "#??#Group"+k+"#??#";
    						    		        subMap.put(key, value);
    						    		        expression=  expression.replace(value, key);
    						    		        k++;
    						    		 
    						    	}
    						    	if ((expression.contains("*")||expression.contains("/")||expression.contains("+")||expression.contains("-"))){
    						    		String newExpression = expression.replace(" ","");
    						    		chaine= chaine.replace(ssCndSpl[j], newExpression);
    						    		expression = newExpression;
    						    		newExpression = MathExpressionParser.getMathExpresion(newExpression);
    						    		if (newExpression !=null){
    						    			newExpression = MathExpressionParser.doFormula(newExpression);
    						    			
    						    			if (!subMap.isEmpty()){
    						    				for (Iterator itr = subMap.entrySet().iterator(); itr.hasNext();) {
    						    					
    						    					Map.Entry<String,String> me = (Map.Entry<String,String>) itr.next();
    						    					String key = me.getKey();
    						    					String value =  me.getValue();
    						    					newExpression = newExpression.replace(key, value);
    						    				}
    						    				
    						    			}
    						    			int strPos = chaine.indexOf(expression,pos);
    						    			chaine = chaine.substring(0,strPos)+BRACEST+newExpression +BRACEEND+chaine.substring(strPos+expression.length()) ;
      						    	}
    									
    								}
    								
    							}
    						}	
    					}
    					
    				}
    				pos = endPos;
    			}
    			
    		}
    		return chaine;
    }
    // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed"/>
    
    // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
    private String replaceSpecialChar(String chaine,String ruleName){

    	String findChaine = SPECIALCHAR  + " =";
    	 
    	int pos = chaine.indexOf(findChaine);
    	if (pos >= 0){
    		chaine = chaine.replaceFirst(findChaine, " ");
    		int endPos = chaine.indexOf(";", pos);
    		chaine = chaine.substring(0, endPos-1)+")"+chaine.substring(endPos);
    		//Use of BigDecimal
    		String formula = chaine.substring(pos, chaine.indexOf(";", pos)-1);
    		String initFormula = formula; 
    		formula = formula.replace(" ","");

  			Pattern pat= Pattern.compile("[\\\"][^\\\"].*?[\\\"]"); 
  			Matcher mat=pat.matcher(formula);
  			 if (!mat.find()  ){
  				pat = Pattern.compile("[/][^/]|[/][^\\*]|[^\\*][/]|[^,][\\+]|[^,][\\-]|[^,][\\*]");
  				mat = pat.matcher(formula);
  				 if (mat.find()){
  					 String javaFormula = MathExpressionParser.doFormula(formula);
  					 endPos = chaine.indexOf(";", pos);
  					 chaine = chaine.substring(0,pos)+javaFormula + chaine.substring(pos+initFormula.length()) ;
  				 }
  				 
  			 }
     		chaine = replaceSpecialChar(chaine,ruleName);
    	}
     	return chaine;
     }
    // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed"/>
    
    protected final boolean notConstant(ByteBuffer buffer, StringBuffer rule) {
        int b = buffer.position();
        byte c = buffer.get();
        while (('0' <= c && c <= '9') || c == '.') {
            rule.append((char) c);
            c = buffer.get();
        }
        buffer.position(buffer.position() - 1);
        if (b != buffer.position()) {
            rule.append(" ");
        }
        return (b == buffer.position());
    }

    protected final boolean notString(ByteBuffer buffer, StringBuffer rule) {
        int b = buffer.position();
        byte c = buffer.get();
        String operator  = null;
        
        if (c == '"') {
            rule.append((char) c);
            String comparator = rule.substring(rule.length()-5,rule.length()-3);
            
            if (comparator.equals("==")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
            	operator=" == 0 ";
            	
            }else if (comparator.equals("!=")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
            	operator=" != 0 ";
            }else if (comparator.equals("<=")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
            	operator=" <= 0 ";
            }else if (comparator.equals(">=")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
            	operator=" >= 0 ";
            }else if (comparator.equals(" <")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
            	operator=" < 0 ";
            }else if (comparator.equals(" >")){
            	rule.delete(rule.length()-5, rule.length()) ;
            	rule.append(".compareTo(\"");
             	operator=" > 0 ";
            }
             buffer.mark();
            c = buffer.get();
            int len = 1;
            while (c != '"' && buffer.hasRemaining()) {
                c = buffer.get();
                len++;
            }
            buffer.reset();
            byte[] minibuf = new byte[len - 1];
            buffer.get(minibuf);
            buffer.get();
            String s = lc.byte2string(minibuf);
            rule.append(s);
            rule.append((char) c);
            if(operator !=null){
            	rule.append(")");
            	rule.append(operator);
            }
            rule.append(" ");
        } else {
            buffer.position(buffer.position() - 1);
        }
        return (b == buffer.position());
    }
    protected final boolean notKeywords(ByteBuffer buffer, StringBuffer rule) {
        byte c = buffer.get();
        buffer.position();
        Object token;
        ArrayList tokenList = environment.getTokenList();
        switch (c) {
        case '%':
        case '$': {
            byte b1 = buffer.get();
            // System.out.println("Byte is "+b1);
            byte b2 = buffer.get();
            // System.out.println("Byte is "+b2);
            int code = unsignedByteToInt(b1) * 256 + unsignedByteToInt(b2) - 256;
            String cs = String.valueOf(code);
            // System.out.println("Code is "+code);
            // System.out.println("String is "+cs);
            int idx = Collections.binarySearch(tokenList, cs, new sortByCode());
            if (idx >= 0) {
                token = tokenList.get(idx);
                RuleVariable ruleVar = (RuleVariable) token;
                if (ruleVar.getMode()==READONLYMODE){
                	if (ruleVar.getType()==STRING_TYPE){
                		//rule.append("//"+ruleVar.getName()   );
                		//rule.append("\n");
                		rule.append(TAXATIONPARSER+".getVarSValue"+BRACEST+ruleVar.getCode()  +BRACEEND + " ");
                	}else if (ruleVar.getType()==DOUBLE_TYPE){
                		//rule.append("//"+ruleVar.getName() );
                		//rule.append("\n");
                		// GVA <patch ID="JAVA PARSER, DoubleValue Bug #616" version="4.2.2" type="modification" date="Jun, 2014" author="ahmed">
                		//rule.append(TAXATIONPARSER+".getVarDValue("+ruleVar.getCode()  +" )" + " ");
                		rule.append(TAXATIONPARSER+".getVarDValue"+BRACEST+ruleVar.getCode()  +BRACEEND+".doubleValue"+BRACEST+BRACEEND + " ");
                		// GVA <patch ID="JAVA PARSER, DoubleValue Bug #616" version="4.2.2" type="modification" date="Jun, 2014" author="ahmed">            
                		
                	}
                	
                }else{
  	                		String nextChar = "";
                			int b = buffer.position();
                	        byte nextC = buffer.get();
                	        String firstChar = ""+((char) nextC);
                	        nextChar = nextChar + ((char) nextC);                	        
                	        
                	        nextC = buffer.get();
                	        nextChar = nextChar + ((char) nextC);                	        
                	        //buffer.position(buffer.position() - 1);
                	        buffer.position(buffer.position() - 2);
                	        
                	        if (firstChar.equals("=")&& !nextChar.equals("==") &&  !nextChar.equals("=>")&& !nextChar.equals("=<")){  // No way : && !nextChar.equals("!=")&& !nextChar.equals(">=")&& !nextChar.equals("<=")
                	        	rule.append("//"+ruleVar.getName()   );
                        		rule.append("\n");
                        		
                	        	if (ruleVar.getType()==STRING_TYPE){
                	        		rule.append(TAXATIONPARSER+".setVarSValue"+BRACEST + ruleVar.getCode() + " , " +SPECIALCHAR);  // To be replaced by setvarS or setVarD
                	        		
                	        	}else{
                	        		rule.append(TAXATIONPARSER+".setVarDValue"+BRACEST + ruleVar.getCode() + " , " +SPECIALCHAR);  // To be replaced by setvarS or setVarD
                	        	}
                	        	
                	        }else{
                	        	 
                            	if (ruleVar.getType()==STRING_TYPE){
                            		 
                            		rule.append(TAXATIONPARSER+".getVarSValue"+BRACEST + ruleVar.getCode()  +BRACEEND + " ");
                            	}else if (ruleVar.getType()==DOUBLE_TYPE){
                            		 // GVA <patch ID="JAVA PARSER, DoubleValue Bug #616" version="4.2.2" type="modification" date="Jun, 2014" author="ahmed">
                            		//rule.append(TAXATIONPARSER+".getVarDValue(" + ruleVar.getCode()  +" )" + " ");
                            		rule.append(TAXATIONPARSER+".getVarDValue"+BRACEST + ruleVar.getCode()  +BRACEEND+".doubleValue"+BRACEST+BRACEEND  + " ");
                            		// GVA <patch ID="JAVA PARSER, DoubleValue Bug #616" version="4.2.2" type="modification" date="Jun, 2014" author="ahmed">        
                            	}
                	        }
                	 
                }
            } else {
                return false;
            }
            break;
        }
        case '@': {
            byte b1 = buffer.get();
            String cs = String.valueOf(unsignedByteToInt(b1) * 1000);
            int idx = Collections.binarySearch(tokenList, cs, new sortByCode());
            if (idx >= 0) {
                token = tokenList.get(idx);
                String functionName = getFunctionName(((RuleFunction) token).getName());
                if (functionName==null)return  false;
                rule.append(TAXATIONPARSER+"."+functionName + " ");
            } else {
                return false;
            }
            break;
        }
        case '=':
        case '!':
        case '<':
        case '>': {
            String cs;
            byte b1 = buffer.get();
            if (b1 == '=') {
                /*
                 * rule.append((char)c); rule.append((char)'=' + " ");
                 */
                cs = (char) c + "=";
            } else {
                buffer.position(buffer.position() - 1);
                cs = String.valueOf((char) c);
            }
            int idx = Collections.binarySearch(tokenList, cs, new sortByCode());
            if (idx >= 0) {
                token = tokenList.get(idx);
                rule.append(((RuleKeyword) token).getJavaName()+ " ");
            } else {
                return false;
            }
            break;
        }
        default: {
            String cs = String.valueOf((char) c);
            int idx = Collections.binarySearch(tokenList, cs, new sortByCode());
            if (idx >= 0) {
                token = tokenList.get(idx);
                String s = ((RuleKeyword) token).mnemonic;
                if ("E".equals(s) || "N".equals(s)) {
                    int l = rule.length();
                    rule.delete(l - 4, l);
                }
                rule.append(((RuleKeyword) token).getJavaName());
                if (":".equals(s) || "E".equals(s)) {
                    addNewLine(rule);
                }
                if ("T".equals(s)) {
                    indentLevel++;
                    addNewLine(rule);
                }
                if ("N".equals(s) && indentLevel > 0) {
                    indentLevel--;
                }
                if (!":".equals(s) && !"T".equals(s) && !"E".equals(s)) {
                    rule.append(" ");
                }
            } else {
                return false;
            }
        }
        }
        return true;
    }
    
    protected final void addNewLine(StringBuffer rule) {
        rule.append("\n");
        if (indentLevel > 0) {
            for (int i = 0; i < indentLevel * 4; i++) {
                rule.append(" ");
            }
        }
    }
    protected int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    private final String getFunctionName(String functionName){
    	try {
    		Class taxationParserInterface  = un.asytax.taxation.TaxationParserInterface.class;
    		//Class taxationParserInterface = Class.forName("un.asytax.taxation.TaxationParserInterface");
    		
    		//Get the methods
    		Method[] methods = taxationParserInterface.getDeclaredMethods();
    		
    		//Loop through the methods  
    		for (Method method : methods) {
    			//System.out.println(method.getName());
    			if (functionName.equalsIgnoreCase(method.getName())){
    				return method.getName();
    			}
    		}
    		return null;
    		
    	}catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public  final byte[]  toBytes(Object obj) throws java.io.IOException{
		 try{
			 ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			 ObjectOutputStream oos = new ObjectOutputStream(bos); 
			 oos.writeObject(obj);
			 oos.flush(); 
			 oos.close(); 
			 bos.close();
			 byte [] data = bos.toByteArray();
			 return data;
			 
		 }catch(Exception  ioe){
			 DebugOutput.print("Error : convert Object  to byte");
			 return null;
		 }
	  }
    
    public final static Object toObject(byte[] bytes){
		  
		 Object object = null;
		 try{
			 ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInputStream ois = new ObjectInputStream(bis);
			 object = ois.readObject();
			 ois.close();
			 bis.close();
			 
		 }catch(Exception e){
			 DebugOutput.print("error : convert Object  to byte" + e.getMessage());
			 return null;
		 } 
		 return object;
	}
    
    // GVA <patch ID="#617 - rules with NUMERIC names do not compile" version="4.2.2" type="modification" date="June 11, 2014" author="JD">

    public static  String getClassName(String ruleName){
    	
    	String className = ruleName.replaceAll("\\W", "_");
    	return "T_" + className.trim()+ "Rule";

    }
    // GVA <patch ID="#617 - rules with NUMERIC names do not compile" version="4.2.2" type="modification" date="June 11, 2014" author="JD"/>

    public class TaxationParserEnvironmentWithoutLng extends un.asygate.taxation.TaxationParserEnvironment{
    	
    
    	protected  String lng(String property) {
    		return property;
    	}
    }
     
    private String header1 = "package un.kernel.util.rulecompiler;"+
    						"\n\n"+
    						//"import so.kernel.core.KDocument;"+
    						//"\n\n"+
    						"import java.math.BigDecimal;\n"+
    						"import un.asytax.taxation.TaxationParserInterface;\n"+
    						"import un.kernel.util.rulecompiler.GlobalVariables;\n"+
    						"\n\n"+
    						"\n\n"+
    						"\n\n"+
    						"\n\n"+
							"public class " ;
    
    private String header2 =  " extends un.kernel.util.rulecompiler.AbstractTaxationRule { "+
							"\n\n"+
							"public TaxationParserInterface taxationParseInterface ;"+
							"\n\n"+
							"public GlobalVariables globalVariables ;"+
							"\n\n"+
							"\n\n"+
							"public ";
    
    private String header3 = "(TaxationParserInterface taxationParseInterface,GlobalVariables globalVariables){"+
							"\n\n"+
							"this.taxationParseInterface = taxationParseInterface;"+
							"\n\n"+
							"this.globalVariables = globalVariables;"+
							"\n\n"+
							"}"+
							"\n\n"+
							"\n\n"+
							"\n\n"+
							"\n\n"+
							"\n\n"+
							"\n\n"+
							 "public String getVersion(){"+
							"\n\n"+
							"return \""+ COMPILERVERSION +"\";"+
							"\n\n"+
							"}"+
							"\n\n"+
							//"public void apply(KDocument doc ){  "+
							"public void apply(){  "+
							"\n\n"+
							"globalVariables.setVersionError(!checkVersion());"+
							"\n\n"+
						 	"if (!checkVersion()){"+
							"	globalVariables.addError(globalVariables.ERRCMPMSG);"+
							"\n\n"+
							"   return;"+
							"\n\n"+
							"};"+
						 	
    						"\n\n"+
    						"\n\n";
    private String footer = " \n\n } \n\n\n }";   //" System.out.println(rule); \n\n } \n\n\n }";
    public  String getVersion(){
    	return  COMPILERVERSION ;
    }
}
