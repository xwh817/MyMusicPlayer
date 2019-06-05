package xwh.player.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xwh.lib.music.entity.Song;
import xwh.lib.music.player.MusicManager;
import xwh.lib.music.player.SongList;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/4.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
	private Context mContext;
	private List<Song> mSongs;

	public SongListAdapter(Context context) {
		mContext = context.getApplicationContext();
	}

	public void setData(List<Song> list) {
		mSongs = list;
		SongList.sSongList = list;
		notifyDataSetChanged();
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

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void update(int position) {
			Song song = mSongs.get(position);
			this.mTextIndex.setText("" + (position + 1));
			this.mTextName.setText(song.name);
			this.mItemView.setTag(position);
		}

		// 给 button1设置一个点击事件
		@OnClick(R.id.item_content)
		public void onItemClick(View item) {
			int position = (int) item.getTag();
			Song song = mSongs.get(position);
			Toast.makeText(mContext, song.toString(), Toast.LENGTH_SHORT).show();
			MusicManager.getInstance().play(position);
		}

	}
}
