package com.nova.crashreport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;

public class CrashReport implements Thread.UncaughtExceptionHandler {

    private Context context;
    private Thread.UncaughtExceptionHandler defaultHandler;
    private String emailAddress;
    private String emailSubject;

    public CrashReporterHandler(Context context) {
        this.context = context;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public CrashReport setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public CrashReport setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
        return this;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // Lee el error
        String errorMessage = throwable.getMessage();
        String stackTrace = Log.getStackTraceString(throwable);

        // Muestra un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage)
                .setPositiveButton(
                        "Enviar informe",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Envía el informe de error por correo electrónico
                                sendErrorReport(
                                        errorMessage, stackTrace, emailAddress, emailSubject);
                            }
                        })
                .setNegativeButton(
                        "Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cierra la aplicación
                                System.exit(1);
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dialog.show();

        // Llama al manejador de excepciones predeterminado
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }

    private void sendErrorReport(
            String errorMessage, String stackTrace, String emailAddress, String emailSubject) {
        // Envia por correo el informe de error
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + emailAddress));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, errorMessage + "\n\n" + stackTrace);
        context.startActivity(Intent.createChooser(emailIntent, "Enviar informe de error"));
    }
}
