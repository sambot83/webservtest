package com.ber.quickstartmaven.quickstart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;





/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello V2 World!" );
        try{
        	
        	 
        	// JSONParser parser = new JSONParser();
        	//String pathJsonIn="jsonreservation";
           // String pathJsonIn="sam.confirmation.suppression.reservations";
        	String pathJsonIn="sam.affiche.reservations";
        	//Object obj = parser.parse(new FileReader());
        
           String obj=readFile(pathJsonIn);
           
           
           JSONObject Jreqobj = new JSONObject(obj);
           
        
          Connection connectionag=getConnection();
              AgentWS agent= new AgentWS(connectionag);
              
              System.out.println(agent.repondre(obj));
              
            
            //JSONObject Jresult=requete.getJSONObject("result");
			//String ACTION=Jresult.getString("action");
          
             // System.out.println(agent.Action_Get_Reservation());
            //  System.out.println(agent.Action_Delete_Reservation());
       
        
        
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
    }
    
    
   
    static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
    
   
  
   
    private static Connection getConnection() throws URISyntaxException, SQLException {
      
    	//String DATABASE_URLSTRING="postgres://odmjordlrnmlzz:612a8061c5f37732eab41b0538c60fbbf2a30eec9ee4e977a18e8e35c6a4c77d@ec2-54-225-68-71.compute-1.amazonaws.com:5432/d2s08uq82ndrje"; 
    	String DATABASE_URLSTRING="postgres://voxufhqziqcrro:23e9024022f3f9bd6c8270e855b050ed005af2c15d689a073a4bacae76d6bfdc@ec2-184-73-167-43.compute-1.amazonaws.com:5432/dfs471uekvsht9";
    	URI dbUri = new URI(DATABASE_URLSTRING);

    	
    	//URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
       // String username = "odmjordlrnmlzz";
        String password = dbUri.getUserInfo().split(":")[1];
      //  String password ="612a8061c5f37732eab41b0538c60fbbf2a30eec9ee4e977a18e8e35c6a4c77d";
       String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath()+"?sslmode=require";
       //?sslmode=require
       System.out.println(dbUrl);
       
       //String dbUrl ="postgres://odmjordlrnmlzz:612a8061c5f37732eab41b0538c60fbbf2a30eec9ee4e977a18e8e35c6a4c77d@ec2-54-225-68-71.compute-1.amazonaws.com:5432/d2s08uq82ndrje";
       // String dbUrl="jdbc:postgresql://host/database?user="+username+"&password="+password+"&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        return DriverManager.getConnection(dbUrl, username, password);
    }
    
}
