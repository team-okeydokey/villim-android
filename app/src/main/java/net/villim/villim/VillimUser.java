package net.villim.villim;

/**
 * Created by seongmin on 5/31/17.
 */

public class VillimUser {
    public int id;
    public String name;
    public String email;
    public String profilePicUrl;

    public static VillimUser getUserFromServer(int id) {
        VillimUser user = new VillimUser();
        user.name = "Kim Woo Bin";
        return user;
    }
}
