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
import com.mettlesolutions.bankgame.Calculation;
import com.mettlesolutions.bankgame.DataHandler;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;


public class AdminCalculation extends HttpServlet
{
   HttpSession _httpSsn;
   DataHandler _dataHndlr;
   boolean roundComplete = true;

    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
      _httpSsn = req.getSession(false);
      _dataHndlr = DataHandler.getDataHandlerInstance();
      HtmlBuffer bfr = new HtmlBuffer();
      if(null == _httpSsn)
      {
        bfr.beginHtml("BANK GAME - ADMIN DEMO in (" +  _dataHndlr.modeOfPlay() + ")", "silver", "black");
        bfr.emptyBlock();
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminLogin", "Go to Admin Login Page");
     }
     else
     {
	updateDataHandlerWithData(req);
	_dataHndlr.writeToFile();
	if(roundComplete)
	  res.sendRedirect(Constants.SRVLT_PATH + "adminReport");
	else
	  res.sendRedirect(Constants.SRVLT_PATH + "adminDemoInput");
     }
     res.setContentType("text/html");
     PrintWriter out = res.getWriter();

     out.println(bfr.getBuffer().toString());
     out.close();
    }


    private void updateDataHandlerWithData(HttpServletRequest req)
    {
 	if(Constants.SMLTNS == _dataHndlr.modeOfPlay())
	{
	  for(int i = 1; i <= _dataHndlr.totalNumOfPlayers(); i++)
	  {
	    String choice = (String) req.getParameter(Constants.ARG_PLAYER_INPUT + i);
	    _dataHndlr.addRndPlyrTimeSlctn("Player" + i, Integer.parseInt(choice));
	  }
	  Calculation.doCalculation(_dataHndlr);
	}
	else // SQNTL.equals(strMode)
	{
	  roundComplete = false;
  	  for(int i = 1; i <= _dataHndlr.totalNumOfPlayers(); i++)
	  {
	    String choice = (String) req.getParameter(Constants.ARG_PLAYER_INPUT + i);
	    if(Constants.YES_SLCTN.equals(choice))
	    {
	      int crntTp = _dataHndlr.crntSqntlTimePeriod("Player" + i);
	      _dataHndlr.addRndPlyrTimeSlctn("Player" + i, crntTp);
	    }
	  }

	  for(int i = 1; i <= _dataHndlr.totalNumOfPlayers(); i++)
	  {
  	    _dataHndlr.incrmntSqntlTimePeriod("Player" + i);
	  }
	  if(_dataHndlr.crntSqntlTimePeriod("Player1") > _dataHndlr.totalTimePeriod())
	  {
	    roundComplete = true;
	    for(int i = 1; i <= _dataHndlr.totalNumOfPlayers(); i++)
	    {
	      if(!_dataHndlr.getCrntRndPlyrTimePrdSlctn().containsKey("Player" + i)
	       || ((Integer) _dataHndlr.getCrntRndPlyrTimePrdSlctn().get("Player" + i)).intValue() == -1)
		_dataHndlr.addRndPlyrTimeSlctn("Player" + i, _dataHndlr.totalTimePeriod());
	    }
	    Calculation.doCalculation(_dataHndlr);
	  }
	}
    }

//    private void doCalculation()
//    {
//      int numOfPlayers = _dataHndlr.totalNumOfPlayers();
//      double eachDeposit = _dataHndlr.amountEachDeposited();
//      double economyRate = _dataHndlr.economyRateOfReturn();
//      double promisedRate = _dataHndlr.promisedRateOfReturn();
//      int numOfBanks = _dataHndlr.totalNumOfBanks();
//      int timePeriod = _dataHndlr.totalTimePeriod();
//      int numOfRounds = _dataHndlr.totalNumOfRounds();
//      double conversionRate = _dataHndlr.amountConversionRate();
//      String mode = _dataHndlr.modeOfPlay();
//
//      double rndmVal = (Math.random() * 10) % (numOfBanks - 1) + 1;
      // FIX : assuming 5 banks as constant. may want to change the logic
//      double bankRate = (rndmVal / 3) * economyRate;
//
//      double bankRate = 1/3 * economyRate;
//      HashMap plyrWithdrawlTimes = _dataHndlr.getCrntRndPlyrTimePrdSlctn();
//
//      Vector wthdrwlTimePrprtns = new Vector(timePeriod);
//      for(int tp = 1; tp <= timePeriod; tp++)
//      {
//	double cntr = 0.0;
//	for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
//	{
//	  String username = (String) itr.next();
//	  if(((Integer) plyrWithdrawlTimes.get(username)).intValue() == tp)
//	    cntr++;
//	}
//	double wtp = cntr / numOfPlayers;
//	wthdrwlTimePrprtns.add(new Double(wtp));
//      }
//
//      Vector bankAssetsByTime = new Vector(timePeriod);
//      Vector paymentByTime = new Vector(timePeriod);
//      for(int t = 1; t <= timePeriod; t++)
//      {
//	double initialVal = Math.pow((1.0 + bankRate), t);
//	double diffrnce = 0.0;
//	for(int itr = 1; itr < t; itr++)
//	{
//	  double wthdrwlPrprtn = ((Double) wthdrwlTimePrprtns.get(itr-1)).doubleValue();
//	  diffrnce = diffrnce + (wthdrwlPrprtn * (1.0 + promisedRate) * (Math.pow(1.0 + bankRate, t - itr)));
//	}
//	double thisBankAsset = numOfPlayers * eachDeposit * (initialVal - diffrnce);
//	bankAssetsByTime.add(new Double(thisBankAsset));
//	double thisWthdrwlPrprtn = ((Double) wthdrwlTimePrprtns.get(t-1)).doubleValue();
//	double case1 = eachDeposit * (Math.pow((1.0 + promisedRate), t));
//	double case2 = thisBankAsset / thisWthdrwlPrprtn;
//	paymentByTime.add(new Double(Math.min(case1, case2)));
//      }
//
//      HashMap paymentByPlayer = new HashMap(numOfPlayers);
//      for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
//      {
//	String username = (String) itr.next();
//	Integer wthdrwlTm = (Integer) plyrWithdrawlTimes.get(username);
//	double amtForPlayer = ((Double) paymentByTime.get(wthdrwlTm.intValue() - 1)).doubleValue();
//	paymentByPlayer.put(username, new Double(amtForPlayer));
//      }
//
//      _dataHndlr.updatePlayerRecords(paymentByPlayer);
//    }
}