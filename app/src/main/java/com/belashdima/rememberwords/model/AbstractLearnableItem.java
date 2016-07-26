package com.belashdima.rememberwords.model;

/**
 * Created by belashdima on 26.06.16.
 */
public abstract class AbstractLearnableItem {
    protected int id;
    protected int listId;


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
}
