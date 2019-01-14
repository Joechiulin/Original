package com.pig.original.user;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pig.original.Addfriend.Friend;
import com.pig.original.Addfriend.addfriend;
import com.pig.original.Common.Common;
import com.pig.original.Common.CommonTask;
import com.pig.original.ImageTask.ImageTask;
import com.pig.original.R;

import java.lang.reflect.Type;
import java.util.List;

public class Friendlistfragement extends Fragment {
    private static final String TAG = "SpotListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvFriends;
    private CommonTask spotGetAllTask;
    private CommonTask spotDeleteTask;
    private ImageTask spotImageTask;
    private FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friend, container, false);
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllFriends();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

       rvFriends = view.findViewById(R.id.rvFriends);
       rvFriends.setLayoutManager(new LinearLayoutManager(activity));
        FloatingActionButton btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new addfriend();
                changeFragment(fragment);
            }
        });
        return view;
    }

    private void showAllFriends() {
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/SpotServlet";
            List<Friend> friends = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            spotGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = spotGetAllTask.execute().get();
                Type listType = new TypeToken<List<Friend>>() {
                }.getType();
                friends = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (friends == null || friends.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoSpotsFound);
            } else {
                rvFriends.setAdapter(new SpotsRecyclerViewAdapter(activity, friends));
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllFriends();
    }

    private class SpotsRecyclerViewAdapter extends RecyclerView.Adapter<SpotsRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Friend> friends;
        private int imageSize;

        SpotsRecyclerViewAdapter(Context context, List<Friend> friends) {
            layoutInflater = LayoutInflater.from(context);
            this.friends = friends;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName, tvLogintime, tvLastloginRegion;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivFriend);
                tvName = itemView.findViewById(R.id.tvName);
                tvLogintime = itemView.findViewById(R.id.tvLogintime);
                tvLastloginRegion = itemView.findViewById(R.id.tvLastloginRegion);
            }
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.frienditemview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Friend friend = friends.get(position);
            String url = Common.URL + "/SpotServlet";
            int id = friend.getId();
            spotImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            spotImageTask.execute();
            myViewHolder.tvName.setText(friend.getName());
            myViewHolder.tvLogintime.setText(friend.getLogintime());
            myViewHolder.tvLastloginRegion.setText(friend.getLastloginRegion());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new UserPersonalFragement();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friend", friend);
                    fragment.setArguments(bundle);
                    changeFragment(fragment);
                }
            });
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
//                                case R.id.update:
//                                    Fragment fragment = new SpotUpdateFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("spot", spot);
//                                    fragment.setArguments(bundle);
//                                    changeFragment(fragment);
//                                    break;
                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL + "/SpotServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "spotDelete");
                                        jsonObject.addProperty("spot", new Gson().toJson(friend));
                                        int count = 0;
                                        try {
                                            spotDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = spotDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.msg_DeleteFail);
                                        } else {
                                            friends.remove(friend);
                                            SpotsRecyclerViewAdapter.this.notifyDataSetChanged();
                                            Common.showToast(activity, R.string.msg_DeleteSuccess);
                                        }
                                    } else {
                                        Common.showToast(activity, R.string.msg_NoNetwork);
                                    }
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

    }

    private void changeFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().
                    replace(R.id.content, fragment).addToBackStack(null).commit();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (spotGetAllTask != null) {
            spotGetAllTask.cancel(true);
            spotGetAllTask = null;
        }

        if (spotImageTask != null) {
            spotImageTask.cancel(true);
            spotImageTask = null;
        }

        if (spotDeleteTask != null) {
            spotDeleteTask.cancel(true);
            spotDeleteTask = null;
        }
    }
}

