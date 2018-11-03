package org.httpdb.cmd;

import java.sql.SQLException;

import org.httpdb.cmd.rec.CmdReceiver;
import org.httpdb.schema.CmdSchema;


public class CreateCommand implements Command {
	
	private CmdReceiver receiver;

	public CreateCommand(CmdReceiver receiver) {
		this.receiver = receiver;
	}

	public void exec(CmdSchema cmd) throws SQLException{
		receiver.action(cmd);
	}
 
}
