package org.httpdb.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.httpdb.HttpdbVersion;
import org.httpdb.schema.HttpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hg.httpdb.DSMgr;

/**
 * 
 * *******************************************************************
 * @className	： HttpdbServerServlet
 * @description	： Httpdb Web服务接口
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 16, 2016 1:32:47 PM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings("serial")
public class HttpdbServerServlet extends HttpServlet {
	
	private static Logger LOG = LoggerFactory.getLogger(HttpdbServerServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			// 初始化webapp参数取值对象
			Parameters.initialize(config);
			// 初始化Handler对象工厂
			HttpdbHandlerFactory.initialize(config);
			DSMgr.getInstance().init(config.getServletContext().getResourceAsStream("/WEB-INF/httpdb.xml"));
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.println("<!doctype html><html><body><h1>HttpDB(v" + HttpdbVersion.getVersion() + ") Running...</h1><body><html>");
		writer.flush();
		writer.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置请求编码格式
		request.setCharacterEncoding(Parameters.getString(Parameter.HTTPDB_REQUEST_ENCODING, "UTF-8"));
		//此次请求的cmd命令
		String cmd = request.getParameter("cmd");
		//http转换后的的Schema
		HttpSchema httpSchema = HttpdbHandlerFactory.getRequestHandler(cmd).handle(request , response);
        //此次请求是有可用的
        if (httpSchema.certified()) {
        	try {
				//始终取名称为默认命令值的Connection处理对象
				ResultSet resultset = HttpdbHandlerFactory.getCommandHandler(cmd).handle(httpSchema);
				//对得到的结果集进行处理
				HttpdbHandlerFactory.getResponseHandler(cmd).handle(request , response , resultset);
				//请求处理结束
			} catch (Exception e) {
				// TODO 相应异常信息
			}
        }else {
			//TODO 相应提示信息
		}
	}
	
}
