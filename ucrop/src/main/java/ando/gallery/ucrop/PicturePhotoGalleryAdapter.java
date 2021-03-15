package ando.gallery.ucrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import ando.gallery.ucrop.R;

import java.io.File;
import java.util.List;

import ando.gallery.ucrop.callback.BitmapLoadCallback;
import ando.gallery.ucrop.model.CutInfo;
import ando.gallery.ucrop.model.ExifInfo;
import ando.gallery.ucrop.util.BitmapLoadUtils;
import ando.gallery.ucrop.util.MimeType;
import ando.gallery.ucrop.util.SdkUtils;

/**
 * 图片列表
 */
public class PicturePhotoGalleryAdapter extends RecyclerView.Adapter<PicturePhotoGalleryAdapter.ViewHolder> {
    private final int maxImageWidth = 200;
    private final int maxImageHeight = 220;
    private Context context;
    private List<CutInfo> list;
    private LayoutInflater mInflater;

    public PicturePhotoGalleryAdapter(Context context, List<CutInfo> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    public void setData(List<CutInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.ucrop_picture_gf_adapter_edit_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String path = "";
        CutInfo photoInfo = list.get(position);
        if (photoInfo != null) {
            path = photoInfo.getPath();
        }
        if (photoInfo.isCut()) {
            holder.iv_dot.setVisibility(View.VISIBLE);
            holder.iv_dot.setImageResource(R.drawable.ucrop_oval_true);
        } else {
            holder.iv_dot.setVisibility(View.INVISIBLE);
        }
        boolean isHasVideo = MimeType.isHasVideo(photoInfo.getMimeType());
        if (isHasVideo) {
            holder.mIvPhoto.setVisibility(View.GONE);
            holder.mIvVideo.setVisibility(View.VISIBLE);
            holder.mIvVideo.setImageResource(R.drawable.ucrop_ic_default_video);
        } else {
            holder.mIvPhoto.setVisibility(View.VISIBLE);
            holder.mIvVideo.setVisibility(View.GONE);
            Uri uri = SdkUtils.isQ() || MimeType.isHttp(path) ? Uri.parse(path) : Uri.fromFile(new File(path));
            holder.tvGif.setVisibility(MimeType.isGif(photoInfo.getMimeType()) ? View.VISIBLE : View.GONE);
            BitmapLoadUtils.decodeBitmapInBackground(context, uri, photoInfo.getHttpOutUri(), maxImageWidth,
                    maxImageHeight, new BitmapLoadCallback() {
                        @Override
                        public void onBitmapLoaded(@NonNull Bitmap bitmap,
                                                   @NonNull ExifInfo exifInfo,
                                                   @NonNull String imageInputPath,
                                                   @Nullable String imageOutputPath) {
                            if (holder.mIvPhoto != null && bitmap != null) {
                                holder.mIvPhoto.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Exception bitmapWorkerException) {
                            if (holder.mIvPhoto != null) {
                                holder.mIvPhoto.setImageResource(R.color.ucrop_color_ba3);
                            }
                        }
                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(holder.getAdapterPosition(), v);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvPhoto;
        ImageView iv_dot;
        ImageView mIvVideo;
        TextView tvGif;

        public ViewHolder(View view) {
            super(view);
            mIvPhoto = view.findViewById(R.id.iv_photo);
            mIvVideo = view.findViewById(R.id.iv_video);
            iv_dot = view.findViewById(R.id.iv_dot);
            tvGif = view.findViewById(R.id.tv_gif);
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
