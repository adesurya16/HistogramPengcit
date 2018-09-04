package com.alhudaghifari.bildghifar.tugas1Histogram;

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

import com.alhudaghifari.bildghifar.R;

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
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.fari_smp);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Fari");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMP");
                break;
            case 1:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.fari_sma);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Fari");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMA");
                break;
            case 2:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.fari_kuliah);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Fari");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("Kuliah");
                break;
            case 3:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.ade_smp);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Ade");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMP");
                break;
            case 4:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.ade_sma);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Ade");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMA");
                break;
            case 5:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.ade_kuliah);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Ade");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("Kuliah");
                break;
            case 6:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.kemal_smp);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Kemal");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMP");
                break;
            case 7:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.kemal_sma);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Kemal");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("SMA");
                break;
            case 8:
                viewHolderArticle1.mImageViewKu.setImageResource(R.drawable.kemal_kuliah);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText("Kemal");
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText("Kuliah");
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
        return 9;
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
