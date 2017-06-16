package com.ber.quickstartmaven.quickstart;

import java.text.SimpleDateFormat;
import java.util.*;

public class Reservation {
Calendar debut;
Calendar fin;
String nom;
String phone;
int nombre=0;
String Moyen="";

public Reservation(Calendar debut,Calendar fin,String nom,String phone,int nombre,String Moyen)
{
	this.debut=debut;
	this.fin=fin;
	this.nom=nom;
	this.phone=phone;
	this.nombre=nombre;
	this.Moyen=Moyen;
}
public String ToString()
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

	

return String.valueOf("Reservation au nom de "+nom+" "+nombre+" "+Moyen+" le "+sdf.format(debut.getTime())+" tel "+phone);
}
	
}
