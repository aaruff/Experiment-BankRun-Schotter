/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.util;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

public class HtmlBuffer
{
  StringBuffer strHtmlBfr = null;

  public HtmlBuffer()
  {
    strHtmlBfr = new StringBuffer();
  }

  public StringBuffer getBuffer()
  {
    return strHtmlBfr;
  }

  public void beginHtml(String title)
  {
    beginHtml(title, null, null, -1, null);
  }

  public void beginHtml(String title, String bodyBkgrndClr, String textClr)
  {
    beginHtml(title, bodyBkgrndClr, textClr, -1, null);
  }

  public void beginHtml(String title, String bodyBkgrndClr, String textClr, String script)
  {
    beginHtml(title, bodyBkgrndClr, textClr, -1, null, script);
  }

  public void beginHtml(String title, String bodyBkgrndClr, String textClr, int refreshTimeInSecs, String targetRefreshURL)
  {
        beginHtml(title, bodyBkgrndClr, textClr, refreshTimeInSecs, targetRefreshURL, null);
  }

  public void beginHtml(String title, String bodyBkgrndClr, String textClr, int refreshTimeInSecs, String targetRefreshURL, String script)
  {
    strHtmlBfr.append("\n<HTML>\n<HEAD>");
    if(refreshTimeInSecs != -1)
      strHtmlBfr.append("\n<META http-equiv=\"refresh\" content=\"" + refreshTimeInSecs + ";URL=" + targetRefreshURL + "\">");
    strHtmlBfr.append("\n<TITLE>");
    strHtmlBfr.append(title);
    strHtmlBfr.append("</TITLE>\n</HEAD>\n<BODY");
    if(null != bodyBkgrndClr)
      strHtmlBfr.append(" bgColor=" + bodyBkgrndClr);
    if(null != textClr)
      strHtmlBfr.append(" text=" + textClr);
    if(null != script)
      strHtmlBfr.append(" " + script);
    strHtmlBfr.append(">");
  }

  public void addEmailLinks()
  {
   strHtmlBfr.append("\n<BR><A HREF=\"mailto:andrew.schotter@nyu.edu?cc=ty232@nyu.edu&subject=comments\">");
   strHtmlBfr.append("\n<B><FONT size=FONT_SIZE_THREE>Comments</FONT></B></A>");
  }

  public void addMettleSign()
  {
    strHtmlBfr.append("\n<P ALIGN=right><FONT size=FONT_SIZE_ONE>Developed by : &nbsp;</FONT>" +
                      "<FONT size=FONT_SIZE_TWO color=maroon>Mettle Solutions</FONT></P>");
  }

  public void endHtml()
  {
    strHtmlBfr.append("\n</BODY>\n</HTML>\n");
  }

  public void addHeading(String text, String align, int fontSize, String color)
  {
    addHeading(text, align, fontSize, color, false);
  }

  public void addHeading(String text, String align, int fontSize, String color, boolean underline)
  {
    strHtmlBfr.append("\n<P align=" + align + ">\n<FONT size=" + fontSize + " color=");
    strHtmlBfr.append(color);
    strHtmlBfr.append("><STRONG>");
    if(underline)
      strHtmlBfr.append("<U>");
    strHtmlBfr.append(text);
    if(underline)
      strHtmlBfr.append("</U>");
    strHtmlBfr.append("</STRONG></FONT>\n</P>");
  }

  public void addTodayDt()
  {
    strHtmlBfr.append("\n<BR>\n<SCRIPT LANGUAGE=\"JavaScript\">");
    strHtmlBfr.append("\nfunction makeArray() { \n\tfor (i = 0; i<makeArray.arguments.length; i++)" +
                      "\n\t\tthis[i + 1] = makeArray.arguments[i];\n}");
    strHtmlBfr.append("\nvar months = new makeArray('January','February','March','April','May'," +
                      "'June','July','August','September','October','November','December');");
    strHtmlBfr.append("\nvar RightNow = new Date();");
    strHtmlBfr.append("\nvar day = RightNow.getDate();");
    strHtmlBfr.append("\nvar month = RightNow.getMonth() + 1;");
    strHtmlBfr.append("\nvar year = RightNow.getFullYear();");
    strHtmlBfr.append("\ndocument.write(\"<B><FONT size = 3>Today's Date - &nbsp;&nbsp;</FONT>");
    strHtmlBfr.append("<FONT size = 3 color= maroon>\" + \n");
    strHtmlBfr.append("\" \" + day + \" \" + months[month] + \" \" + year + \n");
    strHtmlBfr.append("\"</I></FONT></B>\")\n</SCRIPT>\n<BR>\n");
  }

 public void addToBody(String strToBody)
 {
    addToBody(strToBody, false, false, 0, null);
 }

 public void addToBody(String strToBody, int fontSize)
 {
    addToBody(strToBody, false, false, fontSize, null);
 }

 public void addToBody(String strToBody, boolean bold, boolean italic)
 {
    addToBody(strToBody, bold, italic, 0, null);
 }

 public void addToBody(String strToBody, boolean bold, int fontSize)
 {
    addToBody(strToBody, bold, false, fontSize, null);
 }

 public void addToBody(String strToBody, boolean bold, boolean italic, int fontSize)
 {
    addToBody(strToBody, bold, italic, fontSize, null);
 }

 public void addToBody(String strToBody, boolean bold, boolean italic, int fontSize, String fontColor)
 {
    strHtmlBfr.append("\n");
    if(bold)
      strHtmlBfr.append("<B>");
    if(italic)
      strHtmlBfr.append("<I>");
    if(fontSize > 0 || null != fontColor)
    {
      strHtmlBfr.append("<FONT");
      if(fontSize > 0)
        strHtmlBfr.append(" SIZE=" + fontSize);
      if(null != fontColor)
        strHtmlBfr.append(" COLOR=" + fontColor);
      strHtmlBfr.append(">");
      strHtmlBfr.append(strToBody);
      strHtmlBfr.append("</FONT>");
    }
    else
      strHtmlBfr.append(strToBody);
    if(italic)
      strHtmlBfr.append("</I>");
    if(bold)
      strHtmlBfr.append("</B>");
 }

 public void beginBlock()
 {
    beginBlock(null);
 }

 public void beginBlock(String align)
 {
    strHtmlBfr.append("\n<P");
    strHtmlBfr.append((null == align)?Constants.EMPTY_STRING:" align=" + align);
    strHtmlBfr.append(">");
 }

 public void endBlock()
 {
    strHtmlBfr.append("\n</P>");
 }

 public void emptyBlock()
 {
    strHtmlBfr.append("\n<P></P>");
 }

 public void addEmptyLine()
 {
    addEmptyLines(1);
 }

 public void addEmptyLines(int numOfLines)
 {
    for(int i = 1; i <= numOfLines; i++)
    {
      strHtmlBfr.append("<BR>");
    }
 }

 public void addEmptySpaces(int numOfSpaces)
 {
    for(int i = 1; i <= numOfSpaces; i++)
    {
      strHtmlBfr.append("&nbsp;");
    }
 }

 public void withinBlock(String text)
 {
    withinBlock(null, text);
 }

 public void withinBlock(String align, String text)
 {
    beginBlock(align);
    addToBody(text);
    endBlock();
 }

 public void beginForm(String name, String action, String method)
 {
    strHtmlBfr.append("\n<FORM");
    if(null != name)
      strHtmlBfr.append(" name=" + name);
    strHtmlBfr.append(" action=");
    strHtmlBfr.append((null == action)?Constants.EMPTY_STRING:action);
    strHtmlBfr.append(" method=");
    strHtmlBfr.append((null == method)?Constants.EMPTY_STRING:method);
    strHtmlBfr.append(">");
 }

 public void endForm()
 {
    strHtmlBfr.append("\n</FORM>");
 }

 public void beginTable(String align, int border, int cellSpcng, int cellPdng)
 {
    strHtmlBfr.append("\n<TABLE");
    strHtmlBfr.append((null == align)?Constants.EMPTY_STRING:" align=" + align);
    strHtmlBfr.append((border == -1)?Constants.EMPTY_STRING:" border=" + border);
    strHtmlBfr.append((cellSpcng == -1)?Constants.EMPTY_STRING:" cellSpacing=" + cellSpcng);
    strHtmlBfr.append((cellPdng == -1)?Constants.EMPTY_STRING:" cellPadding=" + cellPdng);
    strHtmlBfr.append(">");
 }

 public void endTable()
 {
    strHtmlBfr.append("\n</TABLE>");
 }

 public void beginTableRow()
 {
    strHtmlBfr.append("\n\t<TR>");
 }

 public void endTableRow()
 {
    strHtmlBfr.append("\n\t</TR>");
 }

 public void addTableCell(String str, String bkgrndClr, boolean bold)
 {
    strHtmlBfr.append("\n\t<TD");
    strHtmlBfr.append((null == bkgrndClr)?Constants.EMPTY_STRING:" bgColor=" + bkgrndClr);
    strHtmlBfr.append(">");
    if(bold)
      strHtmlBfr.append("<B>");
    strHtmlBfr.append(str);
    if(bold)
      strHtmlBfr.append("</B>");
    strHtmlBfr.append("</TD>");
 }

 public void addTextboxTableCell(String id, String name, String type, int size)
 {
   addTextboxTableCell(id, name, type, size, -1, null, false);
 }

 public void addTextboxTableCell(String id, String name, String type, int size, String value)
 {
   addTextboxTableCell(id, name, type, size, -1, value, false);
 }

 public void addTextboxTableCell(String id, String name, String type, int size, String value, boolean notEditable)
 {
   addTextboxTableCell(id, name, type, size, -1, value, notEditable);
 }

 public void addTextboxTableCell(String id, String name, String type, int size, int maxlength, String value, boolean notEditable)
 {
    strHtmlBfr.append("\n\t<TD><INPUT ");
    strHtmlBfr.append((null == id)?Constants.EMPTY_STRING:" id=" + id);
    strHtmlBfr.append((null == name)?Constants.EMPTY_STRING:" name=" + name);
    strHtmlBfr.append((null == type)?Constants.EMPTY_STRING:" type=" + type);
    strHtmlBfr.append((size == -1)?Constants.EMPTY_STRING:" size=" + size);
    strHtmlBfr.append((maxlength == -1)?Constants.EMPTY_STRING:" maxlength=" + maxlength);
    strHtmlBfr.append((null == value)?Constants.EMPTY_STRING:" value=\"" + value + "\"");
    strHtmlBfr.append((notEditable)?" disabled=\"true\"" :Constants.EMPTY_STRING);
    strHtmlBfr.append("></TD>");
 }

 public void addTextboxTableRadioButtons(String fieldName, HashMap slctNamesAndValues, String defaultSlct)
 {
    addTextboxTableRadioButtons(null, fieldName, slctNamesAndValues, defaultSlct, false);
 }

 public void addTextboxTableRadioButtons(String id, String fieldName, HashMap slctNamesAndValues, String defaultSlct, boolean sameCell)
 {
    addRadioButtons(id, fieldName, slctNamesAndValues, defaultSlct, sameCell, true);
 }

 public void addRadioButtons(String fieldName, HashMap slctNamesAndValues, String defaultSlct, boolean sameCell)
 {
    addRadioButtons(null, fieldName, slctNamesAndValues, defaultSlct, sameCell, false);
 }

 public void addRadioButtons(String id, String fieldName, HashMap slctNamesAndValues, String defaultValue, boolean sameCell, boolean addInTextboxTable)
 {
   addRadioButtons(id, fieldName, slctNamesAndValues, defaultValue, sameCell, addInTextboxTable, false);
 }

 public void addRadioButtons(String fieldName, HashMap slctNamesAndValues, String defaultSlct, boolean sameCell, boolean disabled)
 {
    addRadioButtons(null, fieldName, slctNamesAndValues, defaultSlct, sameCell, false, disabled);
 }

 public void addRadioButtons(String id, String fieldName, HashMap slctNamesAndValues, String defaultValue,
			     boolean sameCell, boolean addInTextboxTable, boolean disabled)
 {
    SortedMap sortMap = new TreeMap(slctNamesAndValues);
    if(addInTextboxTable)
      strHtmlBfr.append("\n\t<TD>");
    for(Iterator itr = sortMap.keySet().iterator(); itr.hasNext();)
    {
      String crntValue = (String) itr.next();
      String crntDsply = (String) slctNamesAndValues.get(crntValue);
      strHtmlBfr.append("\n<INPUT");
      if(disabled)
	strHtmlBfr.append(" disabled");
      strHtmlBfr.append(" type=radio name=" + fieldName);
      strHtmlBfr.append((null == id)?Constants.EMPTY_STRING:" id=" + id);
      strHtmlBfr.append(" value=\"" + crntValue + "\"");
      if(crntValue.equals(defaultValue))
	strHtmlBfr.append(" checked");
      strHtmlBfr.append(">");
      strHtmlBfr.append("&nbsp;&nbsp;" +crntDsply);
      if(!sameCell)
      {
	if(addInTextboxTable)
	  strHtmlBfr.append("</TD>\n\t<TD>");
	else
	  strHtmlBfr.append("<BR>");
      }
    } // end for loop
    if(addInTextboxTable)
      strHtmlBfr.append("\n\t</TD>");
 }

 public void addTextboxTableSlctnBox(String id, String fieldName,
                                     HashMap slctNamesAndValues, String defaultValue)
 {
    SortedMap sortMap = new TreeMap(slctNamesAndValues);
    strHtmlBfr.append("\n\t<TD>");
    strHtmlBfr.append("\n<SELECT");
    strHtmlBfr.append(" size=1 name=" + fieldName);
    strHtmlBfr.append((null == id)?Constants.EMPTY_STRING:" id=" + id);
    strHtmlBfr.append(">");
    for(Iterator itr = sortMap.keySet().iterator(); itr.hasNext();)
    {
      String crntValue = (String) itr.next();
      String crntDsply = (String) slctNamesAndValues.get(crntValue);
      strHtmlBfr.append("\n\t<OPTION value=" + crntValue);
      if(crntValue.equals(defaultValue))
        strHtmlBfr.append(" selected");
      strHtmlBfr.append(">" + crntDsply + "</OPTION>");
    }
    strHtmlBfr.append("\n</SELECT>");
    strHtmlBfr.append("\n\t</TD>");
 }

 // Here check boxes are populated by the number of items in the map
 // and selected are set by those form the map
 public void addCheckBoxesInTable(String fieldname, HashMap slctNamesAndValues, HashMap slctdValues)
 {
    SortedMap sortMap = new TreeMap(slctNamesAndValues);
    strHtmlBfr.append("\n\t<TD>");
    for(Iterator itr = sortMap.keySet().iterator(); itr.hasNext();)
    {
      String crntValue = (String) itr.next();
      String crntDsply = (String) slctNamesAndValues.get(crntValue);
      strHtmlBfr.append("\n<INPUT type=checkbox");
      strHtmlBfr.append(" name=\"" + fieldname + crntValue + "\"");
      strHtmlBfr.append(" value=\"" + crntValue + "\"");
      if(Constants.YES_SLCTN.equals((String) slctdValues.get(crntValue)))
        strHtmlBfr.append(" checked");
      strHtmlBfr.append(">");
      strHtmlBfr.append("&nbsp;&nbsp;" +crntDsply);
      strHtmlBfr.append("&nbsp;&nbsp;&nbsp;&nbsp;");
    }
    strHtmlBfr.append("\n\t</TD>");
 }

 public void addLinkButton(String rltvPath, String btnTxt)
 {
    addLinkButton(rltvPath, btnTxt, true);
 }

 public void addLinkButton(String path, String btnTxt, boolean rltvPath)
 {
  strHtmlBfr.append("\n<FORM METHOD=LINK ACTION=");
  if(rltvPath)
    strHtmlBfr.append(path);
  else
    strHtmlBfr.append(Constants.SRVLT_PATH + path);
  strHtmlBfr.append(">\n<INPUT TYPE=submit VALUE=\"");
  strHtmlBfr.append(btnTxt);
  strHtmlBfr.append("\">\n</FORM>");
 }

 public void addBackButton(String btnTxt)
 {
  strHtmlBfr.append("\n<INPUT TYPE=\"Button\" VALUE=\"");
  strHtmlBfr.append(btnTxt);
  strHtmlBfr.append("\" onClick=\"history.go(-1)\">");
 }

 public void addRefreshButton()
 {
  strHtmlBfr.append("\n<INPUT TYPE=\"Button\" VALUE=\"");
  strHtmlBfr.append("Refresh this page");
  strHtmlBfr.append("\" onClick=\"window.location.reload();\">");
 }

 public void addCloseButton(String btnTxt)
 {
  strHtmlBfr.append("\n<INPUT TYPE=\"Button\" VALUE=\"");
  strHtmlBfr.append(btnTxt);
  strHtmlBfr.append("\" onClick=\"window.close()\">");
 }


public void addHiddenInput(String name, String value)
{
    strHtmlBfr.append("\n<INPUT TYPE=hidden NAME=" + name + " VALUE=\"" + value + "\">");
}

 public void addSubmitButton(String id, String name, String value)
 {
    strHtmlBfr.append("\n<INPUT type=submit");
    strHtmlBfr.append((null == id)?Constants.EMPTY_STRING:" id=" + id);
    strHtmlBfr.append((null == name)?Constants.EMPTY_STRING:" name=" + name);
    strHtmlBfr.append((null == value)?Constants.EMPTY_STRING:" value=\"" + value + "\"");
    strHtmlBfr.append(">");
 }

 public void addResetButton(String value)
 {
    strHtmlBfr.append("\n<INPUT type=reset");
    strHtmlBfr.append((null == value)?Constants.EMPTY_STRING:" value=\"" + value + "\"");
    strHtmlBfr.append(">");
 }
}