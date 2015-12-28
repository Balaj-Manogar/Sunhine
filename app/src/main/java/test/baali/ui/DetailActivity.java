package test.baali.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity
{

    private String TAG = "DetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailActivityFragment, new DetailActivityFragment())
                    .commit();
        }

        TextView data = (TextView) findViewById(R.id.detailInfo);
        data.setText(getIntent().getStringExtra("data"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_item_share));
        Intent data = getIntent();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, data.getStringExtra("data"));
        share.setShareIntent(intent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Log.d(TAG, "Baali: " + id + " Share id: " + R.id.menu_item_share);
        Toast.makeText(this, "Fragment Menu", Toast.LENGTH_SHORT).show();

        if(id == R.id.action_settings)
        {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }

        // This will not work for v7 ShareActionProvider
       /* if(id == R.id.menu_item_share) {
            Log.d(TAG, "onOptionsItemSelected: ");
            ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            if(share != null) {
                Intent data = getIntent();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, data.getStringExtra("data"));
                share.setShareIntent(intent);
            }
            else
            {
                Log.d(TAG, "share is null");
            }
        }*/

        return super.onOptionsItemSelected(item);
    }




}
