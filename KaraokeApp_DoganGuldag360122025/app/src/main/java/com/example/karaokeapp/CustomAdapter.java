package com.example.karaokeapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CardHolder> {

    ArrayList<?> itemList;
    LayoutInflater inflater;
    MediaPlayer player;

    public CustomAdapter(Context context, ArrayList<?> itemList) {

        inflater = LayoutInflater.from(context);
        this.itemList = itemList;

    }


    @NonNull
    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new CardHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (itemList.get(position) instanceof Song) {

            Song selectedSong = (Song) itemList.get(position);
            holder.setData(selectedSong, position);

        } else {

            Record selectedRecord = (Record) itemList.get(position);
            holder.setData(selectedRecord, position);

        }

    }

    @Override
    public int getItemCount() {

        return itemList.size();

    }


    class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemHeader, itemSubHeader;
        ImageView itemImage, deleteItem;

        public CardHolder(View itemView) {

            super(itemView);
            itemHeader = (TextView) itemView.findViewById(R.id.itemHeader);
            itemSubHeader = (TextView) itemView.findViewById(R.id.itemSubHeader);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            deleteItem = (ImageView) itemView.findViewById(R.id.deleteItem);

            itemView.setOnClickListener(this);
            deleteItem.setOnClickListener(this);

        }

        public void setData(Item selectedItem, int position) {

            this.itemHeader.setText(selectedItem.getHeader());
            this.itemSubHeader.setText(selectedItem.getSubHeader());
            this.itemImage.setImageResource(selectedItem.getImageId());

        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();

            if (v == deleteItem) {

                if (itemList.get(position) instanceof Record) {

                    Record selectedRecord = (Record) itemList.get(position);
                    File f = new File(selectedRecord.filePath);
                    f.delete();

                }

                itemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemList.size());

            } else {

                if (itemList.get(position) instanceof Song) {

                    Song selectedSong = (Song) itemList.get(position);
                    Intent intent = new Intent(v.getContext(), KaraokeActivity.class);
                    intent.putExtra("selectedSong", selectedSong);
                    v.getContext().startActivity(intent);

                } if (itemList.get(position) instanceof Record) {

                    Record selectedRecord = (Record) itemList.get(position);
                    playRecord(selectedRecord.filePath);

                }

            }

        }

        public void playRecord(String filePath) {

            player = new MediaPlayer();
            player.setVolume(1.0f, 1.0f);
            try {

                player.setDataSource(filePath);
                player.prepare();
                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer arg0) {

                        player.stop();
                        player.release();
                        player = null;

                    }

                });
            }
            catch (Exception e) {

            }

        }

    }
}

/*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
String path;
try {
    path = MainActivity.karaokesDir.getCanonicalPath();
} catch (IOException e) {
    throw new RuntimeException(e);
}
Uri fileUri = Uri.parse(path);
intent.setDataAndType(fileUri, "*//*");
v.getContext().startActivity(Intent.createChooser(intent, "Open folder"));*/