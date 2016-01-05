package com.mfeldsztejn.githubnotificator.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfeldsztejn.githubnotificator.R;
import com.mfeldsztejn.githubnotificator.dto.common.User;

import quickutils.core.QuickUtils;
import quickutils.core.interfaces.OnEventListener;
import quickutils.core.interfaces.RequestCallback;
import quickutils.core.rest.Header;
import quickutils.core.rest.RequestError;

public class MeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name;
    private TextView username;
    private TextView company;
    private FloatingActionButton fab;
    private ImageView pp;
    private boolean isLoggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        String username = getUsernameFromIntent(getIntent());
        isLoggedUser = username == null;

        initViews();
        getUser(username);
    }

    private String getUsernameFromIntent(Intent i) {
        if (i == null || i.getData() == null) {
            return null;
        } else {
            return i.getData().getQueryParameter("username");
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.activity_me_name);
        username = (TextView) findViewById(R.id.activity_me_username);
        company = (TextView) findViewById(R.id.activity_me_company);
        pp = (ImageView) findViewById(R.id.activity_me_user_pp);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadViews(User user) {
        loadCommonViews(user);
        if (isLoggedUser) {
            configureFabForMe(user);
        } else {
            configureFabForOther(user);
        }
    }

    private void loadCommonViews(User user) {
        toolbar.setTitle(user.getLogin());
        name.setText(user.getName());
        username.setText(user.getLogin());
        company.setText(user.getCompany());
        QuickUtils.image.getBitmapByImageURL(user.getAvatarUrl(), new OnEventListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                pp.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception e) {
                //Do nothing
            }
        });
    }

    private void configureFabForMe(User user) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeActivity.this, "Deberia editar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_edit));
    }

    private void configureFabForOther(final User user) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
                MeActivity.this.startActivity(Intent.createChooser(i, "Send mail"));
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_email));
    }

    private void getUser(String username) {
        Header header = new Header.Builder()
                .add("Authorization", "token 309cbb7eae9f20c31b1e75a891c93c06d5fa969b")
                .add("Accept", "application/vnd.github.v3+json")
                .build();

        String url = "https://api.github.com/user";
        if (username != null) {
            url += "s/" + username;
        }

        QuickUtils.rest.connect().GET(header).load(url).as(User.class).withCallback(new RequestCallback<User>() {
            @Override
            public void onRequestSuccess(User user) {
                loadViews(user);
                Log.d("USER", user.getName());
            }

            @Override
            public void onRequestError(RequestError requestError) {
                Log.e("Error", "There was an error getting the user", requestError);
            }
        });
    }
}
