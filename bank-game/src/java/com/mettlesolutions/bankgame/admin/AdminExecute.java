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
import com.mettlesolutions.bankgame.util.Util;
import com.mettlesolutions.bankgame.util.ServerProperties;
import com.mettlesolutions.bankgame.DataHandler;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.SingleThreadModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;


public class AdminExecute extends HttpServlet implements SingleThreadModel
{
  int _numOfPlayers;
  double _eachDeposit;
  double _economyRate;
  double _promisedRate;
  int _numOfBanks;
  int _timePeriod;
  int _numOfRounds;
  double _conversionRate;
  double _insuranceRate;
  double _showUpFee;
  int _mode;
  int _rndRprtLvl;
  int _prdRprtLvl;
  int _clcltrChoice;
  int _rprtType;
  String _instrctnFile;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
   {
     // FIX : validation against max and min values
      HttpSession httpSsn = req.getSession(false);
      StringBuffer strFormatErrMsgs = new StringBuffer();

      String strNumOfPlyrs = req.getParameter(Constants.ARG_NUM_OF_PLAYERS);
      httpSsn.setAttribute(Constants.ARG_NUM_OF_PLAYERS, strNumOfPlyrs);
      _numOfPlayers = Util.getIntValueFromReq(strNumOfPlyrs, strFormatErrMsgs);

      String strEachDpst = req.getParameter(Constants.ARG_EACH_DEPOSIT);
      httpSsn.setAttribute(Constants.ARG_EACH_DEPOSIT, strEachDpst);
      _eachDeposit = Util.getDoubleValueFromReq(strEachDpst, strFormatErrMsgs);

      String strEcnmyRate = req.getParameter(Constants.ARG_ECONOMY_RATE);
      httpSsn.setAttribute(Constants.ARG_ECONOMY_RATE, strEcnmyRate);
      _economyRate = Util.getDoubleValueFromReq(strEcnmyRate, strFormatErrMsgs);

      String strPrmsdRate = req.getParameter(Constants.ARG_PROMISED_RATE);
      httpSsn.setAttribute(Constants.ARG_PROMISED_RATE, strPrmsdRate);
      _promisedRate = Util.getDoubleValueFromReq(strPrmsdRate, strFormatErrMsgs);

      String strNumOfBnks = req.getParameter(Constants.ARG_NUM_OF_BANKS);
      httpSsn.setAttribute(Constants.ARG_NUM_OF_BANKS, strNumOfBnks);
      _numOfBanks = Util.getIntValueFromReq(strNumOfBnks, strFormatErrMsgs);

      String strTimePrd = req.getParameter(Constants.ARG_TIME_PERIOD);
      httpSsn.setAttribute(Constants.ARG_TIME_PERIOD, strTimePrd);
      _timePeriod = Util.getIntValueFromReq(strTimePrd, strFormatErrMsgs);

      String strNumOfRnds = req.getParameter(Constants.ARG_NUM_OF_ROUNDS);
      httpSsn.setAttribute(Constants.ARG_NUM_OF_ROUNDS, strNumOfRnds);
      _numOfRounds = Util.getIntValueFromReq(strNumOfRnds, strFormatErrMsgs);

      String strCnvrsnRt = req.getParameter(Constants.ARG_CONVERSION_RATE);
      httpSsn.setAttribute(Constants.ARG_CONVERSION_RATE, strCnvrsnRt);
      _conversionRate = Util.getDoubleValueFromReq(strCnvrsnRt, strFormatErrMsgs);

      String strInsrncRt = req.getParameter(Constants.ARG_INSURANCE_RATE);
      httpSsn.setAttribute(Constants.ARG_INSURANCE_RATE, strInsrncRt);
      _insuranceRate = Util.getDoubleValueFromReq(strInsrncRt, strFormatErrMsgs);

      String strShowUpFee = req.getParameter(Constants.ARG_SHOW_UP_FEE);
      httpSsn.setAttribute(Constants.ARG_SHOW_UP_FEE, strShowUpFee);
      _showUpFee = Util.getDoubleValueFromReq(strShowUpFee, strFormatErrMsgs);

      String strMode = req.getParameter(Constants.ARG_MODE);
      httpSsn.setAttribute(Constants.ARG_MODE, strMode);
      _mode = Util.getIntValueFromReq(strMode, strFormatErrMsgs);

      String strRndRprtLvl = req.getParameter(Constants.ARG_RND_RPRT_LVL);
      httpSsn.setAttribute(Constants.ARG_RND_RPRT_LVL, strRndRprtLvl);
      _rndRprtLvl = Util.getIntValueFromReq(strRndRprtLvl, strFormatErrMsgs);

      String strPrdRprtLvl = req.getParameter(Constants.ARG_PRD_RPRT_LVL);
      httpSsn.setAttribute(Constants.ARG_PRD_RPRT_LVL, strPrdRprtLvl);
      _prdRprtLvl = Util.getIntValueFromReq(strPrdRprtLvl, strFormatErrMsgs);

      String strClcltrCh = req.getParameter(Constants.ARG_CLCLTR_CHOICE);
      httpSsn.setAttribute(Constants.ARG_CLCLTR_CHOICE, strClcltrCh);
      _clcltrChoice = Util.getIntValueFromReq(strClcltrCh, strFormatErrMsgs);

      _rprtType = 0;
      String strRprtTxtSlctn = req.getParameter(Constants.ARG_RPRT_TYPE + Integer.toString(Constants.RPRT_TXT));
      if(null != strRprtTxtSlctn && !Constants.EMPTY_STRING.equals(strRprtTxtSlctn))
        _rprtType = _rprtType + Constants.RPRT_TXT;
      String strRprtExlSlctn = req.getParameter(Constants.ARG_RPRT_TYPE + Integer.toString(Constants.RPRT_EXL));
      if(null != strRprtExlSlctn && !Constants.EMPTY_STRING.equals(strRprtExlSlctn))
        _rprtType = _rprtType + Constants.RPRT_EXL;
      httpSsn.setAttribute(Constants.ARG_RPRT_TYPE, Integer.toString(_rprtType));

      String _instrctnFile = req.getParameter(Constants.ARG_INSTRCTN_FILE);
      _instrctnFile = _instrctnFile.trim();
      httpSsn.setAttribute(Constants.ARG_INSTRCTN_FILE, _instrctnFile);

      // FIX: This should be done in a set up process and not here.
      // create instructions and clcltrBtnLnk directories
      // if they do not exist.
      /*
      File instrDrctry = new File(Constants.INSTRCTNS_LCTN);
      if(!instrDrctry.exists())
        instrDrctry.mkdirs();
      File clcltrBtnDrctry = new File(Constants.CLCLTR_BTN_LCTN);
      if(!clcltrBtnDrctry.exists())
        clcltrBtnDrctry.mkdirs();
      */
      
      if(!Constants.EMPTY_STRING.equals(_instrctnFile))
      {
        // check if file exists only from Server1 and skip for others
        if(ServerProperties.getInstrctnsServerAddr().equals(ServerProperties.getServerAddress()))
        {
	  File instrFl = new File(Constants.INSTRCTNS_LCTN + _instrctnFile);
	  if(!instrFl.exists())
	    strFormatErrMsgs.append(_instrctnFile + " file does not exist. " +
                                            "leave blank if no instructions file.");
        }
      }
      else
        _instrctnFile = Constants.BLANK_STRING; // to get around parsing

      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      HtmlBuffer bfr = new HtmlBuffer();
      bfr.beginHtml("BANK GAME - Administrator Confirmation Page", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
      bfr.emptyBlock();

      if(null == httpSsn)// || !ADMIN_USERNAME.equals((String) req.getAttribute(ARG_USERNAME)))
      {
	bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminLogin", "Go to Administrator Login Page");
      }
      else if(!Constants.EMPTY_STRING.equals(strFormatErrMsgs.toString()))
      {
	bfr.addHeading("CANNOT CONTINUE FURTHER. <BR> PLEASE CORRECT THE FOLLOWING...", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
	bfr.addEmptyLines(2);
	bfr.addToBody(strFormatErrMsgs.toString());
	bfr.addEmptyLines(2);
	bfr.addLinkButton("adminInput", "Correct the above values");
      }
      else
      {
	DataHandler dataHndlr = new DataHandler();
	dataHndlr.setValues(_numOfPlayers, _eachDeposit, _economyRate, _promisedRate, _numOfBanks, _timePeriod,
			    _numOfRounds, _conversionRate, _insuranceRate, _showUpFee, _mode, _rndRprtLvl,
			    _prdRprtLvl, _clcltrChoice, _rprtType, _instrctnFile);

	bfr.addHeading("ADMINISTRATOR &nbsp CONFIRMATION PAGE", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
        bfr.addEmptyLine();
        bfr.addToBody("Check if these values are right...", Constants.BOLD, Constants.FONT_SIZE_THREE);
        bfr.addEmptyLine();
	bfr.addToBody(dataHndlr.getAllValuesAsString());
	bfr.addEmptyLine();
	bfr.addLinkButton("adminInput", "Change any of the above values        ");
	bfr.addToBody("\n<INPUT TYPE=\"Button\" value=\"Change Usernames and Passwords\"" +
                      "onClick=\"window.open('" + Constants.SRVLT_PATH + "getPlyrUsernames','Usernames','toolbar=no,status=no," +
                      "scrollbars=yes,resizable=yes,location=no,menubar=no,directories=no,width=640,height=540')\">");
//	bfr.addEmptyLine();
//	bfr.addToBody("<B>Administrator's demo ...</B>");
//	//bfr.addEmptySpaces(4);
//	bfr.addLinkButton("AdminDemoInput", "Check Admin's Demo");
//	bfr.addEmptyLine();
//	bfr.addToBody("<B>Player Can Start the Game Now ...</B>");
	//bfr.addEmptySpaces(4);

	bfr.addLinkButton("adminReport", "View Game As Administrator                ");
        createUsernamesFile();
     }
      bfr.endHtml();
      out.println(bfr.getBuffer().toString());
      out.close();
    }

    private void createUsernamesFile() throws IOException
    {
      String adminPswd = Constants.ADMIN_USERNAME;
      String plyrUsrnmPrfx = "Player";

      File usrnmFile = new File(Constants.USERNAMES_FILENAME);
      if(usrnmFile.exists())
      {
        BufferedReader bfrdRdr = new BufferedReader(new FileReader(usrnmFile));
        String lineFromFile = bfrdRdr.readLine();
	while(null != lineFromFile)
        {
          StringTokenizer strTok = new StringTokenizer(lineFromFile, "=");
          if(Constants.ADMIN_USERNAME.equals(strTok.nextToken()))
          {
            adminPswd = strTok.nextToken();
            break;
          }
	  lineFromFile = bfrdRdr.readLine();
	}
        bfrdRdr.close();
      }
      else
        usrnmFile.createNewFile();

      StringBuffer strBfr = new StringBuffer();
      strBfr.append(Constants.ADMIN_USERNAME + "=" + adminPswd);
      for(int i = 1; i <= _numOfPlayers; i++)
      {
        strBfr.append("\n" + plyrUsrnmPrfx + i + "=" + plyrUsrnmPrfx + i);
      }

      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(usrnmFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();
    }
}