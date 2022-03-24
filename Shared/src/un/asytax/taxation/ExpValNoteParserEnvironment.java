/*
 * CriteriaParserEnvironment.java
 *
 * Created on November 5, 2004, 3:27 PM
 */

package un.asytax.taxation;

import un.asygate.taxation.TaxationParserEnvironment;
import un.kernel.util.AbstractParserEnvironment;
import un.kernel.util.RuleSyntaxTokens;


/**
 * 
 * @author u_site TaxationParserEnvironment
 */
//public class ExpValNoteParserEnvironment extends AbstractParserEnvironment implements RuleSyntaxTokens { // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
public class ExpValNoteParserEnvironment extends TaxationParserEnvironment implements RuleSyntaxTokens {
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed" >
	/*
	public String[][] getVariables() {
		String[][] variables = { { "%", "0", "001", 
			lng("Inv"), 
			lng("Invoice amount in foreign currency") },
				{ "$", "0", "002", 
			lng("InvCur"), 
			lng("Invoice currency code") },
				{ "%", "0", "003", 
			lng("InvCurRat"), 
			lng("Invoice currency exchange rate") },
				{ "%", "1", "004", 
			lng("InvNcy"), 
			lng("Invoice amount in national currency") },
				{ "%", "0", "005", 
			lng("Ifr"), 
			lng("Internal freight amount in foreign currency") },
				{ "$", "0", "006", 
			lng("IfrCur"), 
			lng("Internal freight currency code") },
				{ "%", "0", "007", 
			lng("IfrCurRat"), 
			lng("Internal freight currency exchange rate") },
				{ "%", "1", "008", 
			lng("IfrNcy"), 
			lng("Internal freight amount in national currency") },
				{ "%", "0", "009", 
			lng("Ded"), 
			lng("Deductions amount in foreign currency") },
				{ "$", "0", "010", 
			lng("DedCur"), 
			lng("Deductions currency code") },
				{ "%", "0", "011", 
			lng("DedCurRat"), 
			lng("Deductions currency exchange rate") },
				{ "%", "1", "012", 
			lng("DedNcy"), 
			lng("Deductions amount in national currency") },
				{ "%", "1", "013", 
			lng("TotalMass"), 
			lng("Total gross mass") }, { "%", "1", "014", 
			lng("TotalCost"), 
			lng("Total costs") },
				{ "%", "1", "015", 
			lng("FOBVal"), 
			lng("FOB value in national currency") },
				{ "%", "0", "017", 
			lng("ItmInv"), 
			lng("Item invoice amount in foreign currency") },
				{ "$", "0", "018", 
			lng("ItmInvCur"), 
			lng("Item invoice currency code") },
				{ "%", "0", "019", 
			lng("ItmInvCurRat"), 
			lng("Item invoice currency exchange rate") },
				{ "%", "1", "020", 
			lng("ItmInvNcy"), 
			lng("Item invoice amount in national currency") },
				{ "%", "1", "021", 
			lng("ItmIfr"), 
			lng("Item internal freight amount in foreign currency") },
				{ "$", "0", "022", 
			lng("ItmIfrCur"), 
			lng("Item internal freight currency code") },
				{ "%", "0", "023", 
			lng("ItmIfrCurRat"), 
			lng("Item internal freight currency exchange rate") },
				{ "%", "1", "024", 
			lng("ItmIfrNcy"), 
			lng("Item internal freight amount in national currency") },
				{ "%", "1", "025", 
			lng("ItmDed"), 
			lng("Item deductions amount in foreign currency") },
				{ "$", "0", "026", 
			lng("ItmDedCur"), 
			lng("Item deductions currency code") },
				{ "%", "0", "027", 
			lng("ItmDedCurRat"), 
			lng("Item deductions currency exchange rate") },
				{ "%", "1", "028", 
			lng("ItmDedNcy"), 
			lng("Item deductions amount in national currency") },
				{ "%", "1", "029", 
			lng("ItmTotalCost"), 
			lng("Item total costs") },
				{ "%", "1", "030", 
			lng("ItmStatVal"), 
			lng("Item statistical value") },
				{ "$", "0", "080", 
			lng("CuoCod"), 
			lng("Customs office code") },
				{ "$", "0", "081", 
			lng("MOTBorder"), 
			lng("Mode of transportation at the border") },
				{ "$", "0", "082", 
			lng("MOTInLand"), 
			lng("Mode of transportation inland") },
				{ "%", "0", "083", 
			lng("GrossMass"), 
			lng("Gross mass for the item") }, { "%", "0", "084", 
			lng("ItmPrice"), 
			lng("Item price") },
				{ "$", "0", "085", 
			lng("CtyOrigCod"), 
			lng("Country of origin code") },
				{ "$", "0", "086", 
			lng("ValMethod"), 
			lng("Itm: Valuation method") },
				{ "%", "0", "626", 
			lng("RegDate"), 
			lng("Wrk num. elt: registration's date (julian date)") }, };
		return variables;
	}
*/
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
	public String[][] getFunctions() {
		String[][] functions = { { "01", 
			lng("RoundInf(x)"), 
			lng("if N <= x < N + 1 then N") },
				{ "02", 
			lng("RoundSup(x)"), 
			lng("if N < x <= N + 1 then N + 1") },
				{ "03", 
			lng("Round(x)"), 
			lng("if N <= x < N + 0.5 then N else N + 1") }, { "04", 
			lng("Min(x, y)"), 
			lng("if x < y then x else y") },
				{ "05", 
			lng("Max(x, y)"), 
			lng("if x > y then x else y") }, { "06", 
			lng("Sqr(x)"), 
			lng("Square x * x") },
				{ "07", 
			lng("Abs(x)"), 
			lng("Absolute value of x") }, { "08", 
			lng("JulianDate(s)"), 
			lng("Convert date to julian number") }, };
		return functions;
	}

	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed" >
	/*
	public String[][] getKeywords() {
		String[][] keywords = { { "R", 
			lng("Rule"), 
			lng("Criteria \"criteria name\" ;") },
				{ "I", 
			lng("If"), 
			lng("If condition Then statements [Else statements] Endif") },
				{ "T", 
			lng("Then"), 
			lng("If condition Then statements [Else statements] Endif") },
				{ "E", 
			lng("Else"), 
			lng("If condition Then statements [Else statements] Endif") },
				{ "N", 
			lng("Endif"), 
			lng("If condition Then statements [Else statements] Endif") },
				{ "&", 
			lng("and"), 
			lng("condition 1 and condition 2") }, { "|", 
			lng("or"), 
			lng("condition 1 or condition 2") },
				{ "!", 
			lng("not"), 
			lng("not (condition 1)") }, { "(", "(", 
			lng("open parenthesis") },
				{ ")", 
			lng(")"), 
			lng("close parenthesis") }, { "=", 
			lng("IS"), 
			lng("data element IS expression; (assignment statement)") },
				{ "=", 
			lng(":="), 
			lng("data element := expression; (assignment statement)") },
				{ "==", 
			lng("="), 
			lng("expression 1 = expression 2") }, { "==", 
			lng("EQ"), 
			lng("expression 1 EQ expression 2") },
				{ "<", 
			lng("<"), 
			lng("expression 1 < expression 2") }, { "<", 
			lng("LT"), 
			lng("expression 1 LT expression 2") },
				{ "<=", 
			lng("<="), 
			lng("expression 1 <= expression 2") }, { "<=", 
			lng("LE"), 
			lng("expression 1 LE expression 2") },
				{ ">", 
			lng(">"), 
			lng("expression 1 > expression 2") }, { ">", 
			lng("GT"), 
			lng("expression 1 GT expression 2") },
				{ ">=", 
			lng(">="), 
			lng("expression 1 >= expression 2") }, { ">=", 
			lng("GE"), 
			lng("expression 1 GE expression 2") },
				{ "!=", 
			lng("<>"), 
			lng("expression 1 <> expression 2") }, { "!=", 
			lng("NE"), 
			lng("expression 1 NE expression 2") },
				{ ":", 
			lng(";"), 
			lng("end of statement") }, { "+", "+", 
			lng("expression 1 + expression 2") },
				{ "+", 
			lng("Plus"), 
			lng("expression 1 Plus expression 2") }, { "-", "-", 
			lng("expression 1 - expression 2") },
				{ "-", 
			lng("Minus"), 
			lng("expression 1 Minus expression 2") }, { "*", "*", 
			lng("expression 1 * expression 2") },
				{ "*", 
			lng("Mul"), 
			lng("expression 1 Mul expression 2") }, { "/", "/", 
			lng("expression 1 / expression 2") },
				{ "/", 
			lng("Div"), 
			lng("expression 1 Div expression 2") }, { ",", 
			lng(","), 
			lng("function( expression 1, expression2, ...)") }, };
		return keywords;
	}
*/
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
	
	/** Creates a new instance of CriteriaParserEnvironment */
	public ExpValNoteParserEnvironment() {
		super();
	}

	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
	/*
	private static String lng(String property) {
		return so.i18n.IntlObj.createMessage("un.asytax", property);
	}
	
	 */
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
	
}
