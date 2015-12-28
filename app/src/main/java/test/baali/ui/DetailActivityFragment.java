package test.baali.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{
    private String TAG = "DetailActivityFragment";

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
            Log.d(TAG, "Baali: ");

        if(id == R.id.action_settings)
        {
            Intent settings = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settings);
        }
        if(id == R.id.menu_item_share) {
            Log.d(TAG, "onOptionsItemSelected: ");
            ShareActionProvider share = (ShareActionProvider) item.getActionProvider();
            if(share != null) {
                Intent data = getActivity().getIntent();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, data.getStringExtra("data"));
                share.setShareIntent(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
