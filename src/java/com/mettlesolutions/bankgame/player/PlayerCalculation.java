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
import com.mettlesolutions.bankgame.Calculation;
import com.mettlesolutions.bankgame.DataHandler;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;


public class PlayerCalculation extends HttpServlet
{
   HttpSession _httpSsn;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
      HtmlBuffer bfr = new HtmlBuffer();
      if(null == _httpSsn)
      {
        bfr.beginHtml("BANK GAME - Player (" +  dataHndlr.modeOfPlay() + ") Input Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("playerLogin", "Go to Player Login Page");
	res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(bfr.getBuffer().toString());
	out.close();
     }
     else
     {
      String username = (String) _httpSsn.getAttribute(Constants.ARG_USERNAME);

      //if(dataHndlr.moreRoundsAvlbl() && !dataHndlr.endOfTheGame())
      if(!dataHndlr.endOfTheGame())
      {
	Calculation.doCalculation(dataHndlr);
        dataHndlr.writeToFile();
      }

//      if(dataHndlr.endOfTheGame())
//      {
//        bfr.beginHtml("BANK GAME - Player (" +  dataHndlr.modeOfPlay() + ") Input Page", "silver", "black");
//        bfr.emptyBlock();
//	bfr.addHeading("End of the Game.", ALIGN_CENTER, FONT_SIZE_THREE, TEXT_CLR_RED);
//	bfr.addEmptyLines(2);
//	bfr.addHeading("Total Amount for " + username + " = $ " +
//		      (dataHndlr.showUpFee() + (dataHndlr.getFinalAmount(username) * dataHndlr.conversionRate())) + "<BR>" +
//		      "(show up fee + final amount after conversion)",
//		        ALIGN_CENTER, FONT_SIZE_FOUR, "maroon", false);
//	res.setContentType("text/html");
//        PrintWriter out = res.getWriter();
//        out.println(bfr.getBuffer().toString());
//	out.close();
//      }
//      else
        res.sendRedirect(Constants.SRVLT_PATH + "playerInput");
     }

    }

}