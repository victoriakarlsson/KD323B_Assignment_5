package se.k3.antonochisak.kd323bassignment5.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import se.k3.antonochisak.kd323bassignment5.R;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers;
import se.k3.antonochisak.kd323bassignment5.models.movie.Movie;

/**
 * Created by Mattias on 2015-04-27.
 */
public class TrendingMoviesAdapter extends BaseAdapter {
    ArrayList<Movie> mMovies;
    LayoutInflater mLayoutInflater;
    private int mItemWidth, mItemHeight, mMargin;

    public TrendingMoviesAdapter(ArrayList<Movie> mMovies, LayoutInflater mLayoutInflater) {
        this.mMovies = mMovies;
        this.mLayoutInflater = mLayoutInflater;
    }

    // We always use a viewholder pattern on listviews!
    class ViewHolder {
        @InjectView(R.id.posterya)
        ImageView poster;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);

            int screenWidth = StaticHelpers.getScreenWidth(view.getContext());
            mItemWidth = (screenWidth / 2);
            mItemHeight = (int) ((double) mItemWidth / 0.677);
            mMargin = StaticHelpers.getPixelsFromDp(view.getContext(), 2);
        }
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.trending_list_items, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

 // Load pictures with picasso
            Picasso.with(view.getContext())
            .load(mMovies.get(i).getPoster())
            .resize(mItemWidth, mItemHeight)
    .into(holder.poster);
    return view;
}

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
