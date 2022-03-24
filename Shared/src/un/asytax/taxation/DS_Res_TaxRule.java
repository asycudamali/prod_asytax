package un.asytax.taxation;

import so.kernel.core.KNumberedSubDocument;

public class DS_Res_TaxRule extends KNumberedSubDocument implements C_Tax {

    private static final long serialVersionUID = 1L;

    /**
	 * Definition of the Data Model
	 */
	public void define_DataModel() {
		add(TYP);
		add(NAM);
		add(ORD);
		add(DSC);
		add(PRI);
		add(JAV); // GVA <patch ID="Re-engineer taxation #62" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">

		// GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed">
		add(RES);// HS Restriction
		// GVA <patch ID="agreements with taxation rule #299" version="4.2.1" type="modification" date="Sep 12, 2012" author="ahmed"/>
 
		
	}

	/**
	 * Definition of the Client Business Rules
	 */
	public void define_ClientBusinessRule() {
	}

	public boolean isEmpty() {
		return false;
	}
}
