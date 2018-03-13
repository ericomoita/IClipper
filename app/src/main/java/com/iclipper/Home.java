package com.iclipper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iclipper.adapter.TraducoesListAdapter;
import com.iclipper.data.TraducoesMock;
import com.iclipper.entities.Traducoes;
import com.iclipper.helper.DialogOrdena;
import com.iclipper.helper.Firebase;
import com.iclipper.helper.MonitorarClipboard;
import com.iclipper.helper.Preferences;
import com.iclipper.helper.Speak;
import com.iclipper.listener.OnListClickInteractionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {


    // Variáveis da classe
    private ViewHolder mViewHolder = new ViewHolder();
    private Context mContext;
    Cursor cursor;
    SQLiteDatabase bancoDados;
    List<Traducoes> carList;
    OnListClickInteractionListener listener;
    TraducoesListAdapter carListAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressBar loadingRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseAuth firebaseAuth;
    Preferences preferences;
    MaterialRippleLayout delete;
    ImageButton btOrdenar;
    MaterialRippleLayout btDataDown,btDataUp,btNomeDown,btNomeUp,btNivelDown,btNivelUp;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);//Habilita botao de diminuir ou aumentar volume

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingRecyclerView = (ProgressBar)findViewById(R.id.loadingRecyclerView);
        delete = (MaterialRippleLayout) findViewById(R.id.delete);
        btOrdenar = (ImageButton) findViewById(R.id.ordenar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        btDataDown = findViewById(R.id.btDataDown);
        btDataUp = findViewById(R.id.btDataUp);
        btNomeDown = findViewById(R.id.btNomeDown);
        btNomeUp = findViewById(R.id.btNomeUp);
        btNivelDown = findViewById(R.id.btNivelDown);
        btNivelUp = findViewById(R.id.btNivelUp);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.Laranja);
        bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
        //final SharedPreferences sharedPreferences = getSharedPreferences("ARQUIVO_PREFERENCIA",0);
        startService(new Intent(getApplicationContext(),MonitorarClipboard.class));
         preferences = new Preferences(getApplicationContext());
        //preferences.Preferences(getApplicationContext());
        if( preferences.getUserId() == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
           // System.out.println(Firebase.getUserId());
            try {
                // mDatabase = FirebaseDatabase.getInstance();
                database.getInstance().setPersistenceEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //myRef = database.getInstance().getReference("traducoes").child("Usuario").child("Erico Moita");

            // Instancia variáveis
            this.mContext = this;

            carList = new ArrayList<>();

            preencherLista("data");
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Limpa Recyler view
                carListAdapter.notifyItemRangeRemoved(0, carList.size());
                for(int i = 0; i < carList.size(); i++){
                    carListAdapter.removerItem(i);
                }
                carList.clear();

                preencherLista("data");
            }
        });

        //Abre tela para liberar permissão de janela flutuante
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(Home.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                carListAdapter.removerItem(id);

            }
        };
        this.mViewHolder.mRecyclerCars = (RecyclerView) this.findViewById(R.id.recycler_cars);
        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.mViewHolder.mRecyclerCars.setLayoutManager(linearLayoutManager);

        btOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  final DialogOrdena dialogOrdena = new DialogOrdena(Home.this);

              Dialog dialog = new Dialog(Home.this);
              dialog.setContentView(R.layout.layout_dialog);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }
        });



    }


    public void preencherLista(String ordenacao){
/*        final Calendar data = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String date = formatter.format(data.getTimeInMillis());*/
        //Milessegundos
        // data.getTimeInMillis()
        Firebase.getFirebase().child(preferences.getUserId()).keepSynced(true);
        Firebase.getFirebase().child(preferences.getUserId()).orderByChild(ordenacao).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int contador = 0;
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String key = locationSnapshot.child("key").getValue().toString();
                    long millData = Long.parseLong(locationSnapshot.child("data").getValue().toString());
                    String txtOrigem = locationSnapshot.child("txtOrigem").getValue().toString().toUpperCase();
                    String txtDestino = locationSnapshot.child("txtDestino").getValue().toString().toUpperCase();
                    String lgOrigem = locationSnapshot.child("lgOrigem").getValue().toString().toUpperCase();
                    String lgDestino = locationSnapshot.child("lgDestino").getValue().toString().toUpperCase();
                    String dicionario = locationSnapshot.child("sinonimo").getValue().toString().toUpperCase();
                    String exibeAlerta = locationSnapshot.child("exibeAlerta").getValue().toString();

                    /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String data = formatter.format(millData);*/

                    Traducoes teste = new Traducoes(key,contador, millData, txtOrigem, txtDestino, lgOrigem, lgDestino,dicionario,exibeAlerta);
                    carList.add(contador, teste);

                    contador++;
                }
                // 2 - Definir adapter passando listagem de carros e listener

                //Oculta o loading
                loadingRecyclerView.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
                 Speak speak =  new Speak();
               TextToSpeech tts = speak.carregaAudio(getApplicationContext(),"EN");
                carListAdapter = new TraducoesListAdapter(carList,listener,tts);

                mViewHolder.mRecyclerCars.setAdapter(carListAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Firebase.getFirebaseAuth().signOut();
            preferences.setUserId(null);

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private static class ViewHolder {
        RecyclerView mRecyclerCars;
    }
}
