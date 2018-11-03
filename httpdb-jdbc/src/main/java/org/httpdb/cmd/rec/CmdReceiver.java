package org.httpdb.cmd.rec;

import org.httpdb.schema.CmdSchema;

public interface CmdReceiver {

	public String command();

	public void action(CmdSchema cmd);

}
