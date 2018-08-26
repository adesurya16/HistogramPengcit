package com.alhudaghifari.bildghifar;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alhudaghifari on 8/29/2017.
 */

public class RecyclerPhoto extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerPhoto(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            default :
                view = LayoutInflater.from(mContext).inflate(R.layout.list_year_design, parent, false);
                return new ViewHolderArticle(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int posisi = getItemViewType(position);
        ViewHolderArticle viewHolderArticle1 = (ViewHolderArticle) holder;
        final int posisiAdapter2 = holder.getAdapterPosition();

        switch (posisi) {
            case 0:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.a1998);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 1998");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("belum masuk TK");
                break;
            case 1:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.b2000);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2000");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("udah masuk TK, sedang latihan haji");
                break;
            case 2:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.c2009);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2009");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("Pagelaran SMP");
                break;
            case 3:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.d2010);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2010");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMA kelas 1");
                break;
            case 4:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.e2011);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2011");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("panitia diesnatalis SMA kelas 2");
                break;
            case 5:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.f2013);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2013");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("baru lulus SMA");
                break;
            case 6:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.g2013);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2013");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("awal masuk kuliah");
                break;
            case 7:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.h2014);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2014");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di kosan");
                break;
            case 8:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.i2015);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2015");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di kampus semarang");
                break;
            case 9:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.j2015);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2015");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("awal hijrah ke bandung");
                break;
            case 10:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.k2016);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2016");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("depan labtek v");
                break;
            case 11:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.l2016);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2016");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di mihrab salman");
                break;
            case 12:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.a1998);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 1998 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("belum masuk TK");
                break;
            case 13:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.b2000);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2000 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("udah masuk TK, sedang latihan haji");
                break;
            case 14:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.c2009);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2009 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("Pagelaran SMP");
                break;
            case 15:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.d2010);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2010 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMA kelas 1");
                break;
            case 16:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.e2011);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2011 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("panitia diesnatalis SMA kelas 2");
                break;
            case 17:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.f2013);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2013 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("baru lulus SMA");
                break;
            case 18:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.g2013);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2013 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("awal masuk kuliah");
                break;
            case 19:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.h2014);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2014 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di kosan");
                break;
            case 20:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.i2015);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2015 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di kampus semarang");
                break;
            case 21:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.j2015);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2015 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("awal hijrah ke bandung");
                break;
            case 22:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.k2016);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2016 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("depan labtek v");
                break;
            case 23:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.l2016);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Foto tahun ke : 2016 (Grayscale)");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("di mihrab salman");
                break;
        }

        viewHolderArticle1.mViewContainer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mOnArtikelClickListener != null) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mOnArtikelClickListener != null)
                                        mOnArtikelClickListener.onClick(posisiAdapter2);
                                }
                            }, 250);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return 24;
    }

    //JIKA CONTAINER DI KLIK
    public interface OnArtikelClickListener {

        void onClick(int posisi);
    }

    public void setOnArtikelClickListener(OnArtikelClickListener onArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener;
    }

    protected class ViewHolderArticle extends RecyclerView.ViewHolder {
        public View mViewContainer;
        public CardView mCardViewContainer;

        public ImageView mImageViewKu;

        public TextView mTextViewTeksJudulBerita;
        public TextView mTextViewTeksKeteranganBerita;

        public ViewHolderArticle(View itemView) {
            super(itemView);

            mViewContainer = itemView;
            mCardViewContainer = (CardView) itemView.findViewById(R.id.cardview_container);

            mImageViewKu = (ImageView) itemView.findViewById(R.id.imgKu);

            mTextViewTeksJudulBerita = (TextView) itemView.findViewById(R.id.text_judulfoto);
            mTextViewTeksKeteranganBerita = (TextView) itemView.findViewById(R.id.text_keterangan);
        }
    }
}
