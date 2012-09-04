/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.util;

public class Util
{
  public static int getIntValueFromReq(String strParamValue, StringBuffer errMesgs)
  {
      strParamValue = (strParamValue != null) ?  strParamValue.trim() : "0";
      strParamValue = (!Constants.EMPTY_STRING.equals(strParamValue)) ? strParamValue : "0";
      if(!isValidPositiveInteger(strParamValue))
      {
	errMesgs.append(strParamValue + " is not a valid positive int number.<BR>");
	return 0;
      }

      int rtrnVal = Integer.parseInt(strParamValue);
      return rtrnVal;
  }


  private static boolean isValidPositiveInteger(String strToParse)
  {
     for(int i = 0; i < strToParse.length(); i++)
     {
	char eachChar = strToParse.charAt(i);
	if(!Character.isDigit(eachChar))
	  return false;
     }
     return true;
  }


  public static double getDoubleValueFromReq(String strParamValue, StringBuffer errMesgs)
  {
      strParamValue = (strParamValue != null) ?  strParamValue.trim() : "0";
      strParamValue = (!Constants.EMPTY_STRING.equals(strParamValue)) ? strParamValue : "0";
      if(!isValidPositiveDouble(strParamValue))
      {
	errMesgs.append(strParamValue + " is not a valid positive double number.<BR>");
	return 0;
      }

      double rtrnVal = Double.parseDouble(strParamValue);
      return rtrnVal;
  }


  private static boolean isValidPositiveDouble(String strToParse)
  {
      int foundDecimal = 0;
      for(int i = 0; i < strToParse.length(); i++)
      {
	char eachChar = strToParse.charAt(i);
	if(eachChar == '.')
	{
	  foundDecimal++;
	  continue;
	}

	if(foundDecimal > 1 || !Character.isDigit(eachChar))
	  return false;
     }
     return true;
  }


  public static double valueToTwoDecimals(double valueToChange)
  {
    return (Math.rint(valueToChange * 100) / 100.0);
  }

  public static double valueToThreeDecimals(double valueToChange)
  {
    return (Math.rint(valueToChange * 1000) / 1000.0);
  }
}