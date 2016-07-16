package com.belashdima.rememberwords.model;

/**
 * Created by belashdima on 27.02.16.
 */
public class WordTranslation extends AbstractLearnableItem
{
    private String word;
    private String translation;
    private int notifyNextNum;
    private String notifyNextTime;
    private int customOrder;

    public WordTranslation(int id, String word, String translation, int notifyNextNum, String notifyNextTime, int customOrder) {
        this.id = id;
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
}
