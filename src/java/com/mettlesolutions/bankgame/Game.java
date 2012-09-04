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
import javax.servlet.ServletException;

import com.mettlesolutions.bankgame.util.ServerProperties;

public class Game extends HttpServlet
{
    //public final String SERVER_DETAILS_FILE = getServletContext().getInitParameter("configLocation");
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
	PrintWriter out = res.getWriter();
	out.println(htmlToBeSent());
	out.close();        
    }

      private String htmlToBeSent()
      {
	  HtmlBuffer bfr = new HtmlBuffer();
	  bfr.beginHtml("BANK GAME - Home Page", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
	  bfr.emptyBlock();
	  bfr.addHeading("BANK &nbsp GAME", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
	  bfr.addEmptyLine();
	  bfr.beginBlock();
	  bfr.addToBody("Welcome !!", Constants.BOLD, Constants.FONT_SIZE_FIVE);
	  bfr.endBlock();
	  bfr.addTodayDt();
	  bfr.addEmptyLines(2);

	  bfr.addHeading("Player - Click here to login and start a new game", Constants.ALIGN_LEFT, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_GREEN);
//	  bfr.addToBody("\n<INPUT TYPE=\"Button\" value=\"Player Login\" onClick=\"window.open('" + SRVLT_PATH +
//	              "playerLogin" + "','PlayerLogin','toolbar=no,status=no,scrollbars=yes,resizable=yes,location=no," +
//	              "menubar=no,directories=no');window.parent.close();\">");

	  bfr.addLinkButton("playerLogin", "Player Login");
	  bfr.addEmptyLines(2);
	  bfr.addHeading("Administrator - Click here to sign before starting a new game", Constants.ALIGN_LEFT, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_GREEN);
	  bfr.addLinkButton("adminLogin", "Administrator Login");
          bfr.addEmptyLines(2);
          bfr.addEmailLinks();
          bfr.addEmptyLines(2);
          bfr.addMettleSign();
	  bfr.endHtml();
	  return bfr.getBuffer().toString();
      }
}
