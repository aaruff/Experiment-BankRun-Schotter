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

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;


public class SetPlyrUsernames extends HttpServlet
{
    HashMap _usrnmPswds = new HashMap();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      String adminPswd = req.getParameter("AdminPswd");
      if(null == adminPswd || Constants.EMPTY_STRING.equals(adminPswd))
        adminPswd = Constants.ADMIN_USERNAME;

      _usrnmPswds.put(Constants.ADMIN_USERNAME, adminPswd);

      String numOfPlyrs = req.getParameter(Constants.ARG_NUM_OF_PLAYERS);

      for(int i = 1; i <= Integer.parseInt(numOfPlyrs); i++) {
        String plyrUsr = req.getParameter("PlyrUsrnm" + i);
        if(null == plyrUsr || Constants.EMPTY_STRING.equals(plyrUsr))
          plyrUsr = "Player" + i;
        String plyrPswd = req.getParameter("PlyrPswd" + i);
        if(null == plyrPswd || Constants.EMPTY_STRING.equals(plyrPswd))
          plyrPswd = "Player" + i;

        _usrnmPswds.put(plyrUsr, plyrPswd);
      }

      writeToUsernamesFile();

      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      out.println("\n<HTML><HEAD>\n<SCRIPT LANGUAGE=\"JavaScript\">" +
                  "this.close();</SCRIPT></HEAD><BODY></BODY></HTML>");
      out.close();
    }

    private void writeToUsernamesFile() throws IOException
    {
      File usrnmFile = new File(Constants.USERNAMES_FILENAME);
      if(!usrnmFile.exists())
         usrnmFile.createNewFile();

      StringBuffer strBfr = new StringBuffer();
      boolean frstLine = true;
      for(Iterator itr = _usrnmPswds.keySet().iterator(); itr.hasNext();)
      {
        if(!frstLine)
          strBfr.append("\n");

        String usrnm = (String) itr.next();
        strBfr.append(usrnm + "=" + ((String) _usrnmPswds.get(usrnm)));
        frstLine = false;
      }

      PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(usrnmFile)));
      fileWrtr.print(strBfr.toString());
      fileWrtr.close();
    }
}
