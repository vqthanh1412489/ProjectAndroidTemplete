package com.google.ads.mediation;

import android.location.Location;
import com.google.ads.AdRequest.Gender;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Deprecated
public class MediationAdRequest {
    private final Date zzgr;
    private final Gender zzgs;
    private final Set<String> zzgt;
    private final boolean zzgu;
    private final Location zzgv;

    public MediationAdRequest(Date date, Gender gender, Set<String> set, boolean z, Location location) {
        this.zzgr = date;
        this.zzgs = gender;
        this.zzgt = set;
        this.zzgu = z;
        this.zzgv = location;
    }

    public Integer getAgeInYears() {
        if (this.zzgr == null) {
            return null;
        }
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTime(this.zzgr);
        Integer valueOf = Integer.valueOf(instance2.get(1) - instance.get(1));
        return (instance2.get(2) < instance.get(2) || (instance2.get(2) == instance.get(2) && instance2.get(5) < instance.get(5))) ? Integer.valueOf(valueOf.intValue() - 1) : valueOf;
    }

    public Date getBirthday() {
        return this.zzgr;
    }

    public Gender getGender() {
        return this.zzgs;
    }

    public Set<String> getKeywords() {
        return this.zzgt;
    }

    public Location getLocation() {
        return this.zzgv;
    }

    public boolean isTesting() {
        return this.zzgu;
    }
}
