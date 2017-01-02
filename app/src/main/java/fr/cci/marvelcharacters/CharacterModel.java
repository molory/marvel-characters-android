package fr.cci.marvelcharacters;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterModel implements Parcelable {

    private final String name;

    private final String description;

    private final String picturePath;

    private final String pictureExtension;

    public CharacterModel(String name, String description, String picturePath, String pictureExtension) {
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
        this.pictureExtension = pictureExtension;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return picturePath + "/standard_fantastic." + pictureExtension;
    }

    protected CharacterModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        picturePath = in.readString();
        pictureExtension = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(picturePath);
        dest.writeString(pictureExtension);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CharacterModel> CREATOR = new Parcelable.Creator<CharacterModel>() {
        @Override
        public CharacterModel createFromParcel(Parcel in) {
            return new CharacterModel(in);
        }

        @Override
        public CharacterModel[] newArray(int size) {
            return new CharacterModel[size];
        }
    };
}