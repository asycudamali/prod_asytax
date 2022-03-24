package un.asytax.services.spi;

import so.kernel.core.DataSet;

//GVA <patch ID="#599 Taxation function: inScanDoc" version="4.2.2" type="NEW" date="Jun 16, 2014" author="Leonardo Flores">
public interface ScanVerifier {
    /**
     * Check if {@code} exits inside EMSMultimedia
     * @param doc D_asysad document
     * @param code Code to look for
     * @param itemNumber Item number inside D_asysad. If item number is equal to zero, it will search over all items.
     * @return true/false
     */
    public Boolean exists(DataSet doc, String code, Number itemNumber);
    public Boolean exists(DataSet doc, String code);
}
