/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.player;

import com.mettlesolutions.bankgame.util.Constants;
import com.mettlesolutions.bankgame.DataHandler;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;


/**
 * HttpSession created holding the following:
 *    ARG_NUM_OF_PLAYERS
 *    ARG_EACH_DEPOSIT
 *    ARG_RATE_OF_RETURN
 *    ARG_NUM_OF_BANKS
 *    ARG_TIME_PERIOD
 *    ARG_NUM_OF_ROUNDS
 *    ARG_CONVERSION_RATE
 *    ARG_MODE
 *    ARG_USRNMS_CRNT_RND_VALUES - Hashmap
 */

public class Player extends HttpServlet implements SingleThreadModel{
    private Map<String, String> userNamePasswords = new HashMap<String, String>();
    
    public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
      String strUser = req.getParameter(Constants.ARG_USERNAME);
      String strPasswd = req.getParameter(Constants.ARG_PASSWORD);

      HttpSession httpSsn = req.getSession(true);
      httpSsn.setMaxInactiveInterval(Constants.PLAYER_MAX_INACTIVE_INTERVAL);

      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();

      // for demo checking username and password here but this will be
      // checking against a file/table containing all usernames and passwords
      if(!validPlayerLogin(strUser, strPasswd))
      {
	httpSsn.setAttribute(Constants.ARG_INVALID_USERNAME, strUser);
	res.sendRedirect(Constants.SRVLT_PATH + "playerLogin");
      }
      else
      {
	httpSsn.setAttribute(Constants.ARG_USERNAME, strUser);
	DataHandler dataHndlr = DataHandler.getDataHandlerInstance();

	if(!dataHndlr.isLoaded())
	{
	  out.println("<Font size=4 color=red align=center><B>Requires Administrator's Permission. Sorry you cannot login now.</B></font><BR>");
	  out.println("<FORM METHOD=LINK ACTION=" + Constants.SRVLT_PATH + "playerLogin><INPUT TYPE=submit VALUE=\"Try Logging in again...\"></FORM>");
	  out.close();
	  return;
	}

	if(!dataHndlr.allPlyrsLoggedIn())
	{
	  if(dataHndlr.addPlayer(strUser))
	  {
	    dataHndlr.writeToFile();
	    res.sendRedirect(Constants.SRVLT_PATH + "playerInputAndReport");
	  }
	  else
	  {
	    out.println("<B><BR><BR>SORRY. LOGIN NAME ALREADY IN USE. TRY ANOTHER LOGIN NAME...</B><BR><BR>");
	    out.println("\n<FORM METHOD=LINK ACTION=" + Constants.SRVLT_PATH + "playerLogin>\n<INPUT TYPE=submit VALUE=\"GO TO LOGIN PAGE\">\n</FORM>");
	    out.close();
	  }
	}
	else
	{
	  out.println("<B><BR><BR>SORRY. NO MORE PLAYERS FOR THIS GAME. TRY NEXT GAME...</B>");
	  out.close();
	}
      }
    }

    private boolean validPlayerLogin(String usrnm, String pswd) throws IOException {
        // generate subject IDs
        for(int i = 0; i < 24; ++i){
            userNamePasswords.put(Integer.toString(i), Integer.toString(i));
        }
        
        boolean validUsernamePassword  = false;
        for(Map.Entry<String, String> entry : userNamePasswords.entrySet()){
            if(usrnm.equals(entry.getKey()) && pswd.equals(entry.getValue())){
                validUsernamePassword = true;
            }
        }
       
        return validUsernamePassword;
    }

}