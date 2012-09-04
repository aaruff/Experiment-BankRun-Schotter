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

public class PlayerReport extends HttpServlet
{
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
        bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("playerLogin", "Go to Player Login Page");
     }
     else // end of each round
     {
	bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Report Page", "silver", "black");
        String username = (String) _httpSsn.getAttribute(Constants.ARG_USERNAME);
//	bfr.addTodayDt();
        int crntTmPrd = 0;
        if(dataHndlr.modeOfPlay() == Constants.SQNTL)
          crntTmPrd = dataHndlr.crntSqntlTimePeriod(username);
	bfr.withinBlock("<FONT size=3>" + strMode + " Play : You are - " + username +
                        " and this is Round - " + dataHndlr.currentRoundNum() +
                        ((crntTmPrd != 0)?("&nbsp;&nbsp;Time Period - " + crntTmPrd):Constants.EMPTY_STRING) + "</FONT>");
	bfr.addHeading(username + " report is as follows...", Constants.ALIGN_CENTER, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON, true);
	bfr.addEmptyLine();
	bfr.addToBody(dataHndlr.getValuesAsString(username));
      }
      bfr.endHtml();
      return bfr.getBuffer().toString();
    }
}