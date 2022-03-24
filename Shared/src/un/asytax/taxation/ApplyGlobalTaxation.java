package un.asytax.taxation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import so.kernel.core.DataSet;
import so.kernel.core.HTVerifier.Row;
import so.kernel.core.KNumberedSubDataSet;
import so.kernel.core.KNumberedSubDocument;
import so.kernel.core.Data.ErrorStatus;


import so.util.DebugOutput;
import un.asygate.taxation.TaxationParserEnvironment;
import un.asygate.taxationrules.C_TaxationRules;
import un.kernel.core.ArefHTCompatible;
import un.kernel.core.KDocument;
import un.kernel.core.PropertyLoader;
import un.kernel.util.AbstractParserEnvironment;
import un.kernel.util.RuleSyntaxClasses;
import un.kernel.util.dynamicparser.RuleParser;
import un.kernel.util.rulecompiler.AbstractTaxationRule;
import un.kernel.util.rulecompiler.ClassRule;
import un.kernel.util.rulecompiler.GlobalVariables;
import un.kernel.util.rulecompiler.RuleUtils;

public class ApplyGlobalTaxation implements C_Tax, RuleSyntaxClasses {

    private final DataSet doc;

    private ArrayList tokenList = null;

    private final TaxationParserInterface taxParser;

    private RuleParser ruleParser; // AJ Dynamic
    
    private boolean isAref =  "true".equalsIgnoreCase(PropertyLoader.getInstance().getPropertyValue("Client.un.asytax.usesAref"));
   
    // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    GlobalVariables globalVariables ;
  
   // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
    

    public ApplyGlobalTaxation(DataSet doc, TaxationParserInterface taxParser, String location, GlobalVariables globalVariables) {// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        this.doc = doc;
        this.taxParser = taxParser;
        
        
        System.out.println("Is aref  " + isAref);
       
        AbstractParserEnvironment env = new TaxationParserEnvironment();
        tokenList = env.getTokenList();
     // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        if(!JAVAPARSER){
        	if (ruleParser == null) {
        		ruleParser = new RuleParser(new ByteArrayInputStream(new byte[0]));
        	} else {
        		ruleParser.ReInit(System.in);
        	}
        	
        }else{
            this.globalVariables = globalVariables;
        	
        }
        // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    }

    private KNumberedSubDataSet getGlobalConfigRules(ArefHTCompatible commonVerifier) {
        KNumberedSubDataSet unsorted_global_rules = new KNumberedSubDataSet(null, new DS_Res_TaxRule());
        DS_Res_TaxRule globalRule;
        ArefHTCompatible verifier = commonVerifier;
        // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
        Date wde;
        if (doc.ds(PTY) == null) {
            wde = (Date) doc.de(WDE).getContent();
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
        Integer rul_typ = new Integer(2);
     // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
        /*
        Iterator<?> it = verifier.find(TAXATION_RULES, wde, RUL_NAM, new String[] { RUL_TYP }, new Object[] { rul_typ });
        while (it.hasNext()) {
            String rul_nam = (String) it.next();
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                globalRule = (DS_Res_TaxRule) unsorted_global_rules.at(unsorted_global_rules.countDocuments() + 1);
                globalRule.de(TYP).setInteger(2);
                globalRule.de(NAM).setString(rul_nam);

                Iterator findRnk = verifier.find(TAXATION_RULES, wde, RUL_RNK, RUL_NAM, rul_nam);
                Number rul_rnk = ((Number) findRnk.next());
                globalRule.de(ORD).setInteger(rul_rnk.intValue());
                if (findRnk != null) verifier.closeIterator(findRnk);

                Iterator findPri = verifier.find(TAXATION_RULES, wde, RUL_PRI, RUL_NAM, rul_nam);
                Number rul_pri = ((Number) findPri.next());
                globalRule.de(PRI).setInteger(rul_pri.intValue());
                if (findPri != null) verifier.closeIterator(findPri);

                Iterator findDsc = verifier.find(TAXATION_RULES, wde, RUL_DSC, RUL_NAM, rul_nam);
                Object rul_dsc = findDsc.next();
                globalRule.de(DSC).tryToSetContent(rul_dsc);
                if (findDsc != null) verifier.closeIterator(findDsc);
            }
        }
        */
        Iterator<?> it = verifier.find(TAXATION_RULES, wde,  new String[] { RUL_TYP }, new Object[] { rul_typ });
        while (it.hasNext()) {
            Object  row=  it.next();
        	String rul_nam = (String)getColumn(row,"RUL_NAM");
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                globalRule = (DS_Res_TaxRule) unsorted_global_rules.at(unsorted_global_rules.countDocuments() + 1);
                globalRule.de(TYP).setInteger(2);
                globalRule.de(NAM).setString(rul_nam);
                 
                Number rul_rnk = (Number) getColumn(row,"RUL_RNK");
                globalRule.de(ORD).setInteger(rul_rnk.intValue());
                 
                Number rul_pri = (Number) getColumn(row,"RUL_PRI");
                globalRule.de(PRI).setInteger(rul_pri.intValue());
                 
                Object rul_dsc = getColumn(row,"RUL_DSC");
                globalRule.de(DSC).tryToSetContent(rul_dsc);
                
                // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                Object rulJavDsc = getColumn(row,C_TaxationRules.RUL_JAV_DSC);
                if (rulJavDsc!=null){
                	globalRule.de(JAV).tryToSetContent(RuleUtils.toObject((byte[])rulJavDsc) );
                }
                // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
                
            }
        }
     // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>

        if (it != null) verifier.closeIterator(it);

        KNumberedSubDataSet sorted_global_rules = new KNumberedSubDataSet(null, new DS_Res_TaxRule());
        for (int o = 1; o <= unsorted_global_rules.countDocuments(); o++) {
            sorted_global_rules.at(o);
        }

        for (int k = 1; k <= unsorted_global_rules.countDocuments(); k++) {
            DS_Res_TaxRule globalRuleToAdd = (DS_Res_TaxRule) unsorted_global_rules.at(k);
            int l = 1;
            globalRule = (DS_Res_TaxRule) sorted_global_rules.at(1);
            while (globalRuleToAdd.de(PRI).getInt() > globalRule.de(PRI).getInt(100)) {
                l++;
                if (l == sorted_global_rules.countDocuments()) {
                    break;
                }
                globalRule = (DS_Res_TaxRule) sorted_global_rules.at(l);
            }

            for (int b = sorted_global_rules.countDocuments() - 1; b >= l; b--) {
                DS_Res_TaxRule curGlobalRule = (DS_Res_TaxRule) sorted_global_rules.getDocument(b);
                if (!"".equals(curGlobalRule.de(NAM).getString("").trim())) {
                    DS_Res_TaxRule nextGlobalRule = (DS_Res_TaxRule) sorted_global_rules.getDocument(b + 1);
                    nextGlobalRule.de(TYP).tryToSetContent(curGlobalRule.de(TYP).getContent());
                    nextGlobalRule.de(NAM).tryToSetContent(curGlobalRule.de(NAM).getContent());
                    nextGlobalRule.de(DSC).tryToSetContent(curGlobalRule.de(DSC).getContent());
                    nextGlobalRule.de(ORD).tryToSetContent(curGlobalRule.de(ORD).getContent());
                    nextGlobalRule.de(PRI).tryToSetContent(curGlobalRule.de(PRI).getContent());
                    nextGlobalRule.de(JAV).tryToSetContent(curGlobalRule.de(JAV).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                }
            }
            DS_Res_TaxRule curGlobalRule = (DS_Res_TaxRule) sorted_global_rules.getDocument(l);
            curGlobalRule.de(TYP).tryToSetContent(globalRuleToAdd.de(TYP).getContent());
            curGlobalRule.de(NAM).tryToSetContent(globalRuleToAdd.de(NAM).getContent());
            curGlobalRule.de(DSC).tryToSetContent(globalRuleToAdd.de(DSC).getContent());
            curGlobalRule.de(ORD).tryToSetContent(globalRuleToAdd.de(ORD).getContent());
            curGlobalRule.de(PRI).tryToSetContent(globalRuleToAdd.de(PRI).getContent());
            curGlobalRule.de(JAV).tryToSetContent(globalRuleToAdd.de(JAV).getContent()); // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
            
        }
        return sorted_global_rules;
    }

    public void processTaxation(ArefHTCompatible commonVerifier) {
        KNumberedSubDataSet global_tax_rules = getGlobalConfigRules(commonVerifier);
        for (int i = 1; i <= global_tax_rules.countDocuments(); i++) {
        	KNumberedSubDocument rule = global_tax_rules.at(i);
          
        	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        	//System.out.print("************************ PARSER TYPE ********* : " + JAVAPARSER + " \n");
        	if(!JAVAPARSER){
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
        	else{
        		Collections.sort(tokenList, new sortByCode());
    			taxParser.setTokenList(tokenList);
    			String name = rule.de(NAM).getString("");
    			if(rule.de(JAV).getContent()!=null){
    				try{
    					ClassRule classRule =  (ClassRule)rule.de(JAV).getContent();
    					if (!classRule.isValid(name)){
    						DebugOutput.print("rule is not valid " + name);
    						globalVariables.addError(name);
    					}
    					String className = classRule.getClassName();
    					System.out.println(rule.de(NAM).getString("") );
    					((AbstractTaxationRule)classRule.getInstance(this.getClass().getClassLoader(),taxParser,globalVariables)).apply();
     				}catch (Exception e){
     					globalVariables.addError(name);
    					e.printStackTrace();
    				}
    				
    			}else{
    				globalVariables.addError(name);
    				//DebugOutput.print("Syntaxe error in rule " + name);
    			}
				
        		
        	}
        	
        	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
        }
    }

    public DataSet returnDocument() {
        return doc;
    }
    
    private Object getColumn(Object rowIn, String column){
    	if (isAref){
    		so.util.Row row = (so.util.Row)rowIn;
    		return row.get(column);
    	}else{
    		Row row = (Row)rowIn;
    		return row.get(column);
    	}
    }
}
