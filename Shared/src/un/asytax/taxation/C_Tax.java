package un.asytax.taxation;

import static so.kernel.core.events.EventConstants.INTERNAL_EVENTS_MAX;
import so.kernel.core.Operation;
import un.kernel.core.PropertyLoader;

public interface C_Tax {

    
	// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
	public static final String LOCATION_SERVER = "Server";
    public static final String LOCATION_CLIENT = "Client";

    public static final boolean JAVAPARSER=PropertyLoader.getInstance().getPropertyValue("Client.javaparser")==null?false:new Boolean(PropertyLoader.getInstance().getPropertyValue("Client.javaparser"));
   

 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
    
    
	// GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed">
	public static boolean REMOTE_TARTAB = System.getProperty("un.remotetartabinvocation")==null?false:new Boolean(System.getProperty("un.remotetartabinvocation"));
	public static boolean REMOTE_TAXTAR = System.getProperty("un.remotetaxtarinvocation")==null?false:new Boolean(System.getProperty("un.remotetaxtarinvocation"));
    
	// GVA <patch ID="TUNING" version="4.2.1" type="modification" date="DEC 12, 2012" author="ahmed"/>
	
	
	// Number of global taxes visible on assessment notice form
	public static final int NBR_GLO_TAXES_VISIBLE = 3;

	// Status
	public static final String ST_POSTED = "Posted";

	public static final String ST_REGISTERED = "Registered";

	public static final String ST_DELETED = "Deleted";

	public static final String ST_ARCHIVE = null;

	// Operation class name
	public static final String OC_NEW = "New";

	public static final String OC_RETRIEVE = "Retrieve";

	public static final String OC_REGISTER = "Register";

	public static final String OC_DELETE = "Delete";

	public static final String OC_ARCHIVE = "Archive";

	public static final String OC_OPEN = "Open";

	// Operation name
	public static final String OP_VIEW = Operation.VIEW_OPERATION_NAME;

	public static final String OP_DESIGN = Operation.DESIGN_OPERATION_NAME;

	public static final String OP_POST = "Post";

	public static final String OP_REGISTER = "Register";

	public static final String OP_AMEND_POSTED = "Amend Posted";

	public static final String OP_REGISTER_POSTED = "Register Posted";

	public static final String OP_DELETE_POSTED = "Delete Posted";

	public static final String OP_FULL_AMEND_REGISTERED = "Full Amend Registered";

	public static final String OP_RESTRICTED_AMEND_REGISTERED = "Restricted Amend Registered";

	public static final String OP_DELETE_REGISTERED = "Delete Registered";

	public static final String OP_DIRECT_REGISTER_POSTED = "Direct Register Posted";

	public static final String OP_DIRECT_DELETE_POSTED = "Direct Delete Posted";

	public static final String OP_DIRECT_DELETE_REGISTERED = "Direct Delete Registered";

	public static final String OP_ARCHIVE = "Archive";

	public static final String OP_LOCK = Operation.LOCK_OPERATION_NAME;

	public static final String OP_UNLOCK = Operation.UNLOCK_OPERATION_NAME;

	public static final String OP_PRINT = "Print";

	public static final String OP_FIND_POSTED = "Find Posted";

	public static final String OP_FIND_REGISTERED = "Find Registered";

	public static final String OP_FIND_SWISS = "Find Swiss enterprise";

	public static final String OP_FIND_BY_NUMBER = "Find by number";

	public static final String OP_OPEN = "Open";

	public static final String OA_CLOSE = "Close";

	// Operation Identificator
	public static final int OI_POST = Operation.INTERNAL_OPERATIONS_MAX + 1;

	public static final int OI_REGISTER = Operation.INTERNAL_OPERATIONS_MAX + 2;

	public static final int OI_AMEND_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 3;

	public static final int OI_REGISTER_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 4;

	public static final int OI_DELETE_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 5;

	public static final int OI_FULL_AMEND_REGISTERED = Operation.INTERNAL_OPERATIONS_MAX + 6;

	public static final int OI_RESTRICTED_AMEND_REGISTERED = Operation.INTERNAL_OPERATIONS_MAX + 7;

	public static final int OI_DELETE_REGISTERED = Operation.INTERNAL_OPERATIONS_MAX + 8;

	public static final int OI_DIRECT_REGISTER_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 9;

	public static final int OI_DIRECT_DELETE_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 10;

	public static final int OI_DIRECT_DELETE_REGISTERED = Operation.INTERNAL_OPERATIONS_MAX + 11;

	public static final int OI_ARCHIVE = Operation.INTERNAL_OPERATIONS_MAX + 12;

	public static final int OI_PRINT = Operation.INTERNAL_OPERATIONS_MAX + 13;

	public static final int OI_FIND_POSTED = Operation.INTERNAL_OPERATIONS_MAX + 14;

	public static final int OI_FIND_REGISTERED = Operation.INTERNAL_OPERATIONS_MAX + 15;

	public static final int OI_FIND_SWISS = Operation.INTERNAL_OPERATIONS_MAX + 16;

	public static final int OI_FIND_BY_NUMBER = Operation.INTERNAL_OPERATIONS_MAX + 17;

	public static final int OE_OPEN = Operation.INTERNAL_OPERATIONS_MAX + 18;

	// Visual middle Events

	public static final int MEN_ITM_NEW = INTERNAL_EVENTS_MAX + 1;

	public static final int ACT_ITM_NEW = INTERNAL_EVENTS_MAX + 2;

	public static final int MEN_ITM_DEL = INTERNAL_EVENTS_MAX + 3;

	public static final int ACT_ITM_DEL = INTERNAL_EVENTS_MAX + 4;

	public static final int DO_PRINT = INTERNAL_EVENTS_MAX + 8;

	public static final int DO_PRINT_CUSTOMISED = INTERNAL_EVENTS_MAX + 16;

	public static final int DO_PDF = INTERNAL_EVENTS_MAX + 3000;

	public static final int DO_PDF_WYSIWYG = INTERNAL_EVENTS_MAX + 3001;

	public static final int GENERATE_PRD_SUM_DCL = INTERNAL_EVENTS_MAX + 30;

	// Middle events
	public static final int CHK_CMP = INTERNAL_EVENTS_MAX + 5;

	public static final int CHK_TAR = INTERNAL_EVENTS_MAX + 6;

	public static final int CHK_TAR_HDC = INTERNAL_EVENTS_MAX + 7;

	public static final int CHK_DEC = INTERNAL_EVENTS_MAX + 9;

	public static final int VIEW_BOL = INTERNAL_EVENTS_MAX + 10;

	public static final int VIEW_RCPT = INTERNAL_EVENTS_MAX + 11;

	public static final int VIEW_EXIT_NOTE = INTERNAL_EVENTS_MAX + 12;

	public static final int DO_REGISTER_TO_ASSESS = INTERNAL_EVENTS_MAX + 13;

	public static final int VIEW_INSP_ACT = INTERNAL_EVENTS_MAX + 14;

	public static final int VIEW_CRITERIA = INTERNAL_EVENTS_MAX + 15;

	public static final int CHECK = INTERNAL_EVENTS_MAX + 17;

	public static final int CHK_AUTHORIZATION = INTERNAL_EVENTS_MAX + 1001;

	public static final int CHK_PRV_SUM_DCL = INTERNAL_EVENTS_MAX + 1002;

	public static final int DO_REGISTER_LINE_TRANSACTION = INTERNAL_EVENTS_MAX + 1003;

	public static final int CHK_ACC = INTERNAL_EVENTS_MAX + 1004;

	public static final int CHK_CRE_ACC = INTERNAL_EVENTS_MAX + 1005;

	public static final int CHK_PRE_ACC = INTERNAL_EVENTS_MAX + 1006;

	public static final int CHK_BAL = INTERNAL_EVENTS_MAX + 1007;

	public static final int DO_REGISTER_TO_PAID = INTERNAL_EVENTS_MAX + 1008;

	public static final int DO_ASSESS_TO_PAID = INTERNAL_EVENTS_MAX + 1009;

	public static final int DO_REFUND_PREPAID = INTERNAL_EVENTS_MAX + 1010;

	public static final int ASK_SEARCH_GLOB = INTERNAL_EVENTS_MAX + 1011;

	public static final int ASK_SEARCH_ITEM = INTERNAL_EVENTS_MAX + 1978;

	public static final int ASK_SEARCH_TARIF = INTERNAL_EVENTS_MAX + 1013;

	public static final int GET_NEXT_SERIAL_NUMBER = INTERNAL_EVENTS_MAX + 2014;

	public static final int GET_RCPT_TYP = INTERNAL_EVENTS_MAX + 2015;

	public static final int DO_TEST = INTERNAL_EVENTS_MAX + 2016;

	public static final int INIT_ITEM = INTERNAL_EVENTS_MAX + 2017;

	public static final int HISTORY = INTERNAL_EVENTS_MAX + 2018;

	public static final int PAYMENT = INTERNAL_EVENTS_MAX + 2019;

	public static final int CANCEL = INTERNAL_EVENTS_MAX + 2020;

	public static final int GET_ITEM_EXIT_NOTE_KEYS = INTERNAL_EVENTS_MAX + 2022;

	public static final int GET_EXIT_NOTE_KEYS = INTERNAL_EVENTS_MAX + 2023;

	public static final int GET_INSP_ACT_KEYS = INTERNAL_EVENTS_MAX + 2024;

	public static final int GET_LOGIN_NAME = INTERNAL_EVENTS_MAX + 2025;

	public static final int GET_SAD_TO_BE_PAID = INTERNAL_EVENTS_MAX + 2026;

	public static final int GET_LINE_TRANSACTIONS = INTERNAL_EVENTS_MAX + 2027;

	public static final int REMOTE_CHECK = INTERNAL_EVENTS_MAX + 2029;

	public static final int GET_USR_AUTHORIZED_CUOS = INTERNAL_EVENTS_MAX + 2030;

	public static final int FULL_HISTORY = INTERNAL_EVENTS_MAX + 2031;

	public static final int GET_PREPAID_RCPT_KEYS = INTERNAL_EVENTS_MAX + 2032;

	public static final int GET_REFUND_PREPAID_RCPT_KEYS = INTERNAL_EVENTS_MAX + 2033;

	public static final int VAL_SET_WORK = INTERNAL_EVENTS_MAX + 4015;

	public static final int VAL_VIT = INTERNAL_EVENTS_MAX + 4016;

	public static final int VAL_TOTAL = INTERNAL_EVENTS_MAX + 4017;

	public static final int VAL_SET_ALPHA = INTERNAL_EVENTS_MAX + 4018;

	public static final int REC_DAT = INTERNAL_EVENTS_MAX + 8123; // Recovering working Date from server part

	public static final int REC_TMZ = INTERNAL_EVENTS_MAX + 8124; // Recovering Time from server part

	public static final int COMPUTE_LINE = INTERNAL_EVENTS_MAX + 20;

	public static final int COMPUTE_ITEM_TAXATION = INTERNAL_EVENTS_MAX + 21;

	public static final int COMPUTE_ITEM_TAX_BASE = INTERNAL_EVENTS_MAX + 22;

	public static final int COMPUTE_ITEM_VAT_BASE = INTERNAL_EVENTS_MAX + 23;

	public static final int COMPUTE_ITEM_REGIME = INTERNAL_EVENTS_MAX + 24;
	
	public static final int HT_TAR_TAB = INTERNAL_EVENTS_MAX + 2850;
    public static final int HT_TAR_TAB2 = INTERNAL_EVENTS_MAX + 2852;
    public static final int HT_TAR_TAB1 = INTERNAL_EVENTS_MAX + 2851;
    public static final int TAXATION = INTERNAL_EVENTS_MAX + 1149;

	// Import/Export visualEvents
	public static final int IMP_XML = INTERNAL_EVENTS_MAX + 300;

	public static final String IMP_XML_NAM = "Import from ASYCUDA XML file";

	public static final int IMP_XML_FUL = INTERNAL_EVENTS_MAX + 301;

	public static final String IMP_XML_FUL_NAM = "Kernel import from XML file";

	public static final int EXP_XML = INTERNAL_EVENTS_MAX + 310;

	public static final String EXP_XML_NAM = "Export to ASYCUDA XML file";

	public static final int EXP_XML_CLB = INTERNAL_EVENTS_MAX + 311;

	public static final String EXP_XML_CLB_NAM = "Export to clipboard in ASYCUDA XML format";

	public static final int EXP_XML_FUL = INTERNAL_EVENTS_MAX + 312;

	public static final String EXP_XML_FUL_NAM = "Kernel export to XML file";

	public static final String ASYSAD_CHAR_TYPE = "un.broker.asysad.F_asysad_Char";

	// Data name

	// Country

	public static final String CTY_TAB = "UN_CTY_TAB";

	public static final String CTY_COD = "CTY_COD";

	public static final String CTY_DSC = "CTY_DSC";

	public static final String CTY_TAB_EVENT = "CTY_TAB_EVENT";

	public static final int CTY_LOAD = INTERNAL_EVENTS_MAX + 110;

	// Declarant

	public static final String DEC_TAB = "UN_DEC_TAB";

	public static final String DEC_COD = "DEC_COD";

	public static final String DEC_NAM = "DEC_NAM";

	public static final String DEC_ADR = "DEC_ADR";

	public static final String DEC_AD2 = "DEC_AD2";

	public static final String DEC_AD3 = "DEC_AD3";

	public static final String DEC_AD4 = "DEC_AD4";

	public static final String DEC_BRK = "DEC_BRK";

	public static final int DEC_LOAD = INTERNAL_EVENTS_MAX + 111;

	// Attached documents
	public static final String ATD_TAB = "UN_ATD_TAB";

	public static final String ATD_COD = "ATD_COD";

	public static final String ATD_DSC = "ATD_DSC";

	public static final int ATD_LOAD = INTERNAL_EVENTS_MAX + 112;

	// LOCODE

	public static final String LOC_TAB = "UN_LOC_TAB";

	public static final String LOC_COD = "LOC_COD";

	public static final String LOC_DSC = "LOC_DSC";

	public static final int LOC_LOAD = INTERNAL_EVENTS_MAX + 113;

	// Mode of Transport

	public static final String MOT_TAB = "UN_MOT_TAB";

	public static final String MOT_COD = "MOT_COD";

	public static final String MOT_NAM = "MOT_DSC";

	public static final int MOT_LOAD = INTERNAL_EVENTS_MAX + 114;

	// Package

	public static final String PKG_TAB = "UN_PKG_TAB";

	public static final String PKG_COD = "PKG_COD";

	public static final String PKG_NAM = "PKG_DSC";

	public static final int PKG_LOAD = INTERNAL_EVENTS_MAX + 115;

	// Terms of Payment

	public static final String TOP_TAB = "UN_TOP_TAB";

	public static final String TOP_COD = "TOP_COD";

	public static final String TOP_NAM = "TOP_DSC";

	public static final int TOP_LOAD = INTERNAL_EVENTS_MAX + 116;

	// Terms of Delivery

	public static final String TOD_TAB = "UN_TOD_TAB";

	public static final String TOD_COD = "TOD_COD";

	public static final String TOD_NAM = "TOD_DSC";

	public static final int TOD_LOAD = INTERNAL_EVENTS_MAX + 117;

	// Unit of measurement

	public static final String UOM_TAB = "UN_UOM_TAB";

	public static final String UOM_COD = "UOM_COD";

	public static final String UOM_NAM = "UOM_DSC";

	public static final int UOM_LOAD = INTERNAL_EVENTS_MAX + 118;

	// Transaction (1)

	public static final String TR1_TAB = "UN_TR1_TAB";

	public static final String TR1_COD = "TR1_COD";

	public static final String TR1_DSC = "TR1_DSC";

	public static final int TR1_LOAD = INTERNAL_EVENTS_MAX + 119;

	// Transaction (2)

	public static final String TR2_TAB = "UN_TR2_TAB";

	public static final String TR2_COD = "TR2_COD";

	public static final String TR2_DSC = "TR2_DSC";

	public static final int TR2_LOAD = INTERNAL_EVENTS_MAX + 120;

	/**
	 * Regulation - National reference database
	 * 
	 */

	// Customs procedure (extended)
	public static final String CP4_TAB = "UN_CP4_TAB";

	public static final String CP4_COD = "CP4_COD";

	public static final String CP4_NAM = "CP4_DSC";

	public static final int CP4_LOAD = INTERNAL_EVENTS_MAX + 121;

	// Customs procedure (link)

	public static final String CP4_CP3 = "UN_CP4_CP3";

	public static final String CP3_COD = "CP3_COD";

	public static final int CP4_CP3_LOAD = INTERNAL_EVENTS_MAX + 122;

	// Customs sites, offices

	public static final String CUO_TAB = "UN_CUO_TAB";

	public static final String CUO_COD = "CUO_COD";

	public static final String CUO_NAM = "CUO_NAM";

	public static final String CUO_BRD = "CUO_BRD";

	public static final int CUO_LOAD = INTERNAL_EVENTS_MAX + 123;

	// Foreign currency daily rates

	public static final String CUR_TAB = "UN_CUR_TAB";

	public static final String CUR_COD = "CUR_COD";

	public static final String CUR_DSC = "CUR_DSC";

	static public final String CUR_TAB_EVENT = "CUR_TAB_EVENT";

	public static final int CUR_LOAD = INTERNAL_EVENTS_MAX + 124;

	// Currency exchange rates

	public static final String RAT_TAB = "UN_RAT_TAB";

	public static final String CUR_REF = "CUR_REF";

	public static final String RAT_EXC = "RAT_EXC";

	public static final int RAT_LOAD = INTERNAL_EVENTS_MAX + 125;

	static public final String RAT_TAB_EVENT = "RAT_TAB_EVENT";

	// Model of declaration

	public static final String MOD_TAB = "UN_MOD_TAB";

	public static final String MOD_COD = "MOD_COD";

	public static final String CP1_COD = "CP1_COD";

	public static final String MOD_DSC = "MOD_DSC";

	public static final String MOD_FLW = "MOD_FLW";

	public static final int MOD_LOAD = INTERNAL_EVENTS_MAX + 126;

	// Preference

	public static final String PRF_TAB = "UN_PRF_TAB";

	public static final String PRF_COD = "PRF_COD";

	public static final String PRF_NAM = "PRF_DSC";

	public static final int PRF_LOAD = INTERNAL_EVENTS_MAX + 127;

	static public final String PRF_TAB_EVENT = "PRF_TAB_EVENT";

	// Region

	public static final String REG_TAB = "UN_REG_TAB";

	public static final String REG_COD = "REG_COD";

	public static final String REG_DSC = "REG_DSC";

	public static final int REG_LOAD = INTERNAL_EVENTS_MAX + 128;

	// Transit shed

	// REMARK: UNA instead of UN to show how it works only
	public static final String SHD_TAB = "UNA_SHD_TAB";

	public static final String SHD_COD = "SHD_COD";

	public static final String SHD_NAM = "SHD_NAM";

	public static final int SHD_LOAD = INTERNAL_EVENTS_MAX + 129;

	// Warehouse

	public static final String WHS_TAB = "UN_WHS_TAB";

	public static final String WHS_COD = "WHS_COD";

	public static final String WHS_NAM = "WHS_NAM";

	public static final String WHS_PUB = "WHS_PUB";

	public static final int WHS_LOAD = INTERNAL_EVENTS_MAX + 130;

	// Bank

	public static final String BNK_TAB = "UN_BNK_TAB";

	public static final String BNK_COD = "BNK_COD";

	public static final String BNK_NAM = "BNK_NAM";

	public static final int BNK_LOAD = INTERNAL_EVENTS_MAX + 131;

	// Bank branch

	public static final String BRA_TAB = "UN_BRA_TAB";

	public static final String BRA_COD = "BRA_COD";

	public static final String BRA_NAM = "BRA_NAM";

	public static final int BRA_LOAD = INTERNAL_EVENTS_MAX + 132;

	// Previous procedure

	public static final String CPP_TAB = "UN_CPP_TAB";

	public static final String CPP_COD = "CPP_COD";

	public static final String CPP_NAM = "CPP_DSC";

	public static final String ACT_COD = "ACT_COD";

	public static final int CPP_LOAD = INTERNAL_EVENTS_MAX + 133;

	// Countries - mode of transport

	public static final String CTY_MOT = "UN_CTY_MOT";

	public static final int CTY_MOT_LOAD = INTERNAL_EVENTS_MAX + 134;

	public static final String CUO_MOT = "UN_CUO_MOT";

	public static final int CUO_MOT_LOAD = INTERNAL_EVENTS_MAX + 135;

	public static final String SHD_DEC = "UN_SHD_DEC";

	public static final int SHD_DEC_LOAD = INTERNAL_EVENTS_MAX + 136;

	public static final String PRF_CTY = "UN_PRF_CTY";

	public static final int PRF_CTY_LOAD = INTERNAL_EVENTS_MAX + 137;

	static public final String PRF_CTY_EVENT = "PRF_CTY_EVENT";

	public static final String CAP_TAB = "UN_CAP_TAB";

	public static final String CAP_COD = "CAP_COD";

	public static final String CAP_LIC = "CAP_LIC";

	public static final int CAP_TAB_LOAD = INTERNAL_EVENTS_MAX + 138;

	// Warehouse - companies

	public static final String WHS_CMP = "UN_WHS_CMP";

	public static final String CMP_COD = "CMP_COD";

	public static final int WHS_CMP_LOAD = INTERNAL_EVENTS_MAX + 139;

	public static final String CPR_TAB = "UN_CPR_TAB";

	public static final String CPR_COD = "CPR_COD";

	public static final String CPR_DEL = "CPR_DEL";

	public static final int CPR_LOAD = INTERNAL_EVENTS_MAX + 140;

	// Processing program

	public static final String PRG_TAB = "UN_PRG_TAB";

	public static final String PRG_COD = "PRG_COD";

	public static final String PRG_NAM = "PRG_DSC";

	public static final int PRG_LOAD = INTERNAL_EVENTS_MAX + 141;

	// Processing program - office

	public static final String PRG_CUO = "UN_PRG_CUO";

	public static final int PRG_CUO_LOAD = INTERNAL_EVENTS_MAX + 142;

	// Taxes

	public static final String TAX_TAB = "UN_TAX_TAB";

	public static final String TAX_COD = "TAX_COD";

	public static final String TAX_NAM = "TAX_DSC";

	public static final int TAX_LOAD = INTERNAL_EVENTS_MAX + 143;

	static public final String TAX_TAB_EVENT = "TAX_TAB_EVENT";

	// Companies
	public static final String CMP_TAB = "UN_CMP_TAB";

	public static final String CMP_NAM = "CMP_NAM";

	public static final String CMP_ADR = "CMP_ADR";

	public static final String CMP_AD2 = "CMP_AD2";

	public static final String CMP_AD3 = "CMP_AD3";

	public static final String CMP_AD4 = "CMP_AD4";

	public static final int CMP_LOAD = INTERNAL_EVENTS_MAX + 144;

	// Agreements AJ-11022009
	public static final String AGR_TAB = "UN_AGR_TAB";

	public static final String AGR_CMP = "UN_AGR_CMP";

	public static final String AGR_COD = "AGR_COD";

	// Commodity codes
	public static final String TAR_TAB = "UN_TAR_TAB";

	public static final String HS6_COD = "HS6_COD";

	public static final String TAR_PR1 = "TAR_PR1";

	public static final String TAR_PR2 = "TAR_PR2";

	public static final String RUL_COD = "RUL_COD";

	public static final String MKT_COD = "MKT_COD";

	public static final int TAR_LOAD = INTERNAL_EVENTS_MAX + 145;

	// Addtional tariff columns
	public static final String TAR_COL = "UN_TAR_COL";

	public static final String COL_NUM = "COL_NUM";

	public static final String HSC_COD = "HSC_COD";

	public static final String TAR_TXX = "TAR_TXX";

	public static final int TAR_COL_LOAD = INTERNAL_EVENTS_MAX + 146;

	// Rate of duties
	public static final String RTX_TAB = "UN_RTX_TAB";

	public static final String RTX_COD = "RTX_COD";

	public static final String RTX_RAT = "RTX_RAT";

	public static final int RTX_LOAD = INTERNAL_EVENTS_MAX + 147;

	// Customs procedure (national)

	public static final String CP3_TAB = "UN_CP3_TAB";

	public static final String CP3_NAM = "CP3_DSC";

	public static final int CP3_LOAD = INTERNAL_EVENTS_MAX + 148;

	public static final String RUL_TAB = "UN_RUL_TAB";

	public static final String RUL_DSC = "RUL_DSC";

	public static final String RUL_PTY = "RUL_PTY";

	public static final String RUL_DEF = "RUL_DEF";

	public static final int RUL_LOAD = INTERNAL_EVENTS_MAX + 149;

	public static final String TAX_TAR = "UN_TAX_TAR";

	public static final String LST_COD = "LST_COD";

	public static final int TAX_TAR_LOAD = INTERNAL_EVENTS_MAX + 150;

	public static final String TAX_CMP = "UN_TAX_CMP";

	public static final int TAX_CMP_LOAD = INTERNAL_EVENTS_MAX + 151;

	public static final String MKT_TAB = "UN_MKT_TAB";

	public static final String IGW_VAL = "IGW_VAL";

	public static final String IGW_CUR = "IGW_CUR";

	public static final String INW_VAL = "INW_VAL";

	public static final String INW_CUR = "INW_CUR";

	public static final String IU1_VAL = "IU1_VAL";

	public static final String IU1_CUR = "IU1_CUR";

	public static final String IU2_VAL = "IU2_VAL";

	public static final String IU2_CUR = "IU2_CUR";

	public static final String IU3_VAL = "IU3_VAL";

	public static final String IU3_CUR = "IU3_CUR";

	public static final String EGW_VAL = "EGW_VAL";

	public static final String EGW_CUR = "EGW_CUR";

	public static final String ENW_VAL = "ENW_VAL";

	public static final String ENW_CUR = "ENW_CUR";

	public static final String EU1_VAL = "EU1_VAL";

	public static final String EU1_CUR = "EU1_CUR";

	public static final String EU2_VAL = "EU2_VAL";

	public static final String EU2_CUR = "EU2_CUR";

	public static final String EU3_VAL = "EU3_VAL";

	public static final String EU3_CUR = "EU3_CUR";

	public static final String MKT_MOD = "MKT_MOD";

	public static final String STA_MOD = "STA_MOD";

	public static final int MKT_LOAD = INTERNAL_EVENTS_MAX + 152;

	// Quota

	public static final String QUO_TAB = "UN_QUO_TAB";

	public static final String QUO_COD = "QUO_COD";

	public static final String QUO_DSC = "QUO_DSC";

	public static final int QUO_LOAD = INTERNAL_EVENTS_MAX + 153;

	static public final String QUO_TAB_EVENT = "QUO_TAB_EVENT";

	public static final String TAXATION_RULES = "TAXATION_RULES";

	public static final String RUL_TYP = "RUL_TYP";

	public static final String RUL_NAM = "RUL_NAM";

	public static final String RUL_RNK = "RUL_RNK";

	public static final String RUL_PRI = "RUL_PRI";

	public static final int TAXATION_RULES_LOAD = INTERNAL_EVENTS_MAX + 154;

	// company info
	public static final String CMP_IDE_COD = "IDE_COD"; // company code (KEY)

	public static final String CMP_IDE_CTY_COD = "IDE_CTY_COD"; // country code (KEY)

	public static final String CMP_IDE_NAM = "IDE_NAM"; // company name

	public static final String CMP_IDE_ADD = "IDE_ADD"; // company address.

	// static public final String MDE = "MDE"; // My Data Element
	static public final String INSTANCE_ID = "instanceId"; // instance Id

	static public final String ACC = "ACC"; // Account

	static public final String ADD = "ADD"; // Address

	static public final String ADJ = "ADJ"; // Rate of adjustment

	static public final String AIC = "AIC"; // A.I. code

	static public final String ALP = "ALP"; // Alpha coefficient

	static public final String AMT = "AMT"; // Amount

	static public final String AST = "AST"; // Assessment

	static public final String ATD = "ATD"; // Attached documents

	static public final String ATT = "ATT"; // Attached documents

	static public final String AUT = "AUT"; // Authorization

	static public final String AYR = "AYR"; // Assessment year - used in Asmt Notice

	static public final String BNK = "BNK"; // Bank

	static public final String BRA = "BRA"; // Branch

	static public final String BRD = "BRD"; // Border

	static public final String BSE = "BSE"; // Base

	static public final String CAP = "CAP"; // Commom Agriculture Policy reference

	static public final String CDE = "CDE"; // Date of declaration

	static public final String CIF = "CIF"; // Costs, insurance, freight

	static public final String CIT = "CIT"; // City

	static public final String CLR = "CLR"; // Clearance office

	static public final String CMP = "CMP"; // Company

	static public final String CNL = "CNL"; // Cancellation

	static public final String COD = "COD"; // Code

	static public final String COF = "COF"; // Customs officer name

	static public final String COL = "COL"; // Selected colour

	static public final String CON = "CON"; // Consignee

	static public final String CRG = "CRG"; // Region code

	static public final String CST = "CST"; // Costs

	static public final String CTF = "CTF"; // Container flag

	static public final String CTL = "CTL"; // Control

	static public final String CTN = "CTN"; // Container(s)

	static public final String CT1 = "CT1"; // Container(s)

	static public final String CT2 = "CT2"; // Container(s)

	static public final String CT3 = "CT3"; // Container(s)

	static public final String CT4 = "CT4"; // Container(s)

	static public final String CTR = "CTR"; // Counter

	static public final String CTY = "CTY"; // Country

	static public final String CUO = "CUO"; // Customs office

	static public final String CUR = "CUR"; // Currency

	static public final String DAT = "DAT"; // Date

	static public final String DEC = "DEC"; // Declarant

	static public final String DED = "DED"; // Deductions

	static public final String DEL = "DEL"; // Deletion

	static public final String DES = "DES"; // Destination

	static public final String DOC = "DOC"; // Document reference

	static public final String DPA = "DPA"; // Departure / arrival

	static public final String DSC = "DSC"; // Description
	
	static public final String JAV=" JAV"; // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">  JAVA DSC =ClassRule

	static public final String DTY = "DTY"; // Duties & Taxes

	static public final String EFR = "EFR"; // External freight

	static public final String EPT = "EPT"; // Export

	static public final String ERR = "ERR"; // Error

	static public final String EXP = "EXP"; // Exporter

	static public final String EXT = "EXT"; // Extended

	static public final String FCX = "FCX"; // Foreign currency

	static public final String FEE = "FEE"; // Global taxes

	static public final String FIN = "FIN"; // Financial

	static public final String FIS = "FIS"; // Financial settlement

	static public final String FLG = "FLG"; // Indicator flag

	static public final String FLD = "FLD"; // Field no.

	static public final String FLT = "FLT"; // First destination / last provenance

	static public final String FL1 = "FL1"; // First destination / last provenance label 1

	static public final String FL2 = "FL2"; // First destination / last provenance label 2

	static public final String FLW = "FLW"; // SAD Flow - export/import

	static public final String FRE = "FRE"; // Free

	static public final String FRM = "FRM"; // Form

	static public final String ICT = "ICT"; // Incoterm

	static public final String IDE = "IDE"; // Identification

	static public final String IFR = "IFR"; // Internal freight

	static public final String INF = "INF"; // Information

	static public final String INL = "INL"; // Inland

	static public final String ITM = "ITM"; // Item

	static public final String GDS = "GDS"; // Goods description

	static public final String GEN = "GEN"; // General

	static public final String GRS = "GRS"; // Gross

	static public final String GC1 = "GC1"; // Global tax code 1

	static public final String GC2 = "GC2"; // Global tax code 2

	static public final String GC3 = "GC3"; // Global tax code 3

	static public final String GD1 = "GD1"; // Global tax code desc 1

	static public final String GD2 = "GD2"; // Global tax code desc 2

	static public final String GD3 = "GD3"; // Global tax code desc 3

	static public final String GLT = "GLT"; // Global tax list

	static public final String GT1 = "GT1"; // Global tax code total 1

	static public final String GT2 = "GT2"; // Global tax code total 2

	static public final String GT3 = "GT3"; // Global tax code total 3

	static public final String GTY = "GTY"; // Guarantee

	static public final String GTX = "GTX"; // Global taxes

	static public final String HSC = "HSC"; // Harmonised system

	static public final String INS = "INS"; // Insurance

	static public final String INV = "INV"; // Invoice

	static public final String ITX = "ITX"; // Item taxes

	static public final String LBL = "LBL"; // Label

	static public final String LDL = "LDL"; // Loading list

	static public final String LIC = "LIC"; // Licence

	static public final String LIM = "LIM"; // Time limit (Date)

	static public final String LIN = "LIN"; // Line

	static public final String LNK = "LNK"; // Link

	static public final String LOC = "LOC"; // Location

	static public final String LOP = "LOP"; // Place of loading

	static public final String MAN = "MAN"; // Manifest

	static public final String MKT = "MKT"; // Market value

	static public final String MOP = "MOP"; // Mode of payment

	static public final String MOT = "MOT"; // Means of transport

	static public final String MNL = "MNL"; // Manual

	static public final String MPN = "MPN"; // Mode of payment name

	static public final String MRK = "MRK"; // Marks

	static public final String MK1 = "MK1"; // Marks and no. of packages 1

	static public final String MK2 = "MK2"; // Marks and no. of packages 2

	static public final String MK3 = "MK3";

	static public final String MK4 = "MK4";

	static public final String MK5 = "MK5";

	static public final String MOD = "MOD";

	static public final String NA1 = "NA1"; // Nature of transaction #1

	static public final String NA2 = "NA2"; // Nature of transaction #2

	static public final String NAM = "NAM"; // Name

	static public final String NAT = "NAT"; // National

	static public final String NB1 = "NB1"; // Commodity code

	static public final String NB2 = "NB2"; // Commodity code - national precision #2

	static public final String NB3 = "NB3"; // Commodity code - national precision #3

	static public final String NB4 = "NB4"; // Commodity code - national precision #4

	static public final String NB5 = "NB5"; // Commodity code - national precision #5

	static public final String NBR = "NBR"; // Number

	static public final String NET = "NET"; // Net

	static public final String NMU = "NMU"; // National monetary unit

	static public final String NUM = "NUM"; // SAD_NUM

	static public final String OCC = "OCC"; // Occurrence

	static public final String ORD = "ORD"; // Order

	static public final String ORG = "ORG"; // Origin

	static public final String OTC = "OTC"; // Other costs

	static public final String PAG = "PAG"; // Page No

	static public final String PCK = "PCK"; // Total no. of Packages

	static public final String PLC = "PLC"; // Place of declaration

	static public final String PRC = "PRC"; // Procedure code

	static public final String PRE = "PRE"; // Precision

	static public final String PRF = "PRF"; // Preference

	static public final String PRI = "PRI"; // Price

	static public final String PRV = "PRV"; // Previous

	static public final String PST = "PST"; // Post-entry

	static public final String PTY = "PTY"; // Properties

	static public final String QTY = "QTY"; // Quantity

	static public final String QUO = "QUO"; // Quota

	static public final String RAT = "RAT"; // Rate

	static public final String RCP = "RCP"; // Receipt

	static public final String REF = "REF"; // Reference

	static public final String REG = "REG"; // Registration

	static public final String REP = "REP"; // Representative

	static public final String RNK = "RNK"; // Rank

	static public final String RES = "RES"; // Principal responsible

	static public final String REV = "REV"; // Loss of revenue

	static public final String RSP = "RSP"; // Principal responsible

	static public final String RSV = "RSV"; // Reserved

	static public final String RUL = "RUL";

	static public final String RYR = "RYR"; // Registration year - used in asmt notice

	static public final String SAD = "SAD"; // Single Administartive Document

	static public final String SER = "SER"; // Serial number

	static public final String SFT = "SFT"; // Shift number

	static public final String SGT = "SGT"; // Signature

	static public final String SIT = "SIT"; // Situation

	static public final String SLS = "SLS"; // Seals

	static public final String STA = "STA";

	static public final String STD = "STD"; // Statement date

	static public final String STN = "STN"; // Statement number

	static public final String STS = "STS"; // Statement serial letter

	static public final String STV = "STV"; // Statistical value

	static public final String SUP = "SUP"; // Supplementary unit

	static public final String TAB = "TAB"; // Table

	static public final String TAR = "TAR"; // Tarification

	static public final String TAX = "TAX"; // Taxation

	static public final String TBP = "TBP"; // To be paid/paid/to be reimbursed/reimbursed

	static public final String TIM = "TIM"; // Time delay

	static public final String TMP = "TMP"; // Temporary

	static public final String TOD = "TOD"; // Terms of delivery

	static public final String TOP = "TOP"; // Terms of payment

	static public final String TOT = "TOT"; // Total no. of forms

	static public final String TPT = "TPT"; // Transport

	static public final String TRA = "TRA"; // Transaction

	static public final String TRD = "TRD"; // Trading

	static public final String TRS = "TRS"; // Transit

	static public final String TXT = "TXT"; // Text

	static public final String TYP = "TYP"; // Type

	static public final String UOM = "UOM"; // Unit of measurement

	static public final String UM1 = "UM1"; // Unit of measurement code 1

	static public final String UM2 = "UM2"; // Unit of measurement code 2

	static public final String UM3 = "UM3"; // Unit of measurement code 3

	static public final String VAL = "VAL"; // Value

	static public final String VDT = "VDT"; // Value details

	static public final String VGS = "VGS"; // Valuation, general segment

	static public final String VIT = "VIT"; // Valuation, item

	static public final String VMT = "VTM"; // Valuation method

	static public final String VMC = "VMC"; // Valuation method code

	static public final String WDE = "WDE"; // Working date

	static public final String TMZ = "TMZ"; // Time zone

	static public final String WGT = "WGT"; // Weight

	static public final String WHS = "WHS"; // Warehouse

	static public final String WRK = "WRK"; // Working mode

	static public final String YER = "YER"; // Year

	static public final String ZIP = "ZIP"; // ZIP code

	static public final String AD1 = "AD1"; // Address

	static public final String AD2 = "AD2"; // Address

	static public final String VLD = "VLD";

	static public final String BAL = "BAL";

	static public final String BEG = "BEG";

	static public final String END = "END";

	static public final String RLS = "RLS"; // Release

	static public final String LOGIN_NAME = "LOGIN_NAME";

	static public final String BLU = "BLU";

	static public final String RED = "RED";

	static public final String YEL = "YEL";

	static public final String GRE = "GRE";

	static public final String QUE = "QUE";

	// result fields
	static public final String RESULT_ASSIGNED_SERIAL = "SAD result assigned serial";

	static public final String USR_AUTHORIZED_CUOS = "User authorized customs offices";

	static public final String AUTHORIZATION_WARNING_MESSAGE = "Warning - You are not authorized to process declarations in this office ";

	static public final String AUTHORIZATION_ERROR_MESSAGE = "You are not authorized to perform this operation in this office ";

	static public final String USR_DEC_BRK = "Declarant broker";

	static public final String OWNER_DEC = "Owner declarant";

	static public final String LNG = "LNG"; // Language

	static public final String CHP2 = "CHP2"; // Telephone number

	static public final String SEC2 = "SEC2"; // Title

	static public final String OTH = "OTH"; // Temporary entity

	static public final String CHP = "CHP"; // Turnover

	static public final String NOT = "NOT"; // VAT

	static public final String SEC = "SEC"; // World Wide Web home page

	static public final String KY1 = "KY1"; //

	static public final String KY2 = "KY2"; //

	static public final String KY3 = "KY3"; //

	static public final String KY4 = "KY4"; //

	static public final String NOM = "NOM"; //

	static public final String KEY = "KEY"; //

	static public final String TIT = "TIT"; //

	static public final String COM = "COM"; //

	static public final String WRD = "WRD"; //

	static public final String TAB2 = "TAB2"; //

	static public final String TAB4 = "TAB4"; //

	static public final String TAB5 = "TAB5"; //

	static public final String TAB6 = "TAB6"; //

	static public final String TAB8 = "TAB8"; //

	static public final String TAB10 = "TAB10"; //

	static public final String COD1 = "COD1"; //

	static public final String COD2 = "COD2"; //

	static public final String COD3 = "COD3"; //

	static public final String NAM1 = "NAM1"; //

	static public final String NAM2 = "NAM2"; //

	static public final String NAM3 = "NAM3"; //

	static public final String TBL = "TBL"; //

	static public final String VEC = "VEC"; //

	static public final String CUS = "CUS"; //

	static public final String ADT = "ADT"; //

	static public final String UNT = "UNT"; //

	// Hatem added Quota Management
	static public final String QTA = "QTA";

	// Hatem

	static public final String UOM_TAB_EVENT = "UOM_TAB_EVENT";
}
