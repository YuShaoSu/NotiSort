package com.example.jefflin.notipreference.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ESMPojo implements Serializable {

    @SerializedName("survey_properties")
    @Expose
    private ESMProperties ESMProperties;
    @SerializedName("ESMQuestions")
    @Expose
    private List<ESMQuestion> ESMQuestions = new ArrayList<ESMQuestion>();

    /**
     *
     * @return
     * The ESMProperties
     */
    public ESMProperties getESMProperties() {
        return ESMProperties;
    }

    /**
     *
     * @param ESMProperties
     * The survey_properties
     */
    public void setESMProperties(ESMProperties ESMProperties) {
        this.ESMProperties = ESMProperties;
    }

    /**
     *
     * @return
     * The ESMQuestions
     */
    public List<ESMQuestion> getESMQuestions() {
        return ESMQuestions;
    }

    /**
     *
     * @param ESMQuestions
     * The ESMQuestions
     */
    public void setESMQuestions(List<ESMQuestion> ESMQuestions) {
        this.ESMQuestions = ESMQuestions;
    }

}