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

import java.util.Vector;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;

public class PlayerExecute extends HttpServlet implements SingleThreadModel
{
    public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
      HttpSession httpSsn = req.getSession(false);
      String choice = (String) req.getParameter(Constants.ARG_PLAYER_INPUT);

      if(null == httpSsn)
      {
	HtmlBuffer bfr = new HtmlBuffer();

      	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("playerLogin", "Go to Login Page");

        res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
        PrintWriter out = res.getWriter();
        out.println(bfr.toString());
        out.close();
      }
      else
      {
	String username = (String) httpSsn.getAttribute(Constants.ARG_USERNAME);

        // This is to make player input page to reload only the first time
        // this value is checked and when true is changed so as to fail
        // on the next reload of the same page
        //httpSsn.setAttribute(ARG_PAGE_RELOAD, YES_SLCTN);

	DataHandler dataHndlr = new DataHandler();
        synchronized(dataHndlr)
        {
          dataHndlr = DataHandler.getDataHandlerInstance();
        }

	int mode = dataHndlr.modeOfPlay();

	if(Constants.SMLTNS == mode)
	{
           synchronized(dataHndlr)
           {
              dataHndlr.addRndPlyrTimeSlctn(username, Integer.parseInt(choice));
	      dataHndlr.writeToFile();
           }
	}
	else // SQNTL == mode
	{
	  if(Constants.YES_SLCTN.equals(choice))
	  {
	    int crntTp = dataHndlr.crntSqntlTimePeriod(username);
            synchronized(dataHndlr)
            {
	      dataHndlr.addRndPlyrTimeSlctn(username, crntTp);
            }
	  }
          else // NO_SLCTN
          {
            if(dataHndlr.crntSqntlTimePeriod(username) == (dataHndlr.totalTimePeriod() - 1))
            {
	      int crntTp = dataHndlr.crntSqntlTimePeriod(username);
              synchronized(dataHndlr)
              {
	        dataHndlr.addRndPlyrTimeSlctn(username, crntTp + 1); // last time period chosen
              }
            }

            synchronized(dataHndlr)
            {
              dataHndlr.incrmntSqntlTimePeriod(username);
            }
          }
        }

        synchronized(dataHndlr)
        {
          dataHndlr.writeToFile();
	  dataHndlr = dataHndlr.getDataHandlerInstance();
        }

	if(dataHndlr.allPlyrsInputGiven())
	{
          synchronized(dataHndlr)
          {
	    dataHndlr.writeToFile();
          }
	  res.sendRedirect(Constants.SRVLT_PATH + "playerCalculation");
	}
	else
	  res.sendRedirect(Constants.SRVLT_PATH + "playerInput");
      }
    }
}