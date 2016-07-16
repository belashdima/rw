package com.belashdima.rememberwords;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by belashdima on 18.03.16.
 */
public class NotifyTimeGetter
{
    public static Date nextNotificationTimeGetter(int numberOfNotification) {
        ArrayList<Double> notifyMoments = createNotifyMoments(numberOfNotification);
        System.out.println(notifyMoments);
        int minutesDifference = (int) Math.round(notifyMoments.get(numberOfNotification) - notifyMoments.get(numberOfNotification-1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minutesDifference); //minus number would decrement the minutes
        return cal.getTime();
    }

    private static ArrayList<Double> createNotifyMoments(int numberOfNotification) {
        ArrayList<Double> notifyMoments = new ArrayList<Double>();
        notifyMoments.add(0.0);

        int i=0;
        for (; i<12; i++) {
            notifyMoments.add(func(i));
        }

        if(numberOfNotification>=12) {
            while (i<numberOfNotification) {
                Random r = new Random();
                double rangeMin = 21600;
                double rangeMax = 43200;
                double randomValue = rangeMin + (rangeMax-rangeMin)*r.nextDouble();
                notifyMoments.add(randomValue);
                i++;
            }
        }

        return notifyMoments;
    }

    private static double func(double x) {
        return myExp(x)*10; //Math.exp(x)/0.1;
    }

    public static double myExp(double x) {
        ArrayList<Double> exps = new ArrayList<>();
        for (double i = 2.5; i >= 1.0 ; i=i-0.07) {
            exps.add(i);
        }
        // exp.size()=22

        double sum = 1;
        for (int i = 0; i < x; i++) {
            sum = sum * exps.get(i);
        }

        return sum;
    }
}
