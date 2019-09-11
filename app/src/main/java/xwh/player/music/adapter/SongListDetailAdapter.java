package xwh.player.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xwh.lib.music.entity.PlayList;
import xwh.lib.music.entity.Song;
import xwh.player.music.PlayerActivity;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/4.
 */
public class SongListDetailAdapter extends RecyclerView.Adapter<SongListDetailAdapter.BaseViewHolder> {
	private static final int ITEM_TYPE_IMAGE = 0;
	private static final int ITEM_TYPE_DESC = 1;
	private static final int ITEM_TYPE_LIST = 2;

	private Context mContext;
	private PlayList mPlayList;

	public SongListDetailAdapter(Context context, PlayList playList) {
		mContext = context;
		mPlayList = playList;
	}

	public void setData(PlayList playList) {
		mPlayList = playList;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ITEM_TYPE_IMAGE;
		}
		if (position == 1) {
			return ITEM_TYPE_DESC;
		}
		return ITEM_TYPE_LIST;
	}

	@NonNull
	@Override
	public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		BaseViewHolder viewHolder = null;
		switch (viewType) {
			case ITEM_TYPE_IMAGE:
				viewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_playlist_item_image, parent, false));
				break;
			case ITEM_TYPE_DESC:
				viewHolder = new TextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_playlist_item_desc, parent, false));
				break;
			case ITEM_TYPE_LIST:
				viewHolder = new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_song_item, parent, false));
				break;

		}
		return viewHolder;
	}


	@Override
	public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
		holder.update(position);
	}

	@Override
	public int getItemCount() {
		if (mPlayList == null) {
			return 0;
		}
		if (mPlayList.getSongs() == null) {
			return 2;
		}

		return mPlayList.getSongs().size() + 2;
	}


	public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
		public BaseViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		public abstract void update(int position);
	}

	public class ImageViewHolder extends BaseViewHolder {
		@BindView(R.id.image)
		ImageView mImage;

		public ImageViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@Override
		public void update(int position) {
			Glide.with(mContext).load(mPlayList.getCoverImgUrl()).into(mImage);
		}

	}
	public class TextViewHolder extends BaseViewHolder {
		@BindView(R.id.description)
		TextView mDesc;

		public TextViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@Override
		public void update(int position) {
			mDesc.setText(mPlayList.getDescription());
		}

	}

	public class ListViewHolder extends BaseViewHolder {

		@BindView(R.id.item_content)
		View mItemView;
		@BindView(R.id.item_index)
		TextView mTextIndex;
		@BindView(R.id.item_name)
		TextView mTextName;
		@BindView(R.id.item_artist)
		TextView mTextArtist;

		public ListViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@Override
		public void update(int position) {
			position = position - 2;
			Song song = mPlayList.getSongs().get(position);
			mTextIndex.setText("" + (position + 1));
			mTextName.setText(song.getName());
			mTextArtist.setText(song.getArtist());
		}

		// 给 button1设置一个点击事件
		@OnClick(R.id.item_content)
		public void onItemClick(View item) {
			int position = getAdapterPosition();    // viewHolder去获取当前位置
			position = position - 2;
			Song song = mPlayList.getSongs().get(position);
			mContext.startActivity(PlayerActivity.obtainIntent(mContext, song));
		}

	}
}
