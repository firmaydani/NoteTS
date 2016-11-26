package id.sch.smktelkom_mlg.project.xiirpl108182838.notets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.adapter.ListAdapter;
import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.helper.DBAdapter;
import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.model.Catatan;

public class TampilanAwal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListAdapter.IListAdapter {
    public static final int REQUEST_CODE_ADD = 88;
    public static final String CATATAN = "catatan";
    boolean doubleBackToExitPressedOnce = false;
    ImageView ListCatatan;
    ArrayList<Catatan> catList = new ArrayList<>();
    ListAdapter catAdapter;
    private ListView listCatatan;
    private ArrayList<Catatan> dataCatatan;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_awal);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        catAdapter = new ListAdapter(this, catList);
        recyclerView.setAdapter(catAdapter);

        ListCatatan = (ImageView) findViewById(R.id.imageList);

        //setupView();

        ambilData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAdd();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    private void goAdd() {
        Intent i = new Intent(getBaseContext(), TambahCatatanActivity.class);
        i.putExtra("edit", false);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD&&resultCode==RESULT_OK)
        {

        }
    }

    private void setupView() {
        //koneksikan variabel listCatatan dengan list di layout
        listCatatan = (ListView) findViewById(R.id.lvCatatan);
        //event saat listview diklik pindah ke halaman detail
        listCatatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int posisi, long id) {
                v.startAnimation(buttonClick);
                //ambil catatan sesuai dengan listview yang diambil
                Catatan c = dataCatatan.get(posisi);
                //buat intent baru
                Intent i = new Intent(getBaseContext(),
                        DetailActivity.class);
                //tambahkan data yang mau dikirim ke halaman detail
                i.putExtra("judul", c.getTitle());
                i.putExtra("isi", c.getIsi());
                i.putExtra("tanggal", c.getTanggal());
                startActivity(i);

            }
        });
        //event saat listview diklik lama untuk edit dan delete
        listCatatan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int posisi, long arg3) {
                v.startAnimation(buttonClick);
                //ambil data catatan yang dipilih
                Catatan c = dataCatatan.get(posisi);
                //tampilkan dialog edit dan delete
                tampilkanDialog(c);
                return false;
            }
        });
    }

    protected void tampilkanDialog(final Catatan c) {
        String opsiDialog[] = { "Edit Catatan", "Hapus Catatan" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TampilanAwal.this);
        builder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }

                });
        builder.setTitle("Edit atau Hapus");
        builder.setItems(opsiDialog, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //saat edit dipilih pindah ke halaman edit
                                Intent i = new Intent(getBaseContext(),
                                        TambahCatatanActivity.class);
                                i.putExtra("edit", true);
                                i.putExtra("id", c.getId());
                                i.putExtra("judul", c.getTitle());
                                i.putExtra("isi", c.getIsi());
                                i.putExtra("tanggal", c.getTanggal());
                                i.putExtra("warna", c.getWarna());
                                startActivity(i);
                                break;
                            case 1:
                                //saat delete dipilih, tampilkan konfirmasi delete
                                AlertDialog.Builder builderx = new AlertDialog.Builder(TampilanAwal.this);
                                builderx.setNeutralButton("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog,int which) {
                                                dialog.dismiss();
                                            }

                                        });
                                builderx.setPositiveButton("Hapus",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface
                                                                        dialog, int which) {
                                                //koneksi db dan hapus data yang akan dihapus
                                                DBAdapter db = new DBAdapter(TampilanAwal.this);
                                                db.open();
                                                db.deleteData(c.getId());
                                                db.close();
                                                // refreshh list view
                                                ambilData();

                                            }
                                        });
                                builderx.setTitle("Hapus Catatan");
                                builderx.setMessage("Apakah Anda Yakin Ingin menghapus Catatan ini?");
                                builderx.show();
                                break;
                        }
                    }
                });
        builder.show();
    }

    //method untuk mengambil semua data catatan dari database
    private void ambilData() {
        dataCatatan = new ArrayList<Catatan>();
        //buat object dari class DBAdapter yang ada di package helper
        DBAdapter db = new DBAdapter(getBaseContext());
        //buka koneksi databse
        db.open();
        //ambil semua data catatan dgn method getData()
        Cursor cur = db.getData();

        cur.moveToFirst();
        if (cur.getCount() > 0) {
            while (cur.isAfterLast() == false) {
                Catatan c = new Catatan();
                c.setId((cur.getInt(cur
                        .getColumnIndexOrThrow(
                                DBAdapter.IDCATATAN))));
                c.setTitle((cur.getString(
                        cur.getColumnIndexOrThrow(
                                DBAdapter.TITLE_CATATAN))));
                c.setIsi((cur.getString(cur
                        .getColumnIndexOrThrow(
                                DBAdapter.ISI_CATATAN))));
                c.setTanggal((cur.getString(cur
                        .getColumnIndexOrThrow(DBAdapter.TANGGAL_CATATAN))));
                c.setWarna((cur.getInt(cur
                        .getColumnIndexOrThrow(
                                DBAdapter.WARNA_CATATAN))));
                //tambahkan ke arraylist dataCatatan
                catList.add(c);
                cur.moveToNext();
            }
            //tutup koneksi database
            db.close();

//            for (int i = 0; i < arJudul.length; i++) {
//                catList.add(new Hotel(arJudul[i], arDeskripsi[i], arFoto[i]));
//            }
//            catAdapter.notifyDataSetChanged();

            // masukkan kedalam custom listview
            //buat adapter dari inner class CustomAdapter
//            TampilanAwal.CustomAdapter adapter = new TampilanAwal.CustomAdapter(getBaseContext(), dataCatatan);
//            //masukkan adapter ke dalam listView
//            listCatatan.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            TampilanAwal.super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan BACK lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tambah) {
            // Handle the camera action
            goAdd();
        } else if (id == R.id.nav_memo) {

        } else if (id == R.id.nav_pengaturan) {

        } else if (id == R.id.nav_tentang) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void doClick(final int pos) {
        //view.startAnimation(buttonClick);
        //ambil catatan sesuai dengan listview yang diambil
        Catatan c = catList.get(pos);
        //buat intent baru
        Intent i = new Intent(getBaseContext(), DetailActivity.class);
        //tambahkan data yang mau dikirim ke halaman detail
        i.putExtra("id", c.getId());
        i.putExtra("judul", c.getTitle());
        i.putExtra("isi", c.getIsi());
        i.putExtra("tanggal", c.getTanggal());
        i.putExtra("warna", c.getWarna());
        startActivity(i);
    }

    @Override
    public void doEdit(int pos) {

    }

    @Override
    public void doDelete(int pos) {

    }

    @Override
    public void doFav(int pos) {

    }

    @Override
    public void doShare(int pos) {

    }

    // subclass untuk custom adapter pada listview
    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Catatan> dataz;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context c, ArrayList<Catatan> data) {
            context = c;
            dataz = data;
            inflater = (LayoutInflater) context
                    .getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return dataz.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            View vi = view;
            if (view == null)
                vi = inflater.inflate(R.layout.custom_listview, null);

            TextView title = (TextView) vi.findViewById(
                    R.id.tvCustomTitle);

            Catatan c = dataz.get(position);
            //masukkan judul dari catatan
            title.setText(c.getTitle());
            int color = c.getWarna();

            //set warna backgroud listview sesuai warna yang dipilih
            if (color == 0) {
                vi.setBackgroundColor(Color.parseColor("#EEE657"));
            } else if (color == 1) {
                vi.setBackgroundColor(Color.parseColor("#E3000E"));
            } else if (color == 2) {
                vi.setBackgroundColor(Color.parseColor("#83D6DE"));
            } else if (color == 3) {
                vi.setBackgroundColor(Color.parseColor("#88F159"));
            } else if (color == 4) {
                vi.setBackgroundColor(Color.parseColor("#D6DAC2"));
            }

            return vi;
        }

    }
}
