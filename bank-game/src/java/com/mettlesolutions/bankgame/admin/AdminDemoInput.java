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

public class AdminDemoInput extends HttpServlet implements SingleThreadModel
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
      int mode = dataHndlr.modeOfPlay();
      String strMode = dataHndlr.playMode();
      int totTimePeriod = dataHndlr.totalTimePeriod();
      int numOfPlayers = dataHndlr.totalNumOfPlayers();

      if(null == _httpSsn) // check also username as attribute if true
      {
        bfr.beginHtml("BANK GAME - ADMIN DEMO in (" +  strMode + ") mode - Input Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminLogin", "Go to Admin Login Page");
	return bfr.getBuffer().toString();
     }
     else if(dataHndlr.moreRoundsAvlbl())
     {
        if(dataHndlr.currentRoundNum() == 0)
	{
	  for(int i = 1; i <= dataHndlr.totalNumOfPlayers(); i++)
	  {
            dataHndlr.addPlayerFromAdmin("Player" + i);
	  }
	}

        bfr.beginHtml("BANK GAME - ADMIN DEMO in (" +  strMode + ") mode - Input Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addTodayDt();
	bfr.withinBlock(strMode + " Play : round - " + (dataHndlr.currentRoundNum() + 1));
	bfr.addEmptyLine();

	HashMap choice = new HashMap();
	bfr.beginForm("AdminCalculation", Constants.SRVLT_PATH + "adminCalculation", Constants.POST_METHOD);
	if(Constants.SMLTNS == mode)
	{
	  bfr.withinBlock("Please select the Time Periods to withdraw for the players...");
	  bfr.addEmptyLines(2);
	  for(int itr = 1; itr <= totTimePeriod; itr++)
	  {
	      choice.put(String.valueOf(itr), "Period " + itr);
	  }

	  for(int i = 1; i <= numOfPlayers; i++)
	  {
	    bfr.addToBody("Time Period Selection for Player " + i);
	    bfr.addEmptyLine();
	    bfr.addRadioButtons(Constants.ARG_PLAYER_INPUT + i, choice, String.valueOf(totTimePeriod), true);
	    bfr.addEmptyLines(2);
	  }
	}
	else //if(SQNTL == mode && dataHndlr.moreSqntlTimePeriodAvlbl())
	{
	  // CHECK FOR MORE TIME PERIODS...
	  bfr.withinBlock("Time Period : " + dataHndlr.crntSqntlTimePeriod("Player1"));
	  bfr.withinBlock("Does the Player want to withdraw now ?");
	  bfr.addEmptyLines(2);
	  choice.put(Constants.YES_SLCTN, Constants.YES_SLCTN);
	  choice.put(Constants.NO_SLCTN, Constants.NO_SLCTN);
	  for(int i = 1; i <= numOfPlayers; i++)
	  {
	    if(!dataHndlr.getCrntRndPlyrTimePrdSlctn().containsKey("Player" + i)
	    || ((Integer) dataHndlr.getCrntRndPlyrTimePrdSlctn().get("Player" + i)).intValue() == -1)
	    {
	      bfr.addToBody("For Player " + i);
	      bfr.addEmptyLine();
	      bfr.addRadioButtons(Constants.ARG_PLAYER_INPUT + i, choice, Constants.NO_SLCTN, true);
	      bfr.addEmptyLines(2);
	    }
	    else
	    {
	      int tp = ((Integer) dataHndlr.getCrntRndPlyrTimePrdSlctn().get("Player" + i)).intValue();
	      bfr.addToBody("Player " + i + " has selected a time period " + tp);
      	      bfr.addEmptyLines(2);
	    }
	  }
	}
	bfr.addEmptyLines(2);
	bfr.addSubmitButton(Constants.SUBMIT, Constants.SUBMIT, Constants.SUBMIT);
	bfr.addResetButton(Constants.RESET);
	bfr.endForm();
      }
      else
      {
	bfr.addHeading("COMPLETED ALL "+ dataHndlr.totalNumOfRounds() + " ROUNDS. END OF THE GAME.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(3);
	bfr.addLinkButton("adminReport", "Admin Report");
	bfr.addEmptyLines(5);
	bfr.addHeading("TRY ANOTHER ONE ....", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addLinkButton("adminLogin", "Admin Login");
      }
      bfr.endHtml();
      dataHndlr.writeToFile();
      return bfr.getBuffer().toString();
    }
}