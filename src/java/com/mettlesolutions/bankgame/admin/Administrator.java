/**
 * Title:        Game
 * Description:  Bank
 * Copyright:    Copyright (c) 2001
 * Company:      Mettle Solutions
 * @author       Kishore Metla
 * @version      1.0
 */

package com.mettlesolutions.bankgame.admin;

import com.mettlesolutions.bankgame.util.Constants;

/*import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;*/
import java.io.IOException;
//import java.util.StringTokenizer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpSession;


public class Administrator extends HttpServlet implements SingleThreadModel{

    @Override
    public void doPost(HttpServletRequest servletResponse, HttpServletResponse servletRequest)	throws ServletException, IOException {
      	String userName = servletResponse.getParameter(Constants.ARG_USERNAME);
	    String password = servletResponse.getParameter(Constants.ARG_PASSWORD);

        HttpSession httpSession = servletResponse.getSession(true);

	    httpSession.setMaxInactiveInterval(Constants.ADMIN_MAX_INACTIVE_INTERVAL);
        
        // redirect to WEB_ROOT/adminInput
        if(userName.equals("Administrator") && password.equals("cesscess")){
          httpSession.setAttribute(Constants.ARG_USERNAME, userName);
          servletRequest.sendRedirect(Constants.SRVLT_PATH + "adminInput");
        }
        else{
          httpSession.setAttribute(Constants.ARG_INVALID_USERNAME, userName);
          servletRequest.sendRedirect(Constants.SRVLT_PATH + "adminLogin");
        }

    }
/*
    private boolean validAdmin(String usrnm, String pswd) throws IOException {
        // load the username file directory
        File usrnmFileDrctrs = new File(Constants.USERNAMES_FILE_LCTN);

        // Create the username parent directory 
        if(!usrnmFileDrctrs.exists()){
            usrnmFileDrctrs.mkdirs();
        }
        
      	File usrnmFile = new File(Constants.USERNAMES_FILENAME);

        // Create a new userNameFile and place Admin login=password
        if(!usrnmFile.exists()) {
            //System.out.println("Cannot find the file ->" + Constants.USERNAMES_FILENAME + ".");
            if(usrnm.equals(Constants.ADMIN_USERNAME) && (pswd.equals(Constants.ADMIN_USERNAME))) {
                usrnmFile.createNewFile();
                PrintWriter fileWrtr = new PrintWriter(new BufferedWriter(new FileWriter(usrnmFile)));
                fileWrtr.println("Administrator=Administrator");
                fileWrtr.close();
                return true;
            }
            else{
                return false;
            } 
        }

       if(!usrnm.equals(Constants.ADMIN_USERNAME)){
          return false;
       }

       BufferedReader bfrdRdr = new BufferedReader(new FileReader(usrnmFile));
       String lineFromFile = bfrdRdr.readLine();

       while(null != lineFromFile){
           StringTokenizer strTok = new StringTokenizer(lineFromFile, "=");
           if(usrnm.equals(strTok.nextToken()) && pswd.equals(strTok.nextToken())){
             bfrdRdr.close();
             return true;
           }

           lineFromFile = bfrdRdr.readLine();
        }

        bfrdRdr.close();
        return false;
    }
 
 */
}
