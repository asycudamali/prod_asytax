package un.asytax.taxation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import so.kernel.Global;
import so.kernel.core.DataSet;
import so.kernel.core.KDocument;
import so.kernel.core.KNumberedSubDataSet;
import so.kernel.core.KNumberedSubDocument;
import so.kernel.core.KernelEventConstants;
import so.kernel.core.MessageDestination;
import so.kernel.core.SubDataSet;
import so.kernel.core.TransactionEvent;
import so.kernel.core.interfaces.KDocumentInterface;
import so.util.calendar.DateValue;
import un.asytax.services.ScanVerifierService;
import un.asytax.services.spi.ScanVerifier;
import un.kernel.core.ArefHTCompatible;
import un.kernel.core.Rule;
import un.kernel.util.RuleSyntaxClasses;
import un.kernel.util.dynamicparser.ParserInterface;

public class TaxationParserInterface implements C_Tax, ParserInterface, RuleSyntaxClasses {
    // <patch ID=Taxation symtab not static=GVA version=4.1.0 type=change Date=15 Apr 2011 author=john.david>
    // removed unused static variable (symtab) - 15/04/2011
    // </patch>

	// GVA <patch ID="#601 Taxation: displayErrStop does not work on server side" version="4.2.2" type="BUG" date="Jun 17, 2014" author="Leonardo Flores">
    private ArrayList<String> errorMessage;
	// </patch ID="Taxation: displayErrStop does not work on server side">


    private ArrayList tokenList;

    private DataSet doc;

    private DataSet itm;

    private int ruleRnk;

    private DataSet res1, res2;

    private ArrayList inListTar;

    private boolean inListTarLoaded;

    private String itmComCod;

    private ArefHTCompatible verifier;

    private Date wde;

    private final ManualDataListener manualDataListener = null;
    private String location;

    @Override
    public int getVarID(String varName) {
        ArrayList tokenList = getTokenList();
        Collections.sort(tokenList, new sortByName());
        int idx = Collections.binarySearch(tokenList, varName, new sortByName());
        if (idx >= 0) {
            Object token = tokenList.get(idx);
            if (token instanceof RuleVariable) {
                return ((RuleVariable) token).getCode();
            }
        }
        return -1;
    }

    public TaxationParserInterface(ArrayList tokenList, DataSet doc, DataSet itm, int ruleRnk, ArefHTCompatible commonVerifier, String location) {
        this.tokenList = tokenList;
        this.doc = doc;
        this.itm = itm;
        this.ruleRnk = ruleRnk;
        this.location = location;
        inListTar = new ArrayList(50);
        inListTarLoaded = false;
        // verifier = HTVerifier.HTVerifierFactory.getCurrentFactory().getVerifier(doc);
        verifier = commonVerifier;
        if (doc.ds(PTY) == null) {
            wde = (Date) doc.ds(IDE).de(DAT).getContent();
        } else {
            wde = (Date) doc.ds(PTY).de(WDE).getContent();
        }
    }

    public TaxationParserInterface(ArrayList tokenList, DataSet doc, DataSet itm, int ruleRnk, DataSet res1, DataSet res2, ArefHTCompatible commonVerifier, String location) {
        this(tokenList, doc, itm, ruleRnk, commonVerifier, location);
        this.res1 = res1;
        this.res2 = res2;
    }

    public void setTokenList(ArrayList tok) {
        tokenList = tok;
    }

    public ArrayList getTokenList() {
        return tokenList;
    }

    public DataSet getItm() {
		return itm;
	}

	public void setItm(DataSet itm) {
		this.itm = itm;
	}

	public int getRuleRnk() {
		return ruleRnk;
	}


	public void setRuleRank(int rank) {
        ruleRnk = rank;
    }

    public DataSet getRes1() {
		return res1;
	}

	public void setRes1(DataSet res1) {
		this.res1 = res1;
	}

	public DataSet getRes2() {
		return res2;
	}

	public void setRes2(DataSet res2) {
		this.res2 = res2;
	}

	public DataSet getDoc() {
		return doc;
	}

	public void setDoc(DataSet doc) {
		this.doc = doc;
	}

	// GVA <patch ID="Taxation Memory Optimization" version="4.2" type="Change" date="7 Aug 2012" author="jdavid">
	public void init(ArrayList tokenList, DataSet itm, int ruleRnk, DataSet res1, DataSet res2) {
		this.res1 = res1;
		this.res2 = res2;
		this.tokenList = tokenList;
		this.itm = itm;
		this.ruleRnk = ruleRnk;
		itmComCod = null;
		inListTar = new ArrayList(50);
		inListTarLoaded = false;
		if (doc.ds(PTY) == null) {
			wde = (Date) doc.ds(IDE).de(DAT).getContent();
		} else {
			wde = (Date) doc.ds(PTY).de(WDE).getContent();
		}
	}
	//</patch>
	public TaxationParserInterface() {
        /*
         * this.tokenList = tokenList; this.doc = doc; this.itm = itm; this.ruleRnk = ruleRnk;
         */

    }

    @Override
    public Double roundInf(Double val) {
        return new Double(Math.floor(val.doubleValue()));
    }

    @Override
    public Double roundSup(Double val) {
        return new Double(Math.ceil(val.doubleValue()));
    }

    @Override
    public Double round(Double val1) {
        return new Double(Math.round(val1.doubleValue()));
    }

    // GVA <patch ID="Improvement #511 - New functions" version="4.2.2" type="NEW" date="Dec 29, 2013" author="Leonardo Flores">
    @Override
    public Double roundExt(Double number, Integer decimals, Integer mode){
        BigDecimal bigDecimal = new BigDecimal(number.toString());
        return bigDecimal.setScale(decimals,RoundingMode.valueOf(mode)).doubleValue();
    }
	// </patch ID="Improvement #511 - New functions">

    @Override
    public Double min(Double val1, Double val2) {
        return new Double(Math.min(val1.doubleValue(), val2.doubleValue()));
    }

    @Override
    public Double max(Double val1, Double val2) {
        return new Double(Math.max(val1.doubleValue(), val2.doubleValue()));
    }

    @Override
    public Double abs(Double val1) {
        return new Double(Math.abs(val1.doubleValue()));
    }

    @Override
    public Double addAttDoc(String s) {
        if (itm == null) {
            return new Double(-1);
        } else {
            String oldAttDoc = "";
            /*
             * if (itm.ds(TAR) == null) {//commente pour l inslure dans la simulation de la taxation du tariif return new Double(-1); }
             */
            if (!"".equals(itm.ds(TAR).de(ATT).getString("").trim())) {
                oldAttDoc = itm.ds(TAR).de(ATT).getString("").trim() + " ";
            }
            String newAttDoc = oldAttDoc + s.trim() + " ";

            if (oldAttDoc.startsWith(s.trim())) {
                return new Double(1);
            } else if (oldAttDoc.indexOf(s.trim()) != -1) {
                return new Double(-10);
            } else if (newAttDoc.length() > 56) {
                return new Double(-11);
            } else {
                itm.ds(TAR).de(ATT).tryToSetContent(newAttDoc);
                return new Double(0);
            }
        }
    }

    /*
     * public void addManualDataListener(ManualDataListener manualDataListener) { this.manualDataListener = manualDataListener; }
     */

    @Override
    public Double askTax(String s1, String s2, Double d1, Double d2, Double d3) {
        return new Double(0);
    }
    
    @Override
    public Double addTax(String s1, String s2, Double d1, Double d2, Double d3) {
        return new Double(0);
    }
    

    @Override
    public Double curRate(String s) {

        Iterator it = verifier.find(RAT_TAB, wde, RAT_EXC, CUR_COD, s.trim());
        if (it.hasNext()) {
            Number rat_exc = (Number) it.next();
            if (it != null) verifier.closeIterator(it);
            return new Double(rat_exc.doubleValue());
        }
        if (it != null) verifier.closeIterator(it);
        return new Double(-1);
    }

    @Override
    public Double delAllTax(Double d) {
        doc.ds(TAX).de(DEL).tryToSetContent(true);
        return new Double(0);
    }

    @Override
    public Double delAttDoc(String s) {
        if (itm == null) {
            return new Double(-12);
        } else {
            /*
             * if (itm.ds(TAR) == null) { return new Double(-12); }
             */
            String attDoc = itm.ds(TAR).de(ATT).getString("").trim() + " ";

            int startIndex = attDoc.indexOf(s.trim());
            int endIndex = attDoc.indexOf(" ", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                StringBuffer buffer = new StringBuffer(attDoc);
                buffer.delete(startIndex, endIndex + 1);
                itm.ds(TAR).de(ATT).setString(buffer.toString());
                return new Double(0);
            } else {
                return new Double(-12);
            }
        }
    }
    
    // GVA <patch ID="askTax" version="4.2.1" type="modification" date="Nov 28, 2012" author="ahmed">
    public void saveAskTaxDeleted(String taxCode){
    }
    // GVA <patch ID="askTax" version="4.2.1" type="modification" date="Nov 28, 2012" author="ahmed">

    @Override
    public Double delTax(String s) {
        boolean exists = false;
    
        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);

            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    if ("M".equals(glo_tax.de(TYP).getContent())) {
                    	 // GVA <patch ID="askTax" version="4.2.1" type="modification" date="Nov 28, 2012" author="ahmed">
                    		saveAskTaxDeleted(s);
                        // GVA <patch ID="askTax" />
                    }
                    // The number of visible taxes on the Assessment Notice is 3 so we need
                    // to have at least 3 instance of the DS_ at ALL time

                    // IF the deleted tax is one of the "visible" (one of 3 shown on the Assessment Notice)
                    if (i <= NBR_GLO_TAXES_VISIBLE) {
                        // We keep the information on the tax to delete
                        DataSet glo_taxToDel = new DataSet(DEL);
                        glo_taxToDel.copyFrom(glo_tax_sds.at(i));

                        // We slide down the taxes
                        for (int j = i + 1; j <= glo_tax_sds.countDocuments(); j++) {
                            KNumberedSubDocument glo_tax2 = glo_tax_sds.at(j - 1);
                            KNumberedSubDocument glo_tax3 = glo_tax_sds.at(j);
                            /*
                             * glo_tax2.de(COD).copyFrom(glo_tax3.de(COD)); glo_tax2.de(DSC).copyFrom(glo_tax3.de(DSC)); glo_tax2.de(BSE).copyFrom(glo_tax3.de(BSE)); glo_tax2.de(RAT).copyFrom(glo_tax3.de(RAT)); glo_tax2.de(AMT).copyFrom(glo_tax3.de(AMT)); glo_tax2.de(MOP).copyFrom(glo_tax3.de(MOP));
                             */
                            glo_tax2.de(COD).tryToSetContent(glo_tax3.de(COD).getContent());
                            glo_tax2.de(DSC).tryToSetContent(glo_tax3.de(DSC).getContent());
                            glo_tax2.de(BSE).tryToSetContent(glo_tax3.de(BSE).getContent());
                            glo_tax2.de(RAT).tryToSetContent(glo_tax3.de(RAT).getContent());
                            glo_tax2.de(AMT).tryToSetContent(glo_tax3.de(AMT).getContent());
                            glo_tax2.de(MOP).tryToSetContent(glo_tax3.de(MOP).getContent());
                            glo_tax2.de(TYP).tryToSetContent(glo_tax3.de(TYP).getContent());
                        }
                        // IF there are 3 taxes or less (1, 2 or 3), so third one need to be set to NULL
                        if (glo_tax_sds.countDocuments() == NBR_GLO_TAXES_VISIBLE) {
                            KNumberedSubDocument glo_tax4 = glo_tax_sds.at(glo_tax_sds.countDocuments());
                            glo_tax4.de(COD).tryToSetContent(null);
                            glo_tax4.de(DSC).tryToSetContent(null);
                            glo_tax4.de(BSE).tryToSetContent(null);
                            glo_tax4.de(RAT).tryToSetContent(null);
                            glo_tax4.de(AMT).tryToSetContent(null);
                            glo_tax4.de(MOP).tryToSetContent(null);
                            glo_tax4.de(TYP).tryToSetContent(null);
                            // ELSE - The Number of taxes is more than 3 so we need to delete the last taxes (after the slide).
                        } else {
                            // We do as if the last tax was the one to delete (to avoid problem with the DS management - old correction)
                            KNumberedSubDocument glo_taxLast = glo_tax_sds.at(glo_tax_sds.countDocuments());
                            glo_taxLast.de(COD).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(COD).getContent());
                            glo_taxLast.de(DSC).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(DSC).getContent());
                            glo_taxLast.de(BSE).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(BSE).getContent());
                            glo_taxLast.de(RAT).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(RAT).getContent());
                            glo_taxLast.de(AMT).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(AMT).getContent());
                            glo_taxLast.de(MOP).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(MOP).getContent());
                            glo_taxLast.de(TYP).tryToSetContent(glo_taxToDel.ds(KDocumentInterface.NORMAL_ID).de(TYP).getContent());

                            glo_tax_sds.deleteDocument(glo_tax_sds.countDocuments());
                        }
                        // ELSE - The tax is a hidden one (not the first 3 that are displayed on the ASN).
                        // No problem in this case, we can delete directly
                    } else {
                        glo_tax_sds.deleteDocument(i);
                    }
                    break;
                }
            }

            KNumberedSubDataSet tmp_glo_tax_sds = (KNumberedSubDataSet) doc.ds(TMP).ds(GTX).ds(GLT);
            for (int i = 1; i <= tmp_glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument tmp_glo_tax = tmp_glo_tax_sds.at(i);
                if (tmp_glo_tax.de(COD).getString("").trim().equals(s.trim())) {
                    tmp_glo_tax_sds.deleteDocument(i);
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(0);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    if ("M".equals(itm_tax.de(TYP).getContent())) {
                    	// GVA <patch ID="askTax" version="4.2.1" type="modification" date="Nov 28, 2012" author="ahmed">
                		  saveAskTaxDeleted(s);
                        // GVA <patch ID="askTax" />
                    }

                    // System.out.println("deleting tax " + s);
                    if ("M".equals(itm_tax.de(TYP).getString("").trim()) && doc.ds(GTX) != null) {
                        KNumberedSubDataSet itm_tax_sds2 = (KNumberedSubDataSet) itm.ds(TMP).ds("ASK");
                        // System.out.println("Adding deleted tax to temp segment");
                        KNumberedSubDocument askdel_tax = itm_tax_sds2.at(itm_tax_sds2.countDocuments() + 1);
                        askdel_tax.de(COD).tryToSetContent(itm_tax.de(COD).getContent());
                        askdel_tax.de(BSE).tryToSetContent(itm_tax.de(BSE).getContent());
                        askdel_tax.de(RAT).tryToSetContent(itm_tax.de(RAT).getContent());
                        askdel_tax.de(AMT).tryToSetContent(itm_tax.de(AMT).getContent());
                        askdel_tax.de(MOP).tryToSetContent(itm_tax.de(MOP).getContent());
                        askdel_tax.de(TYP).tryToSetContent(itm_tax.de(TYP).getContent());
                    }
                    for (int j = i + 1; j <= itm_tax_sds.countDocuments(); j++) {
                        KNumberedSubDocument itm_tax2 = itm_tax_sds.at(j - 1);
                        KNumberedSubDocument itm_tax3 = itm_tax_sds.at(j);
                        itm_tax2.de(COD).tryToSetContent(itm_tax3.de(COD).getContent());
                        // itm_tax2.de(DSC).copyFrom(itm_tax3.de(DSC));
                        itm_tax2.de(BSE).tryToSetContent(itm_tax3.de(BSE).getContent());
                        itm_tax2.de(RAT).tryToSetContent(itm_tax3.de(RAT).getContent());
                        itm_tax2.de(AMT).tryToSetContent(itm_tax3.de(AMT).getContent());
                        itm_tax2.de(MOP).tryToSetContent(itm_tax3.de(MOP).getContent());
                        itm_tax2.de(TYP).tryToSetContent(itm_tax3.de(TYP).getContent());
                    }
                    KNumberedSubDocument itm_tax4 = itm_tax_sds.at(itm_tax_sds.countDocuments());
                    itm_tax4.de(COD).tryToSetContent(null);
                    // itm_tax4.de(DSC).tryToSetContent(null);
                    itm_tax4.de(BSE).tryToSetContent(null);
                    itm_tax4.de(RAT).tryToSetContent(null);
                    itm_tax4.de(AMT).tryToSetContent(null);
                    itm_tax4.de(MOP).tryToSetContent(null);
                    itm_tax4.de(TYP).tryToSetContent(null);

                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(0);
            }
        }
    }

    @Override
    public Double displayErrStop(String s) {
        MessageDestination field;
        if (doc.ds(PTY) == null) {
            field = doc.de("IND");
        } else {
            field = doc.ds(PTY).de(WDE);
        }
    	// GVA <patch ID="#601 Taxation: displayErrStop does not work on server side" version="4.2.2" type="BUG" date="Jun 17, 2014" author="Leonardo Flores">
        if (Global.isServer()) addServerSideMessage(lng(s));
        else
    	// </patch ID="Taxation: displayErrStop does not work on server side">
        Rule.error(field, lng(s), KernelEventConstants.DOCUMENT_ALWAYS_VERIFY); 
        return new Double(0);
    }

    @Override
    public Double doTaric(String s) {
        return new Double(0);
    }

    @Override
    public Double doTax(String s1, String s2, Double d1, Double d2, Double d3) {
        return new Double(0);
    }
    

    @Override
    public Double getLength(String s) {
        if (s.length() > 0) {
            return new Double(s.length());
        } else {
            return new Double(0);
        }
    }

    @Override
    public Double inListOr(String s) {
        return new Double(0);
    }

    @Override
    public Double inListAnd(String s) {
        return new Double(0);
    }

    @Override
    public Double inListBol(String s) {
        return new Double(0);
    }

    @Override
    public Double inListCom(String s) {
        String search_cmp_cod = "";

        if (doc == null) {
            return new Double(1);
        }
        if (doc.ds(PTY) == null) {
            return new Double(1);
        }
        if (doc.ds(PTY).de(FLW).getString("").trim().equals("E")) {
            search_cmp_cod = doc.ds(CMP).ds(EXP).de(COD).getString("").trim();
        } else if (doc.ds(PTY).de(FLW).getString("").trim().equals("I")) {
            search_cmp_cod = doc.ds(CMP).ds(CON).de(COD).getString("").trim();
        }

        if ("".equals(search_cmp_cod)) {
            return new Double(1);
        }

//        Date s_date = (Date) doc.ds(PTY).de(WDE).getContent();
//        if (s_date == null) s_date = new DateValue(new Date().getTime());
//        java.util.Date now = s_date;
        boolean found = verifier.contains(TAX_CMP, wde, new String[] { LST_COD, CMP_COD }, new Object[] { s.trim(), search_cmp_cod.trim() });
        if (found) {
            return new Double(0);
        }
        return new Double(1);
    }

    @Override
    public Double inListCty(String s) {
        return new Double(0);
    }

    @Override
    public Double inListCtyPd(String s) {
        return new Double(0);
    }

    @Override
    public Double inListDec(String s) {
        return new Double(0);
    }

    @Override
    public Double inListFin(String s) {
        return new Double(0);
    }

    @Override
    public Double inListTar(String s) {
    	 
        if (s == null || s.equals("")) return new Double(1);

        if (doc == null) {
            return new Double(1);
        }

        if (itm == null) {
            return new Double(1);
        } else {
            if (itmComCod == null || "".equals(itmComCod)) itmComCod = getVarSValue(402);
        }

        if ("".equals(itmComCod)) {
            return new Double(1);
        }
        System.out.println("Old inlisttar()");
        if (!inListTarLoaded) {

            DataSet criteria = new DataSet();

            criteria.add(COD);
            criteria.add(WDE);

            criteria.de(COD).tryToSetContent(itmComCod);
            criteria.de(WDE).tryToSetContent(wde);
            DataSet dest = null;
            if (location.equals("Client") && REMOTE_TAXTAR) {// (doc instanceof KDocument){
                TransactionEvent te = ((KDocument) doc).applyMiddleEvent(TAXATION, criteria);
                dest = te.getResult();
            } else { // if server then we need to call rule SR_Taxation directly without middle event
                dest = getFromTaxTar(itmComCod, wde, verifier);
            }

            if (dest != null) {

                ArrayList it1cod = (ArrayList) dest.de("it1cod").getContent();
                for (int i = 0; i < it1cod.size(); i++) {
                    String lst_cod = (String) it1cod.get(i);

                    if (!inListTar.contains(lst_cod)) inListTar.add(lst_cod);
                }

                // String lst_cod2 = dest.de("it2cod").getString("");
                ArrayList it2cod = (ArrayList) dest.de("it2cod").getContent();
                for (int i = 0; i < it2cod.size(); i++) {
                    String lst_cod2 = (String) it2cod.get(i);

                    if (!inListTar.contains(lst_cod2)) inListTar.add(lst_cod2);
                }

                // String lst_cod3 = dest.de("it3cod").getString("");
                ArrayList it3cod = (ArrayList) dest.de("it3cod").getContent();
                for (int i = 0; i < it3cod.size(); i++) {
                    String lst_cod3 = (String) it3cod.get(i);

                    if (!inListTar.contains(lst_cod3)) inListTar.add(lst_cod3);
                }

                // String lst_cod4 = dest.de("it4cod").getString("");
                ArrayList it4cod = (ArrayList) dest.de("it4cod").getContent();

                for (int i = 0; i < it4cod.size(); i++) {
                    String lst_cod4 = (String) it4cod.get(i);

                    if (!inListTar.contains(lst_cod4)) inListTar.add(lst_cod4);
                }

                // String lst_cod5 = dest.de("it5cod").getString("");

                ArrayList it5cod = (ArrayList) dest.de("it5cod").getContent();
                for (int i = 0; i < it5cod.size(); i++) {
                    String lst_cod5 = (String) it5cod.get(i);

                    if (!inListTar.contains(lst_cod5)) inListTar.add(lst_cod5);
                }

                // String lst_cod6 = dest.de("it6cod").getString("");
                ArrayList it6cod = (ArrayList) dest.de("it6cod").getContent();
                for (int i = 0; i < it6cod.size(); i++) {
                    String lst_cod6 = (String) it6cod.get(i);

                    if (!inListTar.contains(lst_cod6)) inListTar.add(lst_cod6);
                }

                ArrayList it7cod = (ArrayList) dest.de("it7cod").getContent();
                for (int i = 0; i < it7cod.size(); i++) {
                    String lst_cod7 = (String) it7cod.get(i);

                    if (!inListTar.contains(lst_cod7)) inListTar.add(lst_cod7);
                }

                ArrayList it8cod = (ArrayList) dest.de("it8cod").getContent();
                for (int i = 0; i < it8cod.size(); i++) {
                    String lst_cod8 = (String) it8cod.get(i);

                    if (!inListTar.contains(lst_cod8)) inListTar.add(lst_cod8);
                }

                ArrayList it10_11cod = (ArrayList) dest.de("it10_11cod").getContent();
                for (int i = 0; i < it10_11cod.size(); i++) {
                    String lst_cod10_11 = (String) it10_11cod.get(i);

                    if (!inListTar.contains(lst_cod10_11)) inListTar.add(lst_cod10_11);
                }

            }

            inListTarLoaded = true;
        }

        if (inListTar.contains(s)) return new Double(0);

        return new Double(1);

    }

    @Override
    public Double julianDate(String s) {
        int year = 0;
        int month = 0;
        int day = 0;
        try {
            year = Integer.parseInt(s.substring(0, 4));
            month = Integer.parseInt(s.substring(4, 6));
            day = Integer.parseInt(s.substring(6));
        } catch (NumberFormatException e) {
        }

        double timeofday;
        int julian;
        int GREG = 15 + 31 * (10 + 12 * 1582);

        int adj;

        if (year < 0) {
            year = year + 1;
        }
        if (month > 2) {
            month = month + 1;
        } else {
            year = year - 1;
            month = month + 13;
        }

        julian = (int) (365.25 * year) + (int) (30.6001 * month) + day + 1720995;

        if (day + 31 * (month + 12 * year) >= GREG) {
            adj = year / 100;
            julian = julian + 2 - adj + adj / 4;
        }

        return new Double(julian);

    }

    @Override
    public Double rate(String s) {
        return rateCol(s, new Double(ruleRnk));
    }

    @Override
    public Double rateCol(String s, Double d) {
        if (s == null) {
            return new Double(-14);
        }
        int colNbr = d.intValue();

        if (colNbr < 1) {
            return new Double(-14);
        } else {
            // Ensure that column number is 2 digits long
            String s2 = (new Integer(colNbr)).toString();
            if (s2.length() > 2) {
                return new Double(-14);
            }
            if (s2.length() == 1) {
                s2 = "0" + s2;
            }

            if (colNbr <= 15) {
                String s3 = "TAR_T" + s2;

                if (s.length() != 10 && s.length() != 11) {
                    // Display error - commodity code not found
                    return new Double(-4);
                }
                String search_hs6_cod = s.substring(0, 6);
                String search_tar_pr1 = s.substring(6, 8);
                String search_tar_pr2;
                if (s.length() == 10) {
                    search_tar_pr2 = s.substring(8, 10);
                } else {
                    search_tar_pr2 = s.substring(8, 11);
                }

                // debut salem

                DataSet criteria = new DataSet();

                criteria.add(HS6_COD);
                criteria.add(TAR_PR1);
                criteria.add(TAR_PR2);
                criteria.add("S3");
                criteria.add(WDE);

                criteria.de(HS6_COD).tryToSetContent(search_hs6_cod);
                criteria.de(TAR_PR1).tryToSetContent(search_tar_pr1);
                criteria.de(TAR_PR2).tryToSetContent(search_tar_pr2);
                criteria.de("S3").tryToSetContent(s3);
                criteria.de(WDE).tryToSetContent(wde);
                DataSet dest = null;
                String tar_rat = null;
                // System.out.println("Location " + location);
             // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
                //if (location.equals("Server")) {
                if (location.equals("Server")|| !REMOTE_TARTAB) {
             // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>   	
                    Iterator it = verifier.find(TAR_TAB, wde, s3, new String[] { HS6_COD, TAR_PR1, TAR_PR2 }, new Object[] {
                            search_hs6_cod,
                            search_tar_pr1,
                            search_tar_pr2 });
                    if (it.hasNext()) tar_rat = (String) it.next();
                    if (it != null) verifier.closeIterator(it);

                } else {
                    if (doc instanceof KDocument) {

                        // SAD Optimisation January 2011 - begin
                        TransactionEvent te = ((KDocument) doc).applyMiddleEvent(HT_TAR_TAB2, criteria);
                        dest = te.getResult();
                        // SAD Optimisation January 2011 - end

                    }
                }

                boolean doContinue;
             // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
                //if (location.equals("Server")) {
                if (location.equals("Server")|| !REMOTE_TARTAB) {
             // GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>                	
                    doContinue = true;
                } else {
                    if (dest != null) {
                        tar_rat = dest.de("it").getString("");
                        doContinue = true;
                    } else {
                        doContinue = false;
                    }
                }

                if (doContinue == true) {
                    // tar_rat = dest.de("it").getString("");

                    if (tar_rat == null || tar_rat.equals("")) {
                        return new Double(-6);
                    }

                    double rate;
                    try {
                        rate = (new Double(tar_rat)).doubleValue();
                        return new Double(rate);
                    } catch (NumberFormatException _) {
                        Iterator it2 = verifier.find(RTX_TAB, wde, RTX_RAT, RTX_COD, tar_rat);
                        if (it2.hasNext()) {
                            Number rtx_rat = (Number) it2.next();
                            /** Start Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                            verifier.closeIterator(it2);
                            /** End Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                            return new Double(rtx_rat.doubleValue());
                        }
                        if (it2 != null) verifier.closeIterator(it2);
                        // Display error - rate not found
                        return new Double(-5);
                    }
                }

                // Display error - commodity code not found
                return new Double(-4);
            } else {

                if (s.length() != 10 && s.length() != 11) {
                    // Display error - commodity code not found
                    return new Double(-4);
                }

                String search_hsc_cod = null;

                for (int i = 1; i <= 5; i++) {
                    switch (i) {
                    case 1:
                        search_hsc_cod = s;
                        break;
                    case 2:
                        search_hsc_cod = s.substring(0, 8);
                        break;
                    case 3:
                        search_hsc_cod = s.substring(0, 6);
                        break;
                    case 4:
                        search_hsc_cod = s.substring(0, 4);
                        break;
                    case 5:
                        search_hsc_cod = s.substring(0, 2);
                        break;
                    }

                    Iterator it = verifier.find(TAR_COL, wde, TAR_TXX, new String[] { COL_NUM, HSC_COD }, new Object[] { s2, search_hsc_cod });
                    if (it.hasNext()) {
                        String tar_txx = (String) it.next();
                        /** Start Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                        verifier.closeIterator(it);
                        /** End Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                        if (tar_txx.equals("")) {
                            return new Double(-6);
                        }

                        double rate;
                        try {
                            rate = (new Double(tar_txx)).doubleValue();
                            return new Double(rate);
                        } catch (NumberFormatException _) {

                            Iterator it2 = verifier.find(RTX_TAB, wde, RTX_RAT, RTX_COD, tar_txx);

                            if (it2.hasNext()) {

                                Number rtx_rat = (Number) it2.next();
                                /** Start Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                                verifier.closeIterator(it2);
                                /** End Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                                return new Double(rtx_rat.doubleValue());
                            }
                            if (it2 != null) verifier.closeIterator(it2);
                            // Display error - rate not found
                            return new Double(-5);
                        }
                    }
                    /** Start Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
                    else if (it != null) verifier.closeIterator(it);
                    /** End Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */

                }
                // Display error - commodity code not found
                return new Double(-4);
            }
        }
    }

    @Override
    public Double relTax(String s1, String s2, Double d1, Double d2, Double d3) {
        boolean exists = false;
        String mop = null;

        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s1.trim())) {
                    exists = true;
                    mop = glo_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return updTax(s1, mop, d1, d2, d3);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s1.trim())) {
                    exists = true;
                    mop = itm_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return updTax(s1, mop, d1, d2, d3);
            }
        }
    }

    @Override
    public Double setWrkDate(Double d) {
        return new Double(0);
    }

    @Override
    public Double sqr(Double d) {
        return new Double(Math.pow(d.doubleValue(), 2.0));
    }

    @Override
    public Double subStrFnd(String s1, String s2, Double d1, Double d2, Double d3) {
        int i1 = (int) d1.doubleValue();
        int i2 = (int) d2.doubleValue();
        int i3 = (int) d3.doubleValue();
        boolean caseSensitive;

        if (i3 == 1) {
            caseSensitive = true;
        } else {
            caseSensitive = false;
        }

        if (s2.length() > s1.length()) {
            return new Double(-1);
        } else if (s1.trim().equals("") || s2.trim().equals("")) {
            return new Double(-1);
        }

        if (i1 < 0) {
            i1 = 1;
        } else if (i1 == 0 && i2 != 0) {
            i1 = 1;
        }

        if (i2 < 0) {
            return new Double(-1);
        } else if (i1 != 0 && i2 == 0) {
            return new Double(-1);
        }

        if (i1 == 0 && i2 == 0) {
            if (caseSensitive) {
                if (s1.indexOf(s2) != -1) {
                    return new Double(0);
                } else {
                    return new Double(-1);
                }
            } else {
                String expr = "(?i).*" + s2 + ".*";
                if (s1.matches(expr)) {
                    return new Double(0);
                } else {
                    return new Double(-1);
                }
            }
        }

        // GVA <patch ID="Bug #251 SubStrFnd not working like ++" version="4.2.2" type="modification" date="Aug 23, 2013" author="JD">
        if (((i2 - i1) + 1) > s1.length()) {
            return new Double(-1);
        } else if (i2 > s1.length()) {
            return new Double(-1);
        } else {
            s1 = s1.substring((i1 - 1), (i1 - 1) + i2);
        // GVA <patch ID="Bug #251 SubStrFnd not working like ++"/>
            if (s2.length() > s1.length()) {
                return new Double(-1);
            } else {
                if (caseSensitive) {
                    if (s1.indexOf(s2) != -1) {
                        return new Double(0);
                    } else {
                        return new Double(-1);
                    }
                } else {
                    String expr = "(?i).*" + s2 + ".*";
                    if (s1.matches(expr)) {
                        return new Double(0);
                    } else {
                        return new Double(-1);
                    }
                }
            }
        }
    }

    @Override
    public Double taxAmount(String s) {
        boolean exists = false;
        double amt = 0.0;

        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    amt = glo_tax.de(AMT).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(amt);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    amt = itm_tax.de(AMT).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(amt);
            }
        }
    }
    
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    public Double taxMP(String var, String s2) {
    	
    	return taxMP(getVarID(var),s2);
    	
    }
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
    
    @Override
    public Double taxMP(Integer i1, String s2) {
        boolean exists = false;
        String mop = null;
        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s2.trim())) {
                    exists = true;
                    mop = glo_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                if (setVarSValue(i1.intValue(), mop) != 0) {
                    return new Double(-1);
                } else {
                    return new Double(0);
                }
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s2.trim())) {
                    exists = true;
                    mop = itm_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                if (setVarSValue(i1.intValue(), mop) != 0) {
                    return new Double(-1);
                } else {
                    return new Double(0);
                }
            }
        }
    }

    @Override
    public Double taxRate(String s) {
        boolean exists = false;
        double rat = 0.0;

        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    rat = glo_tax.de(RAT).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(rat);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    rat = itm_tax.de(RAT).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(rat);
            }
        }
    }

    @Override
    public Double updTax(String s1, String s2, Double d1, Double d2, Double d3) {
        return new Double(0);
    }

    @Override
    public Double valStr(String d) {
        Double d1 = new Double(0);
        if (!"".equals(d)) {
            try {
                d1 = new Double(d);
            } catch (Exception e) {
            }
            return d1;
        } else {
            return new Double(0);
        }
    }

    @Override
    public String getVarSValue(int varID) {
        return "";
    }

    @Override
    public int setVarSValue(int varID, String value) {
        return 0;
    }

    @Override
    public Double getVarDValue(int varID) {
        return new Double(0);
    }

    @Override
    public int setVarDValue(int varID, Number value) {// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
        return 0;
    }

    @Override
    public Double addAttRef(String s) {
        // A new attached document must be added to the list of attached documents for this item
        // ++ must know that this came from rule
        // attached document code must be permanently disabled if come from rule
        // attached document reference mandatory if from rule
        // display decription if code exists - if not, display blank

        if (itm == null) {
            return new Double(-1);
        }/*
          * else if(doc.ds(GTX) == null) { return new Double(-1); }
          */
        /*
         * else if (itm.ds(ATD).ds(DOC) == null) {//commente pour l inslure dans la simulation de la taxation du tariif return new Double(-1); }
         */else if (res2 == null) {
            return new Double(-1);
        }

        // Check that attached document code is valid
        boolean codeFound = false;
        // GVA <patch ID="name of document not saved Bug #562" version="4.2.1" type="modification" date="Mar 13, 2014" author="ahmed">
        //codeFound = verifier.contains(ATD_TAB, wde, ATD_COD, s.trim());
        Iterator<String>  itr = (Iterator<String>)verifier.find(ATD_TAB, wde,ATD_DSC, ATD_COD, s.trim());
        codeFound = itr.hasNext();
        // GVA <patch ID="name of document not saved Bug #562" version="4.2.1" type="modification" date="Mar 13, 2014" author="ahmed"/>




        if (!codeFound) {
        	 if (itr != null) verifier.closeIterator(itr);
            return new Double(-1);
        }
        // GVA <patch ID="name of document not saved  Bug #562" version="4.2.1" type="modification" date="Mar 13, 2014" author="ahmed">
        String atdDsc = itr.next();
        if (itr != null) verifier.closeIterator(itr);
        // GVA <patch ID="name of document not saved  Bug #562" version="4.2.1" type="modification" date="Mar 13, 2014" author="ahmed"/>


        KNumberedSubDataSet atd_sds = (KNumberedSubDataSet) itm.ds(ATD).ds(DOC);
        /** Start Date: Jan 17, 2011 - SAD Improvement - Leonardo Flores */
        boolean alreadyExisted = false;
        DataSet oldPrv = null;
        // Retrieve previously entered attached document reference and date from tmp segment, if one exists
        KNumberedSubDataSet tmp_att_sds = (KNumberedSubDataSet) res2.ds(ATD);
        for (int j = 1; j <= tmp_att_sds.countDocuments(); j++) {
            KNumberedSubDocument tmp_att = tmp_att_sds.at(j);
            if (tmp_att.de(COD).getString("").trim().equals(s.trim()) && tmp_att.de(RUL).getString("").trim().equals("1")) oldPrv = tmp_att;
        }

        // Checking if was already added
        for (int i = 1; i <= atd_sds.countDocuments(); i++) {
            KNumberedSubDocument att = atd_sds.getDocument(i);
         // GVA <patch ID="#584 Automatic att. doc disappears if manual with same code exists" version="4.2.2" type="modification" date="May 9, 2014" author="JD">
            if (s.equals(att.de(COD).getString("").trim()) && "1".equals(att.de(RUL).getString("").trim())) {
         // GVA <patch ID="#584 Automatic att. doc disappears if manual with same code exists"/>
                alreadyExisted = true;
                att.de(COD).setEnabled(false); // No needed, but just in case
                att.de(RUL).setString("1");
                break;
            }
        }

        // Adding new attached document
        if (!alreadyExisted) {
            KNumberedSubDocument att = atd_sds.addDocument();
            att.de(COD).setString(s);
            att.de(COD).setEnabled(false); // No needed, but just in case
            // GVA <patch ID="name of document not saved  Bug #562" version="4.2.2" type="modification" date="May 29, 2014" author="JD">
            att.de(NAM).setString(atdDsc);
            // GVA <patch ID="name of document not saved  Bug #562"/>
            att.de(RUL).setString("1");
            if (oldPrv != null) {
                att.de(REF).tryToSetContent(oldPrv.de(REF).getContent());
                att.de(DAT).tryToSetContent(oldPrv.de(DAT).getContent());
            }
        }

        // for (int i = 1; i <= atd_sds.countDocuments(); i++) {
        // KNumberedSubDocument att = (KNumberedSubDocument) atd_sds.at(i);
        // if ("".equals(att.de(COD).getString("").trim())) {
        // att.de(COD).setString(s);
        // att.de(COD).setEnabled(false);
        // att.de(RUL).setString("1");
        // // Retrieve previously entered attached document reference and date from tmp segment, if one exists
        // KNumberedSubDataSet tmp_att_sds = (KNumberedSubDataSet) res2.ds(ATD);
        // for (int j = 1; j <= tmp_att_sds.countDocuments(); j++) {
        // KNumberedSubDocument tmp_att = (KNumberedSubDocument) tmp_att_sds.at(j);
        //
        // if (tmp_att.de(COD).getString("").trim().equals(s.trim()) && tmp_att.de(RUL).getString("").trim().equals("1")) {
        // att.de(REF).tryToSetContent(tmp_att.de(REF).getContent());
        // att.de(DAT).tryToSetContent(tmp_att.de(DAT).getContent());
        // }
        // }
        // inserted = true;
        // break;
        // }
        // } // SY change to display att doc on items
        /** End modification: Jan 17, 2011 - SAD Improvement - Leonardo Flores */
        Double rezult = addAttDoc(s);
        if (rezult.doubleValue() < 0) return rezult;
        // end change
        return new Double(0);
    }

    @Override
    public Double askWrkDate(Double d) {
        return new Double(0);
    }

    @Override
    public Double displayErr(String s) {
        // KOptionPane.showMessageDialog(DesktopMain.sharedInstance(), s);
        return new Double(0);
    }

    @Override
    public Double relTaxPart(String s1, String s2, Double d1, Double d2, Double d3) {
        boolean exists = false;
        String mop = null;

        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s1.trim())) {
                    exists = true;
                    mop = glo_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return updTax(s1, mop, d1, d2, d3);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s1.trim())) {
                    exists = true;
                    mop = itm_tax.de(MOP).getString("").trim();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return updTax(s1, mop, d1, d2, d3);
            }
        }
    }

    @Override
    public Double setSectionColor(Double d, String s) {
        return new Double(0);
    }

    @Override
    public Double setSectionColor2(Double d, String s) {
        return new Double(0);
    }

    @Override
    public Double taxBasisVal(String s) {
        boolean exists = false;
        double bse = 0.0;

        if (itm == null) {
            if (doc.ds(GTX) == null) {
                return new Double(-1);
            }
            KNumberedSubDataSet glo_tax_sds = (KNumberedSubDataSet) doc.ds(GTX).ds(GLT);
            for (int i = 1; i <= glo_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument glo_tax = glo_tax_sds.at(i);
                if (glo_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    bse = glo_tax.de(BSE).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(bse);
            }
        } else {
            KNumberedSubDataSet itm_tax_sds = (KNumberedSubDataSet) itm.ds(TAX).ds(LIN);
            for (int i = 1; i <= itm_tax_sds.countDocuments(); i++) {
                KNumberedSubDocument itm_tax = itm_tax_sds.at(i);
                if (itm_tax.de(COD).getString("").trim().equals(s.trim())) {
                    exists = true;
                    bse = itm_tax.de(BSE).getDouble();
                    break;
                }
            }

            if (!exists) {
                return new Double(-1);
            } else {
                return new Double(bse);
            }
        }
    }

    private static String lng(String property) {
        return so.i18n.IntlObj.createMessage("un.asytax", property);
    }

    private String removeCommas(String s) {
        StringBuffer buffer = new StringBuffer();
        char[] charArray = s.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] != ',') {
                buffer.append(charArray[i]);
            }
        }

        return buffer.toString();
    }

    private DataSet getFromTaxTar(String itmComCod, Date wde, ArefHTCompatible verifier) {
        String cod1 = itmComCod.substring(0, 1);
        String cod2 = itmComCod.substring(0, 2);
        String cod3 = itmComCod.substring(0, 3);
        String cod4 = itmComCod.substring(0, 4);
        String cod5 = itmComCod.substring(0, 5);
        String cod6 = itmComCod.substring(0, 6);
        String cod7 = itmComCod.substring(0, 7);
        String cod8 = itmComCod.substring(0, 8);

        Iterator it1cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod1);
        Iterator it2cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod2);
        Iterator it3cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod3);
        Iterator it4cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod4);
        Iterator it5cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod5);
        Iterator it6cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod6);
        Iterator it7cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod7);
        Iterator it8cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, cod8);
        Iterator it10_11cod = verifier.find(TAX_TAR, wde, LST_COD, HSC_COD, itmComCod);

        ArrayList arr1 = new ArrayList();
        ArrayList arr2 = new ArrayList();
        ArrayList arr3 = new ArrayList();
        ArrayList arr4 = new ArrayList();
        ArrayList arr5 = new ArrayList();
        ArrayList arr6 = new ArrayList();
        ArrayList arr7 = new ArrayList();
        ArrayList arr8 = new ArrayList();
        ArrayList arr10_11 = new ArrayList();

        while (it1cod.hasNext()) {
            arr1.add(it1cod.next());
        }
        while (it2cod.hasNext()) {
            arr2.add(it2cod.next());
        }
        while (it3cod.hasNext()) {
            arr3.add(it3cod.next());
        }
        while (it4cod.hasNext()) {
            arr4.add(it4cod.next());
        }
        while (it5cod.hasNext()) {
            arr5.add(it5cod.next());
        }
        while (it6cod.hasNext()) {
            arr6.add(it6cod.next());
        }
        while (it7cod.hasNext()) {
            arr7.add(it7cod.next());
        }
        while (it8cod.hasNext()) {
            arr8.add(it8cod.next());
        }

        while (it10_11cod.hasNext()) {
            arr10_11.add(it10_11cod.next());
        }

        DataSet itemComCod = new DataSet();
        itemComCod.add("it1cod").tryToSetContent(arr1);
        itemComCod.add("it2cod").tryToSetContent(arr2);
        itemComCod.add("it3cod").tryToSetContent(arr3);
        itemComCod.add("it4cod").tryToSetContent(arr4);
        itemComCod.add("it5cod").tryToSetContent(arr5);
        itemComCod.add("it6cod").tryToSetContent(arr6);
        itemComCod.add("it7cod").tryToSetContent(arr7);
        itemComCod.add("it8cod").tryToSetContent(arr8);
        itemComCod.add("it10_11cod").tryToSetContent(arr10_11);

        /** Start Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */
        if (it1cod != null) verifier.closeIterator(it1cod);
        if (it2cod != null) verifier.closeIterator(it2cod);
        if (it3cod != null) verifier.closeIterator(it3cod);
        if (it4cod != null) verifier.closeIterator(it4cod);
        if (it5cod != null) verifier.closeIterator(it5cod);
        if (it6cod != null) verifier.closeIterator(it6cod);
        if (it7cod != null) verifier.closeIterator(it7cod);
        if (it8cod != null) verifier.closeIterator(it8cod);
        if (it10_11cod != null) verifier.closeIterator(it10_11cod);
        /** End Date: Mar 10, 2011 - FIX: Close iterator - Leonardo Flores */

        return itemComCod;

    }

    @Override
    public Double addToTargetedProducts() {
        return new Double(0);
    }

    @Override
    public Double addToAcceptedValue() {
        return new Double(0);
    }

    @Override
    public Double addToRefusedValue() {
        return new Double(0);
    }

    @Override
    public Double addSection(String s) {
        // TODO Auto-generated method stub
        return new Double(0);
    }

    @Override
    public Double addSectionColor(Double d, String s) {
        // TODO Auto-generated method stub
        return new Double(0);
    }

    @Override
    public Double assignSectionExaminer(String s, String e) {
        // TODO Auto-generated method stub
        return new Double(0);
    }

    @Override
    public Double newImporter(Double d) {
        return new Double(0);
    }

    @Override
    public Double newDeclarant(Double d) {
        return new Double(0);
    }

    @Override
    public Double newExporter(Double d) {
        return new Double(0);
    }
    
    // GVA <patch ID="Improvement #511 - New functions" version="4.2.2" type="NEW" date="Dec 29, 2013" author="Leonardo Flores">
    @Override
    public Double displayMsgStop(String s, Object... arguments){
        return displayErrStop(s);
    }
    @Override
    public String formatMsg(String msg, Object... arguments) {
        return MessageFormat.format(msg,arguments);
    }
	// </patch ID="Improvement #511 - New functions">
	// GVA <patch ID="#599 Taxation function: inScanDoc" version="4.2.2" type="NEW" date="Jun 16, 2014" author="Leonardo Flores">
    public Double inScanDoc(String cod){
        return inScanDoc(cod,0);
    }
    public Double inScanDoc(String cod, int number){
       if (doc == null || number < 0) return new Double(-1);
       
       // 1. Get items subdataset
       DataSet sdsItems = doc.ds(ITM).getDataSet(SubDataSet.ALL);
       int totalItems = sdsItems.sizePos();
       if (totalItems < number) return new Double(-1);
       
       // 2. If number equal to zero, we look for an attached code in the whole SAD (all items)
       ScanVerifier verifier = ScanVerifierService.getInstance().getVerifier();
       Boolean exists = verifier != null ? verifier.exists(doc,cod,number) : null;
       if (exists == null || exists.equals(Boolean.TRUE)) return new Double(0);
       
       // 4. Not found
       return new Double(-1);
    }
	// GVA <patch ID="#599 Taxation function: inScanDoc" version="4.2.2" type="NEW" date="Jun 16, 2014" author="Leonardo Flores">

    // GVA <patch ID="#601 Taxation: displayErrStop does not work on server side" version="4.2.2" type="BUG" date="Jun 17, 2014" author="Leonardo Flores">
    public ArrayList<String> getTaxationErrors(){
        return this.errorMessage;
    }
    public void addServerSideMessage(String message){
        if (this.errorMessage == null) this.errorMessage = new ArrayList<String>();
        this.errorMessage.add(message);
    }
	// </patch ID="Taxation: displayErrStop does not work on server side">
    
    // GVA <patch ID="#838 Container counter function" version="4.3.0" type="NEW" date="Jan 26, 2015" author="Leonardo Flores">
    public Double ctnCounter(Integer item, Object... arguments){
    	return new Double(0);
    }
	// </patch ID="#838 Container counter function">
    
    
 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
    //public int setVarDValue(Number varID, Number value){
    //	return setVarDValue(varID,value.doubleValue());
    //}

	 
	public Double roundInf(Number d){
		return  roundInf(d.doubleValue());
	}
	

	public Double roundSup(Number dl){
		return  roundSup(dl.doubleValue());
	}

	public Double round(Number d){
		return round(d.doubleValue());
	}

	public Double min(Number d1, Number d2){
		return min(d1.doubleValue(),d2.doubleValue());
	}

	public Double max(Number d1, Number d2){
		return max(d1.doubleValue(),d2.doubleValue());
	}

	public Double sqr(Number d){
		return sqr(d.doubleValue());
	}

	public Double abs(Number d){
		return abs(d.doubleValue());
	}

	
	public Double subStrFnd(String s1, String s2, Number d1, Number d2, Number d3){
		return subStrFnd( s1,  s2, d1.doubleValue(), d2.doubleValue(),d3.doubleValue());
	}

	

	public Double setWrkDate(Number d){
		return setWrkDate(d.doubleValue());
	}

	public Double doTax(String s1, String s2, Number d1, Number d2, Number d3){
		return doTax( s1,  s2,d1.doubleValue(), d2.doubleValue(),d3.doubleValue());
	}

	public Double updTax(String s1, String s2, Number d1, Number d2, Number d3){
		return updTax( s1,  s2, d1.doubleValue(),d2.doubleValue(), d3.doubleValue());
	}

	public Double relTax(String s1, String s2, Number d1, Number d2, Number d3){
		return relTax( s1,  s2, d1.doubleValue(), d2.doubleValue(), d3.doubleValue());
	}

	public Double relTaxPart(String s1, String s2, Number d1, Number d2, Number d3){
		return relTaxPart( s1,  s2, d1.doubleValue(), d2.doubleValue(),d3.doubleValue());
	}


	public Double rateCol(String s, Number d){
		return rateCol( s, d.doubleValue());
	}

	public Double askTax(String s1, String s2, Number d1, Number d2, Number d3){
		return askTax( s1,  s2, d1.doubleValue(),  d2.doubleValue(), d3.doubleValue());
	}

	public Double delAllTax(Number d){
		return delAllTax( d.doubleValue());
	}

	public Double askWrkDate(Number d){
		return askWrkDate( d.doubleValue());
	}

	public Double setSectionColor(Number d, String s){
		return setSectionColor( d.doubleValue(),  s);
	}

	public Double setSectionColor2(Number d, String s){
		return setSectionColor2( d.doubleValue(),  s);
	}
	
	 
	public Double addSectionColor(Number d, String s){
		return addSectionColor( d.doubleValue(),  s);
	}
	
	public Double newImporter(Number d){
		return newImporter( d.doubleValue());
	}
	public Double newDeclarant(Number d){
		return newDeclarant( d.doubleValue());
	}
	public Double newExporter(Number d){
		return newExporter( d.doubleValue());
	}

	// GVA <patch ID="#885 SPM - AddTax" version="4.3.0" type="NEW" date="Mar 18, 2015" author="JD">
	public Double addTax(String s1, String s2, Number d1, Number d2, Number d3) {
		return addTax(s1, s2, d1.doubleValue(), d2.doubleValue(), d3.doubleValue());
	}
	// GVA <patch ID="#885 SPM - AddTax"/>
	
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
    
    
}
