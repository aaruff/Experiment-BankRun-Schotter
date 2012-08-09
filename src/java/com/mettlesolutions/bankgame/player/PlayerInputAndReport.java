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

import java.util.Vector;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

// should be the html output to call both the frames
public class PlayerInputAndReport extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      HttpSession httpSsn = req.getSession(false);
      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      if(null == httpSsn)
      {
	out.println("\n<HTML>\n<HEAD><TITLE>BANK GAME</TITLE></HEAD>");
	out.println("\n<BODY><FONT SIZE=4 TEXT=\"red\" ALIGN=\"center\">");
	out.println("No valid Session. Please Login.</FONT>");
	out.println("\n<BR><BR>");
	out.println("\n<FORM METHOD=LINK ACTION=" + Constants.SRVLT_PATH + "playerLogin>");
	out.println("\n<INPUT TYPE=submit VALUE=\"Go to Player Login Page\">\n</FORM>");
	out.println("\n</BODY>\n</HTML>");
      }
      else
      {
        out.println("\n<HTML>\n<HEAD><TITLE>BANK GAME</TITLE></HEAD>");
        out.println("\n<FRAMESET ROWS=\"40%,60%\">");
        out.println("\n<FRAME NAME=\"input\" src=\"" + Constants.SRVLT_PATH + "playerInput\">");
        out.println("\n<FRAME NAME=\"report\" src=\"" + Constants.SRVLT_PATH + "playerReport\">");
        out.println("\n</FRAMESET>\n</HEAD>");
      }
      out.close();
    }
}