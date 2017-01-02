package fr.cci.marvelcharacters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private final List<CharacterModel> characterModelList ;

    private CharacterAdapterListener listener;

    public CharacterAdapter(CharacterAdapterListener listener, List<CharacterModel> characterModelList) {
        this.listener = listener;
        this.characterModelList = characterModelList;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        final CharacterModel characterModel = characterModelList.get(position);
        holder.tvName.setText(characterModel.getName());
        Glide.with(holder.itemView.getContext()).load(characterModel.getPictureUrl()).into(holder.ivPicture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCharacterClick(characterModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return characterModelList.size();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPicture;

        private final TextView tvName;

        private CharacterViewHolder(View itemView) {
            super(itemView);
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_item_character_picture);
            tvName = (TextView) itemView.findViewById(R.id.tv_item_character_name);
        }
    }
}
