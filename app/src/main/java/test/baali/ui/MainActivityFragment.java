package test.baali.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    private ArrayAdapter<String> weatherAdapter;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        List<String> weatherForecast = Arrays.asList("Today - Sunny -- 88 / 63",
                "Tomorrow - Foggy - 70 / 46", "Wed - Cloudy - 72 / 63", "Thu - Rainy - 35 / 25",
                "Fri - Foggy - 65 / 38", "Sat - Sunny - 85 / 70");

        weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id
                .list_item_forecast_textview, weatherForecast);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        ListView weatherListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        weatherListView.setAdapter(weatherAdapter);
        return rootView;
    }
}
