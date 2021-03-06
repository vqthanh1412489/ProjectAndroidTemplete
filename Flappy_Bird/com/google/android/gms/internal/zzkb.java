package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplaySessionCallbacks;
import com.google.android.gms.cast.internal.zzl;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzi;
import com.google.android.gms.internal.zzke.zza;

public class zzkb extends zzi<zzkd> implements DeathRecipient {
    private static final zzl zzQW;
    private CastDevice zzQH;
    private CastRemoteDisplaySessionCallbacks zzVB;

    /* renamed from: com.google.android.gms.internal.zzkb.1 */
    class C08121 extends zza {
        final /* synthetic */ zzke zzVC;
        final /* synthetic */ zzkb zzVD;

        C08121(zzkb com_google_android_gms_internal_zzkb, zzke com_google_android_gms_internal_zzke) {
            this.zzVD = com_google_android_gms_internal_zzkb;
            this.zzVC = com_google_android_gms_internal_zzke;
        }

        public void zzaR(int i) throws RemoteException {
            zzkb.zzQW.zzb("onRemoteDisplayEnded", new Object[0]);
            if (this.zzVC != null) {
                this.zzVC.zzaR(i);
            }
            if (this.zzVD.zzVB != null) {
                this.zzVD.zzVB.onRemoteDisplayEnded(new Status(i));
            }
        }
    }

    static {
        zzQW = new zzl("CastRemoteDisplayClientImpl");
    }

    public zzkb(Context context, Looper looper, CastDevice castDevice, CastRemoteDisplaySessionCallbacks castRemoteDisplaySessionCallbacks, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 83, connectionCallbacks, onConnectionFailedListener);
        this.zzVB = castRemoteDisplaySessionCallbacks;
        this.zzQH = castDevice;
    }

    public void binderDied() {
    }

    public void disconnect() {
        try {
            ((zzkd) zznM()).disconnect();
        } catch (RemoteException e) {
        } finally {
            super.disconnect();
        }
    }

    protected String getServiceDescriptor() {
        return "com.google.android.gms.cast.remote_display.ICastRemoteDisplayService";
    }

    protected String getStartServiceAction() {
        return "com.google.android.gms.cast.remote_display.service.START";
    }

    public /* synthetic */ IInterface zzT(IBinder iBinder) {
        return zzay(iBinder);
    }

    public void zza(zzkc com_google_android_gms_internal_zzkc) throws RemoteException {
        zzQW.zzb("stopRemoteDisplay", new Object[0]);
        ((zzkd) zznM()).zza(com_google_android_gms_internal_zzkc);
    }

    public void zza(zzkc com_google_android_gms_internal_zzkc, int i) throws RemoteException {
        ((zzkd) zznM()).zza(com_google_android_gms_internal_zzkc, i);
    }

    public void zza(zzkc com_google_android_gms_internal_zzkc, zzke com_google_android_gms_internal_zzke, String str) throws RemoteException {
        zzQW.zzb("startRemoteDisplay", new Object[0]);
        ((zzkd) zznM()).zza(com_google_android_gms_internal_zzkc, new C08121(this, com_google_android_gms_internal_zzke), this.zzQH.getDeviceId(), str);
    }

    public zzkd zzay(IBinder iBinder) {
        return zzkd.zza.zzaA(iBinder);
    }
}
