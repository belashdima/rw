package com.belashdima.rememberwords.database;

import com.belashdima.rememberwords.model.AbstractLearnableItem;

import java.util.List;

/**
 * Created by belashdima on 20.08.16.
 *
 * interface created to avoid dependency on implementation
 */
public interface DatabaseCommunicator {

    void saveNewAbstractLearnableItem(AbstractLearnableItem abstractLearnableItem);

    void removeAbstractLearnableItem(AbstractLearnableItem abstractLearnableItem);

    /**
     * Used on "Undo" operation, must insert with the same id
     */
    void insertAbstractLearnableItemBack(AbstractLearnableItem removedItem);

    void modifyWordTranslation(int id, String word, String translation);



    List<AbstractLearnableItem> getAbstractLearnableItemsByParentGroupId(int parentGroupId);

    AbstractLearnableItem getAbstractLearnableItemById(int id);

    List<AbstractLearnableItem> getAbstractLearnableItemsForNotification(int id);
}
