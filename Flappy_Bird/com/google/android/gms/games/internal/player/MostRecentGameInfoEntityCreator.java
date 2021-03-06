package com.google.android.gms.games.internal.player;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;

public class MostRecentGameInfoEntityCreator implements Creator<MostRecentGameInfoEntity> {
    static void zza(MostRecentGameInfoEntity mostRecentGameInfoEntity, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, mostRecentGameInfoEntity.zztu(), false);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, mostRecentGameInfoEntity.getVersionCode());
        zzb.zza(parcel, 2, mostRecentGameInfoEntity.zztv(), false);
        zzb.zza(parcel, 3, mostRecentGameInfoEntity.zztw());
        zzb.zza(parcel, 4, mostRecentGameInfoEntity.zztx(), i, false);
        zzb.zza(parcel, 5, mostRecentGameInfoEntity.zzty(), i, false);
        zzb.zza(parcel, 6, mostRecentGameInfoEntity.zztz(), i, false);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzdG(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzfO(x0);
    }

    public MostRecentGameInfoEntity zzdG(Parcel parcel) {
        Uri uri = null;
        int zzab = zza.zzab(parcel);
        int i = 0;
        long j = 0;
        Uri uri2 = null;
        Uri uri3 = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    str2 = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    j = zza.zzi(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    uri3 = (Uri) zza.zza(parcel, zzaa, Uri.CREATOR);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    uri2 = (Uri) zza.zza(parcel, zzaa, Uri.CREATOR);
                    break;
                case Place.TYPE_ATM /*6*/:
                    uri = (Uri) zza.zza(parcel, zzaa, Uri.CREATOR);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new MostRecentGameInfoEntity(i, str2, str, j, uri3, uri2, uri);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public MostRecentGameInfoEntity[] zzfO(int i) {
        return new MostRecentGameInfoEntity[i];
    }
}
