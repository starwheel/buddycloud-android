package com.buddycloud;

import org.json.JSONArray;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.buddycloud.http.ProfilePicCache;
import com.buddycloud.model.ModelCallback;
import com.buddycloud.model.SubscribedChannelsModel;
import com.buddycloud.preferences.Preferences;

public class SubscribedChannelsAdapter extends BaseAdapter {

	private static final int MAX_COUNTER = 50;
	private static final double AVATAR_DIP = 75.;
	
	private final Activity parent;
	
	public SubscribedChannelsAdapter(Activity parent) {
		this.parent = parent;
		fetchSubscribers();
	}
	
	private void fetchSubscribers() {
		SubscribedChannelsModel.getInstance().refresh(parent, new ModelCallback<JSONArray>() {
			@Override
			public void success(JSONArray response) {
				notifyDataSetChanged();
			}
			
			@Override
			public void error(Throwable throwable) {
				// TODO Auto-generated method stub
			}
		});
	}

	private String fetchAvatar(String channel, String apiAddress) {
		int avatarSize = (int) (AVATAR_DIP * parent.getResources().getDisplayMetrics().density + 0.5);
		String url = apiAddress + "/" + channel + "/media/avatar?maxheight=" + avatarSize;
		Bitmap avatar = ProfilePicCache.getInstance().getBitmap(url);
		if (avatar == null) {
			url = Preferences.FALLBACK_PERSONAL_AVATAR;
			if (channel.contains("@topics.buddycloud.org")) {
				url = Preferences.FALLBACK_TOPIC_AVATAR;
			}
			ProfilePicCache.getInstance().getBitmap(url);
		}
		return url;
	}
	
	@Override
	public int getCount() {
		return SubscribedChannelsModel.getInstance().get(parent).length();
	}

	@Override
	public Object getItem(int arg0) {
		return SubscribedChannelsModel.getInstance().get(parent).opt(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return SubscribedChannelsModel.getInstance().get(parent).opt(arg0).hashCode();
	}

	@Override
	public View getView(int position, View arg1, ViewGroup viewGroup) {
		LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
		View retView = inflater.inflate(R.layout.subscriber_entry, viewGroup, false);

		String channelJid = SubscribedChannelsModel.getInstance().get(parent).optString(position);
		
		TextView userIdView = (TextView) retView.findViewById(R.id.fbUserId);
		userIdView.setText(channelJid);
		
//		ImageView avatarView = (ImageView) retView.findViewById(R.id.fbProfilePic);
//		avatarView.setImageBitmap(ProfilePicCache.getInstance().getBitmap(
//				subscribedChannel.getAvatarURL()));
//		
//		TextView descriptionView = (TextView) retView.findViewById(R.id.fbMessage);
//		descriptionView.setText(subscribedChannel.getDescription());
//		
//		TextView unreadCounterView = (TextView) retView.findViewById(R.id.unreadCounter);
//		unreadCounterView.setText(subscribedChannel.getUnread().toString());
//		
//		SharedPreferences sharedPreferences = parent.getSharedPreferences(Preferences.PREFS_NAME, 0);
//		String myChannel = sharedPreferences.getString(Preferences.MY_CHANNEL_JID, null);
//		
//		if (subscribedChannel.getJid().equals(myChannel)) {
//			retView.setBackgroundColor(
//					viewGroup.getResources().getColor(R.color.bc_bg_grey));
//			retView.findViewById(R.id.unreadWrapper).setBackgroundColor(
//					viewGroup.getResources().getColor(R.color.bc_orange));
//		} else {
//			if (subscribedChannel.getUnread() == 0) {
//				retView.findViewById(R.id.unreadWrapper).setBackgroundColor(
//						viewGroup.getResources().getColor(R.color.bc_bg_grey));
//			} else {
//				retView.findViewById(R.id.unreadWrapper).setBackgroundColor(
//						viewGroup.getResources().getColor(R.color.bc_green));
//			}
//		}
		
        return retView;
	}
}
