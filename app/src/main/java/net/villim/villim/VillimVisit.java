package net.villim.villim;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_VISITOR_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_TIME;
import static net.villim.villim.VillimKeys.KEY_VISIT_STATUS;

/**
 * Created by seongmin on 6/15/17.
 */

public class VillimVisit implements Parcelable {
    public static final int PENDING = 0;
    public static final int CONFIRMED = 1;
    public static final int DONE = 2;

    public int visitId;
    public int houseId;
    public int visitorId;
    public String visitTime;
    public int visitStatus;

    public VillimVisit() {

    }

    protected VillimVisit(Parcel in) {
        visitId = in.readInt();
        houseId = in.readInt();
        visitorId = in.readInt();
        visitTime = in.readString();
        visitStatus = in.readInt();
    }

    public static final Creator<VillimVisit> CREATOR = new Creator<VillimVisit>() {
        @Override
        public VillimVisit createFromParcel(Parcel in) {
            return new VillimVisit(in);
        }

        @Override
        public VillimVisit[] newArray(int size) {
            return new VillimVisit[size];
        }
    };

    public static VillimVisit[] visitArrayFromJsonArray(JSONArray jsonArray) {

        if (jsonArray == null) {
            return new VillimVisit[0];
        }

        VillimVisit[] visits = new VillimVisit[jsonArray.length()];

        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                VillimVisit visit = createVisitFromJSONObject(jsonArray.getJSONObject(i));
                visits[i] = visit;
            }

        } catch (JSONException e) {

        }
        return visits;
    }

    public static VillimVisit createVisitFromJSONObject(JSONObject userInfo) {
        /* Create user instance */
        VillimVisit visit = new VillimVisit();
        visit.visitId = userInfo.optInt(KEY_VISIT_ID);
        visit.houseId = userInfo.optInt(KEY_HOUSE_ID);
        visit.visitorId = userInfo.optInt(KEY_VISITOR_ID);
        visit.visitTime = userInfo.optString(KEY_VISIT_TIME);
        visit.visitStatus = userInfo.optInt(KEY_VISIT_STATUS);
        return visit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(visitId);
        dest.writeInt(houseId);
        dest.writeInt(visitorId);
        dest.writeString(visitTime);
        dest.writeInt(visitStatus);
    }

    public static String stringFromVisitStatus(Context context, int status) {
        switch (status) {
            case PENDING:
                return context.getString(R.string.status_pending);
            case CONFIRMED:
                return context.getString(R.string.status_confirmed);
            case DONE:
                return context.getString(R.string.status_done);
            default:
                return context.getString(R.string.status_pending);
        }
    }
}


