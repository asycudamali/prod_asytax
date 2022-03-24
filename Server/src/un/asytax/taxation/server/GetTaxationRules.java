package un.asytax.taxation.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import so.kernel.core.DataSet;
import so.kernel.core.KNumberedSubDataSet;
import so.kernel.core.KernelEvent;
import so.kernel.core.events.ServerEvent;
import so.kernel.server.GCFServerEvent;
import so.kernel.server.ServerBinder;
import so.util.DebugOutput;
import un.asytax.taxation.C_Tax;
import un.asytax.taxation.DS_Res_TaxRule;

public class GetTaxationRules implements C_Tax {

	private static ServerBinder s;

	public GetTaxationRules(ServerBinder s) {
		// super(s);
		this.s = s;
	}

	public void processServer(KernelEvent e) {
		ServerEvent event = (ServerEvent) e;
		if (event.getID() == C_Tax.ASK_SEARCH_GLOB || event.getID() == C_Tax.ASK_SEARCH_ITEM) {
			DataSet doc = (DataSet) event.getSource();
			DataSet result = event.getDestination();
			GCFServerEvent ev = (GCFServerEvent) e;
			PreparedStatement ps = null;
			ResultSet rs = null;
			byte[] rule;
			result.numberedItm(TAX, new DS_Res_TaxRule());
			try {
				Connection conn = s.getConnection(1);
				String selectStatement;

				if (event.getID() == C_Tax.ASK_SEARCH_GLOB) {
					selectStatement = "SELECT RUL_TYP, RUL_NAM, RUL_RNK, RUL_DSC, RUL_PRI from Taxation_Rules where RUL_TYP = ? ORDER BY RUL_PRI";
					ps = conn.prepareStatement(selectStatement);
					ps.setInt(1, 2);
				} else if (event.getID() == C_Tax.ASK_SEARCH_ITEM) {
					selectStatement = "SELECT RUL_TYP, RUL_NAM, RUL_RNK, RUL_DSC, RUL_PRI from Taxation_Rules where (RUL_TYP = ?) or (RUL_TYP = ?) ORDER BY RUL_PRI";
					ps = conn.prepareStatement(selectStatement);
					ps.setInt(1, 1);
					ps.setInt(2, 3);
				}
				rs = ps.executeQuery();
				int counter = 0;
				while (rs.next()) {
					if (!"".equals(rs.getString("RUL_NAM").trim())) {
						counter++;
						DS_Res_TaxRule taxRule = (DS_Res_TaxRule) ((KNumberedSubDataSet) result.ds(TAX)).at(counter);
						taxRule.de(TYP).setInteger(rs.getInt("RUL_TYP"));
						taxRule.de(NAM).setString(rs.getString("RUL_NAM"));
						DebugOutput.print("Rule " + rs.getString("RUL_NAM"));
						taxRule.de(ORD).setInteger(rs.getInt("RUL_RNK"));
						taxRule.de(DSC).tryToSetContent(rs.getBytes("RUL_DSC"));
						taxRule.de(PRI).setInteger(rs.getInt("RUL_PRI"));
					}
				}
			} catch (SQLException _) {
				_.printStackTrace();
				DebugOutput.print("SQL Error: " + _.toString() + " " + _.getErrorCode() + " " + _.getSQLState());
				// setError(null, "SQL error", event, _);
				return;
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException _) {
						DebugOutput.print("RS not null SQL error: " + _.toString());
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException _) {
						DebugOutput.print("PS not null SQL Error: " + _.toString());
					}
				}
			}

		}

	}

}// class
