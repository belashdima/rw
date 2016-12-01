package com.belashdima.rememberwords.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by belashdima on 27.02.16.
 */
public class WordTranslation extends AbstractLearnableItem {
    private String word;
    private String translation;
    private int notifyNextNum;
    private String notifyNextTime;
    private int customOrder;

    public WordTranslation(int id, int listId, String word, String translation, int notifyNextNum, String notifyNextTime, int customOrder) {
        super(id, listId);
        this.word = word;
        this.translation = translation;
        this.notifyNextNum = notifyNextNum;
        this.notifyNextTime = notifyNextTime;
        this.customOrder = customOrder;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getNotifyNextNum() {
        return notifyNextNum;
    }

    public void setNotifyNextNum(int notifyNextNum) {
        this.notifyNextNum = notifyNextNum;
    }

    public String getNotifyNextTime() {
        return notifyNextTime;
    }

    public void setNotifyNextTime(String notifyNextTime) {
        this.notifyNextTime = notifyNextTime;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }

    @Override
    public String getMainInscription() {
        return getWord();
    }

    @Override
    public String getAuxiliaryInscription() {
        return getTranslation();
    }


    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeInt(this.listId);
        out.writeString(this.word);
        out.writeString(this.translation);
        out.writeInt(this.notifyNextNum);
        out.writeString(this.notifyNextTime);
        out.writeInt(this.customOrder);
    }

    public static final Parcelable.Creator<WordTranslation> CREATOR = new Parcelable.Creator<WordTranslation>() {
        public WordTranslation createFromParcel(Parcel in) {
            return new WordTranslation(in);
        }

        public WordTranslation[] newArray(int size) {
            return new WordTranslation[size];
        }
    };

    private WordTranslation(Parcel in) {
        super(in);
        this.word = in.readString();
        this.translation = in.readString();
        this.notifyNextNum = in.readInt();
        this.notifyNextTime = in.readString();
        this.customOrder = in.readInt();
    }
    //
}
