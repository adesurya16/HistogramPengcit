package com.alhudaghifari.bildghifar.tugasUTS;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alhudaghifari.bildghifar.R;

/**
 * Created by Alhudaghifari on 13:07 16/10/18
 */
public class RecyclerUts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int size;
    private int clicked;

    private OnButtonClickListener onButtonYaListener;

    public RecyclerUts(Context context, int size, int clicked) {
        mContext = context;
        this.size = size;
        this.clicked = clicked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_bottom, parent, false);
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        return new ViewHolderSolution(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolderSolution viewHolderSolution = (ViewHolderSolution) holder;

        if (position == clicked)
            viewHolderSolution.mLinearLayoutIcon.setBackgroundColor(mContext.getResources().getColor(R.color.halfblueclicked));

        switch (position) {
            case 0:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_brightness_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Brightness");
                break;
            case 1:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_grayscale_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Grayscale");
                break;
            case 2:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_bw_white);
                viewHolderSolution.mTextViewNamaIcon.setText("BnW");
                break;
            case 3:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_equalizer_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Equalizer");
                break;
            case 4:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_predictnum_chaincode_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Chaincode");
                break;
            case 5:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_predictnum_thinning_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Predict Char");
                break;
            case 6:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_photo_filter_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Mean");
                break;
            case 7:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_photo_filter_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Median");
                break;
            case 8:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_photo_filter_white);
                viewHolderSolution.mTextViewNamaIcon.setText("difference");
                break;
            case 9:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_photo_filter_white);
                viewHolderSolution.mTextViewNamaIcon.setText("gradient");
                break;
            case 10:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_blur_linear_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Sobel");
                break;
            case 11:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_blur_linear_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Prewit");
                break;
            case 12:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_blur_linear_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Frei-Chen");
                break;
            case 13:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_blur_linear_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Robert");
                break;
            case 14:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_refresh_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Refresh");
                break;
            default:
                viewHolderSolution.mImageViewIcon.setImageResource(R.drawable.ic_refresh_white);
                viewHolderSolution.mTextViewNamaIcon.setText("Reset");
                break;
        }

        viewHolderSolution.mLinearLayoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonYaListener != null) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (onButtonYaListener != null)
                                onButtonYaListener.onClick(position);
                        }
                    }, 250);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    protected class ViewHolderSolution extends RecyclerView.ViewHolder {
        public View mViewContainer;
        public LinearLayout mLinearLayoutIcon;
        public ImageView mImageViewIcon;
        public TextView mTextViewNamaIcon;

        public ViewHolderSolution(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            mLinearLayoutIcon = (LinearLayout) itemView.findViewById(R.id.linlayMenuBottom);
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            mTextViewNamaIcon = (TextView) itemView.findViewById(R.id.tvTextIcon);
        }
    }

    public interface OnButtonClickListener {
        void onClick(int posisi);
    }

    public void setOnButtonYaListener(OnButtonClickListener onButtonYaListener) {
        this.onButtonYaListener = onButtonYaListener;
    }
}
