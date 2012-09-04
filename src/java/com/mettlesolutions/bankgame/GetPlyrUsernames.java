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

public class GetPlyrUsernames extends HttpServlet
{
    HashMap _usrnmPswd = new HashMap();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
	PrintWriter out = res.getWriter();
        fillMap();
	out.println(htmlToBeSent());
	out.close();
    }

    private String htmlToBeSent()
    {
      HtmlBuffer bfr = new HtmlBuffer();
      bfr.beginHtml("BANK GAME - SET LOGINS AND PASSWORDS", Constants.BKGRND_CLR_SILVER, Constants.TEXT_CLR_BLACK);
      bfr.emptyBlock();
      bfr.addHeading("SET LOGINS AND PASSWORDS", Constants.ALIGN_CENTER, Constants.FONT_SIZE_SIX, Constants.TEXT_CLR_ORNGRED);
      bfr.addEmptyLine();

      bfr.beginForm("LoginPassword", Constants.SRVLT_PATH + "setPlyrUsernames", Constants.POST_METHOD);
      bfr.addEmptyLine();
      bfr.addHeading("Set Logins and Passwords (Administrator login cannot be changed)",
                     Constants.ALIGN_CENTER, Constants.FONT_SIZE_TWO, Constants.TEXT_CLR_BLUE);
      bfr.beginTable(Constants.ALIGN_CENTER, Constants.TBL_BORDER_SIZE_TEN, Constants.TBL_CELL_SPCNG_SIZE_TEN, Constants.TBL_CELL_PDNG_SIZE_ONE);

      bfr.beginTableRow();
      bfr.addTextboxTableCell("AdminUsrnm", "AdminUsrnm", Constants.TEXT_BOX_TYPE_TEXT, 20, Constants.ADMIN_USERNAME, true);
      String adminPswd = (_usrnmPswd.containsKey(Constants.ADMIN_USERNAME)) ? ((String) _usrnmPswd.get(Constants.ADMIN_USERNAME)) : Constants.ADMIN_USERNAME;
      bfr.addTextboxTableCell("AdminPswd", "AdminPswd", Constants.TEXT_BOX_TYPE_PASSWORD, 20, adminPswd);
      bfr.endTableRow();

      int cntr = 0;
      SortedMap sortMap = new TreeMap(_usrnmPswd);
      for(Iterator itr = sortMap.keySet().iterator(); itr.hasNext();)
      {
         bfr.beginTableRow();
         String usrnm = (String) itr.next();
         // Admin is already given
         if(!Constants.ADMIN_USERNAME.equals(usrnm))
         {
           cntr++;
           bfr.addTextboxTableCell("PlyrUsrnm" + cntr, "PlyrUsrnm" + cntr, Constants.TEXT_BOX_TYPE_TEXT, 20, usrnm);
           bfr.addTextboxTableCell("PlyrPswd" + cntr, "PlyrPswd" + cntr, Constants.TEXT_BOX_TYPE_PASSWORD, 20,
                                 ((String) _usrnmPswd.get(usrnm)));
           bfr.endTableRow();
         }
      }
      bfr.endTable();

      bfr.addHiddenInput(Constants.ARG_NUM_OF_PLAYERS, Integer.toString(cntr));

      bfr.beginBlock(Constants.ALIGN_CENTER);
      bfr.addSubmitButton("UsrnmPswdSubmit", "UsrnmPswdSubmit", "SAVE");
      bfr.addEmptySpaces(6);
      bfr.addResetButton("RESET");
      bfr.addEmptySpaces(6);
      bfr.addCloseButton("QUIT");
      bfr.endBlock();

      bfr.endForm();
      bfr.endHtml();
      return bfr.getBuffer().toString();
    }

    private void fillMap() throws IOException
    {
      File usrnmFile = new File(Constants.USERNAMES_FILENAME);
      if(!usrnmFile.exists())
        return;

      BufferedReader bfrdRdr = new BufferedReader(new FileReader(usrnmFile));
      String lineFromFile = bfrdRdr.readLine();
      while(null != lineFromFile)
      {
        StringTokenizer strTok = new StringTokenizer(lineFromFile, "=");
        _usrnmPswd.put(strTok.nextToken(), strTok.nextToken());
	lineFromFile = bfrdRdr.readLine();
      }
      bfrdRdr.close();
    }
}
