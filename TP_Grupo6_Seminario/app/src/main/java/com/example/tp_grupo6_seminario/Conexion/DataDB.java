package com.example.tp_grupo6_seminario.Conexion;

public class DataDB {
    //Información de la BD

/*
    public static String host="southernforce.com.ar";
    public static String port="3306";
    public static String nameBD="southern_TP_FINAL_TSSI_G4";
    public static String user="southern_G4";
    public static String pass="TSSI@2023.";

 */



    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10688909";
    public static String user="sql10688909";
    public static String pass="cszPnh99JW";





    //Información para la conexion
    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/"+nameBD;
    public static String driver = "com.mysql.jdbc.Driver";
}
