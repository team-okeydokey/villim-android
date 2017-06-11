package net.villim.villim;

import org.json.JSONException;
import org.json.JSONObject;

import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_NAME;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;

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

    public static VillimUser createUserFromJSONObject(JSONObject userInfo) {
        /* Create user instance */
        VillimUser user = new VillimUser();

         /* No need to null check here because if we dont set it, it's going to be null anyway */
        try {
            user.id = Integer.parseInt(userInfo.get(KEY_ID).toString());
            user.name = userInfo.get(KEY_NAME).toString();
            user.email = userInfo.get(KEY_EMAIL).toString();
            user.profilePicUrl = userInfo.get(KEY_PROFILE_PIC_URL).toString();
        } catch (JSONException e) {

        }
        return user;
    }
}
