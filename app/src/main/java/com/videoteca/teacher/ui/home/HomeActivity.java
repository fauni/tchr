package com.videoteca.teacher.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.videoteca.teacher.R;
import com.videoteca.teacher.data.model.realm.CursoData;
import com.videoteca.teacher.data.model.realm.SessionData;
import com.videoteca.teacher.data.model.realm.VideoData;
import com.videoteca.teacher.data.model.retrofit.RespUserLogin;
import com.videoteca.teacher.data.model.retrofit.RespUserLogout;
import com.videoteca.teacher.managers.SessionManager;
import com.videoteca.teacher.ui.login.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    //nav_header elements
    private View head_layout;
    //private ImageView image;
    private TextView usernameText;
    private TextView userEmailText;
    //Session manager
    private SessionManager sessionManager;
    //Start Realm Object
    private Realm realm;

    private String TAG = "HomeActivity";
    private NavController navController;

    private HomeViewModel homeViewModel;
    private Context context;
    private String authorization;

    private ShareHomeDataViewModel shareHomeDataViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        context = this;
        // View Model
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory()).get(HomeViewModel.class);
        shareHomeDataViewModel = new ViewModelProvider(this).get(ShareHomeDataViewModel.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Log.i(TAG, "onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(TAG, "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //Log.i(TAG, "onDrawerStateChanged");
            }
        });

        // Instance Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        //Get realm instance
        realm = Realm.getDefaultInstance();

        //Cambiando el contenido de nav_header
        View header=navigationView.getHeaderView(0);
        usernameText = header.findViewById(R.id.username);
        userEmailText = header.findViewById(R.id.user_email);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();

        if (data != null) {
            Gson gson = new Gson();
            RespUserLogin respUserLogin = gson.fromJson(data.getString("RESP_LOGIN_JSON"), RespUserLogin.class);

            sessionManager.setLogin(true);

            realm.beginTransaction();
            SessionData sessionData = new SessionData(respUserLogin.getToken(), respUserLogin.getMessage(), respUserLogin.getCi(), respUserLogin.getNombre(), respUserLogin.getPrimerAp(), respUserLogin.getSegundoAp(), respUserLogin.getEmail(), respUserLogin.getTipo());
            realm.copyToRealmOrUpdate(sessionData);
            realm.commitTransaction();

            RealmQuery<SessionData> querySes = realm.where(SessionData.class);
            RealmResults<SessionData> resultSesDa = querySes.findAll();
            Log.i("Realm Session Data Query", resultSesDa.toString());

            shareHomeDataViewModel.sendSessionData(sessionData);
        }

        if (sessionManager.isLoggedIn()){
            //Get data session Realm
            SessionData sessionData = realm.where(SessionData.class).findFirst();
            Log.i("Home User Name", sessionData.getNombre().concat(" ").concat(sessionData.getPrimer_ap()).concat(" ").concat(sessionData.getSegundo_ap()));
            Log.i("Home User Email", sessionData.getEmail());
            authorization = sessionData.getToken();
            usernameText.setText(sessionData.getNombre().concat(" ").concat(sessionData.getPrimer_ap()).concat(" ").concat(sessionData.getSegundo_ap()));
            userEmailText.setText(sessionData.getEmail());

        } else {
            Intent intentX = new Intent(getApplicationContext(), LoginActivity.class);
            intentX.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentX);
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cursos, R.id.nav_subir, R.id.nav_about)
                .setOpenableLayout(drawer)//.setDrawerLayout(drawer)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        homeViewModel.getLogoutData().observe(this, new Observer<RespUserLogout>() {
            @Override
            public void onChanged(RespUserLogout respUserLogout) {
                Log.e(TAG, respUserLogout.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

        int id = item.getItemId();
        Log.i(TAG, "Holassssssssss!!!");
        Log.i(TAG, "Selected: "+handled);

        if (id == R.id.nav_cursos) {

        } else if (id == R.id.nav_subir) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_salir) {
            logoutUser();
            homeViewModel.logout(context, authorization);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return handled;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void logoutUser() {
        //Clear Session data user
        realm.beginTransaction();
        realm.delete(SessionData.class);
        realm.delete(CursoData.class);
        realm.delete(VideoData.class);
        realm.commitTransaction();
        //Realm check
        RealmQuery<SessionData> querySesDa = realm.where(SessionData.class);
        RealmResults<SessionData> resultSesDa = querySesDa.findAll();
        Log.i("Realm Session Data Query", resultSesDa.toString());

        sessionManager.setLogin(false);

        // Launching the login activity and close (CLEAR) home activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}