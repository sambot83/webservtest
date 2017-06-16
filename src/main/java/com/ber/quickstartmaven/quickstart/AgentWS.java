package com.ber.quickstartmaven.quickstart;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgentWS {

		Connection connection; 
	
	public AgentWS(Connection connection)
	{
		this.connection=connection;
	}

    
	
	
	public String repondre(String JSONIN)
	{
		String rep="ws : not available";
		if(this.connection==null)
		{
			return "connection avec DataBase non etablie";
		}
	
		try {
			 JSONObject Jreqobj = new JSONObject(JSONIN);
			
			JSONObject result=Jreqobj.getJSONObject("result");
			String ACTION=result.getString("action");
			   System.out.println(ACTION);
			   
			if(ACTION.compareTo("sam.confirmation.reservation")==0)
		        {
				return Action_Insert_Reservation( result);
		        }
       
            if(ACTION.compareTo("sam.delete.reservations")==0)
            {
            	return Action_Delete_Reservation();
            }
            if(ACTION.compareTo( "test.database")==0)
            {
            	return TestDataBase();
            }
            if(ACTION.compareTo( "sam.get.reservations")==0)
            {
            	return Action_Get_Reservation();
            }
            
            
            
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		return rep;
	}
	
	public String Action_Insert_Reservation(JSONObject result)
	{
		String message="pb reservation";
		JSONArray contexts;
		try {
			contexts = (JSONArray) result.get("contexts");
				 Reservation newreservation=ParseReservation(contexts);
				 if(newreservation==null)
			   	 {
			   		return  GestionMauvaiseResa(contexts);
			   	 }
			   	 else
			   	 {
			   		 insertreservation( newreservation);
			   		 
			   		 return "reservation enregistrée "+newreservation.ToString();
			   		 
			   	 }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
   	 
		
		return message;
	}
	
	 Reservation ParseReservation(JSONArray contexts)
	    {
	    	try
	{
	    		
	    		// System.out.println((String) requete.toJSONString());
	    	
	    		 //JSONArray contexts = new JSONArray((String)requete.get("contexts"));
	    		 if(contexts.length()==0)
	    		 {
	    			 return null;
	    		 }
	    		 
	    		  for(int i=0; i<contexts.length();i++)
	    		  {
	    		JSONObject context =(JSONObject) contexts.get(i);
	    		String nomcontext=(String) context.get("name");
	    		if(nomcontext.compareTo("reservation")==0)
	    		{
	    		JSONObject parameters=(JSONObject) context.get("parameters");
	    		String nom=(String) parameters.get("nom");
	    		String nombre=(String) parameters.get("number");
	    		String lejour =(String) parameters.get("lejour");
	    		String phonenumber=(String) parameters.get("phone-number");
	    		String heure=(String) parameters.get("heure");	
	    		
	    
	    if(nom.length()>80)
	    {return null;  }
	    
	    if(phonenumber.length()>80)
	    {return null;  }
	    
	  
	    
	    
	    try
	    {
	    	int NOMBRE =(int) Double.parseDouble(nombre);
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
	    	String dateInString = lejour+" "+heure;
	    	Date date = sdf.parse(dateInString);

	    	        Calendar debut = Calendar.getInstance();
	    	debut.setTime(date);
	    	Calendar fin=Calendar.getInstance();
	    	fin.setTime(date);
	    	fin.add(Calendar.HOUR, 1);
	    	Reservation resa = new Reservation(debut,fin,nom,phonenumber,NOMBRE,"jet");
	    //	System.out.println(resa.ToString());
	    	return resa;
	    	
	    }
	    catch(Exception e)
	    {
	    	
	    	e.printStackTrace();
	    }
	    
	    
	    
	    
	    
	    		}
	    		  }
	    		
	    		
	    		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	    	return null;
	    }
	    
	  void insertreservation(Reservation resa)
	    {
		  createTableReservationIfNotExist();
	    	 
	    	
	    	String SQL = "INSERT INTO reservations(nom,debut,fin,moyen,quantite,telephone) "
	                + "VALUES(?,?,?,?,?,?)";
	 
	      
	 
	        try 
	                
	        {
	 PreparedStatement pstmt = this.connection.prepareStatement(SQL);
	 
	            pstmt.setString(1, resa.nom);
	            pstmt.setTimestamp(2,getSqlTimeStamp(resa.debut) );
	            pstmt.setTimestamp(3,getSqlTimeStamp(resa.fin) );
	            pstmt.setString(4, resa.Moyen);
	            pstmt.setInt(5, resa.nombre);
	            pstmt.setString(6, resa.phone);
	            pstmt.executeUpdate();
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	    
	    	
	    	
	    	
	    }
	    
	  static java.sql.Timestamp getSqlTimeStamp(Calendar date) {

	        
	    	return new java.sql.Timestamp(date.getTimeInMillis());

	    }
	 void createTableReservationIfNotExist()
	    {
	    	try {
				
				 
	            Statement stmt = connection.createStatement();
	           // stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
	            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reservations (nom varchar(80),debut timestamp, fin timestamp,moyen varchar(80),quantite int,telephone varchar(80))");
	           
	    			
	    		} catch (SQLException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    }
	    
	    
	  
	public  String GestionMauvaiseResa(JSONArray contexts)
	    {
	    	try
	    	{
	    	    		
	    	    		// System.out.println((String) requete.toJSONString());
	    	    	
	    	    		 //JSONArray contexts = new JSONArray((String)requete.get("contexts"));
	    	    		
	    	    		 
	    	    		  for(int i=0; i<contexts.length();i++)
	    	    		  {
	    	    		JSONObject context =(JSONObject) contexts.get(i);
	    	    		String nomcontext=(String) context.get("name");
	    	    		if(nomcontext.compareTo("reservation")==0)
	    	    		{
	    	    		JSONObject parameters=(JSONObject) context.get("parameters");
	    	    		String messageoriginal=parameters.toString();
	    	            return "demande reservation envoyée par mail "+messageoriginal;
	    	    
	    	 
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    		}
	    	    		  }
	    	    		
	    	    		
	    	    		
	    	}
	    	catch(Exception e)
	    	{
	    		
	    	}
	    	return "probleme parsing reservation";
	    }
	   
	public  String  TestDataBase()
	    {
		  String rep="problème connecte database";
	    	try {
				
				 
	        Statement stmt = connection.createStatement();
	        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
	        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
	        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
	        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
	       // String res="";
	        while (rs.next()) {
	        	//res+="Read from DB: " + rs.getTimestamp("tick")+"\n";
	            return "Read from DB: " + rs.getTimestamp("tick");
	        }
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  return rep;
	    }
	    
	  
	public  String Action_Delete_Reservation()
	    {
		  String rep="probleme suppression reservations !";
	    	try {
				
				 
	        Statement stmt = connection.createStatement();
	        stmt.executeUpdate("DROP TABLE IF EXISTS reservations");
	     
				return "reservations supprimées";
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return rep;
	    }
	  
	  
	public  String Action_Get_Reservation()
	  {
		  String rep="";
	
		
	    	try {
				
				 
	        Statement stmt = connection.createStatement();
	       
	        ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");
	       // String res="";
	        while (rs.next()) {
	        	//res+="Read from DB: " + rs.getTimestamp("tick")+"\n";
	        	//java.sql.Timestamp TSDEBUT = rs.getTimestamp("debut");
	        	
	        	//rep+=rs.getString(3)+" "+rs.getTimestamp("debut")+"\n";
	        	
	            rep+= rs.getString("nom")+" à "+ rs.getTimestamp("debut")+"\n";
	            
	            
	            
	        }
				return rep;
				
				
			} catch (SQLException e) {
				// TODO Auto-generated cath block
				//e.printStackTrace();
			}
		 
		  return "pas de reservations";
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
}
