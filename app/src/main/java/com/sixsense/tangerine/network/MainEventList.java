package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainEventList {
    @SerializedName("data")
    public List<MainEvent> data;

    public class MainEvent {
        @SerializedName("img")
        public String img;
    }
}