package cop4331.playsurvey;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;


public class Login extends ActionBarActivity {

    private boolean do_request = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoSignup(View view)
    {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public void login(View view)
    {
        if(do_request)
        {
            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet();

                URI host = new URI("NEED_URI_FOR_REUEST");
                getRequest.setURI(host);
                client.execute(getRequest);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }
}
