package un.asytax.sharedUtils;

import java.util.Calendar;

import so.kernel.core.DataSet;
import so.kernel.core.Document;
import so.kernel.core.KernelEvent;
import so.kernel.core.TransactionEvent;
import so.util.calendar.DateValue;
import un.asytax.taxation.C_Tax;

public class ServerDate extends DateValue implements C_Tax {

	public ServerDate() {
		super(0);
	}

	public void initServerDate(Document doc, KernelEvent e) {
		DateValue server_Date_;
		if (e == null) {
			e = new KernelEvent(REC_DAT);
		}
		if (doc.getTransaction() == null) {
			Calendar cal = Calendar.getInstance();
			super.setTime(cal.getTime().getTime());
			return;
		}
		TransactionEvent te_date1 = doc.applyMiddleEvent(REC_DAT, new DataSet());

		if (te_date1.getException() != null || te_date1.getResult() == null || te_date1.getResult().de(DAT) == null) {
			Calendar cal = Calendar.getInstance();
			super.setTime(cal.getTime().getTime());
			return;
		} else {
			server_Date_ = (DateValue) te_date1.getResult().de(DAT).getContent();

			super.setTime(server_Date_.getTime());

		}
	}

}
