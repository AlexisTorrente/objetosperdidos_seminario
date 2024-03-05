package com.example.tp_grupo6_seminario.adapter;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tp_grupo6_seminario.R;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class FTPUploader {
    private static final String FTP_SERVER = "ftp.southernforce.com.ar"; //"51.222.84.216";
    private static final String FTP_USER = "usr_seminatio_tpfinal@southernforce.com.ar";
    private static final String FTP_PASSWORD = "seminario#2023.";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    public static boolean subirFoto(Context contexto, String idPublicacion, String rutaImagen) {
        Log.i("INFO_MISPUBLI"," Llegue hasta subirFoto() ");

        FTPClient ftpClient = new FTPClient();
        FileInputStream archivoLocal = null;

        File archivo = new File(rutaImagen);
        String nombreArchivo = archivo.getName();

        String rutaRemoto = "/"+idPublicacion+"/";

        try {

            Log.i("INFO_MISPUBLI"," entramos al try-catch ");

            boolean exito=false;

            ftpClient.connect(FTP_SERVER);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            boolean exitoLogin = ftpClient.login(FTP_USER, FTP_PASSWORD);
            if (!exitoLogin) {
                Log.i("INFO_MISPUBLI","Error al iniciar sesión en el servidor FTP");
                return false;
            }else{
                Log.i("INFO_MISPUBLI","Me logue correctamente al FTP");
            }

            ftpClient.changeWorkingDirectory(rutaRemoto);

            // Verificar si la rutaRemoto es un directorio
            int ftpClientReplyCode = ftpClient.getReplyCode();
            String ftpClientReplyString = ftpClient.getReplyString();

             //Antes de intentar acceder al archivo, ver que tengan permiso
            if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Si no hay permiso, lo pide (medio al pedo tenerlo acá (ponerlo antes de subir la imagen))
                ActivityCompat.requestPermissions((Activity) contexto, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                exito = false;
                Log.i("INFO_MISPUBLI"," No tenía permiso");

            } else {
                ftpClient.enterLocalPassiveMode();

                if (FTPReply.isPositiveCompletion(ftpClientReplyCode)) {
                    Log.i("INFO_MISPUBLI", "La rutaRemoto es un directorio válido en el servidor FTP");
                }else{
                    Log.i("INFO_MISPUBLI", "La rutaRemoto NO es un directorio válido en el servidor FTP");
                    boolean exitoCreacion = ftpClient.makeDirectory(rutaRemoto);
                    Log.i("INFO_MISPUBLI", "Exito al crear nuevo directorio: "+exitoCreacion);

                }
                    rutaRemoto +=  nombreArchivo;
                    // Ahora puedes intentar subir el archivo al directorio
                    archivoLocal = new FileInputStream(new File(rutaImagen));
                    exito = ftpClient.storeFile(rutaRemoto, archivoLocal);
                    archivoLocal.close();

                    Log.i("INFO_MISPUBLI"," rutaImagen: "+ rutaImagen);
                    Log.i("INFO_MISPUBLI"," rutaRemoto: "+ rutaRemoto);
                    Log.i("INFO_MISPUBLI"," nombreArchivo: "+ nombreArchivo);
                    ftpClientReplyString = ftpClient.getReplyString();
                    Log.i("INFO_MISPUBLI"," ftpClientReplyString: "+ ftpClientReplyString);

                    archivoLocal = new FileInputStream(new File(rutaImagen));
                    exito = ftpClient.storeFile(rutaRemoto, archivoLocal); // <--- El problema en cuestión
                    archivoLocal.close();

                    Log.i("INFO_MISPUBLI"," archivoLocal: "+ archivoLocal.toString());
                    Log.i("INFO_MISPUBLI"," exito tiene: "+ exito);

                    ftpClientReplyCode = ftpClient.getReplyCode();
                    String ftpClientReplyString2 = ftpClient.getReplyString();
                    Log.i("INFO_MISPUBLI"," ftpClientReplyCode: "+ ftpClientReplyCode);
                    Log.i("INFO_MISPUBLI"," ftpClientReplyString: "+ ftpClientReplyString2);

            }

            return exito;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR_MISPUBLI8"," Excepcion: "+e.getMessage());

        } finally {
           cerrarConexion(ftpClient);
        }
        return false;
    }

    public static String obtenerPrimeraImagen(Context contexto, ImageView imageView, String idPublicacion, String rutaImagen) {
        Log.i("INFO_MISPUBLI", "Voy a obtener la imagen de esta publicación ID:" + idPublicacion);

        FTPClient ftpClient = new FTPClient();
        String rutaRemota = "/" + idPublicacion + "/";
        Log.i("INFO_MISPUBLI", "Ruta remota:" + rutaRemota);

        File archivo = new File(rutaImagen);
        String nombreArchivo = archivo.getName();

        String ValorDevuelto = "";
        try {
            // Establecer conexión con el servidor FTP
            conectar(ftpClient);

            // Cambiar al directorio remoto correspondiente a la publicación
            boolean cambioDirectorio = ftpClient.changeWorkingDirectory(rutaRemota);

            if (!cambioDirectorio) {
                Log.i("INFO_MISPUBLI", "No se encontró el directorio remoto para la publicación");
                cerrarConexion(ftpClient);

                return "";
            }else{
                Log.i("INFO_MISPUBLI", "Voy a intentar tener la publicación");

                ftpClient.enterLocalPassiveMode();

                // Listar archivos remotos en el directorio
                FTPFile[] archivosRemotos = ftpClient.listFiles();

                String nombreArchivoRemoto = nombreArchivo;

                if (archivosRemotos != null && archivosRemotos.length > 0) {
                    Log.i("INFO_MISPUBLI", "Listo archivos en el directorio remoto:");
                    int i= 0;

                    for (FTPFile archivoRemoto : archivosRemotos) {
                        i=i+1;

                        nombreArchivoRemoto = archivoRemoto.getName();
                        Log.i("INFO_MISPUBLI", "Nombre de archivo: " + nombreArchivoRemoto);
                    }

                    if(i<3){
                        cerrarConexion(ftpClient);
                        return "";
                    }
                }

                rutaRemota += nombreArchivoRemoto;
                Log.i("INFO_MISPUBLI", "Ruta remota:" + rutaRemota);

                if (archivosRemotos != null && archivosRemotos.length > 0) {
                    // Obtener el nombre de la primera imagen en el directorio

                    Log.i("INFO_MISPUBLI", "Nombre de la primera imagen:" + nombreArchivoRemoto);

                    // Obtener la ruta local para guardar la imagen
                    String rutaLocal = contexto.getCacheDir() + File.separator + nombreArchivoRemoto;
                    Log.i("INFO_MISPUBLI"," Ruta Local donde se guardará la imagen: "+ rutaLocal);

                    String ftpClientReplyString2 = ftpClient.getReplyString();
                    Log.i("INFO_MISPUBLI"," ftpClientReplyString: "+ ftpClientReplyString2);

                    boolean descargaExitosa = false;

                    try{

                        // Descargar la imagen al almacenamiento local
                        FileOutputStream outputStream = new FileOutputStream(rutaLocal);
                        descargaExitosa = ftpClient.retrieveFile(nombreArchivoRemoto, outputStream);
                        outputStream.close();

                   } catch (FTPConnectionClosedException ftpException) {
                        Log.e("ERROR_MISPUBLI1", "FTP Connection Closed: " + ftpException.getMessage());
                        ftpException.printStackTrace();
                        return "";
                    }

                    ftpClientReplyString2 = ftpClient.getReplyString();
                    Log.i("INFO_MISPUBLI"," ftpClientReplyString: "+ ftpClientReplyString2);


                    if (descargaExitosa) {
                        Log.i("INFO_MISPUBLI", "Descarga exitosa de la primera imagen");


                        try{
//                            ((Activity) contexto).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Glide.with(contexto)
//                                            .load(new File(rutaLocal))
//                                            .into(imageView);
//                                    Log.i("INFO_MISPUBLI", "Cargué la imagen de la publicación ID:" + idPublicacion);
//                                }
//                            });

                            ValorDevuelto = rutaLocal;

                        } catch (RuntimeException e) {
                            Log.i("ERROR_MISPUBLI13", "Excepcion de tiempo de ejecución: " + e.getMessage());
                        } catch (OutOfMemoryError e) {
                            Log.i("ERROR_MISPUBLI13", "Error de memoria: " + e.getMessage());
                        } catch (Exception ex) {
                            Log.i("ERROR_MISPUBLI13", "Excepcion general: " + ex.getMessage());
                        }

                    } else {
                        Log.i("INFO_MISPUBLI", "Error al descargar la primera imagen");
                    }



                } else {
                    Log.i("INFO_MISPUBLI", "No se encontraron archivos en el directorio remoto");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR_MISPUBLI3", "Excepcion: " + e.getMessage());
        }
        cerrarConexion(ftpClient);

        return ValorDevuelto;
    }

    public static void conectar(FTPClient ftpClient) {
        try {

            ftpClient.connect(FTP_SERVER);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String ftpClientReplyString2 = ftpClient.getReplyString();
            Log.i("INFO_MISPUBLI"," ftpClientReplyString: "+ ftpClientReplyString2);

            // Iniciar sesión en el servidor FTP
            boolean exitoLogin = ftpClient.login(FTP_USER, FTP_PASSWORD);

            if (!exitoLogin) {
                Log.i("INFO_MISPUBLI", "Error al iniciar sesión en el servidor FTP");
            } else {
                Log.i("INFO_MISPUBLI", "Me logueé correctamente al FTP");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR_MISPUBLI1", "Excepcion: " + e.getMessage());
            }
    }
    private static void cerrarConexion(FTPClient ftpClient) {
        try {
            Log.i("INFO_MISPUBLI"," Voy a cerrar conexión ");
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
                Log.i("INFO_MISPUBLI"," Cerre conexión ");

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR_MISPUBLI4",e.getMessage());

        }
    }

    public static BufferedReader descargarArchivo(String path) {
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            Log.i("INFO_MISPUBLI", "br tiene algo" + br.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("INFO_MISPUBLI", "No logre obtener la publicación");
            Log.i("ERROR_MISPUBLI5", e.getMessage());

        }

        return br;
    }

    public InputStream descargarArchivo(String filePath, FTPClient ftpClient) {
        try {
            return ftpClient.retrieveFileStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}

