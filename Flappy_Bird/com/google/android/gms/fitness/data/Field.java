package com.google.android.gms.fitness.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.example.games.basegameutils.GameHelper;

public final class Field implements SafeParcelable {
    public static final Creator<Field> CREATOR;
    public static final Field FIELD_ACCURACY;
    public static final Field FIELD_ACTIVITY;
    public static final Field FIELD_ALTITUDE;
    public static final Field FIELD_AVERAGE;
    public static final Field FIELD_BPM;
    public static final Field FIELD_CALORIES;
    public static final Field FIELD_CIRCUMFERENCE;
    public static final Field FIELD_CONFIDENCE;
    public static final Field FIELD_DISTANCE;
    public static final Field FIELD_DURATION;
    public static final Field FIELD_EXERCISE;
    public static final Field FIELD_FOOD_ITEM;
    public static final Field FIELD_HEIGHT;
    public static final Field FIELD_HIGH_LATITUDE;
    public static final Field FIELD_HIGH_LONGITUDE;
    public static final Field FIELD_LATITUDE;
    public static final Field FIELD_LONGITUDE;
    public static final Field FIELD_LOW_LATITUDE;
    public static final Field FIELD_LOW_LONGITUDE;
    public static final Field FIELD_MAX;
    public static final Field FIELD_MEAL_TYPE;
    public static final Field FIELD_MIN;
    public static final Field FIELD_NUM_SEGMENTS;
    public static final Field FIELD_NUTRIENTS;
    public static final Field FIELD_PERCENTAGE;
    public static final Field FIELD_REPETITIONS;
    public static final Field FIELD_RESISTANCE;
    public static final Field FIELD_RESISTANCE_TYPE;
    public static final Field FIELD_REVOLUTIONS;
    public static final Field FIELD_RPM;
    public static final Field FIELD_SPEED;
    public static final Field FIELD_STEPS;
    public static final Field FIELD_WATTS;
    public static final Field FIELD_WEIGHT;
    public static final int FORMAT_FLOAT = 2;
    public static final int FORMAT_INT32 = 1;
    public static final int FORMAT_MAP = 4;
    public static final int FORMAT_STRING = 3;
    public static final int MEAL_TYPE_BREAKFAST = 1;
    public static final int MEAL_TYPE_DINNER = 3;
    public static final int MEAL_TYPE_LUNCH = 2;
    public static final int MEAL_TYPE_SNACK = 4;
    public static final int MEAL_TYPE_UNKNOWN = 0;
    public static final String NUTRIENT_CALCIUM = "calcium";
    public static final String NUTRIENT_CALORIES = "calories";
    public static final String NUTRIENT_CHOLESTEROL = "cholesterol";
    public static final String NUTRIENT_DIETARY_FIBER = "dietary_fiber";
    public static final String NUTRIENT_IRON = "iron";
    public static final String NUTRIENT_MONOUNSATURATED_FAT = "fat.monounsaturated";
    public static final String NUTRIENT_POLYUNSATURATED_FAT = "fat.polyunsaturated";
    public static final String NUTRIENT_POTASSIUM = "potassium";
    public static final String NUTRIENT_PROTEIN = "protein";
    public static final String NUTRIENT_SATURATED_FAT = "fat.saturated";
    public static final String NUTRIENT_SODIUM = "sodium";
    public static final String NUTRIENT_SUGAR = "sugar";
    public static final String NUTRIENT_TOTAL_CARBS = "carbs.total";
    public static final String NUTRIENT_TOTAL_FAT = "fat.total";
    public static final String NUTRIENT_TRANS_FAT = "fat.trans";
    public static final String NUTRIENT_UNSATURATED_FAT = "fat.unsaturated";
    public static final String NUTRIENT_VITAMIN_A = "vitamin_a";
    public static final String NUTRIENT_VITAMIN_C = "vitamin_c";
    public static final int RESISTANCE_TYPE_BARBELL = 1;
    public static final int RESISTANCE_TYPE_BODY = 6;
    public static final int RESISTANCE_TYPE_CABLE = 2;
    public static final int RESISTANCE_TYPE_DUMBBELL = 3;
    public static final int RESISTANCE_TYPE_KETTLEBELL = 4;
    public static final int RESISTANCE_TYPE_MACHINE = 5;
    public static final int RESISTANCE_TYPE_UNKNOWN = 0;
    public static final Field zzakA;
    public static final Field zzakv;
    public static final Field zzakw;
    public static final Field zzakx;
    public static final Field zzaky;
    public static final Field zzakz;
    private final String mName;
    private final int zzCY;
    private final int zzakB;

    static {
        FIELD_ACTIVITY = zzcH("activity");
        FIELD_CONFIDENCE = zzcI("confidence");
        zzakv = zzcK("activity_confidences");
        FIELD_STEPS = zzcH("steps");
        FIELD_DURATION = zzcH("duration");
        FIELD_BPM = zzcI("bpm");
        FIELD_LATITUDE = zzcI("latitude");
        FIELD_LONGITUDE = zzcI("longitude");
        FIELD_ACCURACY = zzcI("accuracy");
        FIELD_ALTITUDE = zzcI("altitude");
        zzakw = zzcI("elevation.gain");
        FIELD_DISTANCE = zzcI("distance");
        FIELD_HEIGHT = zzcI("height");
        FIELD_WEIGHT = zzcI("weight");
        FIELD_CIRCUMFERENCE = zzcI("circumference");
        FIELD_PERCENTAGE = zzcI("percentage");
        FIELD_SPEED = zzcI("speed");
        FIELD_RPM = zzcI("rpm");
        FIELD_REVOLUTIONS = zzcH("revolutions");
        FIELD_CALORIES = zzcI(NUTRIENT_CALORIES);
        FIELD_WATTS = zzcI("watts");
        FIELD_MEAL_TYPE = zzcH("meal_type");
        FIELD_FOOD_ITEM = zzcJ("food_item");
        FIELD_NUTRIENTS = zzcK("nutrients");
        FIELD_EXERCISE = zzcJ("exercise");
        FIELD_REPETITIONS = zzcH("repetitions");
        FIELD_RESISTANCE = zzcI("resistance");
        FIELD_RESISTANCE_TYPE = zzcH("resistance_type");
        FIELD_NUM_SEGMENTS = zzcH("num_segments");
        FIELD_AVERAGE = zzcI("average");
        FIELD_MAX = zzcI("max");
        FIELD_MIN = zzcI("min");
        FIELD_LOW_LATITUDE = zzcI("low_latitude");
        FIELD_LOW_LONGITUDE = zzcI("low_longitude");
        FIELD_HIGH_LATITUDE = zzcI("high_latitude");
        FIELD_HIGH_LONGITUDE = zzcI("high_longitude");
        zzakx = zzcH("edge_type");
        zzaky = zzcI("x");
        zzakz = zzcI("y");
        zzakA = zzcI("z");
        CREATOR = new zzi();
    }

    Field(int versionCode, String name, int format) {
        this.zzCY = versionCode;
        this.mName = (String) zzu.zzu(name);
        this.zzakB = format;
    }

    private Field(String name, int format) {
        this(RESISTANCE_TYPE_BARBELL, name, format);
    }

    private boolean zza(Field field) {
        return this.mName.equals(field.mName) && this.zzakB == field.zzakB;
    }

    private static Field zzcH(String str) {
        return new Field(str, RESISTANCE_TYPE_BARBELL);
    }

    private static Field zzcI(String str) {
        return new Field(str, RESISTANCE_TYPE_CABLE);
    }

    private static Field zzcJ(String str) {
        return new Field(str, RESISTANCE_TYPE_DUMBBELL);
    }

    private static Field zzcK(String str) {
        return new Field(str, RESISTANCE_TYPE_KETTLEBELL);
    }

    public static Field zzn(String str, int i) {
        Object obj = -1;
        switch (str.hashCode()) {
            case -2131707655:
                if (str.equals("accuracy")) {
                    obj = null;
                    break;
                }
                break;
            case -1992012396:
                if (str.equals("duration")) {
                    obj = 10;
                    break;
                }
                break;
            case -1655966961:
                if (str.equals("activity")) {
                    obj = RESISTANCE_TYPE_BARBELL;
                    break;
                }
                break;
            case -1569430471:
                if (str.equals("num_segments")) {
                    obj = 24;
                    break;
                }
                break;
            case -1439978388:
                if (str.equals("latitude")) {
                    obj = 17;
                    break;
                }
                break;
            case -1221029593:
                if (str.equals("height")) {
                    obj = 14;
                    break;
                }
                break;
            case -1110756780:
                if (str.equals("food_item")) {
                    obj = 13;
                    break;
                }
                break;
            case -921832806:
                if (str.equals("percentage")) {
                    obj = 26;
                    break;
                }
                break;
            case -791592328:
                if (str.equals("weight")) {
                    obj = 35;
                    break;
                }
                break;
            case -631448035:
                if (str.equals("average")) {
                    obj = RESISTANCE_TYPE_KETTLEBELL;
                    break;
                }
                break;
            case -626344110:
                if (str.equals("high_longitude")) {
                    obj = 16;
                    break;
                }
                break;
            case -619868540:
                if (str.equals("low_longitude")) {
                    obj = 20;
                    break;
                }
                break;
            case -494782871:
                if (str.equals("high_latitude")) {
                    obj = 15;
                    break;
                }
                break;
            case -437053898:
                if (str.equals("meal_type")) {
                    obj = 22;
                    break;
                }
                break;
            case -277306353:
                if (str.equals("circumference")) {
                    obj = 7;
                    break;
                }
                break;
            case -266093204:
                if (str.equals("nutrients")) {
                    obj = 25;
                    break;
                }
                break;
            case -168965370:
                if (str.equals(NUTRIENT_CALORIES)) {
                    obj = RESISTANCE_TYPE_BODY;
                    break;
                }
                break;
            case -126538880:
                if (str.equals("resistance_type")) {
                    obj = 29;
                    break;
                }
                break;
            case 120:
                if (str.equals("x")) {
                    obj = 36;
                    break;
                }
                break;
            case 121:
                if (str.equals("y")) {
                    obj = 37;
                    break;
                }
                break;
            case 122:
                if (str.equals("z")) {
                    obj = 38;
                    break;
                }
                break;
            case 97759:
                if (str.equals("bpm")) {
                    obj = RESISTANCE_TYPE_MACHINE;
                    break;
                }
                break;
            case 107876:
                if (str.equals("max")) {
                    obj = 21;
                    break;
                }
                break;
            case 108114:
                if (str.equals("min")) {
                    obj = 23;
                    break;
                }
                break;
            case 113135:
                if (str.equals("rpm")) {
                    obj = 31;
                    break;
                }
                break;
            case 109641799:
                if (str.equals("speed")) {
                    obj = 32;
                    break;
                }
                break;
            case 109761319:
                if (str.equals("steps")) {
                    obj = 33;
                    break;
                }
                break;
            case 112903913:
                if (str.equals("watts")) {
                    obj = 34;
                    break;
                }
                break;
            case 137365935:
                if (str.equals("longitude")) {
                    obj = 18;
                    break;
                }
                break;
            case 198162679:
                if (str.equals("low_latitude")) {
                    obj = 19;
                    break;
                }
                break;
            case 224520316:
                if (str.equals("edge_type")) {
                    obj = 11;
                    break;
                }
                break;
            case 288459765:
                if (str.equals("distance")) {
                    obj = 9;
                    break;
                }
                break;
            case 811264586:
                if (str.equals("revolutions")) {
                    obj = 30;
                    break;
                }
                break;
            case 829251210:
                if (str.equals("confidence")) {
                    obj = 8;
                    break;
                }
                break;
            case 984367650:
                if (str.equals("repetitions")) {
                    obj = 27;
                    break;
                }
                break;
            case 1857734768:
                if (str.equals("elevation.gain")) {
                    obj = RESISTANCE_TYPE_DUMBBELL;
                    break;
                }
                break;
            case 1863800889:
                if (str.equals("resistance")) {
                    obj = 28;
                    break;
                }
                break;
            case 2036550306:
                if (str.equals("altitude")) {
                    obj = RESISTANCE_TYPE_CABLE;
                    break;
                }
                break;
            case 2056323544:
                if (str.equals("exercise")) {
                    obj = 12;
                    break;
                }
                break;
        }
        switch (obj) {
            case MEAL_TYPE_UNKNOWN /*0*/:
                return FIELD_ACCURACY;
            case RESISTANCE_TYPE_BARBELL /*1*/:
                return FIELD_ACTIVITY;
            case RESISTANCE_TYPE_CABLE /*2*/:
                return FIELD_ALTITUDE;
            case RESISTANCE_TYPE_DUMBBELL /*3*/:
                return zzakw;
            case RESISTANCE_TYPE_KETTLEBELL /*4*/:
                return FIELD_AVERAGE;
            case RESISTANCE_TYPE_MACHINE /*5*/:
                return FIELD_BPM;
            case RESISTANCE_TYPE_BODY /*6*/:
                return FIELD_CALORIES;
            case Place.TYPE_BAKERY /*7*/:
                return FIELD_CIRCUMFERENCE;
            case GameHelper.CLIENT_SNAPSHOT /*8*/:
                return FIELD_CONFIDENCE;
            case Place.TYPE_BAR /*9*/:
                return FIELD_DISTANCE;
            case Place.TYPE_BEAUTY_SALON /*10*/:
                return FIELD_DURATION;
            case Place.TYPE_BICYCLE_STORE /*11*/:
                return zzakx;
            case Place.TYPE_BOOK_STORE /*12*/:
                return FIELD_EXERCISE;
            case ConnectionsStatusCodes.STATUS_ERROR /*13*/:
                return FIELD_FOOD_ITEM;
            case Place.TYPE_BUS_STATION /*14*/:
                return FIELD_HEIGHT;
            case GameHelper.CLIENT_ALL /*15*/:
                return FIELD_HIGH_LATITUDE;
            case Place.TYPE_CAMPGROUND /*16*/:
                return FIELD_HIGH_LONGITUDE;
            case Place.TYPE_CAR_DEALER /*17*/:
                return FIELD_LATITUDE;
            case Place.TYPE_CAR_RENTAL /*18*/:
                return FIELD_LONGITUDE;
            case Place.TYPE_CAR_REPAIR /*19*/:
                return FIELD_LOW_LATITUDE;
            case Place.TYPE_CAR_WASH /*20*/:
                return FIELD_LOW_LONGITUDE;
            case Place.TYPE_CASINO /*21*/:
                return FIELD_MAX;
            case Place.TYPE_CEMETERY /*22*/:
                return FIELD_MEAL_TYPE;
            case Place.TYPE_CHURCH /*23*/:
                return FIELD_MIN;
            case Place.TYPE_CITY_HALL /*24*/:
                return FIELD_NUM_SEGMENTS;
            case Place.TYPE_CLOTHING_STORE /*25*/:
                return FIELD_NUTRIENTS;
            case Place.TYPE_CONVENIENCE_STORE /*26*/:
                return FIELD_PERCENTAGE;
            case Place.TYPE_COURTHOUSE /*27*/:
                return FIELD_REPETITIONS;
            case Place.TYPE_DENTIST /*28*/:
                return FIELD_RESISTANCE;
            case Place.TYPE_DEPARTMENT_STORE /*29*/:
                return FIELD_RESISTANCE_TYPE;
            case Place.TYPE_DOCTOR /*30*/:
                return FIELD_REVOLUTIONS;
            case Place.TYPE_ELECTRICIAN /*31*/:
                return FIELD_RPM;
            case Place.TYPE_ELECTRONICS_STORE /*32*/:
                return FIELD_SPEED;
            case Place.TYPE_EMBASSY /*33*/:
                return FIELD_STEPS;
            case Place.TYPE_ESTABLISHMENT /*34*/:
                return FIELD_WATTS;
            case Place.TYPE_FINANCE /*35*/:
                return FIELD_WEIGHT;
            case Place.TYPE_FIRE_STATION /*36*/:
                return zzaky;
            case Place.TYPE_FLORIST /*37*/:
                return zzakz;
            case Place.TYPE_FOOD /*38*/:
                return zzakA;
            default:
                return new Field(str, i);
        }
    }

    public int describeContents() {
        return MEAL_TYPE_UNKNOWN;
    }

    public boolean equals(Object that) {
        return this == that || ((that instanceof Field) && zza((Field) that));
    }

    public int getFormat() {
        return this.zzakB;
    }

    public String getName() {
        return this.mName;
    }

    int getVersionCode() {
        return this.zzCY;
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public String toString() {
        String str = "%s(%s)";
        Object[] objArr = new Object[RESISTANCE_TYPE_CABLE];
        objArr[MEAL_TYPE_UNKNOWN] = this.mName;
        objArr[RESISTANCE_TYPE_BARBELL] = this.zzakB == RESISTANCE_TYPE_BARBELL ? "i" : "f";
        return String.format(str, objArr);
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzi.zza(this, dest, flags);
    }
}
