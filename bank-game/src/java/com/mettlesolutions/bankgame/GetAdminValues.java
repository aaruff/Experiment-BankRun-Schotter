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
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;


public class GetAdminValues extends HttpServlet implements SingleThreadModel
{
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
	PrintWriter out = res.getWriter();
	out.println(htmlToBeSent());
	out.close();
    }

    private String htmlToBeSent()
    {
      DataHandler adminVlus_DH = DataHandler.getDataHandlerInstance();

      String oldCnvrsnRate = Double.toString(adminVlus_DH.conversionRate());
      String oldShowUpFee = Double.toString(adminVlus_DH.showUpFee());
      String oldRndInfoLvl = Integer.toString(adminVlus_DH.roundInfoLevel());
      String oldPrdInfoLvl = Integer.toString(adminVlus_DH.periodInfoLevel());
      String oldClcltrChoice = Integer.toString(adminVlus_DH.calculatorChoice());
      String oldReportType = Integer.toString(adminVlus_DH.reportSlctn());
      String oldInstrctnFile = adminVlus_DH.instructionFilename();

      HtmlBuffer bfr = new HtmlBuffer();
      bfr.beginHtml("BANK GAME - SET ADMINISTRATOR VALUES", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
      bfr.emptyBlock();
      bfr.addHeading("SET ADMINISTRATOR VALUES", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
      bfr.addEmptyLine();

      bfr.beginForm("SetAdminValues", Constants.SRVLT_PATH + "setAdminValues", Constants.POST_METHOD);
      bfr.addEmptyLine();
      bfr.addHeading("Administrator can change the following values at anytime",
                     Constants.ALIGN_CENTER, Constants.FONT_SIZE_TWO, Constants.TEXT_CLR_BLUE);
      bfr.beginTable(Constants.ALIGN_CENTER, Constants.TBL_BORDER_SIZE_TEN, Constants.TBL_CELL_SPCNG_SIZE_TEN, Constants.TBL_CELL_PDNG_SIZE_ONE);
      addTblCellsInRow(bfr, "Conversion Rate", "txtConvRate", Constants.ARG_CONVERSION_RATE, oldCnvrsnRate);
      addTblCellsInRow(bfr, "Show up Fee", "txtShowUpFee", Constants.ARG_SHOW_UP_FEE, oldShowUpFee);
      bfr.beginTableRow();
      bfr.addTableCell("Round Information Level", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
      HashMap rndInfrmtnLvls = new HashMap(3);
      rndInfrmtnLvls.put(Integer.toString(Constants.RND_LOW), "LOW");
      rndInfrmtnLvls.put(Integer.toString(Constants.RND_MEDIUM), "MEDIUM");
      rndInfrmtnLvls.put(Integer.toString(Constants.RND_HIGH), "HIGH");
      bfr.addTextboxTableRadioButtons("RoundReportLevel", Constants.ARG_RND_RPRT_LVL, rndInfrmtnLvls, oldRndInfoLvl, true);
      bfr.endTableRow();

      bfr.beginTableRow();
      bfr.addTableCell("Period Information Level (Sequential)", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
      HashMap prdInfrmtnLvls = new HashMap(3);
      prdInfrmtnLvls.put(Integer.toString(Constants.PRD_LOW), "LOW");
      prdInfrmtnLvls.put(Integer.toString(Constants.PRD_MEDIUM), "MEDIUM");
      prdInfrmtnLvls.put(Integer.toString(Constants.PRD_HIGH), "HIGH");
      bfr.addTextboxTableRadioButtons("PeriodReportLevel", Constants.ARG_PRD_RPRT_LVL, prdInfrmtnLvls, oldPrdInfoLvl, true);
      bfr.endTableRow();

      bfr.beginTableRow();
      bfr.addTableCell("Calculator choice", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
      HashMap clcltrChoice = new HashMap(3);
      clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_AVG), "Default Type");
      clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_MODE), "Generic Type");
      clcltrChoice.put(Integer.toString(Constants.CLCLTR_BY_INFO_LVL), "Advanced Type");
      bfr.addTextboxTableSlctnBox("clcltrChoice", Constants.ARG_CLCLTR_CHOICE, clcltrChoice, oldClcltrChoice);
      bfr.endTableRow();

      bfr.beginTableRow();
      bfr.addTableCell("Save Report As", Constants.BKGRND_CLR_GRAY, Constants.BOLD);
      HashMap rprtDsplyAndValues = new HashMap(2);
      HashMap rprtSlctdValues = new HashMap(2);
      rprtDsplyAndValues.put(Integer.toString(Constants.RPRT_EXL), "Excel File");
      rprtDsplyAndValues.put(Integer.toString(Constants.RPRT_TXT), "Text File");
      if(oldReportType.equals(Integer.toString(Constants.RPRT_EXL))
         || oldReportType.equals(Integer.toString(Constants.RPRT_BOTH)))
        rprtSlctdValues.put(Integer.toString(Constants.RPRT_EXL), Constants.YES_SLCTN);
      else
        rprtSlctdValues.put(Integer.toString(Constants.RPRT_EXL), Constants.NO_SLCTN);

      if(oldReportType.equals(Integer.toString(Constants.RPRT_TXT))
         || oldReportType.equals(Integer.toString(Constants.RPRT_BOTH)))
        rprtSlctdValues.put(Integer.toString(Constants.RPRT_TXT), Constants.YES_SLCTN);
      else
        rprtSlctdValues.put(Integer.toString(Constants.RPRT_TXT), Constants.NO_SLCTN);

      bfr.addCheckBoxesInTable(Constants.ARG_RPRT_TYPE, rprtDsplyAndValues, rprtSlctdValues);
      bfr.endTableRow();

      // have to leave blank if no file
      addTblCellsInRow(bfr, "Instruction File Name",
      	         "txtInstrctnFile", Constants.ARG_INSTRCTN_FILE, oldInstrctnFile);
      bfr.endTable();

      bfr.beginBlock(Constants.ALIGN_CENTER);
      bfr.addSubmitButton("SetAdminValues", "SetAdminValues", "SAVE");
      bfr.addEmptySpaces(6);
      bfr.addResetButton("RESET");
      bfr.addEmptySpaces(6);
      bfr.addCloseButton("QUIT");
      bfr.endBlock();

      bfr.endForm();
      bfr.endHtml();
      return bfr.getBuffer().toString();
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
