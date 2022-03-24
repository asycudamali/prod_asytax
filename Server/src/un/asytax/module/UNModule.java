/*
 * UNModule.java
 *
 * Created on Feb 8, 2005 5:55:41 PM
 */

package un.asytax.module;

import so.kernel.core.modules.ModuleInstall;
import so.kernel.server.DocumentModulesManager;

/**
 * 
 */
public class UNModule extends ModuleInstall {

	/** Creates a new instance of UNModule */
	public UNModule() {
	}

	/**
	 * Called when an already-installed module is restored (at System startup time). Should perform whatever initializations are required. The module can load resource about the
	 * module. The class loader is a module class loader.
	 * 
	 */
	public void restored() {
		DocumentModulesManager.registerModule(new UNDocumentInfo());
	}

}
