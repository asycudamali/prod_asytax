package un.asytax.sharedUtils;

import java.util.TimeZone;

import so.kernel.core.Document;
import so.kernel.core.KernelEvent;
import un.asytax.taxation.C_Tax;
import un.kernel.util.AWUtil;

public class ServerTimeZone implements C_Tax {

	public TimeZone tz;

	public ServerTimeZone(Document doc, KernelEvent e) {
		if (e == null) {
			e = new KernelEvent(REC_TMZ);
			tz = TimeZone.getTimeZone(AWUtil.GMT0);
		}
		if (doc.getTransaction() == null) {
			tz = TimeZone.getTimeZone(AWUtil.GMT0);
			return;
		}
		if (doc == null) {
			tz = TimeZone.getTimeZone(AWUtil.GMT0);
		}
		tz = TimeZone.getTimeZone(AWUtil.GMT0);

	}

	private static String lng(String property) {
		return so.i18n.IntlObj.createMessage("un.asytax", property);
	}
}
