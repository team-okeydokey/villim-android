package net.villim.villim;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by seongmin on 6/9/17.
 */

public class VillimAmenity {

    private static final Map<Integer,Integer> nameMap = new HashMap<Integer, Integer>()
    {
        {
            put(0, R.string.amenity_bedsheet);
            put(1, R.string.amenity_wifi);
            put(2, R.string.amenity_tv);
            put(3, R.string.amenity_cooking_utensil);
            put(4, R.string.amenity_iron);
            put(5, R.string.amenity_desk);
            put(6, R.string.amenity_towel);
            put(7, R.string.amenity_ac);
            put(8, R.string.amenity_computer);
            put(9, R.string.amenity_fridge);
            put(10, R.string.amenity_hair_dryer);
            put(11, R.string.amenity_smoke_detector);
            put(12, R.string.amenity_first_aid);
            put(13, R.string.amenity_heat);
            put(14, R.string.amenity_cooking_electronics);
            put(15, R.string.amenity_washer);
            put(16, R.string.amenity_closet);
            put(17, R.string.amenity_fire_extinguisher);
        }
    };

    private static final Map<Integer,Integer> drawableMap = new HashMap<Integer, Integer>()
    {
        {
            put(0, R.drawable.ic_whatshot_black_24dp);
            put(1, R.drawable.ic_whatshot_black_24dp);
            put(2, R.drawable.ic_whatshot_black_24dp);
            put(3, R.drawable.ic_whatshot_black_24dp);
            put(4, R.drawable.ic_whatshot_black_24dp);
            put(5, R.drawable.ic_whatshot_black_24dp);
            put(6, R.drawable.ic_whatshot_black_24dp);
            put(7, R.drawable.ic_whatshot_black_24dp);
            put(8, R.drawable.ic_whatshot_black_24dp);
            put(9, R.drawable.ic_whatshot_black_24dp);
            put(10, R.drawable.ic_whatshot_black_24dp);
            put(11, R.drawable.ic_whatshot_black_24dp);
            put(12, R.drawable.ic_whatshot_black_24dp);
            put(13, R.drawable.ic_whatshot_black_24dp);
            put(14, R.drawable.ic_whatshot_black_24dp);
            put(15, R.drawable.ic_whatshot_black_24dp);
            put(16, R.drawable.ic_whatshot_black_24dp);
            put(17, R.drawable.ic_whatshot_black_24dp);
        }
    };


    public int amenityId;
    public int amenityName;
    public int amenityDrawable;

    public static Map getNameMap() {
        return Collections.unmodifiableMap(nameMap);
    }

    public static Map getDrawableMap() {
        return Collections.unmodifiableMap(drawableMap);
    }

    public static String getAmenityName(Context context, int amenityId) {
        int resId = nameMap.get(amenityId);
        return context.getString(resId);
    }

    public static int getAmenityNameResourceId(int amenityId) {
        return nameMap.get(amenityId);
    }

    public static Drawable getAmenityDrawable(Context context, int amenityId) {
        int resId = nameMap.get(amenityId);
        return ContextCompat.getDrawable(context, resId);
    }

    public static int getAmenityDrawableResourceId(int amenityId) {
        return drawableMap.get(amenityId);
    }

}
