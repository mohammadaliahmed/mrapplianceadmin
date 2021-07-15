package com.appsinventiv.mrapplianceadmin.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;


import com.appsinventiv.mrapplianceadmin.ApplicationClass;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by AliAh on 14/05/2018.
 */

public class CommonUtils {


    private CommonUtils() {
        // This utility class is not publicly instantiable
    }


    public static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(ApplicationClass.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String getMonthAndYear(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);


        return DateFormat.format("MMM yyy", smsTime).toString();

    }

    public static String getDayNumber(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("dd", smsTime).toString();
    }


    public static String getDateOnly(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);


        return DateFormat.format("d/M/yyy", smsTime).toString();

    }
    public static String getFullAddress(Context context, Double lat, Double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return address;
    }
    public static String getTimeOnly(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();


        return DateFormat.format("hh:mm aa", smsTime).toString();

    }


    public static void sendMessage(String number, String message) {
//        String request = "https://bulksms.com.pk";
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(request)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        RetrofitClient client = retrofit.create(RetrofitClient.class);
//        Call<Example> call = client.createTask(
//                "923458441448",
//                "5214",
//                "Mr Appliance"
//                , number,
//                message
//        );
//        call.enqueue(new Callback<Example>() {
//            @Override
//            public void onResponse(Call<Example> call, Response<Example> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Example> call, Throwable t) {
//
//            }
//        });

    }

    public static String getNameOfDay(int year, int month, int day) {

        String dateString = String.format("%d-%d-%d", year, month, day);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then get the day of week from the Date based on specific locale.
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

        return dayOfWeek;
    }

    public static String getMonthNumber(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("MM", smsTime).toString();

    }

    public static String getMonthNameAbbr(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    public static String getDayName(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        int day = smsTime.get(Calendar.DAY_OF_WEEK);
        String dayName = "";
        switch (day) {
            case 1:
                dayName = "Sunday";
                break;
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Saturday";
        }


        return dayName;

    }

    public static String getFormattedPrice(Object price) {
        DecimalFormat formatter = new DecimalFormat("##,##,###");
        String formattedPrice = formatter.format(price);
        return formattedPrice;
    }

    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static String getFormattedDateOnly(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("dd-MMM-yyyy, h:mm:aa", smsTime).toString();

    }
    public static String getFormattedDateOnlyy(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("yyyyMMdd", smsTime).toString();

    }

    public static String getDay(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("dd", smsTime).toString();

    }

    public static String getMonthName(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("MMM", smsTime).toString();

    }


    public static String getMonth(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("MM", smsTime).toString();

    }

    public static String getYear(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("yyyy", smsTime).toString();

    }


    public static String getFormattedTime(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("h:mm aa", smsTime).toString();

    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) ApplicationClass.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
