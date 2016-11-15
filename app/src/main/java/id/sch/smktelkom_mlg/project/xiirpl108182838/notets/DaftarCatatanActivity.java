package id.sch.smktelkom_mlg.project.xiirpl108182838.notets;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.helper.DBAdapter;
import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.model.Catatan;
import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.R;

public class DaftarCatatanActivity extends Activity {
    private ListView listCatatan;
    private ArrayList<Catatan> dataCatatan;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_catatan);
        setupView();
        ambilData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean setupView() {
//koneksikan variabel listCatatan dengan list di layout
        listCatatan = (ListView) findViewById(R.id.lvCatatan);
//event saat listview diklik pindah ke halaman detail
        listCatatan.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v,
                                    int posisi, long id) {
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
        listCatatan.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int posisi, long id) {
                //ambil data catatan yang dipilih
                Catatan c = dataCatatan.get(posisi);
//tampilkan dialog edit dan delete
                tampilkanDialog(c);
                return false;
            }
        });
        return true;
    }

    //dialog edit dan delete
    protected void tampilkanDialog(final Catatan c) {
        String opsiDialog[] = {"Edit Catatan", "Hapus Catatan"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                DaftarCatatanActivity.this);
        builder.setNeutralButton("Tutup",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.setTitle("Edit Or Delete");
        builder.setItems(opsiDialog,
                new OnClickListener() {
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
                                AlertDialog.Builder builderx = new
                                        AlertDialog.Builder(
                                        DaftarCatatanActivity.this);
                                builderx.setNeutralButton("Cancel",
                                        new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builderx.setPositiveButton("Hapus",
                                        new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//koneksi db dan hapus data yang akan dihapus
                                                DBAdapter db = new DBAdapter(
                                                        DaftarCatatanActivity.this);
                                                db.open();
                                                db.deleteData(c.getId());
                                                db.close();
// refreshh list view
                                                ambilData();
                                            }
                                        });
                                builderx.setTitle("Hapus Catatan");
                                builderx.setMessage("Apakah Anda Yakin Mau menghapus Catatan ini ? ");
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
                dataCatatan.add(c);
                cur.moveToNext();
            }
//tutup koneksi database
            db.close();
// masukkan kedalam custom listview
//buat adapter dari inner class CustomAdapter
            CustomAdapter adapter = new CustomAdapter(getBaseContext(),
                    dataCatatan);
//masukkan adapter ke dalam listView
            listCatatan.setAdapter(adapter);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DaftarCatatan Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
//masukkan judul dari catatantitle.setText(c.getTitle());
            int color = c.getWarna();
//set warna backgroud listview sesuai warna yang dipilih
            if (color == 0) {
                vi.setBackgroundColor(Color.parseColor("#d45e26"));
            } else if (color == 1) {
                vi.setBackgroundColor(Color.parseColor("#c9403a"));
            } else if (color == 2) {
                vi.setBackgroundColor(Color.parseColor("#3399ff"));
            } else if (color == 3) {
                vi.setBackgroundColor(Color.parseColor("#abcd40"));
            } else if (color == 4) {
                vi.setBackgroundColor(Color.parseColor("#948d8a"));
            }
            return vi;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// tambahkan menu daftar catatan
        getMenuInflater().inflate(R.menu.daftar_catatan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_catatan) {
//jika menu diklik akan pindah ke halaman tambah catatan
            Intent i = new Intent(getBaseContext(),
                    TambahCatatanActivity.class);
            i.putExtra("edit", false);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}