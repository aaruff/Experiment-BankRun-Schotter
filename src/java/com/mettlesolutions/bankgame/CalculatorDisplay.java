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

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class CalculatorDisplay extends HttpServlet
{
    HttpSession _httpSsn;
    String _paramVls = "";

    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      _paramVls = req.getParameter(Constants.PARAM_CLCLTR_TEMP);
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
        // FIX : Calculator Display may be called before any session starts from Instructions page
//      bfr.beginHtml("BANK GAME - Calculator for (" +  strMode + ") mode", "silver", "black");
//      bfr.emptyBlock();
//	bfr.addHeading("No valid Session. Please Login.", ALIGN_CENTER, FONT_SIZE_THREE, TEXT_CLR_RED);
//	bfr.addEmptyLines(2);
//	bfr.addBackButton("BACK");
//	return bfr.getBuffer().toString();
      }

      if(dataHndlr.currentRoundNum() <= dataHndlr.totalNumOfRounds())
      {
        int clcltrType = dataHndlr.calculatorChoice();
        if(clcltrType == Constants.CLCLTR_BY_AVG)
          bfr.beginHtml("BANK GAME - Calculator", "silver", "black");
        else
        {
          bfr.beginHtml("BANK GAME - Calculator for (" +  strMode + ") mode", "silver", "black");
  	  bfr.withinBlock(strMode + " Play : round - " + dataHndlr.currentRoundNum());
        }

	bfr.addEmptyLine();
	HashMap choice = new HashMap();
	bfr.beginForm("Calculator", Constants.SRVLT_PATH + "calculatorResult", Constants.POST_METHOD);
        bfr.addToBody("<FONT size=3>");
        // Mode doesn't matter for calculator by average. same calculator for all
	if(clcltrType == Constants.CLCLTR_BY_AVG || Constants.SMLTNS == mode)
	{
	  if(clcltrType == Constants.CLCLTR_BY_AVG || clcltrType == Constants.CLCLTR_BY_MODE)
	    bfr.withinBlock("Please select a Time Period withdrawl scenario (one time for each player)");
	  else if(clcltrType == Constants.CLCLTR_BY_INFO_LVL)
	    bfr.withinBlock("Please select the Time Period you want to withdraw." +
			    " Make assumptions for other player's choices who have not yet decided");

	  for(int itr = 1; itr <= totTimePeriod; itr++)
	  {
	      choice.put(String.valueOf(itr), "Period " + itr);
	  }

	  HashMap crntRndPlyrTimePrdSlctn = dataHndlr.getCrntRndPlyrTimePrdSlctn();
          // calculator when called before all people logged in
          // since this will not be written to file so create dummies
//          if(crntRndPlyrTimePrdSlctn.size() < dataHndlr.totalNumOfPlayers())
//          {
//            int nmbr = 1;
//            for(int dummy = crntRndPlyrTimePrdSlctn.size(); dummy < dataHndlr.totalNumOfPlayers(); dummy++)
//            {
//              while(crntRndPlyrTimePrdSlctn.containsKey("Player" + nmbr))
//              {
//                nmbr++;
//              }
//              crntRndPlyrTimePrdSlctn.put("Player" + nmbr, new Integer(-1));
//            }
//          }
          // FIX : quick and dirty. change it later
          if(clcltrType == Constants.CLCLTR_BY_AVG)
          {
            crntRndPlyrTimePrdSlctn = new HashMap();
            for(int nm = 1; nm <= dataHndlr.totalNumOfPlayers(); nm++)
            {
              crntRndPlyrTimePrdSlctn.put("Player" + nm, new Integer(-1));
            }
          }

	  int cntr = 0;
	  for(Iterator itr = crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
	  {
            cntr++;
	    boolean disabled = false;
	    String usrnm = (String) itr.next();
	    int slctn = ((Integer) crntRndPlyrTimePrdSlctn.get(usrnm)).intValue();
            // hiding username
            String obfsctrName = "Player" + cntr;
            if(clcltrType == Constants.CLCLTR_BY_INFO_LVL)
               obfsctrName = usrnm;
            bfr.addToBody("Time Period Selection for " + obfsctrName);
            bfr.addEmptyLine();

	    if(clcltrType == Constants.CLCLTR_BY_AVG
               || clcltrType == Constants.CLCLTR_BY_MODE
               || (clcltrType == Constants.CLCLTR_BY_INFO_LVL && slctn == -1))
	    {
	      slctn = totTimePeriod;
	      disabled = false;
	    }
	    else if (clcltrType == Constants.CLCLTR_BY_INFO_LVL && slctn != -1)
	    {
	      bfr.addHiddenInput(obfsctrName, Integer.toString(slctn));
	      disabled = true;
	    }
	    bfr.addRadioButtons(obfsctrName, choice, Integer.toString(slctn), true, disabled);
	    bfr.addEmptyLines(2);
	  }
	}
	else // if(clcltrType != CLCLTR_BY_AVG && SQNTL == mode)
	{
          HashMap plyrErlrVlus = new HashMap();

          // initialize values for the first time
          if(null == _paramVls || Constants.EMPTY_STRING.equals(_paramVls))
          {
            // initialize string param values if null
            _paramVls = Constants.EMPTY_STRING;
            if(clcltrType == Constants.CLCLTR_BY_INFO_LVL)
            {
       	      HashMap crntRndPlyrTimePrdSlctn = dataHndlr.getCrntRndPlyrTimePrdSlctn();
              int crntPeriod = 0;
              for(Iterator itr=crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
              {
                String plyr = (String) itr.next();
                int vlu = ((Integer) crntRndPlyrTimePrdSlctn.get(plyr)).intValue();
                if(vlu == -1)
                {
                  vlu = 0;
                  // There has to be atleast one player with value as -1
                  // because then only can that person invoke this calculator
                  crntPeriod = dataHndlr.crntSqntlTimePeriod(plyr);
                }
                _paramVls = _paramVls + "|" + plyr + "-" + Integer.toString(vlu);
              }
              _paramVls = Integer.toString(crntPeriod) + _paramVls;
            }
            else //(clcltrType == CLCLTR_BY_AVG || clcltrType == CLCLTR_BY_MODE)
            {
              _paramVls = "1"; // current time period = 1
              for(int i = 1; i <= numOfPlayers; i++)
              {
                _paramVls = _paramVls + "|" + "Player" + i + "-" + "0";
              }
            }
          }

          StringTokenizer strTok1 = new StringTokenizer(_paramVls, Constants.PARAM_RCRD_SPRTR);
          int crntClcltrTmPrd = Integer.parseInt(strTok1.nextToken());
          bfr.addHiddenInput("crntPrmTmPrd", String.valueOf(crntClcltrTmPrd));
          while(strTok1.hasMoreTokens())
          {
             StringTokenizer strTok2 = new StringTokenizer(strTok1.nextToken(), Constants.PARAM_PLYR_VLU_SPRTR);
             plyrErlrVlus.put(strTok2.nextToken(), strTok2.nextToken());
          }

          bfr.withinBlock("Please select 'YES' if to withdraw in Time Period : " + crntClcltrTmPrd);
	  if(clcltrType == Constants.CLCLTR_BY_INFO_LVL)
	    bfr.withinBlock("Make assumptions for other player's choices who have not yet decided");

	  choice.put(Constants.YES_SLCTN, Constants.YES_SLCTN);
	  choice.put(Constants.NO_SLCTN, Constants.NO_SLCTN);

          for(Iterator itrtr=plyrErlrVlus.keySet().iterator(); itrtr.hasNext();)
	  {
            String plyr = (String) itrtr.next();
            String valueSlctd = (String) plyrErlrVlus.get(plyr);
            if(valueSlctd.equals("0"))
            {
  	      bfr.addToBody("<BR>Does" + plyr + " want to withdraw now ?");
	      bfr.addEmptyLine();
  	      bfr.addRadioButtons(plyr, choice, Constants.NO_SLCTN, true);
            }
            else
            {
              bfr.addHiddenInput(plyr, valueSlctd);
            }
          }
	}
        bfr.addToBody("</FONT>");
	bfr.addEmptyLines(2);
	bfr.addSubmitButton(Constants.SUBMIT,Constants.SUBMIT, "Calculate");
	bfr.addEmptySpaces(2);
	bfr.addResetButton(Constants.RESET);
	bfr.addEmptySpaces(2);
	bfr.addCloseButton("QUIT");
	bfr.endForm();
      }
      else
      {
	bfr.addHeading("SORRY. COMPLETED ALL "+ dataHndlr.totalNumOfRounds() +
                       " ROUNDS. END OF THE GAME.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(3);
	bfr.addCloseButton("Close Window");
      }
      bfr.endHtml();
      dataHndlr.writeToFile();
      return bfr.getBuffer().toString();
    }
}