package com.belashdima.rememberwords.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by belashdima on 19.06.16.
 */
public class WordTranslationGroup extends AbstractLearnableItem {
    private String name;
    private String language;
    private List<WordTranslation> wordTranslationList;

    public WordTranslationGroup(int id, int listId, String name, String language, List<WordTranslation> wordTranslationList) {
        super(id, listId);
        this.name = name;
        this.language = language;
        this.wordTranslationList = wordTranslationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<WordTranslation> getWordTranslationList() {
        return wordTranslationList;
    }

    public void setWordTranslationList(List<WordTranslation> wordTranslationList) {
        this.wordTranslationList = wordTranslationList;
    }


    @Override
    public String getMainInscription() {
        return getName();
    }

    @Override
    public String getAuxiliaryInscription() {
        return getLanguage();
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WordTranslationGroup> CREATOR = new Parcelable.Creator<WordTranslationGroup>() {
        public WordTranslationGroup createFromParcel(Parcel in) {
            return new WordTranslationGroup(in);
        }

        public WordTranslationGroup[] newArray(int size) {
            return new WordTranslationGroup[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeInt(this.listId);
        out.writeString(this.name);
        out.writeString(this.language);
    }

    private WordTranslationGroup(Parcel in) {
        super(in);
        this.name = in.readString();
        this.language = in.readString();
    }
    //
}
