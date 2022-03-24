//   $Header: /home/asycuda/home/cvsroot/asytax/Shared/src/un/asytax/taxation/DS_Tax.java,v 1.4 2012-05-22 10:16:24 john Exp $

package un.asytax.taxation;

import so.kernel.core.KNumberedSubDocument;

public class DS_Tax extends KNumberedSubDocument implements C_Tax {
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor
	 * 
	 */
	public DS_Tax() {
		super();
	}

	/**
	 * Definition of the Data Model
	 * 
	 */
	public void define_DataModel() {

		add(COD); // Duty - Tax code
		add(BSE); // Duty - Tax base amount
		add(RAT); // Duty - Tax rate
		add(AMT); // Duty - Tax amount
		add(MOP); // MP - Method of payment
		add(TYP); // Type of tax (manual or automatic)

		// End define

	}

	/**
	 * Definition of the Client Business Rules
	 * 
	 */
	public void define_ClientBusinessRule() {

		/*
		 * Rule r_init_tax = new R_Init_Tax(); addRule(r_init_tax, SUB_DOCUMENT_INIT); // Compute tax amount /* // According to tax line MOP, compute CTR for item
		 * de(MOP).addRule(R_TAX_ItemLine_Mop.sharedInstance(), DATA_CHANGING); de(MOP).addRule(R_TAX_ItemLine_Mop.sharedInstance(), DATA_CHANGED);
		 *  // remark these 2 can be grouped // According to tax line MOP, compute AMT or GTY total for item de(MOP).addRule(R_Compute_ItemTaxLineMop.sharedInstance(),
		 * DATA_CHANGING); de(MOP).addRule(R_Compute_ItemTaxLineMop.sharedInstance(), DATA_CHANGED); // According to tax line MOP, compute AMT or GTY total for item
		 * de(AMT).addRule(R_Compute_ItemTotalTax.sharedInstance(), DATA_CHANGING); de(AMT).addRule(R_Compute_ItemTotalTax.sharedInstance(), DATA_CHANGED); // Compute a tax line
		 * amount de(BSE).addRule(R_Compute_TaxAmt.sharedInstance(), DATA_VERIFY); de(RAT).addRule(R_Compute_TaxAmt.sharedInstance(), DATA_VERIFY);
		 */
	}

	/*
	 * Method to indicate if the document is empty. Here a tax line is empty if there is no tax code specified.
	 * 
	 */
	public boolean isEmpty() {
		return (de(COD).getString("").trim().equals(""));
	}

}
