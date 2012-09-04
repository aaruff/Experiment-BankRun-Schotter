/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.player;

import com.mettlesolutions.bankgame.util.Constants;
import com.mettlesolutions.bankgame.util.HtmlBuffer;
import com.mettlesolutions.bankgame.DataHandler;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class PlayerLogin extends HttpServlet 
{
      String _earlierLogin = null;

    @Override
      public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession httpSession = req.getSession(false);
        
        if(null != httpSession){
	  _earlierLogin = (String) httpSession.getAttribute(Constants.ARG_INVALID_USERNAME);
	  httpSession.removeAttribute(Constants.ARG_INVALID_USERNAME);
	}

	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);

	PrintWriter out = res.getWriter();
	out.println(htmlToBeSent());
	out.close();
      }

      private String htmlToBeSent()
      {
	HtmlBuffer htmlBuffer = new HtmlBuffer();
	htmlBuffer.beginHtml("BANK GAME - Player Login Page", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
	htmlBuffer.emptyBlock();

	htmlBuffer.addHeading("PLAYER &nbsp &nbsp LOGIN PAGE", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
	htmlBuffer.addEmptyLine();
	htmlBuffer.addTodayDt();
	htmlBuffer.addEmptyLine();

	addInstruction(htmlBuffer, "Administrator will give you a username and password");
	addInstruction(htmlBuffer, "Username has to be alphanumeric characters only.");
        
	htmlBuffer.addEmptyLines(2);

       	DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
	String instrctnFileName = dataHndlr.instructionFilename();

	if(!Constants.BLANK_STRING.equals(instrctnFileName)) //inserted blank if empty to get around parsing
	{
  	  htmlBuffer.addToBody("\n\n<INPUT TYPE=\"Button\" value=\"Click here for Instructions\" onClick=\"window.open('" +
		        Constants.SRVLT_PATH + "instrctnsWithClcltr','SpecialNotes','toolbar=no,status=yes," +
		        "scrollbars=yes,resizable=yes,location=no,menubar=no,directories=no,width=640,height=480')\">");
	}

	htmlBuffer.beginForm("PlayerLogin", Constants.SRVLT_PATH + "player", Constants.POST_METHOD);
	htmlBuffer.addEmptyLine();
	if(null != _earlierLogin)
	  htmlBuffer.addHeading("Invalid Login. Try Logging in again...", Constants.ALIGN_CENTER, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_RED);
	htmlBuffer.addHeading("Please give your Username and Password", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
	htmlBuffer.beginTable(Constants.ALIGN_CENTER, Constants.TBL_BORDER_SIZE_TEN, Constants.TBL_CELL_SPCNG_SIZE_TEN, Constants.TBL_CELL_PDNG_SIZE_ONE);
	htmlBuffer.beginTableRow();
	htmlBuffer.addTableCell("USERNAME", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	htmlBuffer.addTextboxTableCell("txtUsername", Constants.ARG_USERNAME, Constants.TEXT_BOX_TYPE_TEXT, Constants.TEXT_BOX_SIZE_FIFTEEN, _earlierLogin);
	htmlBuffer.endTableRow();
	htmlBuffer.beginTableRow();
	htmlBuffer.addTableCell("PASSWORD", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	htmlBuffer.addTextboxTableCell("txtPassword", Constants.ARG_PASSWORD, Constants.TEXT_BOX_TYPE_PASSWORD, Constants.TEXT_BOX_SIZE_FIFTEEN);
	htmlBuffer.endTableRow();
	htmlBuffer.endTable();
	htmlBuffer.beginBlock(Constants.ALIGN_CENTER);
	htmlBuffer.addSubmitButton("loginSubmit", "loginSubmit", "LOGIN");
	htmlBuffer.addEmptySpaces(8);
	htmlBuffer.addResetButton("CLEAR");
	htmlBuffer.endBlock();
	htmlBuffer.endForm();
        htmlBuffer.addEmptyLine();
        htmlBuffer.addEmailLinks();
        htmlBuffer.addEmptyLine();
        htmlBuffer.addMettleSign();
	htmlBuffer.endHtml();

	return htmlBuffer.getBuffer().toString();
      }

      private void addInstruction(HtmlBuffer bfr, String text)
      {
	bfr.addToBody(text, Constants.BOLD, Constants.FONT_SIZE_FOUR);
	bfr.addEmptyLine();
      }

      private void addGameRule(HtmlBuffer bfr, String text)
      {
	bfr.addToBody(text, Constants.BOLD, Constants.ITALIC, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_GREEN);
	bfr.addEmptyLine();
      }


}