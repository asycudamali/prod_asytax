package un.kernel.util.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.Callable;

import so.hte.server.HTReplicationRunner;
import so.util.DebugOutput;
import un.NatConfigConverter.server.PascalResourceFile;
import un.kernel.util.rulecompiler.ClassRule;
import un.kernel.util.rulecompiler.CompilerAPI;
import un.kernel.util.rulecompiler.RuleUtils;

class MyCollabletariffTaxation implements  Callable<Boolean>{
	private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	HTReplicationRunner runner = null;
	//Calendar dateFrom = Calendar.getInstance();
	//Calendar dateTo = Calendar.getInstance();
	//String rulNam;
	PascalResourceFile rs;
	//Vector v;
	
	public MyCollabletariffTaxation(HTReplicationRunner runner,PascalResourceFile rs){
		 this.runner = runner;
		 this.rs = rs;
	}
	 
	public Boolean call()  {
		 
	 
		Vector v;
		boolean done;
		rs.setItemPos("NatTariff");
		int count = rs.getPascalInt();
		// DebugOutput.print("---> Tariff rules (" + --count + ")");
		int i = rs.getPascalInt();
		i = rs.getPascalInt();

		for (int j = 0; j < count; j++) {
			i = rs.getPascalInt();
			String name = rs.getPascalString();
			DebugOutput.print("Rule name tariff= " + name);
			byte[] b = null;
			int pri = 0;
			if (name != null && !"".equals(name.trim())) {
				i = rs.getPascalInt();
				int len = rs.getPascalInt();
				i = rs.getPascalInt();
				b = new byte[len];
				for (i = 0; i < len; i++) {
					b[i] = rs.getPascalByte();
				}
				pri = rs.getPascalInt();
				// DebugOutput.print("Priority = " + pri);
			}
			if (name != null && !"".equals(name.trim())) {
				v = new Vector();
				v.add(new Integer(1)); // RUL_TYP
				v.add(name); // RUL_NAM
				v.add(new Integer(j + 1)); // RUL_RNK
				v.add(b); // RUL_DSC
				v.add(new Integer(pri)); // RUL_PRI
				v.add(null); // RUL_NTS
				// GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
				try{
					
					RuleUtils ruleUtils = new RuleUtils();
					ClassRule classRule = ruleUtils.decodeRule(b, name);
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
				/*
				 * if (!done) { errFlg = true; break; }
				 */
			}
		}
		 runner.commit();
		return true;
	}
	
} 


 