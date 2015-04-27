package se.k3.antonochisak.kd323bassignment5.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import org.parceler.transfuse.util.matcher.ImplementsMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.k3.antonochisak.kd323bassignment5.R;
import se.k3.antonochisak.kd323bassignment5.adapters.PopularMoviesAdapter;
import se.k3.antonochisak.kd323bassignment5.adapters.TrendingMoviesAdapter;
import se.k3.antonochisak.kd323bassignment5.api.RestClient;
import se.k3.antonochisak.kd323bassignment5.api.model.ApiResponse;
import se.k3.antonochisak.kd323bassignment5.api.model.RootApiResponse;
import se.k3.antonochisak.kd323bassignment5.models.movie.Movie;

import static se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers.FIREBASE_CHILD;
import static se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers.FIREBASE_URL;

public class TrendingFragment extends MoviesFragment
    implements Callback<List<RootApiResponse>>, ListView.OnItemClickListener {


    // List of movies
    ArrayList<Movie> mMovies;

    // This is pushed to mFireBase
    HashMap<String, Object> mMovieMap;

    RestClient mRestClient;
    Firebase mFireBase;
    Firebase mRef;

    String mCurrentClickedMovie = "";

    CountDownTimer mVoteTimer;
    boolean mIsVoteTimerRunning = false;

    TrendingMoviesAdapter mAdapter;

    @InjectView(R.id.listView)
    ListView mMoviesGrid;

    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovies = new ArrayList<>();
        mMovieMap = new HashMap<>();

        mRestClient = new RestClient();
        mFireBase = new Firebase(FIREBASE_URL);
        mRef = mFireBase.child(FIREBASE_CHILD);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        // Inject views
        ButterKnife.inject(this, view);

        // Create adapter
        mAdapter = new TrendingMoviesAdapter(mMovies, getActivity().getLayoutInflater());
        mMoviesGrid.setAdapter(mAdapter);

        // listener= GridView.OnItemClickListener
        mMoviesGrid.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // listener = Callback<List<ApiResponse>>
        // go to http://docs.trakt.apiary.io/#introduction/extended-info, what should you include?
        mRestClient.getApiService().getTrending("images", this);
        mProgressBar.setVisibility(View.VISIBLE);
        initVoteTimer();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!mIsVoteTimerRunning) {
            voteOnMovie(i);
            mVoteTimer.start();
            mIsVoteTimerRunning = true;
        }
    }

    void initVoteTimer() {
        // So that there can only be one vote per every 3 seconds
        mVoteTimer = new CountDownTimer(0005, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mIsVoteTimerRunning = false;
            }
        };
    }

    void voteOnMovie(final int i) {
        Movie movie = mMovies.get(i);

        // Very important
        mCurrentClickedMovie = movie.getSlugline();

        mMovieMap.put("title", movie.getTitle());
        mMovieMap.put("year", movie.getYear());
        mMovieMap.put("slugline", movie.getSlugline());
        mMovieMap.put("poster", movie.getPoster());
        mMovieMap.put("fanart", movie.getFanArt());

        mRef.child(mCurrentClickedMovie).updateChildren(mMovieMap, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Toast.makeText(getActivity(), "Gillade " + mMovies.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                updateVotes();
            }
        });
    }

    void updateVotes() {
        mRef.child(mCurrentClickedMovie + "/votes").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    public void success(List<RootApiResponse> apiResponses, Response response) {
        Log.i("Success", "we're in success");
        mProgressBar.setVisibility(View.GONE);
            for (RootApiResponse r : apiResponses) {

            // Build a new movie-object for every response and add to list
            Movie movie = new Movie.Builder()
                    .title(r.apiResponse.title)
                    .slugLine(r.apiResponse.ids.getSlug())
                            .poster(r.apiResponse.image.getPoster().getMediumPoster())
                           .fanArt(r.apiResponse.image.getFanArt().getFullFanArt())
                           .year(r.apiResponse.year)
                    .build();
            Log.i("Success", "Added item!"+r.apiResponse.title);
            mMovies.add(movie);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void failure(RetrofitError error) {
        if(error.getKind() == RetrofitError.Kind.NETWORK) {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.retrofit_network_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
