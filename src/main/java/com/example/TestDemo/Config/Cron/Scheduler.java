package com.example.TestDemo.Config.Cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class Scheduler {
    @Scheduled(cron = "${cron.task}") //10 segundos
    public void scheduleTaskUsingCronExpression(){
        long now = System.currentTimeMillis();
        // Crear un objeto Date con los milisegundos actuales
        Date currentDate = new Date(now);

        // Formatear la hora en el formato deseado (por ejemplo, "HH:mm:ss")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = sdf.format(currentDate);

        // Imprimir la hora actual formateada
        System.out.println("Hora actual: " + formattedTime);
    }
}
