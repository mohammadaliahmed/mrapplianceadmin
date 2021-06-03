package com.appsinventiv.mrapplianceadmin.Activities.ChatManagement;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.Models.ChatModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.Constants;
import com.appsinventiv.mrapplianceadmin.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 24/06/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatModel> chatList;

    public int RIGHT_CHAT = 1;
    public int LEFT_CHAT = 0;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatAdapter.ViewHolder viewHolder;
        if (viewType == RIGHT_CHAT) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_layout, parent, false);
            viewHolder = new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_layout, parent, false);
            viewHolder = new ChatAdapter.ViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatModel model = chatList.get(position);
        holder.msgtext.setText(model.getText());
        holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));

        holder.status.setImageResource(R.drawable.ic_read);


        if (model.getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE)) {
            holder.msgtext.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.document.setVisibility(View.GONE);
            Glide.with(context).load(model.getImageUrl()).into(holder.image);

        } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_DOCUMENT)) {
            holder.msgtext.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.document.setVisibility(View.VISIBLE);
        } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_TEXT)) {
            holder.msgtext.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.document.setVisibility(View.GONE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ViewPictures.class);
//                i.putExtra("url", model.getImageUrl());
//                context.startActivity(i);
//                String filename = "" + model.getImageUrl().substring(model.getImageUrl().length() - 7, model.getImageUrl().length());
//
//
//                File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DOWNLOADS) + "/" + filename + ".png");
//
//                if (applictionFile != null && applictionFile.exists()) {
//                } else {
//                    DownloadFile.fromUrl(model.getImageUrl(), filename + ".png");
//
//
//                }


            }
        });
        holder.document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DownloadFile.fromUrl1(model.getDocumentUrl());
//                String filename = "" + model.getDocumentUrl().substring(model.getDocumentUrl().length() - 7, model.getDocumentUrl().length());
//                File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DOWNLOADS) + "/" + filename + model.getMediaType());
//
//                if (applictionFile != null && applictionFile.exists()) {
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
//                    context.startActivity(intent);
//
//                } else {
//                    DownloadFile.fromUrl1(model.getDocumentUrl(), filename + model.getMediaType(), new FileDownloaded() {
//                        @Override
//                        public void onFileDownloaded(String filename) {
//                            CommonUtils.showToast("downloaded");
//                            File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
//                                    Environment.DIRECTORY_DOWNLOADS) + "/" + filename);
//                            Intent intent = new Intent();
//                            intent.setAction(android.content.Intent.ACTION_VIEW);
//
//                            intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
//                            context.startActivity(intent);
//                        }
//                    });
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//
//                    intent.setDataAndType(Uri.fromFile(applictionFile),getMimeType(applictionFile.getAbsolutePath()));
//                    context.startActivity(intent);
//                }
            }
        });


    }

    private String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = chatList.get(position);
        if (model.getUsername().equals(SharedPrefs.getUsername())) {
            return RIGHT_CHAT;
        } else {
            return LEFT_CHAT;
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msgtext, time;
        ImageView status, image, document;

        public ViewHolder(View itemView) {
            super(itemView);
            msgtext = itemView.findViewById(R.id.msgtext);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            document = itemView.findViewById(R.id.document);
            image = itemView.findViewById(R.id.image);
        }
    }
}
