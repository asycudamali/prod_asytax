package un.kernel.util.rulecompiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import un.asytax.taxation.TaxationParserInterface;

public class ClassRule implements Serializable{
		
	 
	private static final long serialVersionUID = 3074363757734349442L;
		Class clazz = null;		
		String className = "";
		String ruleName = "";
		String source  = "";
		long size = 0;
		byte[] byteCode;
		
		int byteCodeSize = 0;
		long byteCodeSum = 0;
		
		int inListTarNbr =0;
		
		Collection inListTarCollection  = new ArrayList();

		public Class getRuleClass(){
			return clazz;
		}
		public int getInListTarNbr() {
			return inListTarNbr;
		}

		public Collection getInListTarCollection() {
			return inListTarCollection;
		}

		public String getClassName() {
			return className;
		}
		
		public String getSource() {
			return source;
		}
		 
		public void resetSource() {
			this.source= null;
			this.size=0;
		}
		public long getSize() {
			return size;
		}
		 
 		
		public byte[] getByteCode() {
			return byteCode;
		}

		public void setByteCode(byte[] byteCode) {
			this.byteCode = byteCode;
			this.byteCodeSize = byteCode.length;
			this.byteCodeSum = checksum(byteCode);
			calculateInListTar();
		}

		public int getByteCodeSize() {
			return byteCodeSize;
		}
		
		
		 public ClassRule(String className,String source) {
			 
				super();
				this.className = className;
				this.source = source;
				this.size = source.length();
				this.byteCode = null;
				this.byteCodeSize = 0;
				
		 }		

		 public ClassRule(String className,String source,byte[] byteCode) {
			 
				super();
				this.className = className;
				this.source = source;
				this.size = source.length();
				this.byteCode = byteCode;
				this.byteCodeSize = this.byteCode.length;
				calculateInListTar();
	 				
			}
		 
		 public boolean isValid(){
			  
			 return (byteCode!=null && byteCodeSize>0 && byteCode.length==byteCodeSize);
		 }
		 
		 public final boolean isValid(String ruleName){
			// GVA <patch ID="#617 - rules with NUMERIC names do not compile" version="4.2.2" type="modification" date="June 11, 2014" author="JD">
			 String name =  RuleUtils.getClassName(ruleName);   
			// GVA <patch ID="#617 - rules with NUMERIC names do not compile"/>
			 return (byteCode!=null && byteCodeSize>0 && byteCode.length==byteCodeSize && name.equals(this.className));
		 }
		 
		public Object getInstance(ClassLoader parent) throws Exception{
			if (clazz ==null){
				ClassLoader classLoader = getClassLoader(parent);//this.getClass().getClassLoader().getParent()
				clazz= classLoader.loadClass(className);
			}
			return clazz.newInstance();
		}
		
		public final Object getInstance(ClassLoader parent, TaxationParserInterface taxationParseInterface,GlobalVariables globalVariables) throws Exception{
			
			if (clazz ==null){
				ClassLoader classLoader = getClassLoader(parent);
				clazz= classLoader.loadClass(className);
			}
			if (clazz !=null){
				return (clazz.getConstructors())[0].newInstance(new Object[]{taxationParseInterface,globalVariables});
			}
			
			return null;
		}
		
		
		public Class getClass(ClassLoader parent) throws Exception{
			ClassLoader classLoader = getClassLoader(parent);
			clazz= classLoader.loadClass(className);
			return clazz;
		}
		
		private  ClassLoader getClassLoader (ClassLoader parent) {
			return new ClassLoader(parent) {
				
				@Override
				protected Class<?> findClass(String name)
						throws ClassNotFoundException {
					if(checksum(byteCode)!=byteCodeSum){
						throw new ClassNotFoundException();	
					}
					return super.defineClass("un.kernel.util.rulecompiler."+name, byteCode, 0,byteCodeSize,this.getClass().getProtectionDomain());// GVA <patch ID="Re-engineer taxation Bug #283" version="4.2.1" type="modification" date="Sep 09, 2013" author="ahmed"/>
				}
			};
			
		}
		
		public void calculateInListTar(){
			 int count = 0;
			String[] result = source.split("\\s");
			for(int i=0;i<result.length;i++){
				if (result[i].equals("taxationParseInterface.inListTar")){
					for (int j=i+1;j<result.length;j++){
						if (!result[j].equals("(")&& !result[j].equals(")")&& !result[j].equals("")){
							String listName = result[j];
							inListTarNbr++;
							inListTarCollection.add(listName.replaceAll("\"", ""));
							break;
						}
					}
				}
			}
			
			//System.out.println("** "+ className + "   "+ inListTarCollection + "  " + inListTarNbr + " **" );
			
		}
		
		private long calculateChecksum(byte[] buf) {
		    int length = buf.length;
		    int i = 0;

		    long sum = 0;
		    long data;

		    // Handle all pairs
		    while (length > 1) {
		      // Corrected to include @Andy's edits and various comments on Stack Overflow
		      data = (((buf[i] << 8) & 0xFF00) | ((buf[i + 1]) & 0xFF));
		      sum += data;
		      // 1's complement carry bit correction in 16-bits (detecting sign extension)
		      if ((sum & 0xFFFF0000) > 0) {
		        sum = sum & 0xFFFF;
		        sum += 1;
		      }

		      i += 2;
		      length -= 2;
		    }

		    // Handle remaining byte in odd length buffers
		    if (length > 0) {
		      // Corrected to include @Andy's edits and various comments on Stack Overflow
		      sum += (buf[i] << 8 & 0xFF00);
		      // 1's complement carry bit correction in 16-bits (detecting sign extension)
		      if ((sum & 0xFFFF0000) > 0) {
		        sum = sum & 0xFFFF;
		        sum += 1;
		      }
		    }

		    // Final 1's complement value correction to 16-bits
		    sum = ~sum;
		    sum = sum & 0xFFFF;
		    return sum;

		  }
		
		
		private long checksum(byte[] buf) {
			int length = buf.length;
		    int i = 0;
		    long sum = 0;
		    while (length > 0) {
		        sum += (buf[i++]&0xff) << 8;
		        if ((--length)==0) break;
		        sum += (buf[i++]&0xff);
		        --length;
		    }

		    return (~((sum & 0xFFFF)+(sum >> 16)))&0xFFFF;
		}
		
}
