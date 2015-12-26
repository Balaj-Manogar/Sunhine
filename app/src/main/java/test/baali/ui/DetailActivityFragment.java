package test.baali.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{

    public DetailActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Intent intent = getActivity().getIntent();
        Toast.makeText(getActivity(), intent.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}