package xwh.player.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xwh.lib.music.entity.Song;
import xwh.player.music.PlayerActivity;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/30.
 */
public class SimpleSongListAdapter extends RecyclerView.Adapter<SimpleSongListAdapter.ViewHolder> {
	private Context mContext;
	private List<Song> mSongs;

	public SimpleSongListAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<Song> list) {
		mSongs = list;
		notifyDataSetChanged();
	}

	public List<Song> getData() {
		return mSongs;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_song_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.update(position);
	}

	@Override
	public int getItemCount() {
		return mSongs == null ? 0 : mSongs.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.item_content)
		View mItemView;
		@BindView(R.id.item_index)
		TextView mTextIndex;
		@BindView(R.id.item_name)
		TextView mTextName;
		@BindView(R.id.item_artist)
		TextView mTextArtist;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void update(int position) {
			Song song = mSongs.get(position);
			mTextIndex.setText("" + (position + 1));
			mTextName.setText(song.getName());
			mTextArtist.setText(song.getArtist());
		}

		// 给 button1设置一个点击事件
		@OnClick(R.id.item_content)
		public void onItemClick(View item) {
			//int position = (int) item.getTag();
			int position = getAdapterPosition();    // viewHolder去获取当前位置
			Song song = mSongs.get(position);
			mContext.startActivity(PlayerActivity.obtainIntent(mContext, song));
		}

	}
}
