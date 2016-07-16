package com.belashdima.rememberwords.model;

/**
 * Created by belashdima on 26.06.16.
 */
public abstract class AbstractLearnableItem {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String getMainInscription();

    public abstract String getAuxiliaryInscription();
}
