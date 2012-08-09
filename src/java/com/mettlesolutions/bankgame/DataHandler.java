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
import com.mettlesolutions.bankgame.util.Util;
import com.mettlesolutions.bankgame.util.ServerProperties;
import com.mettlesolutions.bankgame.player.PlayerRecord;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Date;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class DataHandler
{
  // values set by Administrator before any game starts
  // and not modified during the course of the game until the end
  private  int _numOfPlayers;
  private  double _eachDeposit;
  private  double _economyRate;
  private  double _promisedRate;
  private  int _numOfBanks;
  private  int _timePeriod;
  private  int _numOfRounds;
  private  double _conversionRate;
  private  double _insuranceRate;
  private  double _showUpFee;
  private  int _mode;
  private  int _rndRprtLvl;
  private  int _prdRprtLvl;
  private  int _clcltrChoice;
  private  int _rprtType;
  private  String _instrctnFile;

  // current round number being played. incremented after every round
  private  int _crntRoundNum;
  // current time period being played for SQNTL mode against each user
  private  HashMap _crntSqntlTimePeriod;
  // round number for updated selected value against each user
  private  HashMap _userInputNum;
  // selection values updated against each user
  private  HashMap _crntRndPlyrTimePrdSlctn;
  // player record objects with the selection values against each user
  private  HashMap _playerRecords;
  // bank rate against each round
  private  HashMap _roundBankRate;


  public DataHandler()
  {
    // empty constructor
  }

  public DataHandler(int nop, double ed, double er, double bpr, int nob, int tp, int nor, double cr,
		     double ir, double sf, int m, int rrl, int prl, int cc, int rt, String inf, int crn,
		     HashMap cstp, HashMap uin, HashMap crptps, HashMap pr, HashMap rbr)
  {
    _numOfPlayers = nop;
    _eachDeposit = ed;
    _economyRate = er;
    _promisedRate = bpr;
    _numOfBanks = nob;
    _timePeriod = tp;
    _numOfRounds = nor;
    _conversionRate = cr;
    _insuranceRate = ir;
    _showUpFee = sf;
    _mode = m;
    _rndRprtLvl = rrl;
    _prdRprtLvl = prl;
    _clcltrChoice = cc;
    _rprtType = rt;
    _instrctnFile = inf;

    _crntRoundNum = crn;
    _crntSqntlTimePeriod = cstp;
    _userInputNum = uin;
    _crntRndPlyrTimePrdSlctn = crptps;
    _playerRecords = pr;
    _roundBankRate = rbr;
  }

  // This should be called by the Administrator ONLY
  public void setValues(int nop, double ed, double er, double bpr, int nob, int tp, int nor,
			double cr, double ir, double sf, int m, int rrl, int prl, int cc, int rt, String inf)
  {
    _numOfPlayers = nop;
    _eachDeposit = ed;
    _economyRate = er;
    _promisedRate = bpr;
    _numOfBanks = nob;
    _timePeriod = tp;
    _numOfRounds = nor;
    _conversionRate = cr;
    _insuranceRate = ir;
    _showUpFee = sf;
    _mode = m;
    _rndRprtLvl = rrl;
    _prdRprtLvl = prl;
    _clcltrChoice = cc;
    _rprtType = rt;
    _instrctnFile = inf;

    _crntRoundNum = 0;
    _crntSqntlTimePeriod = new HashMap(_numOfPlayers);
    _userInputNum = new HashMap(_numOfPlayers);
    _crntRndPlyrTimePrdSlctn = new HashMap(_numOfPlayers);
    _playerRecords = new HashMap(_numOfPlayers);
    _roundBankRate = new HashMap(_numOfRounds);
    // This will be written before the user values set
    // in the file and so easy to keep all the round values.
    // Bank rate initialized
    for(int itr = 1; itr <= _numOfRounds; itr++)
    {
      _roundBankRate.put(new Integer(itr), new Double(0.0));
    }
    writeToFile();
  }

  // called from Admin set up at any point
  public void setValues(double cr, double sf, int rrl, int prl, int cc, int rt, String inf)
  {
    _conversionRate = cr;
    _showUpFee = sf;
    _rndRprtLvl = rrl;
    _prdRprtLvl = prl;
    _clcltrChoice = cc;
    _rprtType = rt;
    _instrctnFile = inf;

    writeToFile();
  }

  // getters
  public int totalNumOfPlayers()  {    return _numOfPlayers;  }

  public double amountEachDeposited()  {    return _eachDeposit;  }

  public double economyRateOfReturn()  {    return _economyRate;  }

  public double promisedRateOfReturn()  {    return _promisedRate;  }

  public int totalNumOfBanks()  {    return _numOfBanks;  }

  public int totalTimePeriod()  {    return _timePeriod;  }

  public int totalNumOfRounds()  {    return _numOfRounds;  }

  public double conversionRate()  {    return _conversionRate;  }

  public double insuranceRate()  {    return _insuranceRate;  }

  public double showUpFee()  {    return _showUpFee;  }

  public int modeOfPlay()  {    return _mode;  }

  public int roundInfoLevel()  {    return _rndRprtLvl;  }

  public int periodInfoLevel()  {    return _prdRprtLvl;  }

  public int calculatorChoice()  {    return _clcltrChoice;  }

  public int reportSlctn() {   return _rprtType;  }

  public String instructionFilename()  {    return _instrctnFile;  }

  public int currentRoundNum()  {    return _crntRoundNum;  }

  public HashMap getCrntRndPlyrTimePrdSlctn()  {    return _crntRndPlyrTimePrdSlctn;  }

  public String getMode(int mode)
  {
    switch(mode)
    {
      case Constants.SMLTNS: return Constants.STR_SMLTNS;
      case Constants.SQNTL: return Constants.STR_SQNTL;
      default : return Constants.EMPTY_STRING;
    }
  }

  public String getRndReportLvl(int lvl)
  {
    switch(lvl)
    {
      case Constants.RND_LOW: return "LOW";
      case Constants.RND_MEDIUM: return "MEDIUM";
      case Constants.RND_HIGH: return "HIGH";
      default : return Constants.EMPTY_STRING;
    }
  }

 public String getPrdReportLvl(int lvl)
  {
    switch(lvl)
    {
      case Constants.PRD_LOW: return "LOW";
      case Constants.PRD_MEDIUM: return "MEDIUM";
      case Constants.PRD_HIGH: return "HIGH";
      default : return Constants.EMPTY_STRING;
    }
  }

  public String playMode()
  {
    switch (_mode)
    {
      case Constants.SMLTNS : return Constants.STR_SMLTNS;
      case Constants.SQNTL : return Constants.STR_SQNTL;
      default : return Constants.EMPTY_STRING;
    }
  }

  public String calculatorType()
  {
    switch(_clcltrChoice)
    {
      case Constants.CLCLTR_BY_AVG : return "DEFAULT CALCULATOR";
      case Constants.CLCLTR_BY_MODE : return "GENERIC CALCULATOR";
      case Constants.CLCLTR_BY_INFO_LVL : return "ADVANCED CALCULATOR";
      default : return Constants.EMPTY_STRING;
    }
  }

  public String reportType()
  {
    switch(_rprtType)
    {
      case Constants.RPRT_NONE : return Constants.NONE_STRING;
      case Constants.RPRT_TXT : return "TEXT FILE";
      case Constants.RPRT_EXL : return "EXCEL FILE";
      case Constants.RPRT_BOTH : return "TEXT AND EXCEL FILES";
      default : return Constants.NONE_STRING;
    }
  }

  public boolean isLoaded()
  {
    File adminFile = new File(Constants.ADMIN_FILENAME);
    return (adminFile.exists());
  }

  public  boolean moreRoundsAvlbl()
  {
     return (_crntRoundNum < _numOfRounds);
  }

  public boolean endOfTheGame()
  {
    for(Iterator itr = _playerRecords.keySet().iterator(); itr.hasNext();)
    {
      String username = (String) itr.next();
      PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
      if(!plyrRcrd.roundsComplete(_numOfRounds))
	return false;
    }
    return true;
  }

  public boolean waitForPeriodCmpltn()
  {
    boolean readFrstValue = false;
    int dummy = 0;
    for(Iterator itr = _crntSqntlTimePeriod.keySet().iterator(); itr.hasNext();)
    {
      String username = (String) itr.next();
      int crntTmPrd = ((Integer) _crntSqntlTimePeriod.get(username)).intValue();
      int crntRndSlctn =  ((Integer) _crntRndPlyrTimePrdSlctn.get(username)).intValue();
      if(!readFrstValue && crntRndSlctn == -1)
      {
        readFrstValue = true;
        dummy = crntTmPrd;
      }
      if(crntTmPrd != dummy && crntRndSlctn == -1)
	return false;
    }
    return true;
  }

//  public boolean periodCmpltn()
//  {
//    int crntTmPrd = crntSqntlTimePeriod(null);
//    for(Iterator itr = _crntSqntlTimePeriod.keySet().iterator(); itr.hasNext();)
//    {
//      String username = (String) itr.next();
//      int usrTmPrd = ((Integer) _crntSqntlTimePeriod.get(username)).intValue();
//      int usrRndSlctn =  ((Integer) _crntRndPlyrTimePrdSlctn.get(username)).intValue();
//      if(usrTmPrd < crntTmPrd && usrRndSlctn == -1)
//        return false;
//    }
//    return true;
//  }

  public int crntSqntlTimePeriod(String username)
  {
    if(null != username)
      return ((Integer) _crntSqntlTimePeriod.get(username)).intValue();
    else // username is null (general)
    {
      // if the crnt rnd tmprd slctn is -1 then the max tm prd is the crnt tm prd
      // bcos tmprd does not change if answer is given for that round
      int crntTp = 1;
      for(Iterator itr = _crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
      {
        String player = (String) itr.next();
        Integer crntTmPrd = (Integer) _crntSqntlTimePeriod.get(player);
        Integer slctdPrd = (Integer) _crntRndPlyrTimePrdSlctn.get(player);
        if(slctdPrd.intValue() == -1 && crntTmPrd.intValue() > crntTp)
          crntTp = crntTmPrd.intValue();
      }
      return crntTp;

     // the maximum period held by any player should be the crnt period
//     int max = 1;
//     for(Iterator itr = _crntSqntlTimePeriod.keySet().iterator(); itr.hasNext();)
//     {
//       Integer crntVlu = (Integer) _crntSqntlTimePeriod.get((String) itr.next());
//       if(crntVlu.intValue() > max)
//          max = crntVlu.intValue();
//     }
//     return max;
    }
  }

  public synchronized void incrmntSqntlTimePeriod(String username)
  {
    int erlrTmPrd = crntSqntlTimePeriod(username);
    _crntSqntlTimePeriod.put(username, new Integer(erlrTmPrd+1));
  }

  public boolean allPlyrsInputGiven(int inputNum)
  {
    Collection cllctn = _userInputNum.values();
    if(cllctn.isEmpty())
      return false;

    for(Iterator itrtr = cllctn.iterator(); itrtr.hasNext();)
    {
      Integer userInputNum = (Integer) itrtr.next();
      if(null == userInputNum || userInputNum.intValue() < inputNum)
	return false;
    }
    return true;
  }

  public boolean plyrRndInputGiven(String user)
  {
      Integer userInputNum = (Integer) _userInputNum.get(user);
      if(userInputNum.intValue() != _crntRoundNum)
	return false;
      else
        return true;
  }

  public boolean allPlyrsInputGiven()
  {
    return allPlyrsInputGiven(_crntRoundNum);
  }

  public boolean allPlyrsLoggedIn()
  {
    return (_userInputNum.size() == _numOfPlayers);
  }

  public synchronized boolean addPlayer(String username)
  {
    // checking for allPlyrsLoggedIn() is done before calling this method
    if(_playerRecords.containsKey(username))
     return false;

    _userInputNum.put(username, new Integer(0));
    _crntSqntlTimePeriod.put(username, new Integer(1));
    if(_userInputNum.size() == _numOfPlayers)
      _crntRoundNum++;
    Object erlrObj = _playerRecords.put(username, new PlayerRecord(_numOfRounds));
    return (null == erlrObj);
  }

  public boolean addPlayerFromAdmin(String username)
  {
    if(_playerRecords.containsKey(username))
     return false;

    _userInputNum.put(username, new Integer(0));
    Object erlrObj = _playerRecords.put(username, new PlayerRecord(_numOfRounds));
    return (null == erlrObj);
  }

  public int numOfPlyrsYetToLogin()
  {
    return (_numOfPlayers - _userInputNum.size());
  }

  public int numOfPlyrsYetToSendInput()
  {
    int cntr = 0;
    Collection cllctn = _userInputNum.values();
    for(Iterator itrtr = cllctn.iterator(); itrtr.hasNext();)
    {
      Integer userInputNum = (Integer) itrtr.next();
      if(userInputNum.intValue() < _crntRoundNum)
	cntr++;
    }
    return cntr;
  }

  public int numOfPlyrsYetToCmpltPrd(String username)
  {
    int cntr = 0;
    Integer crntTmPrd = (Integer) _crntSqntlTimePeriod.get(username);
    for(Iterator itrtr = _crntSqntlTimePeriod.keySet().iterator(); itrtr.hasNext();)
    {
      String plyr = (String) itrtr.next();
      Integer plyrCrntTmPrd = (Integer) _crntSqntlTimePeriod.get(plyr);
      Integer plyrInputValue = (Integer) _crntRndPlyrTimePrdSlctn.get(plyr);
      if(plyrCrntTmPrd.intValue() < crntTmPrd.intValue() && plyrInputValue.intValue() == -1)
	cntr++;
    }
    return cntr;
  }

  private boolean anyPlyrInputGiven(int tp)
  {
    for(Iterator itr = _crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
    {
      String player = (String) itr.next();
      Integer crntTmPrd = (Integer) _crntSqntlTimePeriod.get(player);
      Integer slctdPrd = (Integer) _crntRndPlyrTimePrdSlctn.get(player);
      if(slctdPrd.intValue() != -1 && crntTmPrd.intValue() == tp)
        return true;
    }
    return false;
  }

  public boolean sqntlAfterPeriodBeforeRound()
  {
    int crntTp = 1;
    boolean rndCmpltd = true;
    boolean prdCmpltd = true;

    // finding the crnt time period first
    // if the crnt rnd tmprd slctn is -1 then the max tm prd is the crnt tm prd
    // bcos tmprd does not change if answer is given for that round
    for(Iterator itr = _crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
    {
      String player = (String) itr.next();
      Integer crntTmPrd = (Integer) _crntSqntlTimePeriod.get(player);
      Integer slctdPrd = (Integer) _crntRndPlyrTimePrdSlctn.get(player);
      if(slctdPrd.intValue() == -1 && crntTmPrd.intValue() > crntTp)
        crntTp = crntTmPrd.intValue();
    }

    // skipping the last but one period report because we have yes/no for
    // last and last but one period as one last page
    // Note : By the end of last period, it would be round completion and so don't care
       if(crntTp == _timePeriod - 1 && anyPlyrInputGiven(crntTp))
         return false;

    for(Iterator itrtr = _crntSqntlTimePeriod.keySet().iterator(); itrtr.hasNext();)
    {
      String plyr = (String) itrtr.next();
      Integer plyrCrntTmPrd = (Integer) _crntSqntlTimePeriod.get(plyr);
      Integer plyrSlctdPrd = (Integer) _crntRndPlyrTimePrdSlctn.get(plyr);

      if(plyrCrntTmPrd.intValue() != 1 || plyrSlctdPrd.intValue() != -1)
         rndCmpltd = false;

      if((plyrSlctdPrd.intValue() == -1 && plyrCrntTmPrd.intValue() != crntTp)
         || (plyrSlctdPrd.intValue() == crntTp))
        prdCmpltd = false;
    }
    return (!rndCmpltd && prdCmpltd);
  }

  public boolean sqntlAfterRound()
  {
    boolean rndCmpltd = true;

    for(Iterator itrtr = _crntSqntlTimePeriod.keySet().iterator(); itrtr.hasNext();)
    {
      String plyr = (String) itrtr.next();
      Integer plyrCrntTmPrd = (Integer) _crntSqntlTimePeriod.get(plyr);
      Integer plyrSlctdPrd = (Integer) _crntRndPlyrTimePrdSlctn.get(plyr);

      if(plyrCrntTmPrd.intValue() != 1 || plyrSlctdPrd.intValue() != -1)
         rndCmpltd = false;
    }

    return rndCmpltd;
  }

  public synchronized boolean addRndPlyrTimeSlctn(String username, int timePeriodSlctn)
  {
    _userInputNum.put(username, new Integer(_crntRoundNum));
    Object erlrObj = _crntRndPlyrTimePrdSlctn.put(username, new Integer(timePeriodSlctn));
    return (null == erlrObj);
  }

  public synchronized boolean updatePlayerRecords(HashMap plyrAmts)
  {
    for(Iterator itr = plyrAmts.keySet().iterator(); itr.hasNext();)
    {
      String username = (String) itr.next();
      double amtCllctd =  ((Double) plyrAmts.get(username)).doubleValue();
      int timePeriodSlctd = ((Integer) _crntRndPlyrTimePrdSlctn.get(username)).intValue();
      PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
      plyrRcrd.update(_crntRoundNum, timePeriodSlctd, amtCllctd);
      _crntSqntlTimePeriod.put(username, new Integer(1));
    }
    _crntRndPlyrTimePrdSlctn = new HashMap(_numOfPlayers);

    if(_crntRoundNum != _numOfRounds)
      _crntRoundNum++;
    return true;
  }

  public void addRoundBankRate(double rate)
  {
    _roundBankRate.put(new Integer(_crntRoundNum), new Double(rate));
  }

  public double crntBankRate()
  {
    Double bnkRt = (Double) _roundBankRate.get(new Integer(_crntRoundNum));
    return ((null != bnkRt)?bnkRt.doubleValue():0.0);
  }

  public double getPromisedPay(int slctdTmPrd)
  {
     return Util.valueToTwoDecimals(amountEachDeposited() * Math.pow(1 + promisedRateOfReturn(), slctdTmPrd));
  }

  public String getAllValuesAsString()
  {
    return getValuesAsString(null) + getCommonValuesAsString();
  }

  public String getValuesAsString(String forUsername)
  {
    StringBuffer strBfr = new StringBuffer();

    if(_mode == Constants.SQNTL && sqntlAfterPeriodBeforeRound())
    {
      HashMap amtsCllctd = new HashMap();
      Calculation.asCalculator(_crntRndPlyrTimePrdSlctn, amtsCllctd);

      HashMap wthdrwlPrdPrprtn = new HashMap(totalTimePeriod());
      HashMap wthdrwlAmtPrprtn = new HashMap(totalTimePeriod());
      HashMap prdPrmsdPay = new HashMap(totalTimePeriod());

      for(int i =1; i <= totalTimePeriod(); i++)
      {
         wthdrwlPrdPrprtn.put(new Integer(i), new Integer(0));
         wthdrwlAmtPrprtn.put(new Integer(i), new Double(0.0));
         prdPrmsdPay.put(new Integer(i), new Double(getPromisedPay(i)));
      }

      for(Iterator itr = _crntRndPlyrTimePrdSlctn.keySet().iterator(); itr.hasNext();)
      {
         String prdPlyr = (String) itr.next();
         Integer prdSlctd = (Integer)_crntRndPlyrTimePrdSlctn.get(prdPlyr);
         if(prdSlctd.intValue() != -1)
         {
            int incrmntdValue = (((Integer)wthdrwlPrdPrprtn.get(prdSlctd)).intValue()) + 1;
            wthdrwlPrdPrprtn.put(prdSlctd, new Integer(incrmntdValue));
            wthdrwlAmtPrprtn.put(prdSlctd, (Double) amtsCllctd.get(prdPlyr));
         }
      }

      switch (periodInfoLevel())
      {
        case Constants.PRD_LOW :
          break;
        case Constants.PRD_MEDIUM :
          for(Iterator itr = wthdrwlPrdPrprtn.keySet().iterator(); itr.hasNext();)
          {
            Integer prdNum = (Integer) itr.next();
            Integer numWthdrwls = (Integer) wthdrwlPrdPrprtn.get(prdNum);
            if(prdNum.intValue() <= crntSqntlTimePeriod(null))
              strBfr.append("<FONT SIZE=3 COLOR=orangered><B>" + numWthdrwls + "</B></FONT> " +
                            ((numWthdrwls.intValue() != 1) ? "players" : "player") +
                            " withdrew in period <FONT SIZE=3 COLOR=orangered><B>" +
                            prdNum.intValue() + "</B></FONT> and Promised pay for this period is " +
                            "<FONT SIZE=3 COLOR=orangered><B> $ " +
                            ((Double) prdPrmsdPay.get(prdNum)) + "<B></FONT><BR>");
          }
          break;
        case Constants.PRD_HIGH :
          for(Iterator itrtr = wthdrwlPrdPrprtn.keySet().iterator(); itrtr.hasNext();)
          {
            Integer prdNum = (Integer) itrtr.next();
            Integer numWthdrwls = (Integer) wthdrwlPrdPrprtn.get(prdNum);
            if(prdNum.intValue() < crntSqntlTimePeriod(null))
              strBfr.append("<FONT SIZE=3 COLOR=orangered><B>" + numWthdrwls + "</B></FONT> " +
                            ((numWthdrwls.intValue() != 1) ? "players" : "player") +
                            " withdrew in period <FONT SIZE=3 COLOR=orangered><B>" +
                            prdNum.intValue() + "</B></FONT>. Promised pay for this period is " +
                            "<FONT SIZE=3 COLOR=orangered><B> $ " +
                            ((Double) prdPrmsdPay.get(prdNum)) + "</B></FONT>" +
                            " and Payoff is <FONT SIZE=3 COLOR=orangered><B> $ " +
                            ((Double) wthdrwlAmtPrprtn.get(prdNum)) + "</B></FONT><BR>");
          }
      } //end of switch block
    }
    else if(_mode == Constants.SMLTNS || (_mode == Constants.SQNTL && sqntlAfterRound()))
    {
      for(Iterator itr = _playerRecords.keySet().iterator(); itr.hasNext();)
      {
	String username = (String) itr.next();
	if(null == forUsername || username.equals(forUsername))
	{
	  PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
          if(null == forUsername)
          {
	    strBfr.append("\n<BR><B>username=<FONT SIZE=4 COLOR=maroon>" + username);
	    strBfr.append("</FONT> &nbsp&nbsp and his Values are as follows ...</B><BR>");
          }

          for(int rndNum = 1; rndNum < currentRoundNum()
	      || (currentRoundNum() == totalNumOfRounds() && rndNum == totalNumOfRounds() && allPlyrsInputGiven(totalNumOfRounds())); rndNum++)
//          for(int rndNum = currentRoundNum()-1; (currentRoundNum() == totalNumOfRounds() && rndNum == totalNumOfRounds() && allPlyrsInputGiven(totalNumOfRounds()))
//          || (rndNum < totalNumOfRounds() && rndNum > 0); rndNum--)
	  {
	    strBfr.append(" <BR><B>Round number=<FONT SIZE=3 COLOR=orangered>" + rndNum + "</FONT>");
	    double roundPromisedPay = Util.valueToTwoDecimals(amountEachDeposited() * Math.pow(1 + promisedRateOfReturn(), plyrRcrd.roundPeriodSelection(rndNum)));

	    switch (roundInfoLevel())
	    {
	        case Constants.RND_LOW :
                  strBfr.append(" You selected period <FONT SIZE=3 COLOR=orangered>" + plyrRcrd.roundPeriodSelection(rndNum) + "</FONT>,&nbsp;&nbsp;" +
                                " Your promised pay off was <FONT SIZE=3 COLOR=orangered> $ " + roundPromisedPay + "</FONT>,&nbsp;&nbsp;" +
                                " Your payoff is <FONT SIZE=3 COLOR=orangered> $ " + plyrRcrd.roundAmount(rndNum) + "</FONT>");

                  break;
	        case Constants.RND_MEDIUM :
		  strBfr.append(" You selected period <FONT SIZE=3 COLOR=orangered>" + plyrRcrd.roundPeriodSelection(rndNum) + "</FONT>,&nbsp;&nbsp;" +
                                " Your promised pay off was <FONT SIZE=3 COLOR=orangered> $ " + roundPromisedPay + "</FONT>,&nbsp;&nbsp;" +
                                " Your payoff is <FONT SIZE=3 COLOR=orangered> $ " + plyrRcrd.roundAmount(rndNum) + "</FONT>,&nbsp;&nbsp;" +
				" The rate of return in your bank was <FONT SIZE=3 COLOR=orangered>" + getRndBankRate(rndNum) + "</FONT>");
                  break;
	        case Constants.RND_HIGH :
		  strBfr.append(" You selected period <FONT SIZE=3 COLOR=orangered>" + plyrRcrd.roundPeriodSelection(rndNum) + "</FONT>,&nbsp;&nbsp;" +
                                " Your promised pay off was <FONT SIZE=3 COLOR=orangered> $ " + roundPromisedPay + "</FONT>,&nbsp;&nbsp;" +
                                " Your payoff is <FONT SIZE=3 COLOR=orangered> $ " + plyrRcrd.roundAmount(rndNum) + "</FONT>,&nbsp;&nbsp;" +
				" The rate of return in your bank was <FONT SIZE=3 COLOR=orangered>" + getRndBankRate(rndNum) + "</FONT>,&nbsp;&nbsp;" +
				" <BR>Players withdrew their money as follows :<FONT SIZE=3 COLOR=orangered>" + getPrdSlctnPrptns(rndNum) +
                                "</FONT><BR>");

	    } // switch block
	    strBfr.append("</B><BR>");
          } // round number iteration
	} // if username check
      } // player records iterator
    } // player records not null
    return strBfr.toString();
  }

  public double getRndBankRate(int rn)
  {
    for(Iterator itr = _roundBankRate.keySet().iterator(); itr.hasNext();)
    {
      Integer rndNum = (Integer) itr.next();
      if(rndNum.intValue() == rn)
      {
        Double bnkRt = (Double) _roundBankRate.get(rndNum);
        if(bnkRt.doubleValue() > 0.0) // do not test != for double values
	  return Util.valueToThreeDecimals(bnkRt.doubleValue());
      }
    }
    return 0.0;
  }

  public HashMap getPrmsdPayByPrds()
  {
    HashMap prmsdPayByPrds = new HashMap(totalTimePeriod());
    for(int i = 1; i <= totalTimePeriod(); i++)
    {
      double prdPrmsdPay =
              Util.valueToTwoDecimals(amountEachDeposited() * Math.pow(1 + promisedRateOfReturn(), i));
      prmsdPayByPrds.put("Period" + i, new Double(prdPrmsdPay));
    }
    return (HashMap) prmsdPayByPrds.clone();
  }

  public HashMap getPayByPrds(int forRndNum)
  {
    HashMap payByPrds = new HashMap(totalTimePeriod());

    if(null == _playerRecords || _playerRecords.isEmpty())
        return (HashMap) payByPrds.clone();

    for(int i = 1; i <= totalTimePeriod(); i++)
    {
      boolean prdValueSet = false;
      for(Iterator itr = _playerRecords.keySet().iterator(); itr.hasNext();)
      {
	PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get((String) itr.next());
	Integer usrPrd = new Integer(plyrRcrd.roundPeriodSelection(forRndNum));
        if(usrPrd.intValue() == i)
        {
          double amt = plyrRcrd.roundAmount(forRndNum);
          payByPrds.put("Period" + i, new Double(amt));
          prdValueSet = true;
        }
      }
      if(!prdValueSet)
        payByPrds.put("Period" + i, new Double(0.0));
    }
    return (HashMap) payByPrds.clone();
  }

  private String getPrdSlctnPrptns(int rndNum)
  {
    return getPrdSlctnPrptns(rndNum, Constants.RPRT_NONE);
  }

  private String getPrdSlctnPrptns(int rndNum, int rtrnType)
  {
    StringBuffer strBfr = new StringBuffer();
    HashMap prdSlctnPrptns = new HashMap(totalTimePeriod());
    for(int i = 1; i <= totalTimePeriod(); i++)
    {
      prdSlctnPrptns.put("Period" + i, new Integer(0));
    }

    if(null != _playerRecords)
    {
      for(Iterator itr = _playerRecords.keySet().iterator(); itr.hasNext();)
      {
	PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get((String) itr.next());
	Integer usrPrd = new Integer(plyrRcrd.roundPeriodSelection(rndNum));
	Integer valueToIncrmnt = (Integer) prdSlctnPrptns.get("Period" + usrPrd);
	prdSlctnPrptns.put("Period" + usrPrd, new Integer(valueToIncrmnt.intValue() + 1));
      }
    }

    HashMap prmsdPayByPrds = getPrmsdPayByPrds();
    HashMap payByPrds = getPayByPrds(rndNum);

    SortedMap srtdPrprtns = new TreeMap(prdSlctnPrptns);
    if(Constants.RPRT_EXL == rtrnType)
      strBfr.append("\n\tRound\tPeriod\tWithdrawnPlayers\tPromised Pay\tPay");
    for(Iterator itr = srtdPrprtns.keySet().iterator(); itr.hasNext();)
    {
      String prd = (String) itr.next();
      int noOfPlyrs = ((Integer)prdSlctnPrptns.get(prd)).intValue();
      String plyrStr = (noOfPlyrs != 1) ? "players" : "player ";

      if(Constants.RPRT_TXT == rtrnType)
        strBfr.append("\n" + "Round" + rndNum + " " + prd + " = " + noOfPlyrs + " " + plyrStr + "  " +
                      "( Promised Pay off : $ " + prmsdPayByPrds.get(prd) + " and Pay off is : $ " +
                      payByPrds.get(prd) + " )");
      else if(Constants.RPRT_EXL == rtrnType)
        strBfr.append("\n\t" + rndNum + "\t" + prd + "\t" + noOfPlyrs + "\t" + prmsdPayByPrds.get(prd) +
                      "\t" + payByPrds.get(prd));
      else
        strBfr.append("<BR>" + prd + " = " + noOfPlyrs + "&nbsp;" + plyrStr + "&nbsp;&nbsp;" +
                      "( Promised Pay off : $ " + prmsdPayByPrds.get(prd) + " and Pay off is : $ " +
                      payByPrds.get(prd) + " )");
    }

    return strBfr.toString();
  }

  public double getFinalAmount(String forUsername)
  {
    if(null != _playerRecords)
    {
      for(Iterator itr = _playerRecords.keySet().iterator(); itr.hasNext();)
      {
	String username = (String) itr.next();
	if(null == forUsername || username.equals(forUsername))
	{
	  PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
	  return Util.valueToTwoDecimals(plyrRcrd.crntTotalAmount());
	}
      }
    }
    return 0.0;
  }

  public String getCommonValuesAsString()
  {
    StringBuffer strBfr = new StringBuffer();

    strBfr.append("\n<BR> Number of Players = " + _numOfPlayers);
    strBfr.append("\n<BR> Deposit from each player = " + _eachDeposit);
    strBfr.append("\n<BR> Mean Rate of Return (r*) = " + _economyRate);
    strBfr.append("\n<BR> Bank Promised Rate (r') = " + _promisedRate);
    strBfr.append("\n<BR> Number of Banks = " + _numOfBanks);
    strBfr.append("\n<BR> Total Time Periods = " + _timePeriod);
    strBfr.append("\n<BR> Number of Rounds = " + _numOfRounds);
    strBfr.append("\n<BR> Conversion Rate = " + _conversionRate);
    strBfr.append("\n<BR> Insurance Rate = " + _insuranceRate);
    strBfr.append("\n<BR> Show up Fee = " + _showUpFee);
    strBfr.append("\n<BR> Mode of Play = " + getMode(_mode));
    strBfr.append("\n<BR> Round Report Level = " + getRndReportLvl(_rndRprtLvl));
    strBfr.append("\n<BR> Period Report Level = " + getPrdReportLvl(_prdRprtLvl));
    strBfr.append("\n<BR> Calculator Type = " + calculatorType());
    strBfr.append("\n<BR> Report Saved As = " + reportType());
    strBfr.append("\n<BR> Instruction File = " + _instrctnFile);

    return strBfr.toString();
  }

  public synchronized boolean SaveAsFile()
  {
    Date dt = new Date();
    String strDt = dt.toString();

    // directory structure as C:\iPlanet\Servers\..\config\GameReports\<ServerNumber>\<Month_Year>\
    String drctrys = Constants.REPORTS_LCTN + File.separator + ServerProperties.getServerNum() + File.separator + strDt.substring(4, 7) +
                     "_" + strDt.substring(24) + File.separator;
    File drctry = new File(drctrys);
    if(!drctry.exists())
      drctry.mkdirs();

    String drctryPath = drctry.getAbsolutePath();

    // <directory-struct>\Report_<Day_HourMinuteSecond>
    String fileName =  drctryPath + File.separator + "ReportOn_" + strDt.substring(8, 10) + "_" + strDt.substring(11, 13) +
                       strDt.substring(14, 16) + strDt.substring(17, 19);


    if(_rprtType == Constants.RPRT_TXT)
      return writeToTextFile(fileName + ".data");
    else if(_rprtType == Constants.RPRT_EXL)
      return writeToXlsFile(fileName + ".xls");
    else if(_rprtType == Constants.RPRT_BOTH)
      return (writeToTextFile(fileName + ".data") && writeToXlsFile(fileName + ".xls"));
    else //(_rprtType == Constants.RPRT_NONE)
      return false; // it shouldn't come here
  }

  public synchronized boolean writeToFile()
  {
      File rulesFileDrctrs = new File(Constants.ADMIN_FILE_LCTN);
      if(!rulesFileDrctrs.exists())
          rulesFileDrctrs.mkdirs();

    File rulesFile = new File(Constants.ADMIN_FILENAME);
//    if(rulesFile.exists())
//      rulesFile.delete();

    synchronized(rulesFile)
    {
    try
    {
//      if(!rulesFile.createNewFile())
//        return false;
      if(!rulesFile.exists())
        rulesFile.createNewFile();

      StringBuffer strBfr = new StringBuffer();
      strBfr.append(Constants.ARG_NUM_OF_PLAYERS + "=" + _numOfPlayers);
      strBfr.append("\n" + Constants.ARG_EACH_DEPOSIT + "=" + _eachDeposit);
      strBfr.append("\n" + Constants.ARG_ECONOMY_RATE + "=" + _economyRate);
      strBfr.append("\n" + Constants.ARG_PROMISED_RATE + "=" + _promisedRate);
      strBfr.append("\n" + Constants.ARG_NUM_OF_BANKS + "=" + _numOfBanks);
      strBfr.append("\n" + Constants.ARG_TIME_PERIOD + "=" + _timePeriod);
      strBfr.append("\n" + Constants.ARG_NUM_OF_ROUNDS + "=" + _numOfRounds);
      strBfr.append("\n" + Constants.ARG_CONVERSION_RATE + "=" + _conversionRate);
      strBfr.append("\n" + Constants.ARG_INSURANCE_RATE + "=" + _insuranceRate);
      strBfr.append("\n" + Constants.ARG_SHOW_UP_FEE + "=" + _showUpFee);
      strBfr.append("\n" + Constants.ARG_MODE + "=" + _mode);
      strBfr.append("\n" + Constants.ARG_RND_RPRT_LVL + "=" + _rndRprtLvl);
      strBfr.append("\n" + Constants.ARG_PRD_RPRT_LVL + "=" + _prdRprtLvl);
      strBfr.append("\n" + Constants.ARG_CLCLTR_CHOICE + "=" + _clcltrChoice);
      strBfr.append("\n" + Constants.ARG_RPRT_TYPE + "=" + _rprtType);
      strBfr.append("\n" + Constants.ARG_INSTRCTN_FILE + "=" + _instrctnFile);
      strBfr.append("\n" + Constants.ARG_CRNT_ROUND_NUM + "=" + _crntRoundNum);

      for(Iterator itrtr = _roundBankRate.keySet().iterator(); itrtr.hasNext();)
      {
	Integer roundNum = (Integer) itrtr.next();
	Double bankRate = (Double) _roundBankRate.get(roundNum);
	strBfr.append("\n" + roundNum + "=" + bankRate);
      }

      // player record created before any rounds start
      for(Iterator itrtr = _playerRecords.keySet().iterator(); itrtr.hasNext();)
      {
	String username = (String) itrtr.next();
	Integer crntUsrTmPrd = (Integer) _crntSqntlTimePeriod.get(username);
	String strUserValues = crntUsrTmPrd + "&";
	Integer crntInputNum = (Integer) _userInputNum.get(username);
	strUserValues = strUserValues + crntInputNum + "*";
	Integer plyrTimePrdSlctn = (Integer) _crntRndPlyrTimePrdSlctn.get(username);
	if(null == plyrTimePrdSlctn)
	  plyrTimePrdSlctn = new Integer(-1);
	strUserValues = strUserValues + plyrTimePrdSlctn + "#";
	PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
	strUserValues = strUserValues + plyrRcrd.toFileAsString();
	strBfr.append("\n" + username + "=" + strUserValues);
      }
      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(rulesFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();
      return true;
    }
    catch(IOException ioe)
    {
      return false;
    }
    }// synchronized
  }

  public synchronized boolean restart()
  {
    File rulesFile = new File(Constants.ADMIN_FILENAME);
    if(rulesFile.exists())
      rulesFile.delete();

    try
    {
      if(!rulesFile.createNewFile())
        return false;

      StringBuffer strBfr = new StringBuffer();
      strBfr.append(Constants.ARG_NUM_OF_PLAYERS + "=" + _numOfPlayers);
      strBfr.append("\n" + Constants.ARG_EACH_DEPOSIT + "=" + _eachDeposit);
      strBfr.append("\n" + Constants.ARG_ECONOMY_RATE + "=" + _economyRate);
      strBfr.append("\n" + Constants.ARG_PROMISED_RATE + "=" + _promisedRate);
      strBfr.append("\n" + Constants.ARG_NUM_OF_BANKS + "=" + _numOfBanks);
      strBfr.append("\n" + Constants.ARG_TIME_PERIOD + "=" + _timePeriod);
      strBfr.append("\n" + Constants.ARG_NUM_OF_ROUNDS + "=" + _numOfRounds);
      strBfr.append("\n" + Constants.ARG_CONVERSION_RATE + "=" + _conversionRate);
      strBfr.append("\n" + Constants.ARG_INSURANCE_RATE + "=" + _insuranceRate);
      strBfr.append("\n" + Constants.ARG_SHOW_UP_FEE + "=" + _showUpFee);
      strBfr.append("\n" + Constants.ARG_MODE + "=" + _mode);
      strBfr.append("\n" + Constants.ARG_RND_RPRT_LVL + "=" + _rndRprtLvl);
      strBfr.append("\n" + Constants.ARG_PRD_RPRT_LVL + "=" + _prdRprtLvl);
      strBfr.append("\n" + Constants.ARG_CLCLTR_CHOICE + "=" + _clcltrChoice);
      strBfr.append("\n" + Constants.ARG_RPRT_TYPE + "=" + _rprtType);
      strBfr.append("\n" + Constants.ARG_INSTRCTN_FILE + "=" + _instrctnFile);
      strBfr.append("\n" + Constants.ARG_CRNT_ROUND_NUM + "=0");
      for(int itr = 1; itr <= _numOfRounds; itr++)
      {
        _roundBankRate.put(new Integer(itr), new Double(0.0));
      }
      for(Iterator itrtr = _roundBankRate.keySet().iterator(); itrtr.hasNext();)
      {
	Integer roundNum = (Integer) itrtr.next();
	Double bankRate = (Double) _roundBankRate.get(roundNum);
	strBfr.append("\n" + roundNum + "=" + bankRate);
      }

      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(rulesFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();

      return true;
    }
    catch(IOException ioe)
    {
      ioe.printStackTrace();
      return false;
    }
  }

  public synchronized boolean writeToTextFile(String fileName)
  {
    File rprtTxtFile = new File(fileName);
//    if(rprtTxtFile.exists())
//      rprtTxtFile.delete();

    try
    {
      if(!rprtTxtFile.createNewFile())
        return false;

      StringBuffer strBfr = new StringBuffer();
      Date crntDate = new Date();
      strBfr.append("\n " + crntDate + "\n");
      strBfr.append("\n Number of Players = " + _numOfPlayers);
      strBfr.append("\n Deposit from Each Player = " + _eachDeposit);
      strBfr.append("\n Mean Rate of Return (r*) = " + _economyRate);
      strBfr.append("\n Bank Promised Rate (r') = " + _promisedRate);
      strBfr.append("\n Number of Banks = " + _numOfBanks);
      strBfr.append("\n Total Time Periods = " + _timePeriod);
      strBfr.append("\n Number of Rounds = " + _numOfRounds);
      strBfr.append("\n Conversion Rate = " + _conversionRate);
      strBfr.append("\n Insurance Rate = " + _insuranceRate);
      strBfr.append("\n Show up Fee = " + _showUpFee);
      strBfr.append("\n Mode of play = " + getMode(_mode));
      strBfr.append("\n Round Report Level = " + getRndReportLvl(_rndRprtLvl));
      strBfr.append("\n Period Report Level = " + getPrdReportLvl(_prdRprtLvl));
      strBfr.append("\n Type of Calculator = " + calculatorType());
      strBfr.append("\n Report Saved As = " + reportType());
      strBfr.append("\n Instruction Filename = " + _instrctnFile);
      strBfr.append("\n");
      for(Iterator itrtr = _roundBankRate.keySet().iterator(); itrtr.hasNext();)
      {
	Integer roundNum = (Integer) itrtr.next();
	Double bankRate = (Double) _roundBankRate.get(roundNum);
	strBfr.append("\n Round" + roundNum + ", Bank Rate = " + bankRate);
      }

      // player record created before any rounds start
      for(Iterator itrtr = _playerRecords.keySet().iterator(); itrtr.hasNext();)
      {
	String username = (String) itrtr.next();
	PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
	strBfr.append("\n\n " + username.toUpperCase() + " : " + plyrRcrd.toSaveFileAsString());
      }

      strBfr.append("\n");
      for(int rndNum = 1; rndNum <= totalNumOfRounds(); rndNum++)
      {
       strBfr.append("\n");
       strBfr.append(getPrdSlctnPrptns(rndNum, Constants.RPRT_TXT));
      }

      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(rprtTxtFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();
      return true;
    }
    catch(IOException ioe)
    {
      ioe.printStackTrace();
      return false;
    }
  }


  public synchronized boolean writeToXlsFile(String fileName)
  {
    File rprtXlsFile = new File(fileName);
//    if(rprtXlsFile.exists())
//      rprtXlsFile.delete();

    try
    {
      if(!rprtXlsFile.createNewFile())
        return false;

      StringBuffer strBfr = new StringBuffer();
      Date crntDate = new Date();
      strBfr.append("\n" + crntDate + "\n");

      // Column Headings
      strBfr.append("\nPlayer \t round number \t time period selected \t amount collected");
      // player record created before any rounds start
      for(Iterator itrtr = _playerRecords.keySet().iterator(); itrtr.hasNext();)
      {
	String username = (String) itrtr.next();
	PlayerRecord plyrRcrd = (PlayerRecord) _playerRecords.get(username);
	strBfr.append(plyrRcrd.toSaveToXlsFile(username));
      }

      strBfr.append("\n Other Details of the Game are as follows:\n");
      strBfr.append("\nround number \t Bank Rate");
      for(Iterator itrtr = _roundBankRate.keySet().iterator(); itrtr.hasNext();)
      {
	Integer roundNum = (Integer) itrtr.next();
	Double bankRate = (Double) _roundBankRate.get(roundNum);
	strBfr.append("\n" + roundNum + "\t" + bankRate);
      }

      strBfr.append("\n");
      for(int rndNum = 1; rndNum <= totalNumOfRounds(); rndNum++)
      {
       strBfr.append("\n");
       strBfr.append(getPrdSlctnPrptns(rndNum, Constants.RPRT_EXL));
      }

      strBfr.append("\n");
      strBfr.append("\n Number of Players = " + _numOfPlayers);
      strBfr.append("\n Deposit from Each Player = " + _eachDeposit);
      strBfr.append("\n Mean Rate of Return (r*) = " + _economyRate);
      strBfr.append("\n Bank Promised Rate (r') = " + _promisedRate);
      strBfr.append("\n Number of Banks = " + _numOfBanks);
      strBfr.append("\n Total Time Periods = " + _timePeriod);
      strBfr.append("\n Number of Rounds = " + _numOfRounds);
      strBfr.append("\n Conversion Rate = " + _conversionRate);
      strBfr.append("\n Insurance Rate = " + _insuranceRate);
      strBfr.append("\n Show up Fee = " + _showUpFee);
      strBfr.append("\n Mode of play = " + getMode(_mode));
      strBfr.append("\n Round Report Level = " + getRndReportLvl(_rndRprtLvl));
      strBfr.append("\n Period Report Level = " + getPrdReportLvl(_prdRprtLvl));
      strBfr.append("\n Type of Calculator = " + calculatorType());
      strBfr.append("\n Report Saved As = " + reportType());
      strBfr.append("\n Instruction Filename = " + _instrctnFile);
      strBfr.append("\n");

      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(rprtXlsFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();
      return true;
    }
    catch(IOException ioe)
    {
      return false;
    }
  }

  public synchronized static DataHandler getDataHandlerInstance()
  {
    // initializing all values to defaults before copying from persistent storage
    int numOfPlayers = 0;
    double eachDeposit = 0.0;
    double ecnmyRate = 0.0;
    double prmsdRate = 0.0;
    int numOfBanks = 5; // default for now
    int timePeriod = 0;
    int numOfRounds = 0;
    double conversionRate = 0.0;
    double insuranceRate = 0.0;
    double showUpFee = 0.0;
    int mode = Constants.SMLTNS;
    int rndRprtLvl = Constants.RND_LOW;
    int prdRprtLvl = Constants.PRD_LOW;
    int clcltrCh = Constants.CLCLTR_BY_AVG;
    int rprtType = Constants.RPRT_NONE;
    String instrctnFl = Constants.BLANK_STRING; // to get over parsing. cannot leave empty.
    int crntRoundNum = 0;
    HashMap crntSqntlTimePeriod = new HashMap();
    HashMap userInputNum = new HashMap();
    HashMap crntRndPlyrTimePrdSlctn = new HashMap();
    HashMap playerRecords = new HashMap();
    HashMap roundBankRate = new HashMap();

    File adminFile = new File(Constants.ADMIN_FILENAME);
    synchronized(adminFile)
    {
      try
      {
	if(adminFile.exists())
	{
	  int lineCntr = 0;
	  BufferedReader fileRdrLineCntr = new BufferedReader(new FileReader(adminFile));
	  String lineFromFile = fileRdrLineCntr.readLine();
	  while(null != lineFromFile)
	  {
	    lineCntr++;
	    lineFromFile = fileRdrLineCntr.readLine();
	  }
	  fileRdrLineCntr.close();

	  int numOfValues = 2 * lineCntr;
	  String valueFromFile[] = new String[numOfValues];
	  BufferedReader fileRdr = new BufferedReader(new FileReader(adminFile));
	  String strFromFile = fileRdr.readLine();
	  int cntr =0;
	  while(null != strFromFile)
	  {
	    StringTokenizer strTknzr = new StringTokenizer(strFromFile, "=");
	    while(strTknzr.hasMoreTokens())
	    {
	      valueFromFile[cntr] = strTknzr.nextToken();
	      cntr++;
	    }
	    strFromFile = fileRdr.readLine();
	  }
	  fileRdr.close();

	  if(cntr > 0)
	  {
        numOfPlayers = Integer.parseInt(valueFromFile[1]);
	    eachDeposit = Double.parseDouble(valueFromFile[3]);
	    ecnmyRate = Double.parseDouble(valueFromFile[5]);
	    prmsdRate = Double.parseDouble(valueFromFile[7]);
	    numOfBanks = Integer.parseInt(valueFromFile[9]);
	    timePeriod = Integer.parseInt(valueFromFile[11]);
	    numOfRounds = Integer.parseInt(valueFromFile[13]);
	    conversionRate = Double.parseDouble(valueFromFile[15]);
	    insuranceRate = Double.parseDouble(valueFromFile[17]);
	    showUpFee = Double.parseDouble(valueFromFile[19]);
	    mode = Integer.parseInt(valueFromFile[21]);
	    rndRprtLvl = Integer.parseInt(valueFromFile[23]);
	    prdRprtLvl = Integer.parseInt(valueFromFile[25]);
	    clcltrCh = Integer.parseInt(valueFromFile[27]);
        rprtType = Integer.parseInt(valueFromFile[29]);
	    instrctnFl = valueFromFile[31];
	    crntRoundNum = Integer.parseInt(valueFromFile[33]);


	    int crntArrayIndex = 33;
	    //above values take 17 lines and bankrates are for numOfRounds
	    int numOfLinesRead = numOfRounds + 17;

	    for(int itr = 0; itr < numOfRounds; itr++)
	    {
	      String rndNum = valueFromFile[++crntArrayIndex];
	      String bnkRt = valueFromFile[++crntArrayIndex];
	      roundBankRate.put(new Integer(rndNum), new Double(bnkRt));
	    }

	    for(int numOfPlyrRcrdsLeft = lineCntr - numOfLinesRead; numOfPlyrRcrdsLeft != 0; numOfPlyrRcrdsLeft--)
	    {
	      String username = valueFromFile[++crntArrayIndex];
	      String strToTknz = valueFromFile[++crntArrayIndex];
	      StringTokenizer strTknzr99 = new StringTokenizer(strToTknz, "&");
	      String tmpStr = strTknzr99.nextToken();
    	      Integer crntTmPrd = new Integer(1);
	      if(!tmpStr.equals("null"))
	        crntTmPrd = Integer.valueOf(tmpStr);
	      crntSqntlTimePeriod.put(username, crntTmPrd);
   	      if(strTknzr99.hasMoreTokens())
	      {
	        String rmngStr99 = strTknzr99.nextToken();
	        StringTokenizer strTknzr0 = new StringTokenizer(rmngStr99, "*");
	        Integer crntInputNum = Integer.valueOf(strTknzr0.nextToken());
	        userInputNum.put(username, crntInputNum);
	        String rmngStr0 = strTknzr0.nextToken();
	        StringTokenizer strTknzr1 = new StringTokenizer(rmngStr0, "#");
	        Integer crntTmPrdSlctn = Integer.valueOf(strTknzr1.nextToken());
	        crntRndPlyrTimePrdSlctn.put(username, crntTmPrdSlctn);
	        PlayerRecord plyrRcrd = new PlayerRecord(numOfRounds);
	        if(strTknzr1.hasMoreTokens())
	        {
	          String rmngStr1 = strTknzr1.nextToken();
	          StringTokenizer strTknzr2 = new StringTokenizer(rmngStr1, "$");
	          while(strTknzr2.hasMoreTokens())
	          {
	            String eachPlyrRcrd = strTknzr2.nextToken();
	            StringTokenizer strTknzr3 = new StringTokenizer(eachPlyrRcrd, "%");
	            int roundNum = Integer.parseInt(strTknzr3.nextToken());
	            StringTokenizer strTknzr4 = new StringTokenizer(strTknzr3.nextToken(), "@");
	            int tmPrdSlctn = Integer.parseInt(strTknzr4.nextToken());
	            double amt = Double.parseDouble(strTknzr4.nextToken());
                    synchronized(plyrRcrd)
                    {
	              plyrRcrd.update(roundNum, tmPrdSlctn, amt);
                    }
	          }
	        }
                synchronized(plyrRcrd)
                {
	          playerRecords.put(username, plyrRcrd);
                }
	      }
	    }
	  } // cntr > 0
	}
      }
      catch(IOException ioe)
      {
      }
    }
    return new DataHandler(numOfPlayers, eachDeposit, ecnmyRate, prmsdRate, numOfBanks, timePeriod, numOfRounds, conversionRate,
                           insuranceRate, showUpFee, mode, rndRprtLvl, prdRprtLvl, clcltrCh, rprtType, instrctnFl, crntRoundNum,
                           crntSqntlTimePeriod, userInputNum, crntRndPlyrTimePrdSlctn, playerRecords, roundBankRate);
    }
  }