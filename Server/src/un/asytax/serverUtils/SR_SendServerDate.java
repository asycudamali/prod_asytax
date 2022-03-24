package un.asytax.serverUtils;

import java.util.Date;

import so.kernel.core.DataSet;
import so.kernel.core.KernelEvent;
import so.kernel.core.events.ServerEvent;
import so.kernel.server.ServerBinder;
import so.kernel.server.ServerRule;
import so.util.calendar.DateValue;
import un.asytax.taxation.C_Tax;

public class SR_SendServerDate extends ServerRule implements C_Tax {

	public SR_SendServerDate(ServerBinder serverBinder) {
		super(serverBinder);
	}

	protected void apply(KernelEvent e) {
		DataSet result = null;
		DateValue server_date = new DateValue(new Date().getTime());

		ServerEvent se = (ServerEvent) e;
		result = se.getDestination();

		result.add(DAT);
		result.de(DAT).tryToSetContent(server_date);
	}

}
