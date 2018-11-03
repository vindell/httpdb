package org.httpdb.cmd.rec;

import org.httpdb.HttpdbVersion;
import org.httpdb.cmd.Command;
import org.httpdb.schema.CmdSchema;
/**
 * 
 * *******************************************************************
 * @className	： VersionCmdReceiver
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 17, 2016 11:50:56 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class VersionCmdReceiver implements CmdReceiver {

	public String command() {
		return Command.CMD_VER;
	}

	public void action(CmdSchema cmd) {
		
		HttpdbVersion.getVersion();
		
	}

}
