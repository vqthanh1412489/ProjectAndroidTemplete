package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller.SessionInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.google.android.gms.C1044R;
import com.google.android.gms.common.internal.zzy;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzi;
import com.google.android.gms.common.util.zzl;
import com.google.android.gms.common.util.zzs;
import com.google.android.gms.common.util.zzx;
import com.mp3download.zingmp3.C1569R;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class zze {
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static boolean xb;
    public static boolean xc;
    static boolean xd;
    private static boolean xe;
    static final AtomicBoolean xf;
    private static final AtomicBoolean xg;

    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = zzaqq();
        xb = false;
        xc = false;
        xd = false;
        xe = false;
        xf = new AtomicBoolean();
        xg = new AtomicBoolean();
    }

    zze() {
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return zzc.zzaql().getErrorResolutionPendingIntent(context, i, i2);
    }

    @Deprecated
    public static String getErrorString(int i) {
        return ConnectionResult.getStatusString(i);
    }

    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(Context context) {
        InputStream openInputStream;
        try {
            openInputStream = context.getContentResolver().openInputStream(new Builder().scheme("android.resource").authority(GOOGLE_PLAY_SERVICES_PACKAGE).appendPath("raw").appendPath("oss_notice").build());
            String next = new Scanner(openInputStream).useDelimiter("\\A").next();
            if (openInputStream == null) {
                return next;
            }
            openInputStream.close();
            return next;
        } catch (NoSuchElementException e) {
            if (openInputStream != null) {
                openInputStream.close();
            }
            return null;
        } catch (Exception e2) {
            return null;
        } catch (Throwable th) {
            if (openInputStream != null) {
                openInputStream.close();
            }
        }
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext(GOOGLE_PLAY_SERVICES_PACKAGE, 3);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication(GOOGLE_PLAY_SERVICES_PACKAGE);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            context.getResources().getString(C1044R.string.common_google_play_services_unknown_issue);
        } catch (Throwable th) {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!GOOGLE_PLAY_SERVICES_PACKAGE.equals(context.getPackageName())) {
            zzbp(context);
        }
        int i = !zzi.zzci(context) ? 1 : GOOGLE_PLAY_SERVICES_VERSION_CODE;
        PackageInfo packageInfo = null;
        if (i != 0) {
            try {
                packageInfo = packageManager.getPackageInfo(GOOGLE_PLAY_STORE_PACKAGE, 8256);
            } catch (NameNotFoundException e) {
                Log.w("GooglePlayServicesUtil", "Google Play Store is missing.");
                return 9;
            }
        }
        try {
            PackageInfo packageInfo2 = packageManager.getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 64);
            zzf zzbv = zzf.zzbv(context);
            if (i != 0) {
                if (zzbv.zza(packageInfo, zzd.xa) == null) {
                    Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
                    return 9;
                }
                if (zzbv.zza(packageInfo2, zzbv.zza(packageInfo, zzd.xa)) == null) {
                    Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                    return 9;
                }
            } else if (zzbv.zza(packageInfo2, zzd.xa) == null) {
                Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                return 9;
            }
            if (zzl.zzhh(packageInfo2.versionCode) < zzl.zzhh(GOOGLE_PLAY_SERVICES_VERSION_CODE)) {
                Log.w("GooglePlayServicesUtil", "Google Play services out of date.  Requires " + GOOGLE_PLAY_SERVICES_VERSION_CODE + " but found " + packageInfo2.versionCode);
                return 2;
            }
            ApplicationInfo applicationInfo = packageInfo2.applicationInfo;
            if (applicationInfo == null) {
                try {
                    applicationInfo = packageManager.getApplicationInfo(GOOGLE_PLAY_SERVICES_PACKAGE, GOOGLE_PLAY_SERVICES_VERSION_CODE);
                } catch (Throwable e2) {
                    Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", e2);
                    return 1;
                }
            }
            return !applicationInfo.enabled ? 3 : GOOGLE_PLAY_SERVICES_VERSION_CODE;
        } catch (NameNotFoundException e3) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 1;
        }
    }

    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        switch (i) {
            case C1569R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped /*1*/:
            case C1569R.styleable.com_facebook_login_view_com_facebook_logout_text /*2*/:
            case C1569R.styleable.com_facebook_login_view_com_facebook_tooltip_mode /*3*/:
            case C1569R.styleable.Toolbar_contentInsetStartWithNavigation /*9*/:
                return true;
            default:
                return false;
        }
    }

    private static int zzaqq() {
        return 9877000;
    }

    @Deprecated
    public static boolean zzaqr() {
        return zzi.zzaym();
    }

    @Deprecated
    public static void zzaz(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = zzc.zzaql().isGooglePlayServicesAvailable(context);
        if (isGooglePlayServicesAvailable != 0) {
            Intent zzb = zzc.zzaql().zzb(context, isGooglePlayServicesAvailable, "e");
            Log.e("GooglePlayServicesUtil", "GooglePlayServices not available due to error " + isGooglePlayServicesAvailable);
            if (zzb == null) {
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
            }
            throw new GooglePlayServicesRepairableException(isGooglePlayServicesAvailable, "Google Play Services not available", zzb);
        }
    }

    @Deprecated
    public static int zzbk(Context context) {
        int i = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        try {
            return context.getPackageManager().getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, GOOGLE_PLAY_SERVICES_VERSION_CODE).versionCode;
        } catch (NameNotFoundException e) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return i;
        }
    }

    @Deprecated
    public static void zzbn(Context context) {
        if (!xf.getAndSet(true)) {
            try {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager != null) {
                    notificationManager.cancel(10436);
                }
            } catch (SecurityException e) {
            }
        }
    }

    private static void zzbp(Context context) {
        if (!xg.get()) {
            int zzce = zzy.zzce(context);
            if (zzce == 0) {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            } else if (zzce != GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                int i = GOOGLE_PLAY_SERVICES_VERSION_CODE;
                String valueOf = String.valueOf("com.google.android.gms.version");
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 290).append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ").append(i).append(" but found ").append(zzce).append(".  You must have the following declaration within the <application> element:     <meta-data android:name=\"").append(valueOf).append("\" android:value=\"@integer/google_play_services_version\" />").toString());
            }
        }
    }

    public static boolean zzbq(Context context) {
        zzbt(context);
        return xd;
    }

    public static boolean zzbr(Context context) {
        return zzbq(context) || !zzaqr();
    }

    @TargetApi(18)
    public static boolean zzbs(Context context) {
        if (zzs.zzayt()) {
            Bundle applicationRestrictions = ((UserManager) context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (applicationRestrictions != null && ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(applicationRestrictions.getString("restricted_profile"))) {
                return true;
            }
        }
        return false;
    }

    private static void zzbt(Context context) {
        if (!xe) {
            zzbu(context);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zzbu(android.content.Context r7) {
        /*
        r6 = 1;
        r0 = com.google.android.gms.internal.zzsz.zzco(r7);	 Catch:{ NameNotFoundException -> 0x002e }
        r1 = "com.google.android.gms";
        r2 = 64;
        r0 = r0.getPackageInfo(r1, r2);	 Catch:{ NameNotFoundException -> 0x002e }
        if (r0 == 0) goto L_0x002a;
    L_0x000f:
        r1 = com.google.android.gms.common.zzf.zzbv(r7);	 Catch:{ NameNotFoundException -> 0x002e }
        r2 = 1;
        r2 = new com.google.android.gms.common.zzd.zza[r2];	 Catch:{ NameNotFoundException -> 0x002e }
        r3 = 0;
        r4 = com.google.android.gms.common.zzd.zzd.xa;	 Catch:{ NameNotFoundException -> 0x002e }
        r5 = 1;
        r4 = r4[r5];	 Catch:{ NameNotFoundException -> 0x002e }
        r2[r3] = r4;	 Catch:{ NameNotFoundException -> 0x002e }
        r0 = r1.zza(r0, r2);	 Catch:{ NameNotFoundException -> 0x002e }
        if (r0 == 0) goto L_0x002a;
    L_0x0024:
        r0 = 1;
        xd = r0;	 Catch:{ NameNotFoundException -> 0x002e }
    L_0x0027:
        xe = r6;
    L_0x0029:
        return;
    L_0x002a:
        r0 = 0;
        xd = r0;	 Catch:{ NameNotFoundException -> 0x002e }
        goto L_0x0027;
    L_0x002e:
        r0 = move-exception;
        r1 = "GooglePlayServicesUtil";
        r2 = "Cannot find Google Play services package name.";
        android.util.Log.w(r1, r2, r0);	 Catch:{ all -> 0x0039 }
        xe = r6;
        goto L_0x0029;
    L_0x0039:
        r0 = move-exception;
        xe = r6;
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zze.zzbu(android.content.Context):void");
    }

    @TargetApi(19)
    @Deprecated
    public static boolean zzc(Context context, int i, String str) {
        return zzx.zzc(context, i, str);
    }

    @Deprecated
    public static boolean zzd(Context context, int i) {
        return i == 18 ? true : i == 1 ? zzs(context, GOOGLE_PLAY_SERVICES_PACKAGE) : false;
    }

    @Deprecated
    public static boolean zze(Context context, int i) {
        return i == 9 ? zzs(context, GOOGLE_PLAY_STORE_PACKAGE) : false;
    }

    @Deprecated
    public static boolean zzf(Context context, int i) {
        return zzx.zzf(context, i);
    }

    @TargetApi(21)
    static boolean zzs(Context context, String str) {
        boolean equals = str.equals(GOOGLE_PLAY_SERVICES_PACKAGE);
        if (equals && zzd.zzayi()) {
            return false;
        }
        if (zzs.zzayx()) {
            for (SessionInfo appPackageName : context.getPackageManager().getPackageInstaller().getAllSessions()) {
                if (str.equals(appPackageName.getAppPackageName())) {
                    return true;
                }
            }
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, Utility.DEFAULT_STREAM_BUFFER_SIZE);
            if (equals) {
                return applicationInfo.enabled;
            }
            boolean z = applicationInfo.enabled && !zzbs(context);
            return z;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
