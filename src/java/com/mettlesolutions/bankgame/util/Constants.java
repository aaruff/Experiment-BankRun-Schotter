/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.util;
//import java.io.File;

public class Constants {
    //public final static String SERVER_DETAILS_FILE = "C:" + File.separator + "bank-game" + File.separator + "config" + File.separator + "ServerDetails.config";
    //public final static String SERVER_DETAILS_FILE = "/usr/local/Cellar/tomcat6/6.0.33/libexec/webapps/bank-game/config/ServerDetails.config";
    public final static String SERVER_DETAILS_FILE = "/var/lib/tomcat6/webapps/bank-game/config/ServerDetails.config";

    // bank-game is the mapping registered in web.xml
    //public final static String SRVLT_PATH = ServerProperties.SERVER_ADDR + "bank-game/";
    public final static String SRVLT_PATH = ServerProperties.getServerAddress() + "bank-game/";

    public final static String CLCLTR_BTN_LINK_LOC = ServerProperties.SERVER_ADDR + "bank-game/ClcltrBtnLink/";

    public final static String INSTRCTNS_LINK_LOC = ServerProperties.SERVER_ADDR + "bank-game/Instructions/";
    public final static String INSTRCTNS_LCTN = ServerProperties.APP_HOME + "Instructions/";
    public final static String REPORTS_LCTN = ServerProperties.REPORT_LOCATION;

    public final static String CLCLTR_BTN_FILE = "CalculatorButton.html";
    public final static String ADMIN_FILE_LCTN = ServerProperties.CONFIG_LOCATION;
    public final static String ADMIN_FILENAME = ADMIN_FILE_LCTN + "CrntGameValues.data";
    public final static String USERNAMES_FILE_LCTN = ServerProperties.CONFIG_LOCATION;
    public final static String USERNAMES_FILENAME = USERNAMES_FILE_LCTN + "Usernames.data";

    public final static String ADMIN_USERNAME = "Administrator";
    public final static String ARG_USERNAME = "username";
    public final static String ARG_PASSWORD = "password";
    public final static String ARG_INVALID_USERNAME = "invalidusername";
    public final static String ARG_NUM_OF_PLAYERS = "numofplayers"; // int
    public final static String ARG_EACH_DEPOSIT = "eachdeposit"; // double
    public final static String ARG_ECONOMY_RATE = "economyrate"; // double
    public final static String ARG_PROMISED_RATE = "promisedrate"; // double
    public final static String ARG_NUM_OF_BANKS = "numofbanks"; // int
    public final static String ARG_TIME_PERIOD = "timeperiod"; // int
    public final static String ARG_NUM_OF_ROUNDS = "numofrounds"; // int
    public final static String ARG_CONVERSION_RATE = "conversionrate"; // double
    public final static String ARG_SHOW_UP_FEE = "showupfee"; // double
    public final static String ARG_INSURANCE_RATE = "insurancerate"; // double
    public final static String ARG_MODE = "mode"; // String
    public final static String ARG_WITHDRAWL_TIME = "withdrawltime"; // int
    public final static String ARG_CRNT_ROUND = "crntround"; // int
    public final static String ARG_PLAYER_INPUT = "playerinput"; // int
    public final static String ARG_RND_RPRT_LVL = "RoundReportLevel"; // int
    public final static String ARG_PRD_RPRT_LVL = "PeriodReportLevel"; // int
    public final static String ARG_CLCLTR_CHOICE = "clcltrChoice"; // int
    public final static String ARG_RPRT_TYPE = "reportType"; // int
    public final static String ARG_INSTRCTN_FILE = "instrctnFile"; // String
    public final static String ARG_PAGE_RELOAD = "pageReload"; // String

    public final static String ARG_CRNT_ROUND_NUM = "crntroundnum";
    public final static String ARG_CRNT_SQNTL_TIME_PERIOD = "crntsqntltimeperiod";

    public final static String DATA_HANDLER = "datahandler";
    public final static String MAX_NUM_OF_BANKS = "5";

    public final static String EMPTY_STRING = "";
    public final static String BLANK_STRING = " ";
    public final static String NONE_STRING = "NONE";
    public final static String POST_METHOD = "POST";
    public final static String TEXT_HTML_CONTENT_TYPE = "text/html";
    public final static String YES_SLCTN = "Yes";
    public final static String NO_SLCTN = "No";
    public final static String SUBMIT = "submit";
    public final static String RESET = "reset";

    public final static String STR_SMLTNS = "Simultaneous";
    public final static String STR_SQNTL = "Sequential";
    public final static int SMLTNS = 1;
    public final static int SQNTL = 2;

    public final static int RND_LOW = 1;
    public final static int RND_MEDIUM = 2;
    public final static int RND_HIGH = 3;
    public final static int PRD_LOW = 4;
    public final static int PRD_MEDIUM = 5;
    public final static int PRD_HIGH = 6;
    public final static int COMPLETE_REPORT = 7;

    public final static int CLCLTR_BY_AVG = 0;
    public final static int CLCLTR_BY_MODE = 1;
    public final static int CLCLTR_BY_INFO_LVL = 2;

    public final static int RPRT_NONE = 0;
    public final static int RPRT_EXL = 1;
    public final static int RPRT_TXT = 2;
    public final static int RPRT_BOTH = 3;

    public final static String PARAM_CLCLTR_TEMP = "values";
    public final static String PARAM_RCRD_SPRTR = "|";
    public final static String PARAM_PLYR_VLU_SPRTR = "-";

    public final static int ADMIN_MAX_INACTIVE_INTERVAL = -1; // unlimited
    public final static int PLAYER_MAX_INACTIVE_INTERVAL = -1; // unlimited

    public final static String BKGRND_CLR_SILVER = "silver";
    public final static String BKGRND_CLR_GRAY = "gray";
    public final static String TEXT_CLR_BLACK = "black";
    public final static String TEXT_CLR_ORNGRED = "orangered";
    public final static String TEXT_CLR_RED = "red";
    public final static String TEXT_CLR_BLUE = "blue";
    public final static String TEXT_CLR_GREEN = "green";
    public final static String TEXT_CLR_MAROON = "maroon";

    public final static int TEXT_BOX_SIZE_FIFTEEN = 15;
    public final static int TEXT_BOX_SIZE_THIRTYFIVE = 35;
    public final static int TEXT_BOX_MAX_LENGTH_TWENTY = 20;
    public final static String TEXT_BOX_TYPE_TEXT = "text";
    public final static String TEXT_BOX_TYPE_PASSWORD = "password";

    public final static String ALIGN_CENTER = "center";
    public final static String ALIGN_LEFT = "left";

    public final static int FONT_SIZE_ONE = 1;
    public final static int FONT_SIZE_TWO = 2;
    public final static int FONT_SIZE_THREE = 3;
    public final static int FONT_SIZE_FOUR = 4;
    public final static int FONT_SIZE_FIVE = 5;
    public final static int FONT_SIZE_SIX = 6;

    public final static int TBL_BORDER_SIZE_TEN = 10;
    public final static int TBL_CELL_SPCNG_SIZE_TEN = 10;
    public final static int TBL_CELL_PDNG_SIZE_ONE = 1;

    public final static boolean UNDERLINE = true;
    public final static boolean BOLD = true;
    public final static boolean ITALIC = true;

    public final static char[] ALLWD_USRNM_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                                           'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                                           'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                                           'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                                           'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                                           'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8',
                                           '9', '0'};


}
