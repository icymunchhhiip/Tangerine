package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

public class RecipeSearch {
    @SerializedName("r_name")
    public String r_name = "";
    @SerializedName("r_kinds")
    public byte[] r_kinds= new byte[2];
    @SerializedName("r_level")
    public byte[] r_level = new byte[4];
    @SerializedName("r_tool")
    public byte[] r_tool = new byte[4];
    @SerializedName("r_time")
    public byte[] r_time = new byte[3];
}
