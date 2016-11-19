package com.example.ronen.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private MovieEntriesAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new MovieEntriesAdapter(this, new ArrayList<MovieEntry>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onStart() {
        super.onStart();

        NetworkUtils n = new NetworkUtils();
        n.getMovies(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<MovieEntry> movies = NetworkUtils.ConvertJsonStringToMovies(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.update(movies);
                    }
                });

            }
        });
    }

    public static class MovieEntriesAdapter extends RecyclerView.Adapter<MovieEntriesAdapter.ImageViewHolder> {

        private List<MovieEntry> mMovies;
        private final LayoutInflater inflater;
        private Context context;
        public MovieEntriesAdapter(Context context, List<MovieEntry> movies) {
            this.mMovies = movies;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = inflater.inflate(R.layout.movie_item, parent, false);
            ImageViewHolder holder = new ImageViewHolder(itemView);
            holder.imageView = (ImageView)itemView.findViewById(R.id.grid_image_view);

            return holder;
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            MovieEntry movieEntry = mMovies.get(position);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movieEntry.getImageUrl()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        public void update(List<MovieEntry> movies) {
            if (null != movies) {
                mMovies.clear();
                mMovies.addAll(movies);
                notifyDataSetChanged();
            }

        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            public ImageViewHolder (View itemView) {
                super(itemView);
            }
        }

    }


}
