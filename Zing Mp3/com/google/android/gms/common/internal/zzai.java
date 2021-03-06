package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.mp3download.zingmp3.C1569R;

public class zzai implements Creator<ValidateAccountRequest> {
    static void zza(ValidateAccountRequest validateAccountRequest, Parcel parcel, int i) {
        int zzcs = zzb.zzcs(parcel);
        zzb.zzc(parcel, 1, validateAccountRequest.mVersionCode);
        zzb.zzc(parcel, 2, validateAccountRequest.zzawu());
        zzb.zza(parcel, 3, validateAccountRequest.Df, false);
        zzb.zza(parcel, 4, validateAccountRequest.zzaws(), i, false);
        zzb.zza(parcel, 5, validateAccountRequest.zzawv(), false);
        zzb.zza(parcel, 6, validateAccountRequest.getCallingPackage(), false);
        zzb.zzaj(parcel, zzcs);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzcp(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgt(i);
    }

    public ValidateAccountRequest zzcp(Parcel parcel) {
        int i = 0;
        String str = null;
        int zzcr = zza.zzcr(parcel);
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzcr) {
            int zzcq = zza.zzcq(parcel);
            switch (zza.zzgu(zzcq)) {
                case C1569R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped /*1*/:
                    i2 = zza.zzg(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_login_view_com_facebook_logout_text /*2*/:
                    i = zza.zzg(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_login_view_com_facebook_tooltip_mode /*3*/:
                    iBinder = zza.zzr(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_like_view_com_facebook_auxiliary_view_position /*4*/:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzcq, Scope.CREATOR);
                    break;
                case C1569R.styleable.com_facebook_like_view_com_facebook_horizontal_alignment /*5*/:
                    bundle = zza.zzs(parcel, zzcq);
                    break;
                case C1569R.styleable.Toolbar_contentInsetEnd /*6*/:
                    str = zza.zzq(parcel, zzcq);
                    break;
                default:
                    zza.zzb(parcel, zzcq);
                    break;
            }
        }
        if (parcel.dataPosition() == zzcr) {
            return new ValidateAccountRequest(i2, i, iBinder, scopeArr, bundle, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzcr, parcel);
    }

    public ValidateAccountRequest[] zzgt(int i) {
        return new ValidateAccountRequest[i];
    }
}
