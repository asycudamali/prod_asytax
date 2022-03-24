 // GVA <patch ID="Re-engineer taxation" version="4.2.1" type="modification" date="May 22, 2013" author="ahmed">
package un.kernel.util.rulecompiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import un.globalConfig.util.GlobalConfigUtilities;
import un.kernel.core.PropertyLoader;
 
public class CompilerAPI {

	public static boolean log = true;
	
//	public static  void doCompilation (String className, String classSourceCode){
//		try{
//			 
//			String directory = PropertyLoader.getInstance().getPropertyValue("compiler.directory");
//			System.out.println("Compiler directory: " + directory );
//			
//			// Scan in directory for pending xml files
//			File f = new File(directory);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			
//			PrintStream printOutSource=null;
//			PrintStream printOutLog=null;
//			if (log){
//				 
//					FileOutputStream outSource = new FileOutputStream(directory +"/"+ className + ".java" );
//					 
//					printOutSource = new PrintStream(outSource, false, "UTF8");
//					
//					FileOutputStream outLog = new FileOutputStream(directory +"/" + className + ".log" );
//					
//					printOutLog = new PrintStream(outLog, false, "UTF8");
//				 
//			}
//			
//			/*Creating dynamic java source code file object*/
//			SimpleJavaFileObject fileObject = new DynamicJavaSourceCodeObject( className, classSourceCode) ;
//			
//			JavaFileObject javaFileObjects[] = new JavaFileObject[]{fileObject} ;
//			
//			/*Instantiating the java compiler*/
//			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//			
//			/**
//			 * Retrieving the standard file manager from compiler object, which is used to provide
//			 * basic building block for customizing how a compiler reads and writes to files.
//			 *
//			 * The same file manager can be reopened for another compiler task.
//			 * Thus we reduce the overhead of scanning through file system and jar files each time
//			 */
//			
//			StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);
//			
//			/* Prepare a list of compilation units (java source code file objects) to input to compilation task*/
//			Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObjects);
//			
//			/*Prepare any compilation options to be used during compilation*/
//			// we are asking the compiler to place the output files under bin folder.
//			String[] compileOptions = new String[]{"-d", directory} ;
//			Iterable<String> compilationOptionss = Arrays.asList(compileOptions);
//			
//			List<String> optionList = new ArrayList<String>();
//			// set compiler's classpath to be same as the runtime's
//			String classPath  = PropertyLoader.getInstance().getPropertyValue("compiler.classpath");
//			if (System.getProperty("os.name").startsWith("Window")){
//				classPath= classPath.replaceAll(":", ";");
//			}
//			optionList.addAll(Arrays.asList("-classpath",classPath));
//			optionList.addAll(Arrays.asList("-d", directory));
//			
//			
//			/*Create a diagnostic controller, which holds the compilation problems*/
//			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
//			
//			/*Create a compilation task from compiler by passing in the required input objects prepared above*/
//			CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, optionList, null, compilationUnits) ;
//			
//			//Perform the compilation by calling the call method on compilerTask object.
//			boolean status = compilerTask.call();
//			if (log){
//				printOutSource.println(classSourceCode);
//				if (!status){//If compilation error occurs
//					/*Iterate through each compilation problem and print it*/
//					for (Diagnostic diagnostic : diagnostics.getDiagnostics()){
//						System.out.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
//						printOutLog.println("Error on line "+ diagnostic.getLineNumber()+ " in " + diagnostic);
//					}
//				}else{
//					printOutLog.println(" SUCCESSFUL  ");
//				}
//				printOutLog.close();
//				printOutSource.close();
//			}
//			
//			try {
//				stdFileManager.close() ;//Close the file manager
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}catch (Exception e){
//			
//		}
//		
//	}
	public static boolean  doMemoryCompilation (ClassRule classRule){
		try{
			 
			String directory = PropertyLoader.getInstance().getPropertyValue("compiler.directory");
			// GVA <patch ID="Add compile target Bug #995" version="4.3" type="modification" date="Sep 7, 2015" author="ahmed">
			String target = PropertyLoader.getInstance().getPropertyValue("compiler.target");
			String source = PropertyLoader.getInstance().getPropertyValue("compiler.source");
			if (target==null || target.equals("")){
				target= "1.6";
			}
			if (source==null || source.equals("")){
				source= "1.6";
			}
			// GVA <patch ID="Add compile target Bug #995" version="4.3" type="modification" date="Sep 7, 2015" author="ahmed"/>
			File f = new File(directory);
			if (!f.exists()) {
				f.mkdir();
			}
			
			PrintStream printOutSource=null;
			PrintStream printOutLog=null;
			if (log){
				 
					FileOutputStream outSource = new FileOutputStream(directory +"/"+ classRule.getClassName()+ ".java" );
					 
					printOutSource = new PrintStream(outSource, false, "UTF8");
					
					FileOutputStream outLog = new FileOutputStream(directory +"/" + classRule.getClassName() + ".log" );
					
					printOutLog = new PrintStream(outLog, false, "UTF8");
				 
			}
			
			SimpleJavaFileObject fileObject = new DynamicJavaSourceCodeObject( classRule.getClassName(), classRule.getSource()) ;
			JavaFileObject javaFileObjects[] = new JavaFileObject[]{fileObject} ;
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			
			ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager (null, Locale.getDefault(), null));
			
			Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObjects);
			
			String[] compileOptions = new String[]{"-d", directory} ;
			Iterable<String> compilationOptionss = Arrays.asList(compileOptions);
			
			List<String> optionList = new ArrayList<String>();
			String classPath  = PropertyLoader.getInstance().getPropertyValue("compiler.classpath");
			if (System.getProperty("os.name").startsWith("Window")){
				classPath= classPath.replaceAll(":", ";");
			}
			optionList.addAll(Arrays.asList("-classpath",classPath));
			optionList.addAll(Arrays.asList("-d", directory));
			// GVA <patch ID="Add compile target Bug #995" version="4.3" type="modification" date="Sep 7, 2015" author="ahmed">
			optionList.addAll(Arrays.asList("-target", target));
			optionList.addAll(Arrays.asList("-source", source));
			// GVA <patch ID="Add compile target Bug #995" version="4.3" type="modification" date="Sep 7, 2015" author="ahmed"/>
			
			
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			
			CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnits) ;
			
			boolean status = compilerTask.call();
			if (log){
				printOutSource.println(classRule.getSource());
				if (!status){//If compilation error occurs
					/*Iterate through each compilation problem and print it*/
					for (Diagnostic diagnostic : diagnostics.getDiagnostics()){
						 // System.out.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
						printOutLog.println("Error on line "+ diagnostic.getLineNumber()+ " in " + diagnostic);
					}
				}else{
					printOutLog.println(" SUCCESSFUL  ");
				}
				printOutLog.close();
				printOutSource.close();
			}
			JavaClassObject javaClassObject = fileManager.getJavaClassObject();
			 
			if (javaClassObject!=null){
				classRule.setByteCode(javaClassObject.getBytes());
				 
			}
			classRule.resetSource();
			try {
				fileManager.close() ;//Close the file manager
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return classRule.isValid();
		}catch (Exception e){
			return false;
		}
		
	}
	 
}

 
  
class DynamicJavaSourceCodeObject extends SimpleJavaFileObject{
	private String qualifiedName ;
	private String sourceCode ;

	 
	protected DynamicJavaSourceCodeObject(String name, String code) {
		super(URI.create("string:///" +name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
		this.qualifiedName = name ;
		this.sourceCode = code ;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
		return sourceCode ;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
}