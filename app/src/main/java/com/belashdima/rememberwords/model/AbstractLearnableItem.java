package com.belashdima.rememberwords.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by belashdima on 26.06.16.
 */
public abstract class AbstractLearnableItem implements Parcelable {
    protected int id;
    protected int listId;

    protected AbstractLearnableItem(int id, int listId) {
        this.id = id;
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String getMainInscription();

    public abstract String getAuxiliaryInscription();

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }


    // Parcelable implementation
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(listId);
    }

    protected AbstractLearnableItem(Parcel in) {
        this.id = in.readInt();
        this.listId = in.readInt();
    }
    //
}
