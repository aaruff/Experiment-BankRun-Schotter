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
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;

public class CalculatorResult extends HttpServlet implements SingleThreadModel
{
    HttpSession _httpSsn;
    String _rqstr = Constants.EMPTY_STRING;
    HashMap _plyrPrmVls = null;
    int _crntPrmTmPrd = 0;
    boolean _sqntlSlctnCmpltd = true;

    public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      if(null != _httpSsn)
        _rqstr = (String) _httpSsn.getAttribute(Constants.ARG_USERNAME);
      _plyrPrmVls = new HashMap();
      _crntPrmTmPrd = 1;
      _sqntlSlctnCmpltd = true;

      DataHandler dataHndlr = DataHandler.getDataHandlerInstance();

      if(dataHndlr.calculatorChoice() != Constants.CLCLTR_BY_AVG && dataHndlr.modeOfPlay() == Constants.SQNTL)
      {
        int totNumOfPrds = dataHndlr.totalTimePeriod();
        String rcvStr = req.getParameter("crntPrmTmPrd");
        if(null != rcvStr && !Constants.EMPTY_STRING.equals(rcvStr))
          _crntPrmTmPrd = Integer.parseInt(rcvStr);
        // This Map is collected to take the usernames only.
        // nothing to do with those values.
        HashMap plyrVlusMap = dataHndlr.getCrntRndPlyrTimePrdSlctn();
        int cntr = 0;
        int clcltrType = dataHndlr.calculatorChoice();
        for(Iterator itr = plyrVlusMap.keySet().iterator(); itr.hasNext();)
        {
          cntr++;
          String plyr = (String) itr.next();
          if(clcltrType == Constants.CLCLTR_BY_AVG || clcltrType == Constants.CLCLTR_BY_MODE)
            plyr = "Player" + cntr;

          String strVlu = req.getParameter(plyr);

          if(Constants.YES_SLCTN.equals(strVlu))
            _plyrPrmVls.put(plyr, new Integer(_crntPrmTmPrd));
          else if (Constants.NO_SLCTN.equals(strVlu))
          {
            if(_crntPrmTmPrd == (totNumOfPrds - 1))
              _plyrPrmVls.put(plyr, new Integer(totNumOfPrds));
            else
            {
              _plyrPrmVls.put(plyr, new Integer(0));
              _sqntlSlctnCmpltd = false;
            }
          }
          else // numeric value which was passed as hidden input
             _plyrPrmVls.put(plyr, new Integer(strVlu));
        }
      }
      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      out.println(htmlToBeSent(req));
      out.close();
    }

    private String htmlToBeSent(HttpServletRequest req)
    {
      HtmlBuffer bfr = new HtmlBuffer();
      DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
      int mode = dataHndlr.modeOfPlay();
      String strMode = dataHndlr.playMode();
      int totTimePeriod = dataHndlr.totalTimePeriod();
      int numOfPlayers = dataHndlr.totalNumOfPlayers();

       // FIX : Calculator may be called before any session starts from Instructions page
      if(null == _httpSsn) // check also username as attribute if true
      {
//        bfr.beginHtml("BANK GAME - Calculator for (" +  strMode + ") mode", "silver", "black");
//        bfr.emptyBlock();
//	bfr.addHeading("No valid Session. Please Login.", ALIGN_CENTER, FONT_SIZE_THREE, TEXT_CLR_RED);
//	bfr.addEmptyLines(2);
//	bfr.addBackButton("BACK");
//	return bfr.getBuffer().toString();
     }

     int clcltrType = dataHndlr.calculatorChoice();
     int rndInfoLvl = dataHndlr.roundInfoLevel();
     int prdInfoLvl = dataHndlr.periodInfoLevel();

     if(clcltrType == Constants.CLCLTR_BY_AVG)
       bfr.beginHtml("BANK GAME - Calculator Result", "silver", "black");
     else
       bfr.beginHtml("BANK GAME - Calculator Result for (" +  strMode + ") mode", "silver", "black");
     bfr.emptyBlock();
     bfr.addTodayDt();
     if(clcltrType != Constants.CLCLTR_BY_AVG)
       bfr.withinBlock("<FONT size=3>" + strMode + " Play : round - " + dataHndlr.currentRoundNum());
//   if(rndInfoLvl == RND_MEDIUM || rndInfoLvl == RND_HIGH)
//     bfr.withinBlock("Bank Rate is : " + dataHndlr.getRndBankRate(dataHndlr.currentRoundNum()));
     bfr.addEmptyLine();

     HashMap crntRndPlyrTimePrdSlctn = dataHndlr.getCrntRndPlyrTimePrdSlctn();
     // calculator when called before all people logged in
     // since this will not be written to file so create dummies
//     if(crntRndPlyrTimePrdSlctn.size() < dataHndlr.totalNumOfPlayers())
//     {
//        int nmbr = 1;
//        for(int dummy = crntRndPlyrTimePrdSlctn.size(); dummy < dataHndlr.totalNumOfPlayers(); dummy++)
//        {
//           while(crntRndPlyrTimePrdSlctn.containsKey("Player" + nmbr))
//           {
//               nmbr++;
//           }
//           crntRndPlyrTimePrdSlctn.put("Player" + nmbr, new Integer(-1));
//        }
//     }
       // FIX : quick and dirty. change it later
       if(clcltrType == Constants.CLCLTR_BY_AVG)
       {
         crntRndPlyrTimePrdSlctn = new HashMap();
         for(int nm = 1; nm <= dataHndlr.totalNumOfPlayers(); nm++)
         {
            crntRndPlyrTimePrdSlctn.put("Player" + nm, new Integer(-1));
          }
       }

     if(clcltrType == Constants.CLCLTR_BY_AVG || Constants.SMLTNS == mode)
     {
	HashMap clcltrSlctns = new HashMap();
	for(Iterator itr = crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
	{
	  String usrnm = (String) itr.next();
	  Integer slctn = new Integer(req.getParameter(usrnm));
	  clcltrSlctns.put(usrnm, slctn);
	}

	HashMap amtsCllctd = new HashMap();
        if(clcltrType == Constants.CLCLTR_BY_AVG)
         Calculation.asAvgCalculator(clcltrSlctns, amtsCllctd);
        else
         Calculation.asCalculator(clcltrSlctns, amtsCllctd);

        SortedMap srtdAmtMap = new TreeMap(amtsCllctd);
        if(clcltrType == Constants.CLCLTR_BY_AVG || clcltrType == Constants.CLCLTR_BY_MODE)
	{
	   for(Iterator itr = srtdAmtMap.keySet().iterator(); itr.hasNext();)
	   {
	     String username = (String) itr.next();
	     double value = ((Double) amtsCllctd.get(username)).doubleValue();
	     bfr.addToBody("<BR>" + username, true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_MAROON);
             if(clcltrType == Constants.CLCLTR_BY_AVG || rndInfoLvl == Constants.RND_HIGH)
               bfr.addToBody(" withdrawing at period = " + ((Integer)clcltrSlctns.get(username)),
                             true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLACK);
             bfr.addToBody("  Pay off = $ ", true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
             bfr.addToBody("" + value, true, false, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON);
	   }
	 }
	 else // if(clcltrType == CLCLTR_BY_INFO_LVL)
	 {
  	   double rsltAmt = ((Double) amtsCllctd.get(_rqstr)).doubleValue();
	   bfr.addToBody(_rqstr, true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_MAROON);
           if(clcltrType == Constants.CLCLTR_BY_AVG || rndInfoLvl == Constants.RND_HIGH)
               bfr.addToBody(" withdrawing at period = " + ((Integer)clcltrSlctns.get(_rqstr)),
                             true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLACK);
	   bfr.addToBody("<BR> Pay off = $ ", true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
           bfr.addToBody("" + rsltAmt, true, false, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON);
	 }
	}
	else //if(clcltrType != CLCLTR_BY_AVG && SQNTL == mode)
	{
          if(_sqntlSlctnCmpltd)
          {
	    HashMap amtsCllctd = new HashMap();
	    Calculation.asCalculator(_plyrPrmVls, amtsCllctd);

	    for(Iterator itr = amtsCllctd.keySet().iterator(); itr.hasNext();)
	    {
	      String plyr = (String) itr.next();
	      double value = ((Double) amtsCllctd.get(plyr)).doubleValue();
              if(clcltrType == Constants.CLCLTR_BY_MODE
                 || (clcltrType == Constants.CLCLTR_BY_INFO_LVL && plyr.equals(_rqstr)))
              {
	        bfr.addToBody("<BR>" + plyr, true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_MAROON);
                if(rndInfoLvl == Constants.RND_HIGH)
                  bfr.addToBody(" withdrawing at period = " + ((Integer)_plyrPrmVls.get(plyr)),
                                true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLACK);
                bfr.addToBody(" Pay off = $ ", true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
                bfr.addToBody("" + value, true, false, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON);
              }
	    } // end of for loop
          }
          else
          {
	    for(Iterator itr = _plyrPrmVls.keySet().iterator(); itr.hasNext();)
	    {
	      String plyr = (String) itr.next();
	      int value = ((Integer) _plyrPrmVls.get(plyr)).intValue();
              if(value != 0
                && (clcltrType == Constants.CLCLTR_BY_MODE || (clcltrType == Constants.CLCLTR_BY_INFO_LVL && plyr.equals(_rqstr))))
              {
                bfr.addToBody("<BR>" + plyr + " withdrawing at period = " + value,
                              true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_MAROON);
                if(prdInfoLvl == Constants.PRD_HIGH)
                {
	          HashMap amtsCllctd = new HashMap();
	          Calculation.asCalculator(_plyrPrmVls, amtsCllctd);

                  bfr.addToBody(" Pay off = $ ", true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
                  double val = ((Double) amtsCllctd.get(plyr)).doubleValue();
                  bfr.addToBody("" + val, true, false, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON);
                }
              }
            } // end of for loop

            if(prdInfoLvl == Constants.PRD_MEDIUM)
            {
              for(int i = 1; i <= _crntPrmTmPrd; i++)
              {
                bfr.addToBody("Withdrawl in Time Period " + i + " : ", true, false, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
                int cntr = 0;
                for(Iterator itr = _plyrPrmVls.keySet().iterator(); itr.hasNext();)
                {
                  if(((Integer) _plyrPrmVls.get((String) itr.next())).intValue() == i)
                    cntr++;
                }
                bfr.addToBody("" + cntr, true, false, Constants.FONT_SIZE_FOUR, Constants.TEXT_CLR_MAROON);
              }
            }
            _crntPrmTmPrd++;
            String strToPass = "?" + Constants.PARAM_CLCLTR_TEMP + "=" + _crntPrmTmPrd;
            for(Iterator itrtr = _plyrPrmVls.keySet().iterator(); itrtr.hasNext();)
            {
              String plyr = (String) itrtr.next();
              String value = ((Integer) _plyrPrmVls.get(plyr)).toString();
              strToPass = strToPass + "|" + plyr + "-" + value;
            }
            bfr.addEmptyLines(2);
            bfr.addToBody("<A HREF=\"" + Constants.SRVLT_PATH + "calculatorDisplay" + strToPass +
                          "\"><B>Continue ...</B></A>");
          }
	}
      bfr.addToBody("</FONT>");
      bfr.addEmptyLines(2);
      bfr.addEmptySpaces(10);
      bfr.addBackButton("BACK");
      bfr.addEmptySpaces(2);
      bfr.addCloseButton("QUIT");
      bfr.endHtml();
      return bfr.getBuffer().toString();
    }
}