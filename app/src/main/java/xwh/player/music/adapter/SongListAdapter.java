package xwh.player.music.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import xwh.lib.music.entity.Song;
import xwh.lib.music.player.SongList;
import xwh.player.music.PlayerActivity;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/4.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
	private Context mContext;
	private List<Song> mSongs;

	public SongListAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<Song> list) {
		mSongs = list;
		SongList.sSongList = list;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_song_image_item, parent, false));
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
		@BindView(R.id.item_image)
		ImageView mImage;
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
			//mTextIndex.setText("" + (position + 1));
			mTextName.setText(song.getName());
			mTextArtist.setText(song.getArtist());
			if (!TextUtils.isEmpty(song.getCover())) {
				Glide.with(mContext).load(song.getCover(100))
						.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(16,8)))
						.into(mImage);
			}

		}

		// 给 button1设置一个点击事件
		@OnClick(R.id.item_content)
		public void onItemClick(View item) {
			int position = getAdapterPosition();    // viewHolder去获取当前位置
			Song song = mSongs.get(position);
			mContext.startActivity(PlayerActivity.obtainIntent(mContext, song));
		}

	}
}
