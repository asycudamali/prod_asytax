package un.asytax.taxation;




public interface ClientParserInterface {

	public static final int ERROR_MESSAGE = 0;
	  
	  // Field descriptor #256 I
	  public static final int INFORMATION_MESSAGE = 1;
	  
	  // Field descriptor #256 I
	  public static final int WARNING_MESSAGE = 2;
	  
	  // Field descriptor #256 I
	  public static final int QUESTION_MESSAGE = 3;
	  
	  
	public Double askTax(String s1, String s2, Double d1, Double d2, Double d3);
	
	// GVA <patch ID="#885 SPM - AddTax" version="4.3.0" type="NEW" date="Mar 18, 2015" author="JD">
	public Double addTax(String s1, String s2, Double d1, Double d2, Double d3);
	// GVA <patch ID="#885 SPM - AddTax"/>

	
	public void showMessageDialog(Object message, String title, int messageType);
	        
	public Double displayErr(String s) ;	
	
}
