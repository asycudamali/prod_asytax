package un.kernel.util.rulecompiler;

public abstract class AbstractTaxationRule {
	
	
	 
	
	public abstract void apply();
	
	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed">
	public abstract String getVersion();
	
	public boolean checkVersion(){
		if (getVersion().equals(RuleUtils.COMPILERVERSION)){
			return true;
		}
		return false;
	}
	 // GVA <patch ID="taxation rule with faulty calculation of numeric value Bug #769" version="4.2.2" type="modification" date="Nov 17, 2014" author="ahmed"/>
	

}
