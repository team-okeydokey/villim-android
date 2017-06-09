package net.villim.villim;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by seongmin on 5/29/17.
 */

public class VillimLocation implements Parcelable {
    public String name;
    public String addrFull;
    public String addrSummary;
    public String addrDirection;
    public double latitude;
    public double longitude;

    public VillimLocation() {
    }

    public VillimLocation(String locnName) {
        this.name = locnName;
    }

    public VillimLocation(String locName, String locAddrFull, String locAddrSummary, String locAddrDirection) {
        this.name = locName;
        this.addrFull = locAddrFull;
        this.addrSummary = locAddrSummary;
        this.addrDirection = locAddrDirection;
    }

    public VillimLocation(String locName, String locAddrFull, String locAddrSummary, String locAddrDirection, double locLatitude, double locLongitude) {
        this.name = locName;
        this.addrFull = locAddrFull;
        this.addrSummary = locAddrSummary;
        this.addrDirection = locAddrDirection;
        this.latitude = locLatitude;
        this.longitude = locLongitude;
    }

    public static VillimLocation getLocationFromServer(int id) {
        VillimLocation location = new VillimLocation(
                "우리집",
                "서울시 강남구 청담동 42 래미안아파트 3차 102동 101호",
                "서울시 강남구 청담동",
                "강남구청역 4번 출구",
                37.5172, 127.0413);
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(addrFull);
        dest.writeString(addrSummary);
        dest.writeString(addrDirection);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    protected VillimLocation(Parcel in) {
        name = in.readString();
        addrFull = in.readString();
        addrSummary = in.readString();
        addrDirection = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<VillimLocation> CREATOR = new Creator<VillimLocation>() {
        @Override
        public VillimLocation createFromParcel(Parcel in) {
            return new VillimLocation(in);
        }

        @Override
        public VillimLocation[] newArray(int size) {
            return new VillimLocation[size];
        }
    };
}
