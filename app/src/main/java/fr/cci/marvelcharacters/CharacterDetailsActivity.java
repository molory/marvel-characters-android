package fr.cci.marvelcharacters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CharacterDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_CHARACTER_MODEL = "extraCharacterModel";

    private ImageView ivPicture;

    private TextView tvName;

    private TextView tvDescription;

    private CharacterModel characterModel;

    public static Intent newIntent(Context context, CharacterModel characterModel) {
        Intent intent = new Intent(context, CharacterDetailsActivity.class);
        intent.putExtra(EXTRA_CHARACTER_MODEL, characterModel);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupData();
        setTitle(characterModel.getName());
        setContentView(R.layout.activity_character_details);
        setupViews();
        setupActionBar();
    }

    private void setupViews() {
        ivPicture = (ImageView) findViewById(R.id.iv_picture_bg);
        tvName = (TextView) findViewById(R.id.tv_character_details_name);
        tvDescription = (TextView) findViewById(R.id.tv_character_details_description);

        Glide.with(this).load(characterModel.getPictureUrl()).into(ivPicture);

        tvName.setText(characterModel.getName());
        final String description = characterModel.getDescription();
        final boolean isDescriptionEmpty = TextUtils.isEmpty(description);
        tvDescription.setText(isDescriptionEmpty ? getString(R.string.character_empty_description) : description);
        tvDescription.setTypeface(null, isDescriptionEmpty ? Typeface.ITALIC : Typeface.NORMAL);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupData() {
        characterModel = getIntent().getParcelableExtra(EXTRA_CHARACTER_MODEL);
        if (characterModel == null) {
            throw new IllegalStateException("Should not be null. Please use newIntent() method to start this activity");
        }
    }
}
