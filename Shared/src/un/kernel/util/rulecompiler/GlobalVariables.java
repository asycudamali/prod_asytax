
// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
package un.kernel.util.rulecompiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import un.asygate.taxation.TaxationParserEnvironment;
import un.kernel.core.PropertyLoader;

public class GlobalVariables {
/*	public double Action;
	public double Num01;
	public double Num02;
	public double Num03;
	public double Num04;
	public double Num05;
	public double Num06;
	public double Num07;
	public double Num08;
	public double Num09;
	public double Num10;
	public double Num11;
	public double Num12;
	public double Num13;
	public double Num14;
	public double Num15;
	public double Num16;
	public double Num17;
	public double Num18;
	public double Num19;
	public double Num20;

	public String Str01;
	public String Str02;
	public String Str03;
	public String Str04;
	public String Str05;
	public String Str06;
	public String Str07;
	public String Str08;
	public String Str09;
	public String Str10;
	public String Str11;
	public String Str12;
	public String Str13;
	public String Str14;
	public String Str15;
	public String Str16;
	public String Str17;
	public String Str18;
	public String Str19;
	public String Str20;
*/	
	public static String ERRCMPMSG="- All rules must be recompiled"; 
	private boolean errorInRules = false;
	private Collection<String > errorList = null; 
	// GVA <patch ID="Taxation parser error Bug #886" version="4.3.0" type="modification" date="Mar 22, 2015" author="ahmed">
	//private boolean versionError =true;
	public static final boolean JAVAPARSER=PropertyLoader.getInstance().getPropertyValue("Client.javaparser")==null?false:new Boolean(PropertyLoader.getInstance().getPropertyValue("Client.javaparser"));
	private boolean versionError =true && JAVAPARSER;  // Please don't change this value , it must be "true"
	// GVA <patch ID="Taxation parser error Bug #886" version="4.3.0" type="modification" date="Mar 22, 2015" author="ahmed"/>
	
	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
	public boolean isVersionError() {
		return versionError;
	}

	public void setVersionError(boolean versionError) {
		this.versionError = versionError;
	}
	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed"/>
	public Collection<String> getErrors() {
		return errorList;
	}
	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
	public boolean isErrorInRules() {
		if (versionError){
			if (!errorList.contains(ERRCMPMSG)) {
				errorList.add(ERRCMPMSG);
				errorInRules = true;
			}
			System.out.println("-----------------------------------------------------------------------------------------");
          	System.out.println("- Rule version error , please restart the server with option -Dun.forcecompilerule=true -");
          	System.out.println("-----------------------------------------------------------------------------------------");
		}
		return errorInRules || versionError;
	}

	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed"/>
	 

	public void addError(String ruleName){
		
		if (!errorList.contains(ruleName)) {
			errorList.add(ruleName);
			errorInRules = true;
		}
		
		
	}
	public GlobalVariables(){
		super();
		errorList = new ArrayList<String>();
		errorInRules=false;
	}
		
	
	public GlobalVariables(TaxationParserEnvironment env){
		 
		this();
		String[][] cVariables = env.getVariables();
		HashMap map = new HashMap();
		for (int i = 0; i < cVariables.length; i++) {
			String type = cVariables[i][0];
			String mode = cVariables[i][1];
			String name = cVariables[i][3];
			if (mode =="1" ){
			  map.put(name, type.equals("%")?0.0:"");  	
				
			}
		}
		
		
	}
	 
	
}
