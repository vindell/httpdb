package org.httpdb.cmd;

import java.sql.SQLException;

import org.httpdb.schema.CmdSchema;
/*
 * 命令接口
 */
public interface Command {
	
	public static final String CMD_DEF = "def";
	public static final String CMD_VER = "ver";
	
	/*
	 * 执行命令
	 */
	public void exec(CmdSchema cmd) throws SQLException;
	
}
