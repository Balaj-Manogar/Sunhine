package test.baali.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    private ArrayAdapter<String> weatherAdapter;
    private String appId = "2fbe7f29cdb9ba92f88cb28249c3a028";
    private String postalCode = "600088,in";

    private String TAG = "Explore: MainActivityFragment";





    public MainActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setHasOptionsMenu(true);

    }

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onPause()
    {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onAttach(Context context)
    {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart: ");
        String locationPref = getPreferenceLocation();
        postalCode = locationPref + ",in";
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute(postalCode);
        Log.d(TAG, "onStart: Prefernce location: " + postalCode);
    }

    private String getPreferenceLocation()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("location", "600088");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView: ");
        
        /*String[] data = {"Today - Sunny -- 88 / 63",
                "Tomorrow - Foggy - 70 / 46", "Wed - Cloudy - 72 / 63", "Thu - Rainy - 35 / 25",
                "Fri - Foggy - 65 / 38", "Sat - Sunny - 85 / 70"};*/
//        List<String> weatherForecast = new ArrayList<>(Arrays.asList(data));
        List<String> weatherForecast = new ArrayList<>();

        String locationPref = getPreferenceLocation();
        postalCode = locationPref + ",in";
        weatherAdapter = getWeatherAdapter(weatherForecast);
        Log.d(TAG, "onCreateView: Prefernce location: " + postalCode);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // create user selected place weather when he clicks button
        userSelectedPlaceView(rootView);


        inflateWeatherList(rootView);

        createWeatherView(postalCode);
        return rootView;
    }

    private void userSelectedPlaceView(final View rootView)
    {
        Button postalButton = (Button) rootView.findViewById(R.id.btnGetPostalCodeWeather);
        postalButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText userPostalCode = (EditText) rootView.findViewById(R.id.postalCode);
                postalCode = userPostalCode.getText() + ",in";
                Log.d(TAG, postalCode);
                createWeatherView(postalCode);

            }
        });
    }

    private void createWeatherView(String code)
    {
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute(code);
    }

    private void inflateWeatherList(View rootView)
    {
        final ListView weatherListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        weatherListView.setAdapter(weatherAdapter);
        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TextView tv = (TextView) view.findViewById(R.id.list_item_forecast_textview);
                String text = weatherAdapter.getItem(position);
                /*Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT)
                        .show();*/
                Intent detail = new Intent(getActivity(), DetailActivity.class);
                detail.putExtra("data", text);
                startActivity(detail);
            }
        });
    }

    @NonNull
    private ArrayAdapter<String> getWeatherAdapter(List<String> weatherForecast)
    {
        return new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id
                .list_item_forecast_textview, weatherForecast);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            postalCode = getPreferenceLocation() + ",in";
            createWeatherView(postalCode);
            return true;
        }
        if(id == R.id.action_settings)
        {
            Intent settings = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settings);
        }

        if(id == R.id.action_map)
        {
            Log.d(TAG, "onOptionsItemSelected: map ");
            openPreferredLocation();
        }



        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocation()
    {
        String location = getPreferenceLocation();
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        Toast.makeText(getContext(), "Google maps menu", Toast.LENGTH_SHORT).show();
        if(intent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            startActivity(intent);
        }
        else 
        {
            Log.d(TAG, "Couldn't call: " + location + " , no receiving apps installed!");
        }
    }



    private void loadDefaultPreferences()
    {

    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]>
    {

        @Override
        protected String[] doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            Uri.Builder uriBuilder;
            String[] weatherDataResult = null;


            try {


                URL url = new URL(getWeatherURL(params[0]));

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                String jsonForecastString = buffer.toString();
                weatherDataResult = getWeatherDataFromJson(jsonForecastString, 7);

                Log.d(TAG, jsonForecastString);

            } catch (MalformedURLException e) {
                Log.d(TAG, "MalformedURLException: ");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "IOException: ");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {

                    urlConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "doInBackground: " + weatherDataResult);
            return weatherDataResult;
        }

        @Override
        protected void onPostExecute(String[] strings)
        {
            if (strings != null) {
                weatherAdapter.clear();
                for (String str: strings) {
                    weatherAdapter.add(str);
                }
            }
        }

        private String getWeatherURL(String code)
        {
            String baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily";
            Uri builtURI = Uri.parse(baseURL).buildUpon().appendQueryParameter("zip", code).appendQueryParameter("mode",
                    "json").appendQueryParameter
                    ("units", "metric").appendQueryParameter("cnt", "7").appendQueryParameter("appid", appId).build();
            Uri.Builder uriBuilder;
           /* uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http").authority("api.openweathermap.org").appendPath("data").appendPath("2.5").appendPath("forecast").appendPath("daily")
                    .appendQueryParameter("zip", postalCode).appendQueryParameter("mode", "json").appendQueryParameter
                    ("units", "metric").appendQueryParameter("cnt", "7").appendQueryParameter("appid", appId);*/
            return builtURI.toString();
        }

        private String getReadableDateString(long time)
        {
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        private String formatHighLows(double high, double low, String unitType)
        {
            if(unitType.equals(getString(R.string.pref_units_imperial))) {
                high = (high * 1.8) + 32;
                low = (low * 1.8) + 32;
            }
            else if(!unitType.equals(getString(R.string.pref_units_metric)))
            {
                Log.d(TAG, "Unit type not found: " + unitType);
            }

            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + " / " + roundedLow;
            return highLowStr;
        }

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException
        {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String[] resultStrs = new String[numDays];

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitType = preferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));


            for (int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay + i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low, unitType);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            for (String s : resultStrs) {
                Log.v(TAG, "Forecast entry: " + s);
            }
            return resultStrs;

        }


    }

}
