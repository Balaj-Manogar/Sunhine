package test.baali.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Balaji on 25/12/15.
 */
public class WeatherDataParser
{
    public static double getMaxTemperatureForDay(String weatherJsonString, int dayIndex) throws JSONException
    {
        JSONObject json = new JSONObject(weatherJsonString);
        JSONArray days = new JSONArray("list");
        JSONObject dayInfo = days.getJSONObject(dayIndex);
        JSONObject temperature = dayInfo.getJSONObject("temp");

        return temperature.getDouble("max");
    }
}
