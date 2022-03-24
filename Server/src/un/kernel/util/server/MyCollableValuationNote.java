package un.kernel.util.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.Callable;

import so.hte.server.HTReplicationRunner;
import un.NatConfigConverter.server.PascalResourceFile;
import un.kernel.util.rulecompiler.ClassRule;
import un.kernel.util.rulecompiler.CompilerAPI;
import un.kernel.util.rulecompiler.RuleUtils;

class MyCollableValuationNote implements  Callable<Boolean>{
	private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	HTReplicationRunner runner = null;
	//Calendar dateFrom = Calendar.getInstance();
	//Calendar dateTo = Calendar.getInstance();
	//String rulNam;
	PascalResourceFile rs;
	//Vector v;
	
	public MyCollableValuationNote(HTReplicationRunner runner,PascalResourceFile rs){
		 this.runner = runner;
		 this.rs = rs;
	}
	 
	public Boolean call()  {
		Vector v; 
		boolean done;
		String rulNam = "";
		for (int i = 1; i <= 8; i++) {

			switch (i) {
			case 1:
				rulNam = "INVEXP.IT";
				break;
			case 2:
				rulNam = "INVEXP.PRM";
				break;
			case 3:
				rulNam = "INVEXP.PRV";
				break;
			case 4:
				rulNam = "INVEXP.SG";
				break;
			case 5:
				rulNam = "INVIMP.IT";
				break;
			case 6:
				rulNam = "INVIMP.PRM";
				break;
			case 7:
				rulNam = "INVIMP.PRV";
				break;
			case 8:
				rulNam = "INVIMP.SG";
				break;
			}
			rs.setItemPos(rulNam);
			int len = rs.getPascalInt();
			byte k = rs.getPascalByte();
			k = rs.getPascalByte();
			byte[] b = new byte[len];
			for (int j = 0; j < len; j++) {
				b[j] = rs.getPascalByte();

			}
			v = new Vector();
			v.add(new Integer(4)); // RUL_TYP
			v.add(rulNam); // RUL_NAM
			v.add(new Integer(i)); // RUL_RNK
			v.add(b); // RUL_DSC
			v.add(new Integer(1)); // RUL_PRI
			v.add(null); // RUL_NTS
			// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
			try{
				
				RuleUtils ruleUtils = new RuleUtils();
				ClassRule classRule = ruleUtils.decodeRule(b, rulNam);
				CompilerAPI.doMemoryCompilation(classRule);
				if (classRule.isValid()){
				
					byte[] rulJavDsc = ruleUtils.toBytes(classRule); 
					v.add(rulJavDsc); // RUL_DSC_JAV
				 
				}else{
					v.add(null); // RUL_DSC_JAV
				}
			}catch (Exception e){
				v.add(null); // RUL_DSC_JAV
			}
			// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed"/>
			
			// done = runner.addLine(v.toArray());
			try {
				Calendar cal = Calendar.getInstance();
				cal.set(2000, 0, 1, 0, 0, 0);
				runner.add(v.toArray(), formatter.parse(formatter.format(cal.getTime())), null);

			} catch (ParseException e) {
			}

		
	}
		//runner.commit();
		return true;
	}
	
} 


 
