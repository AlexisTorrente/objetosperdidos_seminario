package com.example.tp_grupo6_seminario;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Notification;
import android.content.Context;
import android.os.Build;

public class Notificaciones extends Application {
    private static final String CHANNEL_ID = "mi_canal_id";
    private static final String CHANNEL_NAME = "Mi Canal";
    private static final String CHANNEL_DESCRIPTION = "Descripción del Canal";

    public static void mostrarNotificacion(Context context, String titulo, String mensaje) {
        // Creacion de NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Verificar que el dispositivo tenga Android Oreo (API 26+) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Crea el canal de notificación para versiones de Android Oreo y superiores
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }

        // Armo la notificación
        Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setAutoCancel(true);  // Cierra la notificación cuando el usuario hace clic en ella

        // Muestro la notificación
        notificationManager.notify(1, builder.build());
    }
}

/*
public class Notificaciones extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        System.out.println("El token no fue generado");
                        return;
                    }

                    // Obtener el token exitosamente
                    String token = task.getResult();
                    System.out.println("El token es: " + token);
                });
    }
}

*/