package xwh.player.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xwh.lib.music.entity.PlayList;
import xwh.player.music.PlayListActivity;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/9/9.
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
	private Context mContext;
	private List<PlayList> mPlayLists;

	public PlayListAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<PlayList> list) {
		mPlayLists = list;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_playlist_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.update(position);
	}

	@Override
	public int getItemCount() {
		return mPlayLists == null ? 0 : mPlayLists.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.item_image)
		ImageView mItemImage;
		@BindView(R.id.item_name)
		TextView mTextName;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void update(int position) {
			PlayList playList = mPlayLists.get(position);
			mTextName.setText(playList.getName());
			Glide.with(mContext).load(playList.getCoverImgUrl(320)).into(mItemImage);
		}

		// 设置点击事件
		@OnClick(R.id.item_content)
		public void onItemClick(View item) {
			int position = getAdapterPosition();    // viewHolder去获取当前位置
			PlayList playList = mPlayLists.get(position);
			mContext.startActivity(PlayListActivity.obtainIntent(mContext, playList));
		}
	}
}
