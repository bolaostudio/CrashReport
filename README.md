# CrashReport
Un repositorio muy simple para manejar los reportes de errores en tu aplicación y enviarlos por correo electrónicos.

#### Java

Es necesario agregar una clase de Application donde se use `CrashReport`.
```java

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Crea un objeto CrashReport y establecerlo como manejador de excepciones predeterminado
        CrashReport handler = new CrashReport(getApplicationContext())
                .setEmailAddress("tucorreo@gmail.com")
                .setEmailSubject("Crash/MiApp");
        Thread.setDefaultUncaughtExceptionHandler(handler);
        }
}

```
