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
import com.mettlesolutions.bankgame.util.Util;
import com.mettlesolutions.bankgame.DataHandler;

import java.util.Random;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class PlayerInput extends HttpServlet
{
    HttpSession _httpSsn;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      _httpSsn = req.getSession(false);
      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      out.println(htmlToBeSent());
      out.close();
    }

    private synchronized String htmlToBeSent() {
     HtmlBuffer bfr = new HtmlBuffer();
     DataHandler dataHndlr = new DataHandler();

     synchronized(dataHndlr)
     {
       dataHndlr = DataHandler.getDataHandlerInstance();
     }

     int mode = dataHndlr.modeOfPlay();
     String strMode = dataHndlr.playMode();
     int totTimePeriod = dataHndlr.totalTimePeriod();

     if(null == _httpSsn) // check also username as attribute if true
     {
        bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("playerLogin", "Go to Player Login Page");
        bfr.endHtml();
	return bfr.getBuffer().toString();
     }

     String username = (String) _httpSsn.getAttribute(Constants.ARG_USERNAME);
     String pageReload = Constants.EMPTY_STRING;
     pageReload = (String) _httpSsn.getAttribute(Constants.ARG_PAGE_RELOAD);

     if(dataHndlr.endOfTheGame())
     {
        bfr.beginHtml("BANK GAME - Player (" +  dataHndlr.modeOfPlay() + ") Input Page", "silver", "black", loadScript());
        bfr.emptyBlock();
	bfr.addHeading("End of the Game.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	double value = dataHndlr.showUpFee() + (dataHndlr.getFinalAmount(username) * dataHndlr.conversionRate());
	bfr.addHeading("Total Amount for " + username + " = $ " + Util.valueToTwoDecimals(value) + "<BR>" +
		      "(show up fee + final amount after conversion)",
		        Constants.ALIGN_CENTER, Constants.FONT_SIZE_FOUR, "maroon", false);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("playerLogin TARGET=\"_top\"", "Restart - only after Admin signal");
	bfr.endHtml();
	return bfr.getBuffer().toString();
     }

     if (dataHndlr.plyrRndInputGiven(username)
         && (dataHndlr.numOfPlyrsYetToLogin() != 0
             || (dataHndlr.numOfPlyrsYetToLogin() == 0 && dataHndlr.numOfPlyrsYetToSendInput() != 0)))
     {
     	if(dataHndlr.currentRoundNum() == 0) // login time
	    bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black", 5,
	                  Constants.SRVLT_PATH + "playerInput");
        else
        {
            bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black", 5,
                          Constants.SRVLT_PATH + "playerInput", loadScript());
        }

        bfr.emptyBlock();
	bfr.addTodayDt();
	bfr.withinBlock("<FONT size=3>" + strMode + " Play : You are - " + username + " and this is round - " +
                        dataHndlr.currentRoundNum() + "</FONT>");
        bfr.addEmptyLine();

	bfr.emptyBlock();
	if(dataHndlr.currentRoundNum() == 0)
	   bfr.addHeading("Please wait for " + dataHndlr.numOfPlyrsYetToLogin() +
	                  " players to login ... <BR> (This page refreshes by itself)",
		          Constants.ALIGN_CENTER, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_BLUE, true);
        else
           bfr.addHeading("Please wait for " + dataHndlr.numOfPlyrsYetToSendInput() +
		          " players to input for this round... <BR> (This page refreshes by itself)",
                          Constants.ALIGN_CENTER, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_RED, true);
     }
     else
     {
        if (Constants.SQNTL == mode && !dataHndlr.waitForPeriodCmpltn()
            && dataHndlr.numOfPlyrsYetToCmpltPrd(username) != 0)
        {
          bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black", 5,
	                Constants.SRVLT_PATH + "playerInput", loadScript());

          bfr.emptyBlock();
	  bfr.addTodayDt();
	  bfr.withinBlock("<FONT size=3>" + strMode + " Play : You are - " + username +
                          " and this is round - " + dataHndlr.currentRoundNum() + " , Time period -" +
                          dataHndlr.crntSqntlTimePeriod(username) + "</FONT>");
	  bfr.addEmptyLine();
	  bfr.emptyBlock();
          bfr.addHeading("Please wait for " + dataHndlr.numOfPlyrsYetToCmpltPrd(username) +
                         " players to input for this period ... <BR> (This page refreshes by itself)",
			 Constants.ALIGN_CENTER, Constants.FONT_SIZE_FIVE, Constants.TEXT_CLR_GREEN, true);
   	  bfr.endHtml();
	  return bfr.getBuffer().toString();
        }

        if(dataHndlr.crntBankRate() == 0.0)
        {
          int numOfBanks = dataHndlr.totalNumOfBanks();
	  double economyRate = dataHndlr.economyRateOfReturn();

    	  Random rndm = new Random();
          int rndmVal = rndm.nextInt(numOfBanks) + 1;
	  // FIX : assuming 5 banks as constant. may want to change the logic
	  double bankRate = (rndmVal / 3.0) * economyRate;

          synchronized(dataHndlr)
          {
	    dataHndlr.addRoundBankRate(bankRate);
	    dataHndlr.writeToFile();
	    dataHndlr = DataHandler.getDataHandlerInstance();
          }
        }

	bfr.beginHtml("BANK GAME - Player (" +  strMode + ") Input Page", "silver", "black", loadScript());
        bfr.emptyBlock();
	bfr.addTodayDt();
	bfr.withinBlock("<FONT size=3>" + strMode + " Play : You are - " + username +
                        " and this is round - " + dataHndlr.currentRoundNum() + "</FONT>");
        String clcltrLink = "calculatorDisplay"; // (dataHndlr.calculatorChoice() == CLCLTR_DFLT)
	String clcltrName = "Calculator";
  	if(Constants.CLCLTR_BY_INFO_LVL == dataHndlr.calculatorChoice())
	  clcltrName = "Advanced Calculator";

	bfr.addToBody("<TABLE ALIGN=\"right\"><TR><TD>");
	bfr.addToBody("\n\n<INPUT TYPE=\"Button\" value=\"" + clcltrName + "\" onClick=\"window.open('" + Constants.SRVLT_PATH +
	              clcltrLink + "','Calculator','toolbar=no,status=no,scrollbars=yes,resizable=yes,location=no," +
	              "menubar=no,directories=no,width=640,height=480');\">");
	bfr.addToBody("</TD><TD>");
	bfr.addToBody("\n\n<INPUT TYPE=\"Button\" value=\"Instructions\" onClick=\"window.open('" + Constants.INSTRCTNS_LINK_LOC +
	              dataHndlr.instructionFilename() + "','INSTRUCTIONS','toolbar=no,status=no,scrollbars=yes," +
                      "resizable=yes,location=no,menubar=no,directories=no,width=640,height=480');\">");
	bfr.addToBody("</TD></TR></TABLE>");
  	//bfr.addEmptyLine();

	HashMap choice = new HashMap();
	bfr.beginForm("PlayerExecute", Constants.SRVLT_PATH + "playerExecute", Constants.POST_METHOD);
	if(Constants.SMLTNS == mode)
	{
	   bfr.withinBlock("<FONT size=3>Please select the Time Period you want to withdraw");
	   for(int itr = 1; itr <= totTimePeriod; itr++)
	   {
	     choice.put(String.valueOf(itr), "Period " + itr);
	   }
           bfr.addToBody("</FONT>");
	   bfr.addRadioButtons(Constants.ARG_PLAYER_INPUT, choice, Integer.toString(totTimePeriod), true);
    	}
	else if(Constants.SQNTL == mode)
        {
	   bfr.addToBody("<FONT size=3>Time Period : " + dataHndlr.crntSqntlTimePeriod(username));
	   bfr.addToBody("<BR>Do you want to withdraw now ? <BR>");
           if(dataHndlr.crntSqntlTimePeriod(username) == (dataHndlr.totalTimePeriod() - 1))
              bfr.addToBody("(Choosing 'NO' for this time period will assume 'YES' for the next and last time period)<BR>");
           choice.put(Constants.YES_SLCTN, Constants.YES_SLCTN);
	   choice.put(Constants.NO_SLCTN, Constants.NO_SLCTN);
           bfr.addToBody("</FONT><BR>");
	   bfr.addRadioButtons(Constants.ARG_PLAYER_INPUT, choice, Constants.NO_SLCTN, true);
        }

	bfr.addSubmitButton(Constants.SUBMIT, Constants.SUBMIT, Constants.SUBMIT);
	bfr.addResetButton(Constants.RESET);
	bfr.endForm();
     }

     bfr.endHtml();
     return bfr.getBuffer().toString();
    }

    private String loadScript()
    {
      return "onLoad=\"parent.report.location.reload();\"";
    }
}