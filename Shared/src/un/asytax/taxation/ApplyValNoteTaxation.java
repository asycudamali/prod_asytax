// $Header: /home/asycuda/home/cvsroot/asytax/Shared/src/un/asytax/taxation/ApplyValNoteTaxation.java,v 1.7 2011-10-26 12:46:13 jedidi Exp $

package un.asytax.taxation;

import so.kernel.core.*; //import so.kernel.client.*;
//import so.swing.*;
import un.kernel.util.dynamicparser.RuleParser;
import un.kernel.util.parser.*;
import un.kernel.util.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.math.*;

import un.asytax.taxation.*;

//import un.asytax.taxation.client.*;

public class ApplyValNoteTaxation implements C_Tax, RuleSyntaxClasses {

	private KDocument doc;

	private ArrayList tokenList = null;

	private TaxationParserInterface taxParser;
	
	private RuleParser ruleParser; //AJ Dynamic

	public ApplyValNoteTaxation(KDocument doc, int decType) {
		this.doc = doc;
		AbstractParserEnvironment env;
		if (decType == 0) {
			env = new ExpValNoteParserEnvironment();
		} else {
			env = new ImpValNoteParserEnvironment();
		}
		tokenList = env.getTokenList();
		if (ruleParser == null) {
			ruleParser = new RuleParser(new ByteArrayInputStream(new byte[0]));
		} else {
			ruleParser.ReInit(System.in);
		}
	}

	public KNumberedSubDataSet getValNoteRules() {

		KNumberedSubDataSet valNote_rules = new KNumberedSubDataSet(null, new DS_Res_TaxRule());
		
		/*Vector v_rul_typ = (doc.ref(TAXATION_RULES).getDataColumnVector(RUL_TYP);
		Vector v_rul_nam = doc.ref(TAXATION_RULES).getDataColumnVector(RUL_NAM);
		Vector v_rul_rnk = doc.ref(TAXATION_RULES).getDataColumnVector(RUL_RNK);
		Vector v_rul_dsc = doc.ref(TAXATION_RULES).getDataColumnVector(RUL_DSC);
		Vector v_rul_pri = doc.ref(TAXATION_RULES).getDataColumnVector(RUL_PRI);
		 */
		DS_Res_TaxRule valNoteRule;
		
		Object v_rul_typ = doc.ref(TAXATION_RULES).getDataColumnVector(RUL_TYP);
		Object v_rul_nam 	= doc.ref(TAXATION_RULES).getDataColumnVector(RUL_NAM);
		Object v_rul_rnk 	= doc.ref(TAXATION_RULES).getDataColumnVector(RUL_RNK);
		Object v_rul_dsc 	= doc.ref(TAXATION_RULES).getDataColumnVector(RUL_DSC);
		Object v_rul_pri 	= doc.ref(TAXATION_RULES).getDataColumnVector(RUL_PRI);

		if (v_rul_typ instanceof List){
			for (int i = 0; i < ((List)v_rul_nam).size(); i++) {
				Integer rul_typ = (Integer) ((List)v_rul_typ).get(i);
				Integer rul_rnk = (Integer) ((List)v_rul_rnk).get(i);
				Integer rul_pri = (Integer) ((List)v_rul_pri).get(i);
				String rul_nam = (String) ((List)v_rul_nam).get(i);
				Object rul_dsc = ((List)v_rul_dsc).get(i);
				if (rul_typ.intValue() == 4 && rul_nam != null && !"".equals(rul_nam.trim())) {
					valNoteRule = (DS_Res_TaxRule) valNote_rules.at(valNote_rules.countDocuments() + 1);
					valNoteRule.de(TYP).setInteger(rul_typ.intValue());
					valNoteRule.de(NAM).setString(rul_nam);
					valNoteRule.de(ORD).setInteger(rul_rnk.intValue());
					valNoteRule.de(DSC).tryToSetContent(rul_dsc);
					valNoteRule.de(PRI).setInteger(rul_pri.intValue());
				}
			}	

			
		}else if (v_rul_typ instanceof Vector){
			for (int i = 0; i < ((Vector)v_rul_nam).size(); i++) {
				Integer rul_typ = (Integer) ((Vector)v_rul_typ).elementAt(i);
				Integer rul_rnk = (Integer) ((Vector)v_rul_rnk).elementAt(i);
				Integer rul_pri = (Integer) ((Vector)v_rul_pri).elementAt(i);
				String rul_nam = (String) ((Vector)v_rul_nam).elementAt(i);
				Object rul_dsc = ((Vector)v_rul_dsc).elementAt(i);
				if (rul_typ.intValue() == 4 && rul_nam != null && !"".equals(rul_nam.trim())) {
					valNoteRule = (DS_Res_TaxRule) valNote_rules.at(valNote_rules.countDocuments() + 1);
					valNoteRule.de(TYP).setInteger(rul_typ.intValue());
					valNoteRule.de(NAM).setString(rul_nam);
					valNoteRule.de(ORD).setInteger(rul_rnk.intValue());
					valNoteRule.de(DSC).tryToSetContent(rul_dsc);
					valNoteRule.de(PRI).setInteger(rul_pri.intValue());
				}
			}	
		}
		

		return valNote_rules;
	}

	public void processValNote(TaxationParserInterface taxParser, String whichRule) {

		KNumberedSubDataSet valNote_tax_rules = getValNoteRules();

		for (int i = 1; i <= valNote_tax_rules.countDocuments(); i++) {
			KNumberedSubDocument rule = (KNumberedSubDocument) valNote_tax_rules.at(i);
			if (whichRule.equals(rule.de(NAM).getString("").trim())) {
				ruleParser.ReInit(new ByteArrayInputStream((byte[]) rule.de(DSC).getContent()));
				try {

					ruleParser.Rule();
					Collections.sort(tokenList, new sortByCode());
					taxParser.setTokenList(tokenList);
					ruleParser.interpret(taxParser);

				} catch (Exception ee2) {
					ee2.printStackTrace();

				}
			}
		}
	}

	// 1 - export - GS rule
	// 2 - export - value rule
	// 3 - export - mass rule
	// 4 - export - item (no appt) rule
	// 5 - import - GS rule
	// 6 - import - value rule
	// 7 - import - mass rule
	// 8 - import - item (no appt) rule

	public KDocument returnDocument() {
		return doc;

	}

}
