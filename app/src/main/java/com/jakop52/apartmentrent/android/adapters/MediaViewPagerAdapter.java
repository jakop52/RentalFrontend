package com.jakop52.apartmentrent.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.jakop52.apartmentrent.android.R;

import java.util.List;

public class MediaViewPagerAdapter extends RecyclerView.Adapter<MediaViewPagerAdapter.MediaViewHolder> {

    private Context context;
    private List<Object> mediaList;

    public MediaViewPagerAdapter(Context context, List<Object> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Object mediaItem = mediaList.get(position);
        if (mediaItem instanceof Bitmap) {
            Glide.with(context)
                    .load((Bitmap) mediaItem)
                    .into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.playerView.setVisibility(View.GONE);
        } else if (mediaItem instanceof Drawable) {
            Glide.with(context)
                    .load((Drawable) mediaItem)
                    .into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.playerView.setVisibility(View.GONE);
        } else if (mediaItem instanceof Uri && isVideoUri((Uri) mediaItem)) {
            setUpExoPlayer((Uri) mediaItem, holder);
            holder.imageView.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.VISIBLE);
            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
            holder.playerView.setPlayer(player);
            MediaItem videoItem = MediaItem.fromUri((Uri) mediaItem);
            player.setMediaItem(videoItem);
            player.prepare();
            player.play();
        } else {
        }
    }

    private boolean isVideoUri(Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("video/");
    }

    private void setUpExoPlayer(Uri videoUri, MediaViewHolder holder) {
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        holder.playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, "your-app-name");
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        PlayerView playerView;

        MediaViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewMedia);
            playerView = itemView.findViewById(R.id.playerViewMedia);
        }
    }
}
