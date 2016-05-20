package com.smap.f16.grp12.racketometer.utils.performanceApi;

import com.smap.f16.grp12.racketometer.models.Session;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Performance API JSON to Object parser.
 */
class JsonParser {
    public static List<Session> getSessions(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        List<Session> sessions = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                Session session = getSession(array.getJSONObject(i));
                sessions.add(session);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sessions;
    }

    private static Session getSession(JSONObject jsonObject) throws JSONException {
        DateTime date = new DateTime(getTimestamp(jsonObject));
        double agility = getAgility(jsonObject);
        String description = getDescription(jsonObject);
        int hits = getHits(jsonObject);
        double latitude = getLatitude(jsonObject);
        double longitude = getLongitude(jsonObject);
        double speed = getSpeed(jsonObject);
        double power = getPower(jsonObject);
        long userId = getUserId(jsonObject);

        return new Session(
                date,
                userId,
                description,
                hits,
                speed,
                power,
                agility,
                latitude,
                longitude
        );
    }

    /**
     * Get agility field.
     * @param jsonObject The root JSON object.
     * @return The agility field value.
     * @throws JSONException
     */
    private static double getAgility(JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble("agility");
    }

    /**
     * Get description field.
     * @param jsonObject The root JSON object.
     * @return The description field value.
     * @throws JSONException
     */
    private static String getDescription(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("description");
    }

    /**
     * Get hits field.
     * @param jsonObject The root JSON object.
     * @return The hits field value.
     * @throws JSONException
     */
    private static int getHits(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("hits");
    }

    /**
     * Get latitude field.
     * @param jsonObject The root JSON object.
     * @return The latitude field value.
     * @throws JSONException
     */
    private static double getLatitude(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("location").getDouble("lat");
    }

    /**
     * Get longitude field.
     * @param jsonObject The root JSON object.
     * @return The longitude field value.
     * @throws JSONException
     */
    private static double getLongitude(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("location").getDouble("long");
    }

    /**
     * Get power field.
     * @param jsonObject The root JSON object.
     * @return The power field value.
     * @throws JSONException
     */
    private static double getPower(JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble("power");
    }

    /**
     * Get speed field.
     * @param jsonObject The root JSON object.
     * @return The speed field value.
     * @throws JSONException
     */
    private static double getSpeed(JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble("speed");
    }

    /**
     * Get timestamp field.
     * @param jsonObject The root JSON object.
     * @return The timestamp field value.
     * @throws JSONException
     */
    private static long getTimestamp(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong("date");
    }

    /**
     * Get user id field.
     * @param jsonObject The root JSON object.
     * @return The user id field value.
     * @throws JSONException
     */
    private static long getUserId(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong("userId");
    }
}
