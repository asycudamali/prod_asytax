package un.asytax.services;

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import un.asytax.services.spi.ScanVerifier;

//GVA <patch ID="#599 Taxation function: inScanDoc" version="4.2.2" type="NEW" date="Jun 16, 2014" author="Leonardo Flores">
public class ScanVerifierService {

    private static ScanVerifierService instance;
    private ScanVerifier scanVerifier;
    
    private ScanVerifierService(){
        ServiceLoader<ScanVerifier> loader = ServiceLoader.load(ScanVerifier.class);
        try {
            if (loader.iterator().hasNext()) scanVerifier = loader.iterator().next();
            
          }catch (ServiceConfigurationError ex) {
            Logger.getLogger(ScanVerifierService.class.getName()).log(Level.SEVERE, null, ex);
          }
    }
    
    public static synchronized ScanVerifierService getInstance(){
        if (instance == null) instance = new ScanVerifierService();
        return instance;
    }
    
    public ScanVerifier getVerifier(){
        return scanVerifier;
    }
}
