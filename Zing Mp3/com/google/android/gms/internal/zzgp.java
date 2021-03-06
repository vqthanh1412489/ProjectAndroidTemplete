package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.ads.internal.zzu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzji
public final class zzgp {
    public final String zzbus;
    public final String zzbut;
    public final List<String> zzbuu;
    public final String zzbuv;
    public final String zzbuw;
    public final List<String> zzbux;
    public final List<String> zzbuy;
    public final List<String> zzbuz;
    public final String zzbva;
    public final List<String> zzbvb;
    public final List<String> zzbvc;
    @Nullable
    public final String zzbvd;
    @Nullable
    public final String zzbve;
    public final String zzbvf;
    @Nullable
    public final List<String> zzbvg;
    public final String zzbvh;

    public zzgp(String str, String str2, List<String> list, String str3, String str4, List<String> list2, List<String> list3, String str5, String str6, List<String> list4, List<String> list5, String str7, String str8, String str9, List<String> list6, String str10, List<String> list7) {
        this.zzbus = str;
        this.zzbut = str2;
        this.zzbuu = list;
        this.zzbuv = str3;
        this.zzbuw = str4;
        this.zzbux = list2;
        this.zzbuy = list3;
        this.zzbva = str5;
        this.zzbvb = list4;
        this.zzbvc = list5;
        this.zzbvd = str7;
        this.zzbve = str8;
        this.zzbvf = str9;
        this.zzbvg = list6;
        this.zzbvh = str10;
        this.zzbuz = list7;
    }

    public zzgp(JSONObject jSONObject) throws JSONException {
        String str = null;
        this.zzbut = jSONObject.getString(TtmlNode.ATTR_ID);
        JSONArray jSONArray = jSONObject.getJSONArray("adapters");
        List arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(jSONArray.getString(i));
        }
        this.zzbuu = Collections.unmodifiableList(arrayList);
        this.zzbuv = jSONObject.optString("allocation_id", null);
        this.zzbux = zzu.zzhf().zza(jSONObject, "clickurl");
        this.zzbuy = zzu.zzhf().zza(jSONObject, "imp_urls");
        this.zzbuz = zzu.zzhf().zza(jSONObject, "fill_urls");
        this.zzbvb = zzu.zzhf().zza(jSONObject, "video_start_urls");
        this.zzbvc = zzu.zzhf().zza(jSONObject, "video_complete_urls");
        JSONObject optJSONObject = jSONObject.optJSONObject("ad");
        this.zzbus = optJSONObject != null ? optJSONObject.toString() : null;
        JSONObject optJSONObject2 = jSONObject.optJSONObject(ShareConstants.WEB_DIALOG_PARAM_DATA);
        this.zzbva = optJSONObject2 != null ? optJSONObject2.toString() : null;
        this.zzbuw = optJSONObject2 != null ? optJSONObject2.optString("class_name") : null;
        this.zzbvd = jSONObject.optString("html_template", null);
        this.zzbve = jSONObject.optString("ad_base_url", null);
        optJSONObject = jSONObject.optJSONObject("assets");
        this.zzbvf = optJSONObject != null ? optJSONObject.toString() : null;
        this.zzbvg = zzu.zzhf().zza(jSONObject, "template_ids");
        optJSONObject = jSONObject.optJSONObject("ad_loader_options");
        if (optJSONObject != null) {
            str = optJSONObject.toString();
        }
        this.zzbvh = str;
    }
}
