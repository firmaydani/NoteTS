package id.sch.smktelkom_mlg.project.xiirpl108182838.notets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.helper.DBAdapter;

public class DetailActivity extends AppCompatActivity {

    //variabel textview koneksi ke xml
    private TextView tvJudul, tvIsi, tvTanggal;
    private String judul, tanggal, isi;
    private int id = 0;
    private int warnaDipilih;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupView();
        //ambil data judul, tanggal, dan isi yang dikirim
        //dari halaman daftar catatan
        judul = getIntent().getExtras().getString("judul", "");
        tanggal = getIntent().getExtras().getString("tanggal", "");
        isi = getIntent().getExtras().getString("isi", "");
        id = getIntent().getExtras().getInt("id");
        warnaDipilih = getIntent().getExtras().getInt("warna");

        //isi masing-masing textview sesuai datanya
        tvJudul.setText(judul);
        tvTanggal.setText(tanggal);
        tvIsi.setText(isi);

    }

    private void setupView() {
        tvJudul = (TextView) findViewById(R.id.tvJudul);
        tvTanggal = (TextView) findViewById(R.id.tvTanggal);
        tvIsi = (TextView) findViewById(R.id.tvIsi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.edit) {
            Intent i = new Intent(getBaseContext(), TambahCatatanActivity.class);
            i.putExtra("edit", true);
            i.putExtra("id", this.id);
            i.putExtra("judul", this.judul);
            i.putExtra("isi", this.isi);
            i.putExtra("tanggal", this.tanggal);
            i.putExtra("warna", this.warnaDipilih);
            startActivity(i);
        } else if (id == R.id.delete) {
            AlertDialog.Builder builderx = new AlertDialog.Builder(DetailActivity.this);
            builderx.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            builderx.setPositiveButton("Hapus",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //koneksi db dan hapus data yang akan dihapus
                            DBAdapter db = new DBAdapter(DetailActivity.this);
                            db.open();
                            db.deleteData(DetailActivity.this.id);
                            db.close();
                            Intent intent = new Intent(DetailActivity.this, TampilanAwal.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            builderx.setTitle("Hapus Catatan");
            builderx.setMessage("Apakah Anda Yakin Ingin menghapus Catatan ini?");
            builderx.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
