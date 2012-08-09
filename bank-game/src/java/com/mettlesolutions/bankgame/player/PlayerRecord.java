/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.player;

import java.util.Vector;

public class PlayerRecord
{
    private Vector _resultByRounds;

    public PlayerRecord(int numOfRounds)
    {
      _resultByRounds = new Vector(numOfRounds);
    }

    class RoundResult
    {
      private int _roundNumber;
      private int _slctdTimePeriod;
      private double _amountCollected;

      public RoundResult(int rnd, int tp, double amt)
      {
	_roundNumber = rnd;
	_slctdTimePeriod = tp;
	_amountCollected = amt;
      }
    }

    public synchronized boolean roundsComplete(int totalRounds)
    {
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
	if(rndRslt._roundNumber == totalRounds)
	  return true;
      }
      return false;
    }

    public synchronized void update(int rnd, int tp, double amt)
    {
      _resultByRounds.add(new RoundResult(rnd, tp, amt));
    }

    public synchronized double crntTotalAmount()
    {
      double total = 0.0;
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
	total = total + rndRslt._amountCollected;
      }
      return total;
    }

    public synchronized int roundPeriodSelection(int rndNum)
    {
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
	if(rndRslt._roundNumber == rndNum)
	 return rndRslt._slctdTimePeriod;
      }
      return 0;
    }

    public synchronized double roundAmount(int rndNum)
    {
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
	if(rndRslt._roundNumber == rndNum)
	 return rndRslt._amountCollected;
      }
      return 0.0;
    }

//    public double prvsRoundAmount()
//    {
//      RoundResult rndRslt = (RoundResult) _resultByRounds.get(_resultByRounds.size() -1);
//      return rndRslt._amountCollected;
//    }
//
//    public int prvsRoundSlctn()
//    {
//      RoundResult rndRslt = (RoundResult) _resultByRounds.get(_resultByRounds.size() -1);
//      return rndRslt._slctdTimePeriod;
//    }
//
    public String toString()
    {
      StringBuffer strBfr = new StringBuffer();
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
        strBfr.append(" <BR> round number=<B><FONT SIZE=3 COLOR=orangered>" + rndRslt._roundNumber + "</FONT></B>");
	strBfr.append(" ; selected time period=<B><FONT SIZE=3 COLOR=orangered>" + rndRslt._slctdTimePeriod + "</FONT></B>");
	strBfr.append(" ; amount collected= <B><FONT SIZE=3 COLOR=orangered> $ " + rndRslt._amountCollected + "</FONT></B>");
      }
      return strBfr.toString();
    }

    public String toFileAsString()
    {
      StringBuffer strBfr = new StringBuffer();
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
        strBfr.append(rndRslt._roundNumber + "%");
	strBfr.append(rndRslt._slctdTimePeriod + "@");
	strBfr.append(rndRslt._amountCollected + "$");
      }
      return strBfr.toString();
    }

    public String toSaveFileAsString()
    {
      StringBuffer strBfr = new StringBuffer();
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
        strBfr.append("\n round number = " + rndRslt._roundNumber);
	strBfr.append(" time period selected = " + rndRslt._slctdTimePeriod);
	strBfr.append(" amount collected = $ " + rndRslt._amountCollected);
      }
      return strBfr.toString();
    }


    public String toSaveToXlsFile(String usr)
    {
      StringBuffer strBfr = new StringBuffer();
      for(int i = 0; i < _resultByRounds.size(); i++)
      {
	RoundResult rndRslt = (RoundResult) _resultByRounds.get(i);
	strBfr.append("\n" + usr.toUpperCase());
        strBfr.append("\t" + rndRslt._roundNumber);
	strBfr.append("\t" + rndRslt._slctdTimePeriod);
	strBfr.append("\t" + rndRslt._amountCollected);
      }
      strBfr.append("\n");
      return strBfr.toString();
    }

}
