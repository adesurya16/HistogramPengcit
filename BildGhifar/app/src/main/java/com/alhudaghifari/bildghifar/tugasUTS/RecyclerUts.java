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
public class AdapterUts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int size;

    private OnButtonYaListener onButtonYaListener;

    public AdapterUts (Context context, int size) {
        mContext = context;
        this.size = size;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_bottom, parent, false);
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_pollar));
        return new ViewHolderSolution(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolderSolution viewHolderSolution = (ViewHolderSolution) holder;
//        viewHolderSolution.mTextViewParent.setText(message);
//        viewHolderSolution.mTextViewChild.setText(description);

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

    public interface OnButtonYaListener {
        void onClick(int posisi);
    }

    public void setOnButtonYaListener(OnButtonYaListener onButtonYaListener) {
        this.onButtonYaListener = onButtonYaListener;
    }
}
