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

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class AdminInput extends HttpServlet
{
      HttpSession _httpSsn = null;

      String _oldNumOfPlyrs = null;
      String _oldEachDpst = null;
      String _oldEcnmyRate = null;
      String _oldPrmsdRate = null;
      String _oldNumOfBnks = null;
      String _oldTimePrd = null;
      String _oldNumOfRnds = null;
      String _oldCnvrsnRate = null;
      String _oldInsrncRate = null;
      String _oldShowUpFee = null;
      String _oldMode = null;
      String _oldRndInfoLvl = null;
      String _oldPrdInfoLvl = null;
      String _oldClcltrChoice = null;
      String _oldReportType = null;
      String _oldInstrctnFile = null;

    @Override
      public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
	_httpSsn = req.getSession(false);
	if(null != _httpSsn)
	{
	  _oldNumOfPlyrs = (String)_httpSsn.getAttribute(Constants.ARG_NUM_OF_PLAYERS);
	  _oldEachDpst = (String)_httpSsn.getAttribute(Constants.ARG_EACH_DEPOSIT);
	  _oldEcnmyRate = (String)_httpSsn.getAttribute(Constants.ARG_ECONOMY_RATE);
	  _oldPrmsdRate = (String)_httpSsn.getAttribute(Constants.ARG_PROMISED_RATE);
	  _oldNumOfBnks = (String)_httpSsn.getAttribute(Constants.ARG_NUM_OF_BANKS);
	  _oldTimePrd = (String)_httpSsn.getAttribute(Constants.ARG_TIME_PERIOD);
	  _oldNumOfRnds = (String)_httpSsn.getAttribute(Constants.ARG_NUM_OF_ROUNDS);
	  _oldCnvrsnRate = (String)_httpSsn.getAttribute(Constants.ARG_CONVERSION_RATE);
	  _oldInsrncRate = (String)_httpSsn.getAttribute(Constants.ARG_INSURANCE_RATE);
	  _oldShowUpFee = (String)_httpSsn.getAttribute(Constants.ARG_SHOW_UP_FEE);
	  _oldMode = (String)_httpSsn.getAttribute(Constants.ARG_MODE);
	  _oldRndInfoLvl = (String)_httpSsn.getAttribute(Constants.ARG_RND_RPRT_LVL);
	  _oldPrdInfoLvl = (String)_httpSsn.getAttribute(Constants.ARG_PRD_RPRT_LVL);
	  _oldClcltrChoice = (String)_httpSsn.getAttribute(Constants.ARG_CLCLTR_CHOICE);
	  _oldReportType = (String)_httpSsn.getAttribute(Constants.ARG_RPRT_TYPE);
	  _oldInstrctnFile = (String)_httpSsn.getAttribute(Constants.ARG_INSTRCTN_FILE);
	}

        if(null == _oldNumOfPlyrs && null == _oldEachDpst && null == _oldEcnmyRate
          && null == _oldPrmsdRate && null == _oldNumOfBnks && null == _oldTimePrd
          && null == _oldNumOfRnds && null == _oldCnvrsnRate && null == _oldInsrncRate
          && null == _oldShowUpFee && null == _oldMode && null == _oldRndInfoLvl
          && null == _oldPrdInfoLvl && null == _oldClcltrChoice && null == _oldReportType
          && null == _oldInstrctnFile)
        {
          File adminFile = new File(Constants.ADMIN_FILENAME);
          if(adminFile.exists())
          {
            DataHandler dH = DataHandler.getDataHandlerInstance();
            _oldNumOfPlyrs = Integer.toString(dH.totalNumOfPlayers());
            _oldEachDpst = Double.toString(dH.amountEachDeposited());
            _oldEcnmyRate = Double.toString(dH.economyRateOfReturn());
            _oldPrmsdRate = Double.toString(dH.promisedRateOfReturn());
            _oldNumOfBnks = Integer.toString(dH.totalNumOfBanks());
            _oldTimePrd = Integer.toString(dH.totalTimePeriod());
            _oldNumOfRnds = Integer.toString(dH.totalNumOfRounds());
            _oldCnvrsnRate = Double.toString(dH.conversionRate());
            _oldInsrncRate = Double.toString(dH.insuranceRate());
            _oldShowUpFee = Double.toString(dH.showUpFee());
            _oldMode = Integer.toString(dH.modeOfPlay());
            _oldRndInfoLvl = Integer.toString(dH.roundInfoLevel());
            _oldPrdInfoLvl = Integer.toString(dH.periodInfoLevel());
            _oldClcltrChoice = Integer.toString(dH.calculatorChoice());
            _oldReportType = Integer.toString(dH.reportSlctn());
            _oldInstrctnFile = dH.instructionFilename();
          }
        }

        // set defaults if for the first time
	if(null == _oldNumOfBnks) _oldNumOfBnks = Constants.MAX_NUM_OF_BANKS;
	if(null == _oldMode) _oldMode = Integer.toString(Constants.SMLTNS);
	if(null == _oldRndInfoLvl) _oldRndInfoLvl = Integer.toString(Constants.RND_LOW);
	if(null == _oldPrdInfoLvl) _oldPrdInfoLvl = Integer.toString(Constants.PRD_LOW);
	if(null == _oldClcltrChoice) _oldClcltrChoice = Integer.toString(Constants.CLCLTR_BY_AVG);
        if(null == _oldReportType) _oldReportType = Integer.toString(Constants.RPRT_EXL);
	if(null == _oldInstrctnFile) _oldInstrctnFile = Constants.EMPTY_STRING;

	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
	PrintWriter out = res.getWriter();
	out.println(htmlToBeSent());
	out.close();
      }

      private String htmlToBeSent() {
	HtmlBuffer bfr = new HtmlBuffer();
	bfr.beginHtml("BANK GAME - Administrator Input Page", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
	bfr.emptyBlock();
	bfr.addHeading("ADMINISTRATOR &nbsp INPUT PAGE", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);

	if(null == _httpSsn){
	  bfr.addHeading("No valid Session. Please Login.", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_RED);
	  bfr.addEmptyLines(2);
	  bfr.addLinkButton("adminLogin", "Go to Administrator Login Page");
	  return bfr.getBuffer().toString();
	}

	bfr.beginForm("AdminInput", Constants.SRVLT_PATH + "adminExecute", Constants.POST_METHOD);
	bfr.addEmptyLine();
	bfr.addHeading("Please give the following details (Number of Banks - is not editable)", Constants.ALIGN_CENTER, Constants.FONT_SIZE_THREE, Constants.TEXT_CLR_BLUE);
	bfr.beginTable(Constants.ALIGN_CENTER, Constants.TBL_BORDER_SIZE_TEN, Constants.TBL_CELL_SPCNG_SIZE_TEN, Constants.TBL_CELL_PDNG_SIZE_ONE);
	addTblCellsInRow(bfr, "Number Of Players", "txtNumPlyr", Constants.ARG_NUM_OF_PLAYERS, _oldNumOfPlyrs);
	addTblCellsInRow(bfr, "Amount Deposit By Each", "txtEchDpst", Constants.ARG_EACH_DEPOSIT, _oldEachDpst);
	addTblCellsInRow(bfr, "Mean Rate of Return (r*)", "txtEcnmyRt", Constants.ARG_ECONOMY_RATE, _oldEcnmyRate);
	addTblCellsInRow(bfr, "Bank Promised Rate (r')", "txtPrmsdRt", Constants.ARG_PROMISED_RATE, _oldPrmsdRate);
	addTblCellsInRow(bfr, "Number of Banks", "txtNumBank", Constants.ARG_NUM_OF_BANKS, _oldNumOfBnks, true);
	addTblCellsInRow(bfr, "Time Period", "txtTime", Constants.ARG_TIME_PERIOD, _oldTimePrd);
	addTblCellsInRow(bfr, "Number of Rounds", "txtNumRounds", Constants.ARG_NUM_OF_ROUNDS, _oldNumOfRnds);
	addTblCellsInRow(bfr, "Conversion Rate", "txtConvRate", Constants.ARG_CONVERSION_RATE, _oldCnvrsnRate);
	addTblCellsInRow(bfr, "Insurance Rate", "txtInsrncRate", Constants.ARG_INSURANCE_RATE, _oldInsrncRate);
	addTblCellsInRow(bfr, "Show up Fee", "txtShowUpFee", Constants.ARG_SHOW_UP_FEE, _oldShowUpFee);

        bfr.beginTableRow();
	bfr.addTableCell("Mode of Game", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	HashMap gameModeSlctns = new HashMap(2);
	gameModeSlctns.put(Integer.toString(Constants.SMLTNS), Constants.STR_SMLTNS);
	gameModeSlctns.put(Integer.toString(Constants.SQNTL), Constants.STR_SQNTL);
	bfr.addTextboxTableRadioButtons("rdoMode", Constants.ARG_MODE, gameModeSlctns, _oldMode, true);
	bfr.endTableRow();

        bfr.beginTableRow();
	bfr.addTableCell("Round Information Level", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	HashMap rndInfrmtnLvls = new HashMap(3);
	rndInfrmtnLvls.put(Integer.toString(Constants.RND_LOW), "LOW");
	rndInfrmtnLvls.put(Integer.toString(Constants.RND_MEDIUM), "MEDIUM");
	rndInfrmtnLvls.put(Integer.toString(Constants.RND_HIGH), "HIGH");
	bfr.addTextboxTableRadioButtons("RoundReportLevel", Constants.ARG_RND_RPRT_LVL, rndInfrmtnLvls, _oldRndInfoLvl, true);
	bfr.endTableRow();

        bfr.beginTableRow();
	bfr.addTableCell("Period Information Level (Sequential)", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	HashMap prdInfrmtnLvls = new HashMap(3);
	prdInfrmtnLvls.put(Integer.toString(Constants.PRD_LOW), "LOW");
	prdInfrmtnLvls.put(Integer.toString(Constants.PRD_MEDIUM), "MEDIUM");
	prdInfrmtnLvls.put(Integer.toString(Constants.PRD_HIGH), "HIGH");
	bfr.addTextboxTableRadioButtons("PeriodReportLevel", Constants.ARG_PRD_RPRT_LVL, prdInfrmtnLvls, _oldPrdInfoLvl, true);
	bfr.endTableRow();

        bfr.beginTableRow();
	bfr.addTableCell("Calculator choice", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	HashMap clcltrChoice = new HashMap(3);
        clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_AVG), "Default Type");
	clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_MODE), "Generic Type");
	clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_INFO_LVL), "Advanced Type");
	bfr.addTextboxTableSlctnBox("clcltrChoice", Constants.ARG_CLCLTR_CHOICE, clcltrChoice, _oldClcltrChoice);
	bfr.endTableRow();

        bfr.beginTableRow();
	bfr.addTableCell("Save Report As", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	HashMap rprtDsplyAndValues = new HashMap(2);
        HashMap rprtSlctdValues = new HashMap(2);
	rprtDsplyAndValues.put(Integer.toString(Constants.RPRT_EXL), "Excel File");
	rprtDsplyAndValues.put(Integer.toString(Constants.RPRT_TXT), "Text File");
        if(_oldReportType.equals(Integer.toString(Constants.RPRT_EXL))
           || _oldReportType.equals(Integer.toString(Constants.RPRT_BOTH)))
          rprtSlctdValues.put(Integer.toString(Constants.RPRT_EXL), Constants.YES_SLCTN);
        else
          rprtSlctdValues.put(Integer.toString(Constants.RPRT_EXL), Constants.NO_SLCTN);

        if(_oldReportType.equals(Integer.toString(Constants.RPRT_TXT))
           || _oldReportType.equals(Integer.toString(Constants.RPRT_BOTH)))
          rprtSlctdValues.put(Integer.toString(Constants.RPRT_TXT), Constants.YES_SLCTN);
        else
          rprtSlctdValues.put(Integer.toString(Constants.RPRT_TXT), Constants.NO_SLCTN);

        bfr.addCheckBoxesInTable(Constants.ARG_RPRT_TYPE, rprtDsplyAndValues, rprtSlctdValues);
	bfr.endTableRow();

        // have to leave blank if no file
	addTblCellsInRow(bfr, "Instruction File Name",
		         "txtInstrctnFile", Constants.ARG_INSTRCTN_FILE, _oldInstrctnFile);
	bfr.endTable();

	bfr.addHiddenInput(Constants.ARG_NUM_OF_BANKS, Constants.MAX_NUM_OF_BANKS);
	bfr.beginBlock(Constants.ALIGN_CENTER);
	bfr.addSubmitButton("loginSubmit", "loginSubmit", "SUBMIT");
	bfr.addEmptySpaces(8);
	bfr.addResetButton("CLEAR");
	bfr.endBlock();
	bfr.endForm();

	bfr.endHtml();

	return bfr.getBuffer().toString();
      }

      private void addInstruction(HtmlBuffer bfr, String text)
      {
	bfr.addToBody(text, Constants.BOLD, Constants.FONT_SIZE_FOUR);
	bfr.addEmptyLine();
      }

      private void addTblCellsInRow(HtmlBuffer bfr, String text, String fieldId, String fieldName, String fieldValue)
      {
	addTblCellsInRow(bfr, text, fieldId, fieldName, fieldValue, false);
      }

      private void addTblCellsInRow(HtmlBuffer bfr, String text, String fieldId, String fieldName, String fieldValue, boolean notEditable)
      {
	bfr.beginTableRow();
	bfr.addTableCell(text, Constants.BKGRND_CLR_GRAY, Constants.BOLD);
	bfr.addTextboxTableCell(fieldId, fieldName, Constants.TEXT_BOX_TYPE_TEXT, Constants.TEXT_BOX_SIZE_THIRTYFIVE, fieldValue, notEditable);
	bfr.endTableRow();
      }
}