package un.asytax.taxation.server;

import java.util.Enumeration;

import so.kernel.core.AbstractOperation;
import so.kernel.core.Operation;
import so.kernel.core.OperationClass;
import so.kernel.core.Operations;
import so.kernel.core.RuleInterface;
import so.kernel.core.events.ServerEvent;
import so.kernel.core.exceptions.ServerRuleException;
import so.kernel.server.AccessOperationsContainer;
import so.kernel.server.ConnectionManager;
import so.kernel.server.DPP;
import so.kernel.server.GCFServerBinder;
import so.kernel.server.GCFServerEvent;
import so.kernel.server.OperationFactory;
import so.kernel.server.UserTransactionEnvironment;
import so.kernel.server.rules.ReferenceServerRule;
import so.util.DebugOutput;
import un.adtcommons.server.rules.HistorizedServerRuleFactory;
import un.asytax.serverUtils.SR_SendServerDate;
import un.asytax.serverUtils.SR_SendTimeZone;
import un.asytax.taxation.C_Tax;

/**
 * Server side class that manages the binding document/database
 * 
 * @author GEDT
 */
public class S_Tax extends GCFServerBinder implements C_Tax {

	/**
	 * Name of the resource table identifier for the Customs database tables
	 */
	// private static final String TAR_GEN_TAB = "TAR_GEN_TAB";
	/**
	 * Name of the database identifier for the database manager class. The actual name will be loaded from the resource file.
	 */
	public static final String MANAGER_PROPERTY = S_Tax.class.getName() + "#AsyGate_DriverManager";

	/**
	 * Name of the identifier for the database link URL. The actual name will be loaded from the resource file.
	 */
	// public static final String DB_URL_PROPERTY = S_Tax.class.getName() + "#TAR_DataBaseURL";
	public static final String DB_URL_PROPERTY = S_Tax.class.getName() + "#AsyGate_DataBaseURL";

	/**
	 * Name of the database identifier for the user name. The actual name will be loaded from the resource file.
	 */
	public static final String DB_USER_PROPERTY = S_Tax.class.getName() + "#AsyGate_DataBaseUser";

	/**
	 * Name of the database identifier for the user password. The actual name will be loaded from the resource file.
	 */
	public static final String DB_PASSWD_PROPERTY = S_Tax.class.getName() + "#AsyGate_DataBasePassword";

	/*
	 * public static final String url = Server.getString("module.un.asytar.gcf.url"); public static final String usr = Server.getString("module.un.asytar.gcf.user"); public static
	 * final String passwd = Server.getString("module.un.asytar.gcf.password");
	 */

	/**
	 * Define the document server binder business logic
	 * 
	 */
	public void defineBinder() {

		addServerRule(new ReferenceServerRule(this, new int[] { TAXATION_RULES_LOAD }, TAXATION_RULES));
		// addServerRule(new ReferenceServerRule(this, new int[] {QUO_LOAD}, QUO_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { QUO_LOAD }, QUO_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { CTY_LOAD }, CTY_TAB));
		// addServerRule(new ReferenceServerRule(this, new int[] {PRF_LOAD}, PRF_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { PRF_LOAD }, PRF_TAB));
		// addServerRule(new ReferenceServerRule(this, new int[] {TAX_LOAD}, TAX_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { TAX_LOAD }, TAX_TAB));
		// addServerRule(new ReferenceServerRule(this, new int[] {CUR_LOAD}, CUR_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { CUR_LOAD }, CUR_TAB));

		addServerRule(new SR_SendServerDate(this), REC_DAT);
		addServerRule(new SR_SendTimeZone(this), REC_TMZ);

		// Customs procedure (extended)
		// Customs procedure (link national procedure)

		// Currency exchange rates
		// addServerRule(new ReferenceServerRule(this, new int[] {RAT_LOAD}, RAT_TAB));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { RAT_LOAD }, RAT_TAB));
		// addServerRule(new ReferenceServerRule(this, new int[] {PRF_CTY_LOAD}, PRF_CTY));
		addServerRule(HistorizedServerRuleFactory.getServerRule(this, new int[] { PRF_CTY_LOAD }, PRF_CTY));

		/*
		 * addServerRule(new SR_Request_tables(this, getConnectionManager(0),getConnectionManager(1)), ASK_SEARCH);
		 * 
		 * addServerRule(new SR_Request(this, getConnectionManager(0)), LOAD_LISTDOC); // Get a company data addServerRule(new SR_LoadDecision(this), LOAD_DECISION);
		 * 
		 */

		/*
		 * addServerRule(new GetTaxationRules(this),ASK_SEARCH_ITEM); addServerRule(new GetTaxationRules(this),ASK_SEARCH_GLOB);
		 */

		// Define the Document processing path
		setDPP(createDPP());

	}

	// Define the Document processing path

	protected DPP createDPP() {

		DPP dpp = new DPP();

		dpp.add(null, OP_DESIGN, null);
		dpp.add(null, OP_OPEN, null);
		dpp.add(null, OP_REGISTER, ST_REGISTERED);
		dpp.add(ST_REGISTERED, OP_VIEW, ST_REGISTERED);

		return dpp;
	}

	protected void addReferenceModelEvents(AbstractOperation abstractOperation) {
		abstractOperation.addEventID(TAXATION_RULES_LOAD, TAXATION_RULES);
		// abstractOperation.addEventID(QUO_LOAD, QUO_TAB);
		abstractOperation.addEventID(QUO_LOAD, QUO_TAB_EVENT);
		abstractOperation.addEventID(CTY_LOAD, CTY_TAB_EVENT);
		// abstractOperation.addEventID(PRF_LOAD, PRF_TAB);
		abstractOperation.addEventID(PRF_LOAD, PRF_TAB_EVENT);
		// abstractOperation.addEventID(TAX_LOAD, TAX_TAB);
		abstractOperation.addEventID(TAX_LOAD, TAX_TAB_EVENT);
		// abstractOperation.addEventID(CUR_LOAD, CUR_TAB);
		abstractOperation.addEventID(CUR_LOAD, CUR_TAB_EVENT);
		// abstractOperation.addEventID(RAT_LOAD, RAT_TAB);
		abstractOperation.addEventID(RAT_LOAD, RAT_TAB_EVENT);
		// abstractOperation.addEventID(PRF_CTY_LOAD, PRF_CTY);
		abstractOperation.addEventID(PRF_CTY_LOAD, PRF_CTY_EVENT);
		abstractOperation.addEventID(REC_DAT);
		abstractOperation.addEventID(REC_TMZ);
	}

	public static OperationClass makeElfDesignerOperation() {
		OperationClass oc_Design = new OperationClass("Design", "img/Btn_Design_Normal.gif");
		oc_Design.setKnownIED(false);
		oc_Design.setInLibrary(true);
		Operation op = OperationFactory.makeNilOperation(32, "Design", lng("Cancel"), "img/Btn_Cancel_Normal.gif");
		op.setKnownIED(false);
		op.setInLibrary(true);
		oc_Design.add(op);
		oc_Design.addVisibleEventID(1224, "Export to SOClass - Studio", "img/Icon_Studio_Export.GIF");
		oc_Design.addVisibleEventID(1225, "Export to SOClass - ULA, HTML", "img/Icon_ULA_Export.gif");
		return oc_Design;
	}

	protected Operations createValidOperations() {

		Operations ops = super.createValidOperations();

		/*
		 * // "Design" operation class OperationClass oc_Design = makeElfDesignerOperation(); addReferenceModelEvents(oc_Design); oc_Design.addEventID(ASK_SEARCH);
		 * oc_Design.addEventID(LOAD_LISTDOC); oc_Design.addEventID(LOAD_DECISION); ops.add(oc_Design);
		 * 
		 * 
		 * Operation op_Print = OperationFactory.makeStatusUpdateOperation( OI_PRINT, OP_PRINT, , "img/Btn_Print_Normal.gif"); op_Print.setAutoFinish(true);
		 * 
		 * addReferenceModelEvents(op_Print); op_Print.addEventID(DO_PRINT); op_Print.addEventID(DO_PDF); op_Print.addEventID(DO_PDF_WYSIWYG); ops.add(op_Print);
		 * 
		 */
		OperationClass oc_Open = new OperationClass(OC_OPEN, "img/Btn_Open_Normal.gif");
		oc_Open.setKnownIED(false);
		oc_Open.setInLibrary(true);

		oc_Open.add(OperationFactory.makeNilOperation(OE_OPEN, OP_OPEN, lng(OA_CLOSE), "img/Btn_Cancel_Normal.gif"));
		addReferenceModelEvents(oc_Open);

		ops.add(oc_Open);

		return ops;
	}

	/*
	 * The user has access to view all documents, but the access to other operations (for example retrieve) is restricted to documents created by the same user.
	 * 
	 * If there is no user's ownership, the user has access to all document.
	 */
	protected boolean access(AccessOperationsContainer opContainer) {

		// get user's ownership
		String usrOwner = opContainer.getUserOwner();
		if (usrOwner == null) {
			// there is no user's ownership.

			// user has access to all document.
			// When the method returns false, the kernel bypasses the
			// AccessOperationContainer and the user has access to the
			// document.
			return false;
		}

		if (opContainer.infoIsNull()) {
			// "New" operation class.
			return false;
		}

		// get document's ownership
		String owner = opContainer.getOwner();
		if (usrOwner.equals(owner)) {
			return false;
		}

		// access deny of the operation class OC_RETRIEVE and
		// operations OE_AMEND_POSTED, OE_REGISTER_POSTED,
		// OE_DELETE_POSTED, OE_FULL_AMEND_REGISTERED,
		// OE_RESTRICTED_AMEND_REGISTERED

		Enumeration enume = opContainer.accessedOperations();
		while (enume.hasMoreElements()) {
			AbstractOperation ao = (AbstractOperation) enume.nextElement();
			if (ao instanceof OperationClass) {
				if (OC_RETRIEVE.equals(ao.getName())) {
					opContainer.setAccess(ao, false);
				}
			} else if (ao instanceof Operation) {
				Operation op = (Operation) ao;
				if (op.getID() == OI_AMEND_POSTED || op.getID() == OI_REGISTER_POSTED || op.getID() == OI_DELETE_POSTED
						|| op.getID() == OI_FULL_AMEND_REGISTERED || op.getID() == OI_RESTRICTED_AMEND_REGISTERED
						|| op.getID() == OI_DELETE_REGISTERED) {
					opContainer.setAccess(ao, false);
				}
			}
		}
		return true;
	}

	public void initializeDatabase() {
		ConnectionManager manager = createConnectionManager(DB_URL_PROPERTY, DB_USER_PROPERTY, DB_PASSWD_PROPERTY);

		/*
		 * ConnectionManager manager2 = createConnectionManager( "un.asytar.gcf_URL", DB_USER_PROPERTY, DB_PASSWD_PROPERTY);
		 */

		if (manager == null) {
			System.err.println("Asygate database not found");
		}
		/*
		 * if (manager2 == null) { System.err.println("AsytarGCF database not found"); }
		 */

	}
    //AUDIT
    protected ServerEvent applyEvent(ServerEvent event)
    throws ServerRuleException
    {
    	if (audit ){
    		String operationStartedName = null;
    		String operationEndName = null;
    		if (event instanceof GCFServerEvent){
    			UserTransactionEnvironment env = ((GCFServerEvent)event).getUserTransactionEnvironment();
    			if (env !=null){
    				operationStartedName = env.getStartedOperation()!=null?env.getStartedOperation().getName():"";
    				operationEndName = env.getEndOperation()!=null?env.getEndOperation().getName():"";
    				
    			}
    		}
    		Enumeration rules;
    		DebugOutput.print("Operation started : " + operationStartedName + " / " + operationEndName + "  Started Event id : " + event.getID() + "  " + event.getEventName(event));
    		for(rules = serverRulesContainer.getApplyRules(event); rules.hasMoreElements(); ){
    			RuleInterface rule = (RuleInterface)rules.nextElement();
    			String className = 	rule.getClass().getName();
    			if (!className.startsWith("so")|| className.equals("so.kernel.server.DataVerifierServerRule")){
    				DebugOutput.print("Operation started : " + operationStartedName + " / " + operationEndName + "        Event id : " + event.getID() + "      Rule name : " +className );
    			}
    		}
    		 
    	}	
    	ServerEvent serverEvent =  super.applyEvent(event);
    	if (audit ){
    		DebugOutput.print("End Event id : " + event.getID() + "   " + event.getEventName(event));
    		DebugOutput.print("*************************************************************************" );
    		DebugOutput.print("                                                                         " );
    		DebugOutput.print("                                                                         " );
    	}
    	return serverEvent;
    }
    
    private static int logLevel = DebugOutput.parseLevel(System.getProperty("un.server.logLevel") );
	private boolean audit = logLevel>=20?true:false;
    
	//END AUDIT

	private static so.i18n.IntlObj lng(String key) {
		return new so.i18n.IntlObj("un.asytax", key);
	}

	/*
	 * static String getTAR_GEN_TAB() { return Server.getString(S_Tax.class, TAR_GEN_TAB); }
	 */

}
