package com.belashdima.rememberwords.model;

import java.util.List;

/**
 * Created by belashdima on 19.06.16.
 */
public class WordTranslationGroup extends AbstractLearnableItem {
    private String name;
    private String language;
    private List<WordTranslation> wordTranslationList;

    public WordTranslationGroup(int id, String name, String language, List<WordTranslation> wordTranslationList) {
        this.id = id;
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
}
