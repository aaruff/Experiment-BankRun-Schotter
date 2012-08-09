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

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

// should be the html output to call both the frames
public class InstrctnsWithClcltr extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
      res.setContentType(Constants.TEXT_HTML_CONTENT_TYPE);
      PrintWriter out = res.getWriter();
      DataHandler dataHndlr = DataHandler.getDataHandlerInstance();
      String fileName = dataHndlr.instructionFilename();

      StringBuffer notesBfr = new StringBuffer();
      notesBfr.append("\n<HTML>\n<HEAD>\n<TITLE>INSTRUCTIONS ... </TITLE>");
      notesBfr.append("\n<FRAMESET ROWS=\"10%,* FRAMEBORDER=\"0\" " +
                      "FRAMESPACING=\"0\" BORDER=\"0\">");
      notesBfr.append("\n\t<FRAME SRC=\"" + Constants.CLCLTR_BTN_LINK_LOC + Constants.CLCLTR_BTN_FILE + "\">");
      notesBfr.append("\n\t<FRAME SRC=\"" + Constants.INSTRCTNS_LINK_LOC + fileName + "\">");
      notesBfr.append("\n</FRAMESET>\n</HEAD>\n</HTML>");
      out.println(notesBfr.toString());
      out.close();
    }
}