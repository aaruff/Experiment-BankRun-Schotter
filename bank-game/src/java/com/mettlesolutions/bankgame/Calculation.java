/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame;

import com.mettlesolutions.bankgame.util.Util;

import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


public class Calculation
{
    public static void asAvgCalculator(HashMap plyrWithdrawlTimes, HashMap amtsCllctd)
    {
      double rp = DataHandler.getDataHandlerInstance().economyRateOfReturn();

      int mapSize = plyrWithdrawlTimes.size();
      HashMap amtsCllctdBnk1 = new HashMap(mapSize);
      HashMap amtsCllctdBnk2 = new HashMap(mapSize);
      HashMap amtsCllctdBnk3 = new HashMap(mapSize);
      HashMap amtsCllctdBnk4 = new HashMap(mapSize);
      HashMap amtsCllctdBnk5 = new HashMap(mapSize);
      asCalculator(plyrWithdrawlTimes, amtsCllctdBnk1, (1.0 / 3.0) * rp);
      asCalculator(plyrWithdrawlTimes, amtsCllctdBnk2, (2.0 / 3.0) * rp);
      asCalculator(plyrWithdrawlTimes, amtsCllctdBnk3, (3.0 / 3.0) * rp);
      asCalculator(plyrWithdrawlTimes, amtsCllctdBnk4, (4.0 / 3.0) * rp);
      asCalculator(plyrWithdrawlTimes, amtsCllctdBnk5, (5.0 / 3.0) * rp);

      // for player username iteration
      for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
      {
        String usrnm = (String) itr.next();
        // values are already rounded to two decimals
        double amtBnk1 = ((Double)amtsCllctdBnk1.get(usrnm)).doubleValue();
        double amtBnk2 = ((Double)amtsCllctdBnk2.get(usrnm)).doubleValue();
        double amtBnk3 = ((Double)amtsCllctdBnk3.get(usrnm)).doubleValue();
        double amtBnk4 = ((Double)amtsCllctdBnk4.get(usrnm)).doubleValue();
        double amtBnk5 = ((Double)amtsCllctdBnk5.get(usrnm)).doubleValue();

        double avgAmt = Util.valueToTwoDecimals((amtBnk1 + amtBnk2 + amtBnk3 + amtBnk4 + amtBnk5) / 5.0);

        amtsCllctd.put(usrnm, new Double(avgAmt));
      }
    }

    public static void asCalculator(HashMap plyrWithdrawlTimes, HashMap amtsCllctd)
    {
      double bankRate = DataHandler.getDataHandlerInstance().crntBankRate();
      asCalculator(plyrWithdrawlTimes, amtsCllctd, bankRate);
    }

    public static void asCalculator(HashMap plyrWithdrawlTimes, HashMap amtsCllctd, double bankRate)
    {
      DataHandler clcltrData = DataHandler.getDataHandlerInstance();

      int numOfPlayers = clcltrData.totalNumOfPlayers();
      double eachDeposit = clcltrData.amountEachDeposited();
      int timePeriod = clcltrData.totalTimePeriod();
      double promisedRate = clcltrData.promisedRateOfReturn();
      double insuranceRate = clcltrData.insuranceRate();

      Vector wthdrwlTimePrprtns = new Vector(timePeriod);
      for(int tp = 1; tp <= timePeriod; tp++)
      {
	int cntr = 0;
	for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
	{
	  String username = (String) itr.next();
	  if(((Integer) plyrWithdrawlTimes.get(username)).intValue() == tp)
	    cntr++;
	}
	wthdrwlTimePrprtns.add(new Integer(cntr));
      }

      Vector bankAssetsByTime = new Vector(timePeriod);
      Vector paymentByTime = new Vector(timePeriod);

      for(int t = 1; t <= timePeriod; t++)
      {
	double initialVal = numOfPlayers * Math.pow((1.0 + bankRate), t);
	double diffrnce = 0.0;
	for(int itr = 1; itr < t; itr++)
	{
	  int wthdrwlPrprtn = ((Integer) wthdrwlTimePrprtns.get(itr - 1)).intValue();
	  diffrnce += wthdrwlPrprtn * Math.pow(1.0 + promisedRate, itr) * Math.pow(1.0 + bankRate, t - itr);
	}
	double thisBankAsset = ((initialVal - diffrnce) > 0)?(initialVal - diffrnce):0.00;
	bankAssetsByTime.add(new Double(thisBankAsset));
	int thisWthdrwlPrprtn = ((Integer) wthdrwlTimePrprtns.get(t-1)).intValue();
	double case1 = Math.pow(1.0 + promisedRate, t);
	double case2 = thisBankAsset / thisWthdrwlPrprtn;
	double insrncAmt = insuranceRate * Math.pow(1.0 + promisedRate, t);
	paymentByTime.add(new Double(eachDeposit * Math.max(Math.min(case1, case2), insrncAmt)));
      }

      for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
      {
	String usr = (String) itr.next();
	int wthdrwlTm = ((Integer) plyrWithdrawlTimes.get(usr)).intValue();
        Double value = new Double(0.0);
        if(wthdrwlTm > 0)
          value = new Double(Util.valueToTwoDecimals(((Double) paymentByTime.get(wthdrwlTm - 1)).doubleValue()));
        amtsCllctd.put(usr, value);
      }
    }


    public synchronized static void doCalculation(DataHandler dataHndlr)
    {
      int numOfPlayers = dataHndlr.totalNumOfPlayers();
      double eachDeposit = dataHndlr.amountEachDeposited();
      double economyRate = dataHndlr.economyRateOfReturn();
      double promisedRate = dataHndlr.promisedRateOfReturn();
      int numOfBanks = dataHndlr.totalNumOfBanks();
      int timePeriod = dataHndlr.totalTimePeriod();
      int numOfRounds = dataHndlr.totalNumOfRounds();
      double conversionRate = dataHndlr.conversionRate();
      double insuranceRate = dataHndlr.insuranceRate();
      int mode = dataHndlr.modeOfPlay();

      double bankRate = dataHndlr.crntBankRate();

      HashMap plyrWithdrawlTimes = dataHndlr.getCrntRndPlyrTimePrdSlctn();

      Vector wthdrwlTimePrprtns = new Vector(timePeriod);
      for(int tp = 1; tp <= timePeriod; tp++)
      {
	int cntr = 0;
	for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
	{
	  String username = (String) itr.next();
	  if(((Integer) plyrWithdrawlTimes.get(username)).intValue() == tp)
	    cntr++;
	}
	wthdrwlTimePrprtns.add(new Integer(cntr));
      }

      Vector bankAssetsByTime = new Vector(timePeriod);
      Vector paymentByTime = new Vector(timePeriod);

      for(int t = 1; t <= timePeriod; t++)
      {
	double initialVal = numOfPlayers * Math.pow((1.0 + bankRate), t);
	double diffrnce = 0.0;
	for(int itr = 1; itr < t; itr++)
	{
	  int wthdrwlPrprtn = ((Integer) wthdrwlTimePrprtns.get(itr - 1)).intValue();
	  diffrnce += wthdrwlPrprtn * Math.pow(1.0 + promisedRate, itr) * Math.pow(1.0 + bankRate, t - itr);
	}
	double thisBankAsset = ((initialVal - diffrnce) > 0)?(initialVal - diffrnce):0.00;
	bankAssetsByTime.add(new Double(thisBankAsset));
	int thisWthdrwlPrprtn = ((Integer) wthdrwlTimePrprtns.get(t-1)).intValue();
	double case1 = Math.pow(1.0 + promisedRate, t);
	double case2 = thisBankAsset / thisWthdrwlPrprtn;
	double insrncAmt = insuranceRate * Math.pow(1.0 + promisedRate, t);
	paymentByTime.add(new Double(eachDeposit * Math.max(Math.min(case1, case2), insrncAmt)));
      }

      HashMap paymentByPlayer = new HashMap(numOfPlayers);
      for(Iterator itr = plyrWithdrawlTimes.keySet().iterator(); itr.hasNext();)
      {
	String username = (String) itr.next();
	Integer wthdrwlTm = (Integer) plyrWithdrawlTimes.get(username);
	double amtForPlayer = ((Double) paymentByTime.get(wthdrwlTm.intValue() - 1)).doubleValue();
	paymentByPlayer.put(username, new Double(Util.valueToTwoDecimals(amtForPlayer)));
      }

      synchronized(dataHndlr)
      {
        dataHndlr.updatePlayerRecords(paymentByPlayer);
      }
    }

}