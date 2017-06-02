package net.villim.villim;

/**
 * Created by seongmin on 5/31/17.
 */

public class VillimUser {
    private int id;
    private String username;

    public String name;

    public static VillimUser getUserFromServer(int id) {
        VillimUser user = new VillimUser();
        user.name = "Kim Woo Bin";
        return user;
    }
}
