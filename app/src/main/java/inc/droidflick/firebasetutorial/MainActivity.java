package inc.droidflick.firebasetutorial;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import inc.droidflick.firebasetutorial.adapter.MoviesAdapter;
import inc.droidflick.firebasetutorial.interfaces.FireBaseChildCountCallInterface;
import inc.droidflick.firebasetutorial.interfaces.FirebaseCallInterface;
import inc.droidflick.firebasetutorial.interfaces.FirebaseUrlCallInterface;
import inc.droidflick.firebasetutorial.model.Movie;
import inc.droidflick.firebasetutorial.model.UserModel;
import inc.droidflick.firebasetutorial.firebasenetwork.FireBaseHelper;

public class MainActivity extends AppCompatActivity {


    Context context;

    StorageReference mFStorage = null;

    StorageReference filePath = null;

    Uri uri;

    DatabaseReference databaseReference = null;

    // ArrayList<UserModel> userModelArrayList;

    ArrayList<String> userKeyArrayList;

    //List View Data
    ListView listView;
    ArrayAdapter<String> keyListAdapter;

    //Recycler Data
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    boolean userScrolled = false;

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initListViewUi();

        initRecycleViewUi();

        context = this;
        // userModelArrayList = new ArrayList<>();
        userKeyArrayList = new ArrayList<>();


        //Single Time Call
        loadSingleValueEventData();

        //Call on every Event Triggered.!
        loadValueEventListenerData();

        // Child Count
        getChildCount();

        prepareMovieData(); //dummy data

        implementScrollListener();  // infinite scroll listener.!


        uploadImageFirebase(filePath, uri, firebaseImageUrlCall);

        uploadAudioFirebase(filePath, uri, firebaseAudioUrlCall);

    }

    private void getChildCount() {

        databaseReference = FireBaseHelper.getDataBaseRefrence();
        databaseReference.child("movies");

        FireBaseHelper.getInstance(context)
                .getChildCount(databaseReference, childCountCallInterface);

    }

    FireBaseChildCountCallInterface childCountCallInterface = new FireBaseChildCountCallInterface() {
        @Override
        public void onFirebaseCallComplete(int childCount) {

            Toast.makeText(context, "Child Count :" + childCount, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFirebaseCallFailure(DatabaseError databaseError) {


        }
    };


    private void uploadImageFirebase(StorageReference filePath, Uri uri, FirebaseUrlCallInterface firebaseUrlForImageCall) {

        filePath = mFStorage.child("image").child(FireBaseHelper.getRandomId() + ".png");


        FireBaseHelper.getInstance(context).uploadImage(filePath, uri, firebaseUrlForImageCall);

    }

    FirebaseUrlCallInterface firebaseImageUrlCall = new FirebaseUrlCallInterface() {
        @Override
        public void onFirebaseCallComplete(String url) {

        }

        @Override
        public void onFirebaseCallFailure(@NonNull Exception exception) {

        }
    };


    private void uploadAudioFirebase(StorageReference filePath, Uri uri, FirebaseUrlCallInterface firebaseUrlForImageCall) {

        filePath = mFStorage.child("image").child(FireBaseHelper.getRandomId() + ".mp3");


        FireBaseHelper.getInstance(context).uploadAudio(uri, filePath, firebaseUrlForImageCall);

    }

    FirebaseUrlCallInterface firebaseAudioUrlCall = new FirebaseUrlCallInterface() {
        @Override
        public void onFirebaseCallComplete(String url) {

        }

        @Override
        public void onFirebaseCallFailure(@NonNull Exception exception) {

        }
    };


    private void initRecycleViewUi() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListViewUi() {

        listView = (ListView) findViewById(R.id.listView);

    }

    private void loadSingleValueEventData() {

        databaseReference = FireBaseHelper.getDataBaseRefrence();
        databaseReference.child("movies");

        FireBaseHelper.getInstance(context)
                .addListenerForSingleValueEvent(databaseReference, firebaseSingleValueEventDataInterface);

//        String loading = getResources().getString(R.string.loading);
//        FireBaseHelper.showDialog(context, loading);

    }

    private void loadValueEventListenerData() {

        databaseReference = FireBaseHelper.getDataBaseRefrence();
        databaseReference.child("movies");

        FireBaseHelper.getInstance(context)
                .addValueEventListener(databaseReference, firebaseValueEventListenerInterface);

        String loading = getResources().getString(R.string.loading);
        FireBaseHelper.showDialog(context, loading);

    }

    FirebaseCallInterface firebaseValueEventListenerInterface = new FirebaseCallInterface() {
        @Override
        public void onFirebaseCallComplete(DataSnapshot dataSnapshot) {

            FireBaseHelper.dismissDialog();


            movieList = FireBaseHelper.readSnapShot(dataSnapshot, Movie.class);
            mAdapter = new MoviesAdapter(movieList);
            recyclerView.setAdapter(mAdapter);

//              userKeyArrayList = FireBaseHelper.readSnapShot(dataSnapshot, String.class);
//              userModelArrayList = FireBaseHelper.readSnapShot(dataSnapshot, UserModel.class);
//              updateUi(userKeyArrayList);

        }

        @Override
        public void onFirebaseCallFailure(DatabaseError databaseError) {

            FireBaseHelper.dismissDialog();

        }
    };


    FirebaseCallInterface firebaseSingleValueEventDataInterface = new FirebaseCallInterface() {
        @Override
        public void onFirebaseCallComplete(DataSnapshot dataSnapshot) {

            FireBaseHelper.dismissDialog();


            movieList = FireBaseHelper.readSnapShot(dataSnapshot, Movie.class);
            mAdapter = new MoviesAdapter(movieList);
            recyclerView.setAdapter(mAdapter);

//              userKeyArrayList = FireBaseHelper.readSnapShot(dataSnapshot, String.class);
//              userModelArrayList = FireBaseHelper.readSnapShot(dataSnapshot, UserModel.class);
//              updateUi(userKeyArrayList);

        }

        @Override
        public void onFirebaseCallFailure(DatabaseError databaseError) {

            FireBaseHelper.dismissDialog();

        }
    };

    private void updateUi(ArrayList<String> userKeyArrayList) {

        keyListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, userKeyArrayList);
        // Assign adapter to ListView
        listView.setAdapter(keyListAdapter);

    }


    // Implement scroll listener
    public void implementScrollListener() {

        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView,
                                                     int newState) {

                        super.onScrollStateChanged(recyclerView, newState);

                        // If scroll state is touch scroll then set userScrolled
                        // true
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            userScrolled = true;

                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx,
                                           int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        // Here get the child count, item count and visibleitems
                        // from layout manager

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        // Now check if userScrolled is true and also check if
                        // the item is end then update recycler view and set
                        // userScrolled to false
                        if (userScrolled
                                && (visibleItemCount + pastVisibleItems) == totalItemCount) {
                            userScrolled = false;
                            Toast.makeText(MainActivity.this, "Scrolling", Toast.LENGTH_SHORT).show();
                            prepareMovieData();
                        }
                    }
                });
    }


    private void prepareMovieData() {

        Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2001");
        movieList.add(movie);

        movie = new Movie("Inside Out", "Animation, Kids & Family", "2002");
        movieList.add(movie);

        movie = new Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2003");
        movieList.add(movie);

        movie = new Movie("Shaun the Sheep", "Animation", "2004");
        movieList.add(movie);

        movie = new Movie("The Martian", "Science Fiction & Fantasy", "2005");
        movieList.add(movie);

        movie = new Movie("Mission: Impossible Rogue Nation", "Action", "2006");
        movieList.add(movie);

        movie = new Movie("Up", "Animation", "2007");
        movieList.add(movie);

        movie = new Movie("Star Trek", "Science Fiction", "2008");
        movieList.add(movie);

        movie = new Movie("The LEGO Movie", "Animation", "2009");
        movieList.add(movie);

        movie = new Movie("Iron Man", "Action & Adventure", "2010");
        movieList.add(movie);

        movie = new Movie("Aliens", "Science Fiction", "2011");
        movieList.add(movie);

        movie = new Movie("Chicken Run", "Animation", "2012");
        movieList.add(movie);

        movie = new Movie("Back to the Future", "Science Fiction", "2013");
        movieList.add(movie);

        movie = new Movie("Raiders of the Lost Ark", "Action & Adventure", "2014");
        movieList.add(movie);

        movie = new Movie("Goldfinger", "Action & Adventure", "2015");
        movieList.add(movie);


        mAdapter.notifyDataSetChanged();
    }

}
