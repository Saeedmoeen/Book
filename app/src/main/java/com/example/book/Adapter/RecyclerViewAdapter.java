package com.example.book.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.book.Model.ShowPostModel.Post;
import com.example.book.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<Post> data;
    private Context context;
    public RecyclerViewAdapter(Context context, List<Post> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        Post post = data.get(i);

        Glide.with(context)
                .load(post.getImage())
                .into(viewHolder.imageView);

        viewHolder.tv_title.setText(post.getTitle());
        viewHolder.tv_description.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tv_title, tv_description;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}
