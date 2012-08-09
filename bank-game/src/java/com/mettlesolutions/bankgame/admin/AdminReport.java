/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.admin;

import com.mettlesolutions.bankgame.util.Constants;
import com.mettlesolutions.bankgame.util.HtmlBuffer;
import com.mettlesolutions.bankgame.DataHandler;

import java.util.HashMap;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;

public class AdminReport extends HttpServlet implements SingleThreadModel
{
    // FIX : have to delete Admin file after the end of the game

    HttpSession _httpSsn;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      out.println(htmlToBeSent());
      out.close();
    }

    private String htmlToBeSent()
    {
      HtmlBuffer bfr = new HtmlBuffer();
      DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
      String strMode = dataHndlr.playMode();
      int totTimePeriod = dataHndlr.totalTimePeriod();

      if(null == _httpSsn) // check also username as attribute if true
      {
        bfr.beginHtml("BANK GAME - ADMIN in (" +  strMode + ") mode - Report Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminLogin", "Go to Admin Login Page");
	return bfr.getBuffer().toString();
     }
     else
     {
        bfr.beginHtml("BANK GAME - (" +  strMode + ") mode - Report Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addTodayDt();
	bfr.addEmptyLine();

        if(dataHndlr.reportSlctn() == Constants.RPRT_NONE)
	  bfr.addLinkButton("saveAndRestart", "Restart");
        else
	  bfr.addLinkButton("saveAndRestart", "Save And Restart");

        bfr.addRefreshButton();
        bfr.addToBody("\n<INPUT TYPE=\"Button\" value=\"Change Admin Values\"" +
                      "onClick=\"window.open('" + Constants.SRVLT_PATH + "getAdminValues','AdminValues','toolbar=no,status=no," +
                      "scrollbars=yes,resizable=yes,location=no,menubar=no,directories=no,width=640,height=540')\">");

	bfr.withinBlock(strMode + " Play : round - " + dataHndlr.currentRoundNum());
	bfr.withinBlock("Bank rate for this round : " + dataHndlr.crntBankRate());

	bfr.addHeading("Admin report is as follows...", Constants.ALIGN_CENTER, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_GREEN, true);
//	bfr.addEmptyLine();
	bfr.addToBody(dataHndlr.getAllValuesAsString(), true, true, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
        bfr.addEmptyLines(2);
        bfr.addEmailLinks();
        bfr.addEmptyLines(2);
        bfr.addMettleSign();
	bfr.endHtml();
	return bfr.getBuffer().toString();
      }
    }
}