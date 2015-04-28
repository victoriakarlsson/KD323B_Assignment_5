package se.k3.antonochisak.kd323bassignment5.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    // Viewholder for posters
    class ViewHolder {
        @InjectView(R.id.posterya)
        ImageView poster;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);

            //  int screenWidth = StaticHelpers.getScreenWidth(view.getContext());
            // mItemWidth = (screenWidth / 2);
            // mItemHeight = (int) ((double) mItemWidth / 0.677);
            // mMargin = StaticHelpers.getPixelsFromDp(view.getContext(), 2);
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

        // Loading images with piacsso for list items.
        Log.i("TendingMoviesAdapter", "list item added!");
            Picasso.with(view.getContext())
            .load(mMovies.get(i).getPoster())

                    //.resize(mItemWidth, mItemHeight)
    .into(holder.poster);

        // Setting movie title on list textfield
        TextView mTitle = (TextView) view.findViewById(R.id.tw_title);
        mTitle.setText(mMovies.get(i).getTitle());


        //Converting movie year int to string and setting it on textfield.
        String movieYear = String.valueOf(mMovies.get(i).getYear());
        TextView mYear = (TextView) view.findViewById(R.id.tw_year);
        mYear.setText("Year: " + movieYear);

        // Setting tagline.
        TextView mTag = (TextView) view.findViewById(R.id.tw_tag);
        mTag.setText(mMovies.get(i).getTagline());

        // Setting tagline.
        TextView mOverview = (TextView) view.findViewById(R.id.tw_overview);
        mOverview.setText(mMovies.get(i).getOverview());

        ImageView poster = (ImageView) view.findViewById(R.id.posterya);
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
