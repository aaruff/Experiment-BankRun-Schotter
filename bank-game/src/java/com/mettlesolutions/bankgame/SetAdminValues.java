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

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpSession;


public class SetAdminValues extends HttpServlet implements SingleThreadModel
{
    public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      StringBuffer strFormatErrMsgs = new StringBuffer();

      String strCnvrsnRt = req.getParameter(Constants.ARG_CONVERSION_RATE);
      double conversionRate = Util.getDoubleValueFromReq(strCnvrsnRt, strFormatErrMsgs);

      String strShowUpFee = req.getParameter(Constants.ARG_SHOW_UP_FEE);
      double showUpFee = Util.getDoubleValueFromReq(strShowUpFee, strFormatErrMsgs);

      String strRndRprtLvl = req.getParameter(Constants.ARG_RND_RPRT_LVL);
      int rndRprtLvl = Util.getIntValueFromReq(strRndRprtLvl, strFormatErrMsgs);

      String strPrdRprtLvl = req.getParameter(Constants.ARG_PRD_RPRT_LVL);
      int prdRprtLvl = Util.getIntValueFromReq(strPrdRprtLvl, strFormatErrMsgs);

      String strClcltrCh = req.getParameter(Constants.ARG_CLCLTR_CHOICE);
      int clcltrChoice = Util.getIntValueFromReq(strClcltrCh, strFormatErrMsgs);

      int rprtType = 0;
      String strRprtTxtSlctn = req.getParameter(Constants.ARG_RPRT_TYPE + Integer.toString(Constants.RPRT_TXT));
      if(null != strRprtTxtSlctn && !Constants.EMPTY_STRING.equals(strRprtTxtSlctn))
        rprtType += Constants.RPRT_TXT;
      String strRprtExlSlctn = req.getParameter(Constants.ARG_RPRT_TYPE + Integer.toString(Constants.RPRT_EXL));
      if(null != strRprtExlSlctn && !Constants.EMPTY_STRING.equals(strRprtExlSlctn))
        rprtType += Constants.RPRT_EXL;

      String instrctnFile = req.getParameter(Constants.ARG_INSTRCTN_FILE);
      instrctnFile = instrctnFile.trim();

      String rtrnStr = Constants.EMPTY_STRING;
      if(Constants.EMPTY_STRING.equals(strFormatErrMsgs.toString()))
      {
        DataHandler adminSetVlus_DH = DataHandler.getDataHandlerInstance();
        adminSetVlus_DH.setValues(conversionRate, showUpFee, rndRprtLvl, prdRprtLvl,
                                  clcltrChoice, rprtType, instrctnFile);
        rtrnStr = "\n<HTML><HEAD>\n<SCRIPT LANGUAGE=\"JavaScript\">" +
                  "this.close();</SCRIPT></HEAD><BODY></BODY></HTML>";
      }
      else
      {
        rtrnStr = "\n<HTML><HEAD><TITLE>CONFIRMATION PAGE</TITLE><BODY>\n";
        rtrnStr += strFormatErrMsgs.toString();
        rtrnStr += "\n<INPUT TYPE=\"Button\" VALUE=\"Go Back\" onClick=\"history.go(-1)\">";
        rtrnStr += "\n<BR><BR>\n<INPUT TYPE=\"Button\" VALUE=\"Close\" onClick=\"window.close();\">";
        rtrnStr += "\n</BODY></HTML>";
      }

      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      out.println(rtrnStr);
      out.close();
    }

}
