/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame;

import com.mettlesolutions.bankgame.util.Constants;
import com.mettlesolutions.bankgame.util.HtmlBuffer;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class SaveAndRestart extends HttpServlet
{
    HttpSession _httpSsn;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      HtmlBuffer bfr = new HtmlBuffer();
      if(null == _httpSsn)
      {
        bfr.beginHtml("BANK GAME - Administrator Error Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminLogin", "Go to Administrator Login Page");
	res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(bfr.getBuffer().toString());
	out.close();
     }
     else
     {
        DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
	dataHndlr.SaveAsFile();
	dataHndlr.restart();
	res.sendRedirect(Constants.SRVLT_PATH + "adminInput");
     }
   }
}