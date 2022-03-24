package un.asytax.taxation;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import so.kernel.core.DataSet;
import un.kernel.core.KDocument;
import un.kernel.core.PropertyLoader;

import so.kernel.core.HTVerifier.Row;
import so.kernel.core.KNumberedSubDataSet;
import so.kernel.core.KNumberedSubDocument;

import so.kernel.core.TransactionEvent;
import so.util.DebugOutput;
import un.asygate.taxation.TaxationParserEnvironment;
import un.asygate.taxationrules.C_TaxationRules;
import un.kernel.core.ArefHTCompatible;
import un.kernel.util.AbstractParserEnvironment;
import un.kernel.util.RuleSyntaxClasses;
import un.kernel.util.dynamicparser.RuleParser;
import un.kernel.util.intervalparser.ParseException;
import un.kernel.util.intervalparser.TariffManagementParser;
import un.kernel.util.intervalparser.TariffSyntaxClasses.Interval;
import un.kernel.util.intervalparser.TariffSyntaxClasses.ParserError;
import un.kernel.util.rulecompiler.AbstractTaxationRule;
import un.kernel.util.rulecompiler.ClassRule;
import un.kernel.util.rulecompiler.GlobalVariables;
import un.kernel.util.rulecompiler.RuleUtils;

public class ApplyItemTaxation implements C_Tax, RuleSyntaxClasses {

    private ArrayList tokenList = null;

    private DataSet doc = null;

    private KNumberedSubDataSet itm_tax_rules = null;

    private final String location;

    private RuleParser ruleParser; // AJ Dynamic
    private boolean isAref =  "true".equalsIgnoreCase(PropertyLoader.getInstance().getPropertyValue("Client.un.asytax.usesAref"));
    
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    GlobalVariables globalVariables=null; 
    ClassLoader classLoader = null;
    
    private HashMap<String, Class> classMap = new HashMap<String, Class>();
    
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>

     
    
    public ApplyItemTaxation(DataSet doc, String location,  GlobalVariables globalVariables) {  // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        this.doc = doc;
        this.location = location;
       
        System.out.println("Is aref  " + isAref);
        
        // System.out.println("Location at init " + location);
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
            this.classLoader = this.getClass().getClassLoader();
        	
        }
     // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
    }

    /**
     * @author Ajil Prepare the itm_tax_rules variable
     */
    public void getItemConfigRules(ArefHTCompatible commonVerifier) {

        KNumberedSubDataSet unsorted_itm_rules = new KNumberedSubDataSet(null, new DS_Res_TaxRule());
        DS_Res_TaxRule itmRule;

        // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
        // ************************************************
        ArefHTCompatible verifier = commonVerifier;
        Date wde;
        if (doc.ds(PTY) == null) {
            wde = (Date) doc.ds(IDE).de(DAT).getContent();
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
        // ************************************************
        Integer rul_typ = new Integer(1); // Tariff rule
        System.out.println("working date " + wde);
        // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
        /*
        Iterator it = verifier.find(TAXATION_RULES, wde, RUL_NAM, new String[] { RUL_TYP }, new Object[] { rul_typ });
        while (it.hasNext()) {
            String rul_nam = (String) it.next();
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                itmRule = (DS_Res_TaxRule) unsorted_itm_rules.at(unsorted_itm_rules.countDocuments() + 1);

                itmRule.de(TYP).setInteger(1);
                itmRule.de(NAM).setString(rul_nam);
                Iterator findRnk = verifier.find(TAXATION_RULES, wde, RUL_RNK, RUL_NAM, rul_nam);
                Number rul_rnk = ((Number) findRnk.next());
                itmRule.de(ORD).setInteger(rul_rnk.intValue());
                if (findRnk != null) verifier.closeIterator(findRnk);

                Iterator findPri = verifier.find(TAXATION_RULES, wde, RUL_PRI, RUL_NAM, rul_nam);
                Number rul_pri = ((Number) findPri.next());
                itmRule.de(PRI).setInteger(rul_pri.intValue());
                if (findPri != null) verifier.closeIterator(findPri);

                Iterator Dsc = verifier.find(TAXATION_RULES, wde, RUL_DSC, RUL_NAM, rul_nam);
                Object rul_dsc = Dsc.next();
                itmRule.de(DSC).tryToSetContent(rul_dsc);
                if (Dsc != null) verifier.closeIterator(Dsc);
            }
        }
        */
        Iterator it = verifier.find(TAXATION_RULES, wde,  new String[] { RUL_TYP }, new Object[] { rul_typ });
        while (it.hasNext()) {
        	
        	 Object row=  it.next();
        	
            String rul_nam = (String)getColumn(row,"RUL_NAM");
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                itmRule = (DS_Res_TaxRule) unsorted_itm_rules.at(unsorted_itm_rules.countDocuments() + 1);

                itmRule.de(TYP).setInteger(1);
                itmRule.de(NAM).setString(rul_nam);
                  
               
                Number rul_rnk = (Number)getColumn(row,"RUL_RNK");
                itmRule.de(ORD).setInteger(rul_rnk.intValue());
                

                
                Number rul_pri = (Number) getColumn(row,"RUL_PRI");
                itmRule.de(PRI).setInteger(rul_pri.intValue());
                 

               
                Object rul_dsc = getColumn(row,"RUL_DSC");
                itmRule.de(DSC).tryToSetContent(rul_dsc);
                
                // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                Object rulJavDsc = getColumn(row,C_TaxationRules.RUL_JAV_DSC);
                if (rulJavDsc!=null){
                	itmRule.de(JAV).tryToSetContent(RuleUtils.toObject((byte[])rulJavDsc) );
                }
                // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
                
            }
        }

        
        
        // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>
        if (it != null) verifier.closeIterator(it);
        // ------------------------------------------------------------------------------------------------------------
        Integer rul_typ1 = new Integer(3); // Item rules
        // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
        /*
        Iterator it1 = verifier.find(TAXATION_RULES, wde, RUL_NAM, new String[] { RUL_TYP }, new Object[] { rul_typ1 });
        while (it1.hasNext()) {
            String rul_nam = (String) it1.next();
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                itmRule = (DS_Res_TaxRule) unsorted_itm_rules.at(unsorted_itm_rules.countDocuments() + 1);
                itmRule.de(TYP).setInteger(3);
                itmRule.de(NAM).setString(rul_nam);
                Iterator findRnk = verifier.find(TAXATION_RULES, wde, RUL_RNK, RUL_NAM, rul_nam);
                Number rul_rnk = ((Number) findRnk.next());
                itmRule.de(ORD).setInteger(rul_rnk.intValue());
                if (findRnk != null) verifier.closeIterator(findRnk);

                Iterator findPri = verifier.find(TAXATION_RULES, wde, RUL_PRI, RUL_NAM, rul_nam);
                Number rul_pri = ((Number) findPri.next());
                itmRule.de(PRI).setInteger(rul_pri.intValue());
                if (findPri != null) verifier.closeIterator(findPri);

                Iterator findDsc = verifier.find(TAXATION_RULES, wde, RUL_DSC, RUL_NAM, rul_nam);
                Object rul_dsc = findDsc.next();
                itmRule.de(DSC).tryToSetContent(rul_dsc);
                if (findDsc != null) verifier.closeIterator(findDsc);
            }
        }
        
        */
        Iterator it1 = verifier.find(TAXATION_RULES, wde,  new String[] { RUL_TYP }, new Object[] { rul_typ1 });
        while (it1.hasNext()) {
        		Object row=  it1.next();
        		String rul_nam = (String)getColumn(row,"RUL_NAM");
        	
               // String rul_nam = (String) it1.next();
            if (rul_nam != null && !"".equals(rul_nam.trim())) {
                itmRule = (DS_Res_TaxRule) unsorted_itm_rules.at(unsorted_itm_rules.countDocuments() + 1);
                itmRule.de(TYP).setInteger(3);
                itmRule.de(NAM).setString(rul_nam);
                
                Number rul_rnk = (Number)getColumn(row,"RUL_RNK");
                itmRule.de(ORD).setInteger(rul_rnk.intValue());
                 

                
                Number rul_pri = (Number) getColumn(row,"RUL_PRI");
                itmRule.de(PRI).setInteger(rul_pri.intValue());
                

                
                Object rul_dsc = getColumn(row,"RUL_DSC");
                itmRule.de(DSC).tryToSetContent(rul_dsc);
                
             // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                Object rulJavDsc = getColumn(row,C_TaxationRules.RUL_JAV_DSC);
                if (rulJavDsc!=null){
                	itmRule.de(JAV).tryToSetContent(RuleUtils.toObject((byte[])rulJavDsc) );
                }
                // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
                
            }
        }
  
     // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>
        if (it1 != null) verifier.closeIterator(it1);
        // ------------------------------------------------------------------------------------------------------------
        KNumberedSubDataSet sorted_itm_rules = new KNumberedSubDataSet(null, new DS_Res_TaxRule());
        for (int o = 1; o <= unsorted_itm_rules.countDocuments(); o++) {
            DS_Res_TaxRule rule = (DS_Res_TaxRule) unsorted_itm_rules.at(o);
            rule.de(NAM).getString("");
            sorted_itm_rules.at(o);
        }
        // --------------------------------------------------------------------------
        // Sort the rules according to priorities
        // --------------------------------------------------------------------------
        for (int k = 1; k <= unsorted_itm_rules.countDocuments(); k++) {
            DS_Res_TaxRule itmRuleToAdd = (DS_Res_TaxRule) unsorted_itm_rules.at(k);
            itmRule = (DS_Res_TaxRule) sorted_itm_rules.at(1);
            // ----------------------------------------------------------------------
            int l = 1;
            itmRuleToAdd.de(PRI).getInt();
            itmRule.de(PRI).getInt(100);
            while (itmRuleToAdd.de(PRI).getInt() > itmRule.de(PRI).getInt(100)) {
                l++;
                if (l == sorted_itm_rules.countDocuments()) {
                    break;
                }
                itmRule = (DS_Res_TaxRule) sorted_itm_rules.at(l);
            }
            // ----------------------------------------------------------------------
            for (int b = sorted_itm_rules.countDocuments() - 1; b >= l; b--) {
                DS_Res_TaxRule curItmRule = (DS_Res_TaxRule) sorted_itm_rules.getDocument(b);
                if (!"".equals(curItmRule.de(NAM).getString("").trim())) {
                    DS_Res_TaxRule nextItmRule = (DS_Res_TaxRule) sorted_itm_rules.getDocument(b + 1);
                    nextItmRule.de(TYP).tryToSetContent(curItmRule.de(TYP).getContent());
                    nextItmRule.de(NAM).tryToSetContent(curItmRule.de(NAM).getContent());
                    nextItmRule.de(DSC).tryToSetContent(curItmRule.de(DSC).getContent());
                    nextItmRule.de(ORD).tryToSetContent(curItmRule.de(ORD).getContent());
                    nextItmRule.de(PRI).tryToSetContent(curItmRule.de(PRI).getContent());
                    nextItmRule.de(JAV).tryToSetContent(curItmRule.de(JAV).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                }
            }
            // ----------------------------------------------------------------------
            DS_Res_TaxRule curItmRule = (DS_Res_TaxRule) sorted_itm_rules.getDocument(l);
            curItmRule.de(TYP).tryToSetContent(itmRuleToAdd.de(TYP).getContent());
            curItmRule.de(NAM).tryToSetContent(itmRuleToAdd.de(NAM).getContent());
            curItmRule.de(DSC).tryToSetContent(itmRuleToAdd.de(DSC).getContent());
            curItmRule.de(ORD).tryToSetContent(itmRuleToAdd.de(ORD).getContent());
            curItmRule.de(PRI).tryToSetContent(itmRuleToAdd.de(PRI).getContent());
            curItmRule.de(JAV).tryToSetContent(itmRuleToAdd.de(JAV).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        }
        itm_tax_rules = sorted_itm_rules;
        // ------------------------------------------------------------------------------------------------------------
    }

    /**
     * @param itm
     * @param taxParser
     */
    public void processTaxation(DataSet itm, TaxationParserInterface taxParser, ArefHTCompatible commonVerifier) {

        KNumberedSubDataSet all_itm_rules;
        KNumberedSubDataSet itm_ref_rules;

        String tempAttDoc = "";

        DataSet tmp;
        tmp = new DataSet(TMP);
        tmp.numberedItm(REF, new DS_Res_TaxRule());
        tmp.numberedItm(TOT, new DS_Res_TaxRule());
        all_itm_rules = (KNumberedSubDataSet) tmp.ds(TOT);

        // Get the reference table rules for this item
        itm_ref_rules = (KNumberedSubDataSet) tmp.ds(REF);
        for (int k = 1; k <= itm_ref_rules.countDocuments(); k++) {
            itm_ref_rules.deleteDocument(k);
        }

        // Calculate var611 ***************************************
        double var611;
        if (itm.ds(VIT) == null) {
            var611 = 0.0;
        } else if (itm.ds(VIT).ds(MKT).de(MOD).getInt(0) > 0) {
            var611 = itm.ds(VIT).ds(MKT).de(AMT).getDouble(0.0);
        } else {
            var611 = itm.ds(VIT).de(STV).getDouble(0.0);
        }
        // *********************************************************
        // Get Tarif rule form reference tables
        if (itm.ds(VIT) != null && itm.ds(VIT).ds(MKT).de(MOD).getInt(0) == -1) {
            getUNMKTTABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
        }
     
//Attention
        
        getUNTARTABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
        getUNCTYTABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
        getUNPRFTABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
        getUNCP3TABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
        // AJ 11022009
        getUNAGRTABReferenceRule(doc, itm, itm_ref_rules, commonVerifier);
 
// Attention 
        
        int totNbrRules = itm_ref_rules.countDocuments() + itm_tax_rules.countDocuments();
        for (int o = 1; o <= totNbrRules; o++) {
            all_itm_rules.at(o);
        }

        for (int o = 1; o <= itm_tax_rules.countDocuments(); o++) {
            DS_Res_TaxRule itmRule = (DS_Res_TaxRule) all_itm_rules.at(o);
            KNumberedSubDocument itmTaxRule = itm_tax_rules.at(o);
            itmRule.copyFrom(itmTaxRule);

            DS_Res_TaxRule rule = (DS_Res_TaxRule) all_itm_rules.at(o);
            rule.de(NAM).getString("");
            rule = (DS_Res_TaxRule) itm_tax_rules.at(o);
            rule.de(NAM).getString("");
        }

        // *********************************************************
        // Sort all the rules that apply to this item by priority
        for (int k = 1; k <= itm_ref_rules.countDocuments(); k++) {
            DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(k);
            int l = 1;

            DS_Res_TaxRule itmRule = (DS_Res_TaxRule) all_itm_rules.at(1);
            while (itmRefRule.de(PRI).getInt() > itmRule.de(PRI).getInt(100)) {
                l++;
                if (l == all_itm_rules.countDocuments()) {
                    break;
                }
                itmRule = (DS_Res_TaxRule) all_itm_rules.at(l);
            }

            for (int b = all_itm_rules.countDocuments() - 1; b >= l; b--) {
                DS_Res_TaxRule curItmRule = (DS_Res_TaxRule) all_itm_rules.getDocument(b);
                if (!"".equals(curItmRule.de(NAM).getString("").trim())) {
                    DS_Res_TaxRule nextItmRule = (DS_Res_TaxRule) all_itm_rules.getDocument(b + 1);
                    nextItmRule.de(TYP).tryToSetContent(curItmRule.de(TYP).getContent());
                    nextItmRule.de(NAM).tryToSetContent(curItmRule.de(NAM).getContent());
                    nextItmRule.de(DSC).tryToSetContent(curItmRule.de(DSC).getContent());
                    nextItmRule.de(ORD).tryToSetContent(curItmRule.de(ORD).getContent());
                    nextItmRule.de(PRI).tryToSetContent(curItmRule.de(PRI).getContent());
                    nextItmRule.de(JAV).tryToSetContent(curItmRule.de(JAV).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
                    nextItmRule.de(RES).tryToSetContent(curItmRule.de(RES).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
                }
            }
            DS_Res_TaxRule curItmRule = (DS_Res_TaxRule) all_itm_rules.getDocument(l);
            curItmRule.de(TYP).tryToSetContent(itmRefRule.de(TYP).getContent());
            curItmRule.de(NAM).tryToSetContent(itmRefRule.de(NAM).getContent());
            curItmRule.de(DSC).tryToSetContent(itmRefRule.de(DSC).getContent());
            curItmRule.de(ORD).tryToSetContent(itmRefRule.de(ORD).getContent());
            curItmRule.de(JAV).tryToSetContent(itmRefRule.de(JAV).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
            curItmRule.de(RES).tryToSetContent(itmRefRule.de(RES).getContent());// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
        }
        // *********************************************************

        for (int b = 1; b <= all_itm_rules.countDocuments(); b++) {
            all_itm_rules.at(b);
        }

        if (itm.ds(TAR) != null) {
            tempAttDoc = itm.ds(TAR).de(ATT).getString("");
            itm.ds(TAR).de(ATT).setString("");
        }
        // System.out.println("Item rules total " + all_itm_rules.countDocuments());
        
        for (int i = 1; i <= all_itm_rules.countDocuments(); i++) {
            DS_Res_TaxRule rule = (DS_Res_TaxRule) all_itm_rules.at(i);
            String name = rule.de(NAM).getString("");
          //  System.out.println("Tax rule " + name);
         // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
            if (rule.de(RES).getContent()!=null &&  itm !=null){
            	Vector v = (Vector)rule.de(RES).getContent();
            	String ctrHsCode =  itm.ds(TAR).ds(HSC).de(NB1).getString("")+itm.ds(TAR).ds(HSC).de(NB2).getString("");
            	if (!isInRange(ctrHsCode,v)){
            		continue;
            	};
            	
            }
         // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
            if (!JAVAPARSER){
            	byte[] b = (byte[]) rule.de(DSC).getContent();
            	if ((b != null) && (b.length > 0)) {
            		ruleParser.ReInit(new ByteArrayInputStream((b)));
            		try {
            			ruleParser.Rule();
            			Collections.sort(tokenList, new sortByCode());
            			taxParser.setRuleRank(rule.de(ORD).getInt(0));
            			taxParser.setTokenList(tokenList);
            			ruleParser.interpret(taxParser);
            		} catch (Exception ee2) {
            			ee2.printStackTrace();
            		}
            	}	
            }
             else{
            	taxParser.setRuleRank(rule.de(ORD).getInt(0));
    			taxParser.setTokenList(tokenList);
    			if(rule.de(JAV).getContent()!=null){
    				try{
    					ClassRule classRule =  (ClassRule)rule.de(JAV).getContent();
    					if (!classRule.isValid(name) ){
    						globalVariables.addError(name);
    						DebugOutput.print("rule is not valid " + name);
    					}
    					String className = classRule.getClassName();
    					Object obj ;
    					if (!classMap.containsKey(className)){
    						obj =  classRule.getInstance(classLoader,taxParser,globalVariables); 
    						classMap.put(className, classRule.getRuleClass());
    					}else{
    						obj = (classMap.get(className).getConstructors())[0].newInstance(new Object[]{taxParser,globalVariables});
    					}
    					if (obj instanceof AbstractTaxationRule){
    						((AbstractTaxationRule)obj).apply();
    					}
    				}catch (Exception e){
    					globalVariables.addError(name);
    					e.printStackTrace();
    				}
    				
    			}else{
    				globalVariables.addError(name);
    				//DebugOutput.print("Syntaxe error in rule " + name);
    			}
    // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
            	
            } 
        }

        if (itm.ds(TAR) != null && "".equals(itm.ds(TAR).de(ATT).getString(""))) {
            itm.ds(TAR).de(ATT).setString(tempAttDoc);
        }

        // %%%%%%%%%%%%%%%%serge-debut--2008/10/03%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        // In case a declaration needs a Manifest and a Previous Document at the same
        // time (for instance: 6022), Prev. Doc. will be displayed on the A.D.
        int tempAttDoc_Prv_ = tempAttDoc.indexOf("/");
        if (tempAttDoc_Prv_ > 0) {
            String[] tempAttDoc_Prv = tempAttDoc.split("/");
            String AD = itm.ds(TAR).de(ATT).getString("");
            itm.ds(TAR).de(ATT).setString(tempAttDoc_Prv[0] + "/ " + AD);
        }
        // %%%%%%%%%%%%%%%%%%serge-fin--2008/10/03%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        // Display var611 in market amount field on item valuation note if
        // mktMod = -1
        if (itm.ds(VIT) != null && itm.ds(VIT).ds(MKT).de(MOD).getInt(0) == -1) {
            itm.ds(VIT).ds(MKT).de(AMT).tryToSetContent(var611);
        }
        // Replace statistical value if statMod = 1
        if (itm.ds(VIT) != null && itm.ds(VIT).ds(MKT).de(STA).getInt(0) == 1) {
            itm.ds(VIT).de(STV).tryToSetContent(var611);
        }
    }

    /**
     * 
     * @param doc
     * @param itm
     * @param itm_ref_rules
     */
    private void getUNMKTTABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {

        String searchRulCod = itm.ds(VIT).ds(MKT).de(RUL).getString("").trim();
        ArefHTCompatible verifier = commonVerifier;
        // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
        Date wde;
        // *********************************************************
        if (doc.ds(PTY) == null) {
            wde = (Date) doc.ds(IDE).de(DAT).getContent();
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
        // *********************************************************
        if (verifier.contains(RUL_TAB, wde, RUL_COD, searchRulCod)) {
        	Iterator itr= verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, searchRulCod);
            String rulDsc = (String) itr.next();
        	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed">
        		if (itr != null) verifier.closeIterator(itr); 
        	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed"/>
	
        	itr = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, searchRulCod);
            Object rulDef = itr.next();
        	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed">
    		if (itr != null) verifier.closeIterator(itr); 
    	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed"/>

    		itr = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, searchRulCod);
            String rulPty = itr.next().toString();
        	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed">
    		if (itr != null) verifier.closeIterator(itr); 
    	// CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed"/>


            DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
            itmRefRule.de(NAM).tryToSetContent(rulDsc);
            itmRefRule.de(DSC).tryToSetContent(rulDef);
            itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
        }
        // *********************************************************
    }

    /**
     * 
     * @param doc
     * @param itm
     * @param itm_ref_rules
     */
    private void getUNCTYTABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {
        String search_cty_cod;
        if (itm.ds(GDS) == null)
            search_cty_cod = itm.ds(TAX).ds(CTY).de(COD).getString("").trim();
        else
            search_cty_cod = itm.ds(GDS).ds(ORG).de(CTY).getString("").trim();
        ArefHTCompatible verifier = commonVerifier;

        if (!"".equals(search_cty_cod)) {

            // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
            Date wde;
            if (doc.ds(PTY) == null) {
                wde = (Date) doc.ds(IDE).de(DAT).getContent();
            } else {
                wde = (Date) doc.ds(PTY).de(WDE).getContent();
            }
            Iterator it = verifier.find(CTY_TAB, wde, RUL_COD, CTY_COD, search_cty_cod);
            if (it.hasNext()) {
                String rul_cod2 = (String) it.next();
                // Add the reference table rules to the KNumberedSubDataSet
                if (!"".equals(rul_cod2)) {
                	findRule( wde,  itm_ref_rules,  verifier, rul_cod2) ;
                	/*
                    if (verifier.contains(RUL_TAB, wde, RUL_COD, rul_cod2)) {
                        String rul_dsc = (String) verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, rul_cod2).next();
                        Object rul_def = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, rul_cod2).next();
                        String rul_pty = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, rul_cod2).next().toString();

                        DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
                        itmRefRule.de(NAM).tryToSetContent(rul_dsc);
                        itmRefRule.de(DSC).tryToSetContent(rul_def);
                        itmRefRule.de(PRI).tryToSetContent(new Integer(rul_pty));
                    }
                    */
                }
            }
            if (it != null) verifier.closeIterator(it);
        }
    }

    /**
     * 
     * @param doc
     * @param itm
     * @param itm_ref_rules
     */
    private void getUNPRFTABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {
        // UNPRFTAB rule
        String search_prf_cod;
        if (doc.ds(PTY) == null) {// itm.ds(TAR) == null //sim attached document
            search_prf_cod = itm.ds(TAX).ds(PRF).de(COD).getString("").trim();
        } else {
            search_prf_cod = itm.ds(TAR).de(PRF).getString("").trim();
        }
        ArefHTCompatible verifier = commonVerifier;
        if (!"".equals(search_prf_cod)) {
            // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
            Date wde;
            if (doc.ds(PTY) == null) {
                wde = (Date) doc.ds(IDE).de(DAT).getContent();
            } else {
                wde = (Date) doc.ds(PTY).de(WDE).getContent();
            }
            Iterator it = verifier.find(PRF_TAB, wde, RUL_COD, PRF_COD, search_prf_cod);
            if (it.hasNext()) {
                String rul_cod2 = (String) it.next();

                // Add the reference table rules to the KNumberedSubDataSet
                if (!"".equals(rul_cod2)) {
                	findRule( wde,  itm_ref_rules,  verifier, rul_cod2) ;
                	/*
                    if (verifier.contains(RUL_TAB, wde, RUL_COD, rul_cod2)) {
                        String rulDsc = (String) verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, rul_cod2).next();
                        Object rulDef = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, rul_cod2).next();
                        String rulPty = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, rul_cod2).next().toString();

                        DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
                        itmRefRule.de(NAM).tryToSetContent(rulDsc);
                        itmRefRule.de(DSC).tryToSetContent(rulDef);
                        itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
                    }
                    */
                }
            }
            if (it != null) verifier.closeIterator(it);
        }
    }

    /**
     * 
     * @param doc
     * @param itm
     * @param itm_ref_rules
     */
    private void getUNCP3TABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {
        // UNCP3TAB rule
        String search_cp3_cod;
        if (doc.ds(PTY) == null) {
            search_cp3_cod = itm.ds(TAX).ds(CUS).ds(ADT).de(COD).getString("").trim();
        } else {
            search_cp3_cod = itm.ds(TAR).ds(PRC).de(NAT).getString("").trim();
        }

        Date wde;
        if (doc.ds(PTY) == null) {
            wde = (Date) doc.ds(IDE).de(DAT).getContent();
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
        ArefHTCompatible verifier = commonVerifier;
        if (!"".equals(search_cp3_cod)) {

            // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
            Iterator it = verifier.find(CP3_TAB, wde, RUL_COD, CP3_COD, search_cp3_cod);
            if (it.hasNext()) {

                String rul_cod2 = (String) it.next();

                // Add the reference table rules to the KNumberedSubDataSet
                if (!"".equals(rul_cod2)) {
                	findRule( wde,  itm_ref_rules,  verifier, rul_cod2) ;
                	/*
                    if (verifier.contains(RUL_TAB, wde, RUL_COD, rul_cod2)) {
                        String rulDsc = (String) verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, rul_cod2).next();
                        Object rulDef = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, rul_cod2).next();
                        String rulPty = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, rul_cod2).next().toString();

                        DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
                        itmRefRule.de(NAM).tryToSetContent(rulDsc);
                        itmRefRule.de(DSC).tryToSetContent(rulDef);
                        itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
                    }
                    */
                }
            }
            if (it != null) verifier.closeIterator(it);
        }
    }

    // AJ11022009
    private void getUNAGRTABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {
        // UNAGRTAB rule
        String search_cmp_cod = "";
        ArefHTCompatible verifier = commonVerifier;
        // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
        Date wde;
        if (doc.ds(PTY) == null) {
            return;
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
        // GVA <patch ID="BUG #501 - Compare by SAD Flow" version="4.2.2" type="FIX" date="Dec 11, 2013" author="Leonardo Flores">
		// Dont use EX or IM. Model of declaration code can be different
		// Use flow of declaration instead
		// Export declaration
		if ("E".equals(doc.ds(PTY).de(FLW).getString("I"))) search_cmp_cod = doc.ds(CMP).ds(EXP).de(COD).getString("");
		// Import declaration
		else if ("I".equals(doc.ds(PTY).de(FLW).getString("E"))) search_cmp_cod = doc.ds(CMP).ds(CON).de(COD).getString("");
        // </patch ID="BUG #501 - Compare by SAD Flow">
		
        if (!itm.ds(LNK).ds(PRV).de(WHS).getString("").trim().equals("") && !itm.ds(TAR).ds(PRC).de(EXT).getString("").equals("7171")) {
            // boolean found = verifier.contains(CMP_TAB, wde, new String[]{CMP_COD}, new Object[]{itm.ds(LNK).ds(PRV).de(WHS).getString("").trim()});

            search_cmp_cod = itm.ds(LNK).ds(PRV).de(WHS).getString("").trim();
        }
        if (!"".equals(search_cmp_cod)) {
            Iterator itAgrCmp = verifier.find(AGR_CMP, wde, AGR_COD, CMP_COD, search_cmp_cod);
            while (itAgrCmp.hasNext()) {

                String search_agr_cod = (String) itAgrCmp.next();
                Iterator itAgrTab = verifier.find(AGR_TAB, wde, RUL_COD, new String[] { AGR_COD }, new String[] { search_agr_cod });
                if (itAgrTab.hasNext()) {
                    String rul_cod2 = (String) itAgrTab.next();
                    // Add the reference table rules to the KNumberedSubDataSet
                    if (!"".equals(rul_cod2)) {
                    	// GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed">
                    	//findRule( wde,  itm_ref_rules,  verifier, rul_cod2,new String[] { search_agr_cod }); 
                    	findRuleForAgreement( wde,  itm_ref_rules,  verifier, rul_cod2,search_agr_cod );
                    	// GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed"/>
                    	/*
                        if (verifier.contains(RUL_TAB, wde, RUL_COD, rul_cod2)) {
                            String rulDsc = (String) verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, rul_cod2).next();
                            Object rulDef = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, rul_cod2).next();
                            String rulPty = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, rul_cod2).next().toString();

                            DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
                            itmRefRule.de(NAM).tryToSetContent(rulDsc);
                            itmRefRule.de(DSC).tryToSetContent(rulDef);
                            itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
                            DebugOutput.print("Agr Rule " + rul_cod2);
                        }
                        */
                        
                    }
                    
     
                }
                if (itAgrTab != null) verifier.closeIterator(itAgrTab);
            }
            if (itAgrCmp != null) verifier.closeIterator(itAgrCmp);
        }
    }

    // fin AJ11022009
               
                
    private void getUNTARTABReferenceRule(DataSet doc, DataSet itm, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible commonVerifier) {
        // UNTARTAB rule
        String nb1;
        String search_tar_pr2;
        ArefHTCompatible verifier = commonVerifier;
        if (doc.ds(PTY) == null) {
            nb1 = doc.ds(IDE).de(KEY).getString("").substring(0, 8);
            if (doc.ds(IDE).de(KEY).getString("").length() > 8)
                search_tar_pr2 = doc.ds(IDE).de(KEY).getString("").substring(8);// .substring(8,
            // 10);
            else
                search_tar_pr2 = "000";
        } else {
            nb1 = itm.ds(TAR).ds(HSC).de(NB1).getString("").trim();
            search_tar_pr2 = itm.ds(TAR).ds(HSC).de(NB2).getString("").trim();
        }
        int combinedLength = nb1.length() + search_tar_pr2.length();

        if (nb1.length() == 8 && (combinedLength == 10 || combinedLength == 11)) {
            String search_hs6_cod = nb1.substring(0, 6);
            String search_tar_pr1 = nb1.substring(6, 8);

            // HTVerifier verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
            Date wde;
            if (doc.ds(PTY) == null) {
                wde = (Date) doc.ds(IDE).de(DAT).getContent();
            } else {
                wde = (Date) doc.ds(PTY).de(WDE).getContent();
            }

            DataSet criteria = new DataSet();

            criteria.add(HS6_COD);
            criteria.add(TAR_PR1);
            criteria.add(TAR_PR2);
            criteria.add(WDE);

            criteria.de(HS6_COD).tryToSetContent(search_hs6_cod);
            criteria.de(TAR_PR1).tryToSetContent(search_tar_pr1);
            criteria.de(TAR_PR2).tryToSetContent(search_tar_pr2);
            criteria.de(WDE).tryToSetContent(wde);
            DataSet dest = null;
            Iterator it = null;
            String rul_cod2 = "";
            // System.out.println("Location****** " + location);
         // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
            //if (location.equals("Server")) {
            if (location.equals("Server")|| !REMOTE_TARTAB) {
         // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>
                it = verifier.find(TAR_TAB, wde, RUL_COD, new String[] { HS6_COD, TAR_PR1, TAR_PR2 }, new Object[] {
                        search_hs6_cod,
                        search_tar_pr1,
                        search_tar_pr2 });
                if (it.hasNext()) {
                    rul_cod2 = (String) it.next();
                }

            } else {
                if (doc instanceof KDocument) {

                    // SAD Optimisation January 2011 - begin
                    TransactionEvent te = ((KDocument) doc).applyMiddleEvent(HT_TAR_TAB1, criteria);
                    dest = te.getResult();
                    // SAD Optimisation January 2011 - end

                }
            }

            if (it != null) verifier.closeIterator(it);
            boolean doContinue;
         // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
            //if (location.equals("Server")) {
            if (location.equals("Server")|| !REMOTE_TARTAB) {
      	 // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
                doContinue = true;
            } else {
                if (dest != null) {
                    rul_cod2 = dest.de("it").getString("");
                    doContinue = true;
                } else {
                    doContinue = false;
                }
            }
            if (doContinue == true) {
            	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
            	findRule( wde,  itm_ref_rules,  verifier, rul_cod2) ;
            	/*
                if (rul_cod2!=null && !"".equals(rul_cod2)) {
                    if (verifier.contains(RUL_TAB, wde, RUL_COD, rul_cod2)) {
                        String rulDsc = (String) verifier.find(RUL_TAB, wde, RUL_DSC, RUL_COD, rul_cod2).next();
                        Object rulDef = verifier.find(RUL_TAB, wde, RUL_DEF, RUL_COD, rul_cod2).next();
                        String rulPty = verifier.find(RUL_TAB, wde, RUL_PTY, RUL_COD, rul_cod2).next().toString();

                        DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
                        itmRefRule.de(NAM).tryToSetContent(rulDsc);
                        itmRefRule.de(DSC).tryToSetContent(rulDef);
                        itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
                    }
                }
            	*/
            	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
            }
        }

    }
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    private void findRule(Date wde, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible verifier,String rul_cod2) {
    	
    	
        Iterator itRulTab = verifier.find(RUL_TAB, wde,  new String[] { RUL_COD }, new Object[] { rul_cod2 });
        while (itRulTab.hasNext()) {
        	Object row=   itRulTab.next();
        	
        	 String rulDsc = (String)getColumn(row,RUL_COD); 
             Object rulDef = getColumn(row,RUL_DEF) ;
             String rulPty = (String)getColumn(row,RUL_PTY);  
             
             Object rulJavDsc = getColumn(row,C_TaxationRules.RUL_JAV_DSC);
             
             DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
             itmRefRule.de(NAM).tryToSetContent(rulDsc);
             itmRefRule.de(DSC).tryToSetContent(rulDef);
             itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
             if (rulJavDsc!=null){
            	 itmRefRule.de(JAV).tryToSetContent(RuleUtils.toObject((byte[])rulJavDsc) );
             }
             
        }
     // CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed">
        if (itRulTab != null) verifier.closeIterator(itRulTab);
     // CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed"/>
    } 
    
 // GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed">
 private void findRuleForAgreement(Date wde, KNumberedSubDataSet itm_ref_rules, ArefHTCompatible verifier,String rul_cod2,String  search_agr_cod) {
    	
    	
        Iterator itRulTab = verifier.find(RUL_TAB, wde,  new String[] { RUL_COD }, new Object[] { rul_cod2 });
        while (itRulTab.hasNext()) {
        	Object row=  itRulTab.next();
        	
        	 String rulDsc = (String)getColumn(row,RUL_COD); 
             Object rulDef = getColumn(row,RUL_DEF) ;
             String rulPty = (String)getColumn(row,RUL_PTY);  
             
             Object rulJavDsc = getColumn(row,C_TaxationRules.RUL_JAV_DSC);
             
             DS_Res_TaxRule itmRefRule = (DS_Res_TaxRule) itm_ref_rules.at(itm_ref_rules.countDocuments() + 1);
             itmRefRule.de(NAM).tryToSetContent(rulDsc);
             itmRefRule.de(DSC).tryToSetContent(rulDef);
             itmRefRule.de(PRI).tryToSetContent(new Integer(rulPty));
             if (rulJavDsc!=null){
            	 itmRefRule.de(JAV).tryToSetContent(RuleUtils.toObject((byte[])rulJavDsc) );
             }
             Iterator itAgrTabHsCode = verifier.find(AGR_TAB, wde, "SEL_HSC", new String[] { AGR_COD }, new String[] { search_agr_cod });
                     
             if (itAgrTabHsCode.hasNext()) {
                 String hsCode = (String) itAgrTabHsCode.next();
                 // DebugOutput.print("SEL_HSC Code : " + hsCode);
                 if (!"".equals(hsCode)) {
                     Vector v = parseCode(ConvertString(hsCode, ","));
                     itmRefRule.de(RES).tryToSetContent(v);
                     
                 }
             }     
             
        }
     // CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed">
        if (itRulTab != null) verifier.closeIterator(itRulTab); 
     // CI <patch ID="CLOSE CURSOR Bug #618" version="4.2.1" type="modification" date="APR 27, 2014" author="ahmed"/>
    } 
//GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed"/>
    /**
     * 
     * @return
     */
    public DataSet returnDocument() {
        return doc;
    }
    
    // GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed"/>

    private String ConvertString(String chIn, String pattern) {

        // return chIn.replaceAll(pattern, "]+[");

        return chIn.replaceAll(pattern, "..");
        // return ("[01..997]");
    }
    private Vector parseCode(String line) {
        Vector v = null;
        if (TariffManagementParser.tariffManagementParser == null) {
            TariffManagementParser.tariffManagementParser = new TariffManagementParser(new StringReader(""));
        } else {
            TariffManagementParser.tariffManagementParser.ReInit(System.in);
        }

        TariffManagementParser.ReInit(new StringReader(line));
        try {
            TariffManagementParser.Start();
            v = TariffManagementParser.getIntervalList();

        } catch (ParseException pe) {
            ParserError pError = TariffManagementParser.getError();
            DebugOutput.print(pError.toString());
            // setError(d, );
        }
        return v;
    }
    public boolean isInRange(String ctrHsCodeSt, Vector v) {
    	boolean flgFind  = false;
    	for (int i = 0; i < v.size(); i++) {
            Interval o = (Interval) v.get(i);
            
            
            if (ctrHsCodeSt != null && !"".equals(ctrHsCodeSt)) {
                try {
                    int ctrHsCodeMin = Integer.parseInt(ctrHsCodeSt.substring(0, o.lo.length()));
                    int ctrHsCodeMax = Integer.parseInt(ctrHsCodeSt.substring(0, o.hi.length()));
                    if (ctrHsCodeMin >= Integer.parseInt(o.lo) && ctrHsCodeMax <= Integer.parseInt(o.hi)) {
                        flgFind  = true;
                        break;
                    }
                } catch (Exception ex) {
                    DebugOutput.print("Conversion PB : " + ex.getMessage());
                }
            }
        }
		return flgFind;
	}
    // GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed"/>
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

