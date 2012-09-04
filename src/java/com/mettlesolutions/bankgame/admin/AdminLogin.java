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

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;


public class AdminLogin extends HttpServlet 
{
      String _earlierLogin = null;

      public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
      {
        HttpSession httpSsn = req.getSession(false);
	if(null != httpSsn)
	{
	  _earlierLogin = (String) httpSsn.getAttribute(Constants.ARG_INVALID_USERNAME);
	  httpSsn.removeAttribute(Constants.ARG_INVALID_USERNAME);
	}
	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
	PrintWriter out = res.getWriter();
	out.println(htmlToBeSent());
	out.close();
      }

      private String htmlToBeSent()
      {
	  HtmlBuffer bfr = new HtmlBuffer();
	  bfr.beginHtml("BANK GAME - Administrator Login Page", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
	  bfr.emptyBlock();
	  bfr.addHeading("ADMINISTRATOR &nbsp LOGIN PAGE", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
	  bfr.addEmptyLine();
	  bfr.addTodayDt();
	  bfr.addEmptyLines(2);

	  addInstruction(bfr, "This page is for Administrator ONLY.");
//	  addInstruction(bfr, "Use <I> 'Administrator' </I> as username and password.");
//	  addInstruction(bf"subject-"+r, "There will be some notes coming here soon ...");
//	  addInstruction(bfr, "----------------------");
//	  addInstruction(bfr, "------------------");
//	  addInstruction(bfr, "--------------");
	  bfr.addEmptyLines(2);

	  bfr.beginForm("AdminLogin", Constants.SRVLT_PATH + "administrator", Constants.POST_METHOD);
	  bfr.addEmptyLine();
	  if(null != _earlierLogin && !Constants.EMPTY_STRING.equals(_earlierLogin))
	    bfr.addHeading("Invalid Login. Try Logging in again...", Constants.ALIGN_CENTER, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_RED);
	  bfr.addHeading("Please give your Username and Password", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
	  bfr.beginTable(Constants.ALIGN_CENTER, Constants.TBL_BORDER_SIZE_TEN, Constants.TBL_CELL_SPCNG_SIZE_TEN, Constants.TBL_CELL_PDNG_SIZE_ONE);
	  bfr.beginTableRow();
	  bfr.addTableCell("USERNAME", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
          // _earlierLogin can be used if it is not readonly
	  bfr.addTextboxTableCell("txtUsername", Constants.ARG_USERNAME, Constants.TEXT_BOX_TYPE_TEXT, Constants.TEXT_BOX_SIZE_FIFTEEN, Constants.ADMIN_USERNAME, true);
          bfr.addHiddenInput(Constants.ARG_USERNAME, Constants.ADMIN_USERNAME);
	  bfr.endTableRow();
	  bfr.beginTableRow();
	  bfr.addTableCell("PASSWORD", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	  bfr.addTextboxTableCell("txtPassword", Constants.ARG_PASSWORD, Constants.TEXT_BOX_TYPE_PASSWORD, Constants.TEXT_BOX_SIZE_FIFTEEN);
	  bfr.endTableRow();
	  bfr.endTable();
	  bfr.beginBlock(Constants.ALIGN_CENTER);
	  bfr.addSubmitButton("loginSubmit", "loginSubmit", "LOGIN");
	  bfr.addEmptySpaces(8);
	  bfr.addResetButton("CLEAR");
	  bfr.endBlock();
          bfr.addEmptyLines(2);
          bfr.addEmailLinks();
          bfr.addEmptyLines(2);
          bfr.addMettleSign();
	  bfr.endForm();
          bfr.addEmptyLines(2);
          bfr.addEmailLinks();
          bfr.addEmptyLines(2);
          bfr.addMettleSign();
	  bfr.endHtml();

	  return bfr.getBuffer().toString();
      }

      private void addInstruction(HtmlBuffer bfr, String text)
      {
	bfr.addToBody(text, Constants.BOLD, Constants.FONT_SIZE_FOUR);
	bfr.addEmptyLine();
      }
}