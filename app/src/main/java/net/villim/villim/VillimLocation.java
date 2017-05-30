package net.villim.villim;

/**
 * Created by seongmin on 5/29/17.
 */

public class VillimLocation {
    public String name;
    public String detail;

    public VillimLocation() {
    }

    public VillimLocation(String locationName) {
        this.name = locationName;
    }

    public VillimLocation(String locationName, String locationDetail) {
        this.name = locationName;
        this.detail = locationDetail;
    }

}
