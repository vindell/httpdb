package org.httpdb.cmd;

import java.sql.Connection;
import java.sql.SQLException;

import org.httpdb.cmd.rec.CmdReceiver;
import org.httpdb.schema.CmdSchema;

public class HttpdbCommand {

	public static final String meta = "$meta$";
	
	public static final synchronized void exec(CmdSchema schema,Connection conn) throws SQLException {
		
		//创建接受者
		CmdReceiver receiver = CmdReceiverFactory.getCmdReceiver(schema.getCmd());
		
        //创建命令对象，并设置它的接受者
        Command command = new CreateCommand(receiver);
         
        //创建调用者，将命令对象设置进去
        CmdInvoker invoker = new CmdInvoker();
        invoker.setCommand(command);
         
        //这里可以测试一下
        invoker.run(schema);
        
	}
	 
}
