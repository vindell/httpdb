package org.httpdb.cmd;

import java.sql.SQLException;

import org.httpdb.schema.CmdSchema;

public class CmdInvoker {

	/**
	 * 调用者持有命令对象
	 */
	private Command command;

	/**
	 * 设置命令对象
	 * 
	 * @param command
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

	/**
	 * 执行命令
	 * @param cmd 
	 * @throws SQLException 
	 */
	public void run(CmdSchema cmd) throws SQLException {
		command.exec(cmd);
	}
	
}
