package xwh.player.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import xwh.lib.music.entity.Song;
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
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_song_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Song song = mSongs.get(position);
		holder.update(song);
	}

	@Override
	public int getItemCount() {
		return mSongs == null ? 0 : mSongs.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.item_name)
		TextView mTextName;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void update(Song song) {
			this.mTextName.setText(song.name);
		}

	}
}
