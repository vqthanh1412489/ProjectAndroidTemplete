package com.google.android.gms.games.event;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;

public class EventEntityCreator implements Creator<EventEntity> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(EventEntity eventEntity, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, eventEntity.getEventId(), false);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, eventEntity.getVersionCode());
        zzb.zza(parcel, 2, eventEntity.getName(), false);
        zzb.zza(parcel, 3, eventEntity.getDescription(), false);
        zzb.zza(parcel, 4, eventEntity.getIconImageUri(), i, false);
        zzb.zza(parcel, 5, eventEntity.getIconImageUrl(), false);
        zzb.zza(parcel, 6, eventEntity.getPlayer(), i, false);
        zzb.zza(parcel, 7, eventEntity.getValue());
        zzb.zza(parcel, 8, eventEntity.getFormattedValue(), false);
        zzb.zza(parcel, 9, eventEntity.isVisible());
        zzb.zzH(parcel, zzac);
    }

    public EventEntity createFromParcel(Parcel parcel) {
        boolean z = false;
        String str = null;
        int zzab = zza.zzab(parcel);
        long j = 0;
        Player player = null;
        String str2 = null;
        Uri uri = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        int i = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    str5 = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str4 = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    str3 = zza.zzo(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    uri = (Uri) zza.zza(parcel, zzaa, Uri.CREATOR);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    str2 = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_ATM /*6*/:
                    player = (PlayerEntity) zza.zza(parcel, zzaa, PlayerEntity.CREATOR);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    j = zza.zzi(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_SNAPSHOT /*8*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_BAR /*9*/:
                    z = zza.zzc(parcel, zzaa);
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
            return new EventEntity(i, str5, str4, str3, uri, str2, player, j, str, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public EventEntity[] newArray(int size) {
        return new EventEntity[size];
    }
}
