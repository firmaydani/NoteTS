package id.sch.smktelkom_mlg.project.xiirpl108182838.notets.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.R;
import id.sch.smktelkom_mlg.project.xiirpl108182838.notets.model.Catatan;

/**
 * Created by SMK Telkom SP Malang on 26/11/2016.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<Catatan> catatanList;
    IListAdapter mIListAdapter;

    public ListAdapter(Context context, ArrayList<Catatan> hotelList) {
        this.catatanList = hotelList;
        mIListAdapter = (IListAdapter) context;

    }

//    public ListAdapter(ArrayList<Catatan> hotelList) {
//        this.catatanList = hotelList;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Catatan catatan = catatanList.get(position);
        holder.tvJudul.setText(catatan.title);

        int color = catatan.getWarna();

        //set warna backgroud listview sesuai warna yang dipilih
        if (color == 0) {
            holder.ivFoto.setBackgroundColor(Color.parseColor("#EEE657"));
//            vi.setBackgroundColor(Color.parseColor("#EEE657"));
        } else if (color == 1) {
            holder.ivFoto.setBackgroundColor(Color.parseColor("#E3000E"));
//            vi.setBackgroundColor(Color.parseColor("#E3000E"));
        } else if (color == 2) {
            holder.ivFoto.setBackgroundColor(Color.parseColor("#83D6DE"));
//            vi.setBackgroundColor(Color.parseColor("#83D6DE"));
        } else if (color == 3) {
            holder.ivFoto.setBackgroundColor(Color.parseColor("#88F159"));
//            vi.setBackgroundColor(Color.parseColor("#88F159"));
        } else if (color == 4) {
            holder.ivFoto.setBackgroundColor(Color.parseColor("#D6DAC2"));
//            vi.setBackgroundColor(Color.parseColor("#D6DAC2"));
        }
    }

    @Override
    public int getItemCount() {
        if (catatanList != null)
            return catatanList.size();
        return 0;
    }

    public interface IListAdapter {
        void doClick(int pos);

        void doEdit(int pos);

        void doDelete(int pos);

        void doFav(int pos);

        void doShare(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFoto;
        TextView tvJudul;


        public ViewHolder(View itemView) {
            super(itemView);
            ivFoto = (ImageView) itemView.findViewById(R.id.imageList);
            tvJudul = (TextView) itemView.findViewById(R.id.textViewJudul);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIListAdapter.doClick(getAdapterPosition());
                }
            });

        }
    }

}
