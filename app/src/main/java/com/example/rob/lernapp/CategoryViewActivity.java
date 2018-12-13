package com.example.rob.lernapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rob.lernapp.adapter.TopiclistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityCategory;
import com.example.rob.lernapp.dialoge.StoragePermissionDialog;
import com.example.rob.lernapp.dialoge.StoragePermissionDialog_;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;
import com.example.rob.lernapp.restdataPost.PostResponseResponse;
import com.example.rob.lernapp.restdataPost.PostResponseTopic;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;

@EActivity(R.layout.activity_categoryview)
public class CategoryViewActivity extends AppCompatActivity implements StoragePermissionDialog.StoragePermissionDialogListener {


    public Category category;
    public ArrayList<Topic> topics;
    public ArrayList<Response> responses;
    private TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter;
    public boolean Read_External_Storage_Permission = false;
    public boolean Write_External_Storgae_Permission = false;
    public boolean streamContent = false;
    public SharedPreferences prefs;
    public SnapHelper snapHelper = new PagerSnapHelper();

    private Socket learnappSocket;

    private Gson gsonhelper = new Gson();


    private StoragePermissionDialog storagePermissionDialog;


    @NonConfigurationInstance
    @Bean
    DatabaseUtilityCategory dataBaseUtilTask;

    @ViewById(R.id.topicsrecyclerview)
    RecyclerView topicsrecyclerview;


    @Override
    protected void onPause() {
        super.onPause();
        this.topicsrecyclerview.setVisibility(View.INVISIBLE);
        initializeSockethandler(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_categoryview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_topic) {
            Intent newContent = new Intent(this, NewContentActivity_.class);
            Bundle extras = new Bundle();
            extras.putParcelable("category", this.category);
            newContent.putExtras(extras);
            this.startActivity(newContent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }*/


    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate() {
        if (getIntent().getExtras() != null) {
            this.category = getIntent().getExtras().getParcelable("category");
        }

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        snapHelper.attachToRecyclerView(topicsrecyclerview);
        setTitle(getResources().getText(R.string.topicsactivitylabel) + " - " + this.category.getName());


    }

    void initializeSockethandler(boolean resumed) {

        if (resumed) {

            this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
            this.learnappSocket.connect();
            this.learnappSocket.on("topicAdded", onTopicAddedCategoryViewActivity);
            this.learnappSocket.on("responseAdded", onResponseAddedCategoryViewActivity);
            this.learnappSocket.on("deletedGroup", onDeletedGroupCategoryViewActivity);
        } else {
            this.learnappSocket.off("topicAdded", onTopicAddedCategoryViewActivity);
            this.learnappSocket.off("responseAdded", onResponseAddedCategoryViewActivity);
            this.learnappSocket.off("deletedGroup", onDeletedGroupCategoryViewActivity);
        }

    }

    private Emitter.Listener onDeletedGroupCategoryViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            backToGroups(getJsonObjectforSocketIO(args[0]));
        }
    };

    @UiThread
    public void backToGroups(JsonObject data) {
        Learngroup deletedLearngroup = this.gsonhelper.fromJson(data, Learngroup.class);
        if (this.category.getGroup().get_id().equals(deletedLearngroup.get_id())) {
            Toast.makeText(this, "Gruppe " + deletedLearngroup.getName() + " wurde vom Admin " + deletedLearngroup.getCreator().getName() + " gelöscht", Toast.LENGTH_LONG).show();
            Intent openLearngroups = new Intent(this, LearngroupsActivity_.class);
            openLearngroups.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(openLearngroups);
            this.finish();
        }

    }


    private Emitter.Listener onResponseAddedCategoryViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (args[0] != null && args != null) {

                socketIOResponseAdded(getJsonObjectforSocketIO(args[0]), false);
            } else {
                socketIOResponseAdded(null, true);
            }

        }
    };

    @UiThread
    void socketIOResponseAdded(JsonObject data, boolean error) {

        if (this.streamContent || this.Read_External_Storage_Permission || this.Write_External_Storgae_Permission) {

            if (!error) {
                PostResponseResponse addedResponse = this.gsonhelper.fromJson(data, PostResponseResponse.class);

                if (addedResponse.getCreatedResponse().getTopic().getCategory().get_id().equals(this.category.get_id())) {

                    if (this.topiclistRecyclerviewAdapter != null) {

                        this.responses.add(addedResponse.getCreatedResponse());
                        for (Topic topic :
                                this.topics) {
                            if (topic.get_id().equals(addedResponse.getCreatedResponse().getTopic().get_id())) {
                                this.topiclistRecyclerviewAdapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    }
                }
            } else {
                Toast.makeText(this, "Fehler beim Datenempfang (Hinzugefügte Response)", Toast.LENGTH_LONG).show();
            }
        }


    }


    private Emitter.Listener onTopicAddedCategoryViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (args[0] != null && args != null) {

                socketIOTopicAdded(getJsonObjectforSocketIO(args[0]), false);
            } else {
                socketIOTopicAdded(null, true);
            }

        }
    };

    @UiThread
    void socketIOTopicAdded(JsonObject data, boolean error) {

        if (this.streamContent || this.Read_External_Storage_Permission || this.Write_External_Storgae_Permission) {

            if (!error) {
                PostResponseTopic addedTopic = this.gsonhelper.fromJson(data, PostResponseTopic.class);
                if (addedTopic.getCreatedTopic().getCategory().get_id().equals(this.category.get_id())) {
                    this.topics.add(addedTopic.getCreatedTopic());
                    this.topiclistRecyclerviewAdapter.setTopicNew(this.topics);
                    this.topiclistRecyclerviewAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Fehler beim Datenempfang (Hinzugefügte Topic)", Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        initializeSockethandler(true);

        if (Build.VERSION.SDK_INT >= 23) {

            if (this.prefs.getBoolean("dontAskAgain", false)
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                this.streamContent = true;
                dataBaseUtilTask.getResponses();

            } else if (this.prefs.getBoolean("dontAskAgain", false)
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                this.streamContent = false;
                this.Write_External_Storgae_Permission = true;
                this.Read_External_Storage_Permission = true;
                prefs.edit().putBoolean("dontAskAgain", false).commit();
                Toast.makeText(this, "Medien werden wieder gespeichert!", Toast.LENGTH_SHORT).show();
                dataBaseUtilTask.getResponses();

            } else {
                showStoragePermissionDialog();
            }
        } else {
            dataBaseUtilTask.getResponses();
            this.Read_External_Storage_Permission = true;
            this.Write_External_Storgae_Permission = true;

        }

    }

    private JsonObject getJsonObjectforSocketIO(Object arg) {
        JsonParser parser = new JsonParser();
        return parser.parse(String.valueOf(arg)).getAsJsonObject();
    }

    private void showStoragePermissionDialog() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                this.Read_External_Storage_Permission = true;
                this.Write_External_Storgae_Permission = true;
                dataBaseUtilTask.getResponses();
                prefs.edit().putBoolean("dontAskAgain", false).commit();

            } else {
                storagePermissionDialog = new StoragePermissionDialog_();
                storagePermissionDialog.setVars(this);
                storagePermissionDialog.show(getSupportFragmentManager(), "storagePermissionDialog");
            }

        } else {
            dataBaseUtilTask.getResponses();
            prefs.edit().putBoolean("dontAskAgain", false).commit();
            this.Write_External_Storgae_Permission = true;
            this.Read_External_Storage_Permission = true;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            switch (requestCode) {
                case 2:

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("PermissionResult", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        this.Write_External_Storgae_Permission = true;
                        if (this.Write_External_Storgae_Permission || this.Read_External_Storage_Permission) {
                            dataBaseUtilTask.getResponses();

                        }
                    } else {

                    }
                    break;

                case 3:

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        this.Read_External_Storage_Permission = true;
                        if (this.Write_External_Storgae_Permission || this.Read_External_Storage_Permission) {
                            dataBaseUtilTask.getResponses();

                        }
                    } else {

                    }
                    break;
            }
        }

    }


    private void initilizeTopicsRecyclerview() {


        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        topicsrecyclerview.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topicsrecyclerview.setLayoutManager(horizontalLayoutManagaer);

        this.topiclistRecyclerviewAdapter = new TopiclistRecyclerviewAdapter(this.topics, this.responses, this);
        topicsrecyclerview.setAdapter(this.topiclistRecyclerviewAdapter);
        topicsrecyclerview.addOnScrollListener(horizontalScrollListener);

        topicsrecyclerview.setVisibility(View.VISIBLE);
        topicsrecyclerview.setLayoutAnimation(animation);


    }

    public void getTopicsBack(Topic[] topics) {
        this.topics = new ArrayList<Topic>(Arrays.asList(topics));
        initilizeTopicsRecyclerview();
    }

    public void getResponsesBack(Response[] responses) {
        this.responses = new ArrayList<Response>(Arrays.asList(responses));
        dataBaseUtilTask.getTopics(this.category.get_id());
    }


    @Override
    public void StoragePermissionDialogPositiveClick(DialogFragment dialog) {
        this.streamContent = false;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        dialog.dismiss();
    }

    @Override
    public void StoragePermissionDialogNegativeClick(DialogFragment dialog, CheckBox dontAskAgain) {
        this.streamContent = true;
        dataBaseUtilTask.getResponses();

        if (dontAskAgain.isChecked()) {
            prefs.edit().putBoolean("dontAskAgain", true).commit();
        }

        dialog.dismiss();
    }

    RecyclerView.OnScrollListener horizontalScrollListener = new RecyclerView.OnScrollListener() {

        //public int finalScrollPosition;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    //   recyclerView.smoothScrollToPosition(finalScrollPosition);
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    break;
            }


        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            for (int i = 0; i < topiclistRecyclerviewAdapter.getItemCount(); i++) {

                TopiclistRecyclerviewAdapter.TopicViewHolder itemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerView.findViewHolderForAdapterPosition(i);

                if (itemviewholder != null) {
                    VideoView videoView = itemviewholder.itemView.findViewById(R.id.topiclist_media_videoview);

                    if (videoView != null) {
                        if ((MediaController) videoView.getTag(R.string.mediacontroller) != null) {
                            MediaController mediaController = (MediaController) videoView.getTag(R.string.mediacontroller);
                            mediaController.hide();
                        }

                    }
                }


            }

          /*  if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    int totalItemsInView = layoutManager.getItemCount();
                    this.finalScrollPosition = changeScrollHorizontal(totalItemsInView - 1, recyclerView);
                }
            }*/
        }
    };

    /*
    private int changeScrollHorizontal(int totalItemsInView, RecyclerView recyclerview) {
        int offset = recyclerview.computeHorizontalScrollOffset();
        int extent = recyclerview.computeHorizontalScrollExtent();

        int scrollValue;

        int offsetRest = offset % extent;
        int offsetRestMinus = offset - offsetRest;

        int viewposition;
       // int rangecalcmodulo = offset % (extent);


        int percentage = 0;
        percentage = ((rangecalcmodulo) * 100) / extent;
        float alphaValue = ((float) percentage / 100);


        if (offsetRest > (extent / 2)) {
            int extentMinusRest = extent - offsetRest;
            scrollValue = offset + extentMinusRest;
            viewposition = scrollValue / extent;

            float alphadesc = 1;
            TopiclistRecyclerviewAdapter.TopicViewHolder lastItemVieholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition - 1);
            if (lastItemVieholder != null) {
                View lastItem = lastItemVieholder.itemView;
                lastItem.setAlpha(alphadesc - alphaValue);
            }

            TopiclistRecyclerviewAdapter.TopicViewHolder currentItemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition);
            if (currentItemviewholder != null) {
                View currentItem = currentItemviewholder.itemView;
                currentItem.setAlpha(alphaValue);
            }
        } else {
            scrollValue = offsetRestMinus;
            viewposition = scrollValue / extent;


                TopiclistRecyclerviewAdapter.TopicViewHolder currentItemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition);

                if (currentItemviewholder != null) {
                    VideoView videoView = currentItemviewholder.itemView.findViewById(R.id.topiclist_media_videoview);

                    if (videoView != null) {

                        if (videoView.getTag() != null){
                            if (videoView.getTag().equals(2)){
                                if ((CustomMediaController) videoView.getTag(R.string.mediacontroller) != null) {
                                    CustomMediaController mediaController = (CustomMediaController) videoView.getTag(R.string.mediacontroller);
                                    mediaController.showforever();
                                }
                            }

                        }
                    }
                }


            float alphadesc = 1;
            TopiclistRecyclerviewAdapter.TopicViewHolder currentItemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition);
            if (currentItemviewholder != null) {
                View currentItem = currentItemviewholder.itemView;
                currentItem.setAlpha(alphadesc - alphaValue);
            }


            TopiclistRecyclerviewAdapter.TopicViewHolder lastItemVieholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition + 1);
            if (lastItemVieholder != null) {
                View lastItem = lastItemVieholder.itemView;
                lastItem.setAlpha(alphaValue);
            }

        }



        return viewposition;
    }*/

}


