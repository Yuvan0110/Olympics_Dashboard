package com.example.dashboard.data


import com.google.gson.annotations.SerializedName

data class CountriesResponse (

    @SerializedName("data"  ) var data  : ArrayList<Country> = arrayListOf(),
    @SerializedName("links" ) var links : CountryLinks?          = CountryLinks(),
    @SerializedName("meta"  ) var meta  : CMeta?           = CMeta()

)

data class Country (

    @SerializedName("id"                ) var id              : String? = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("continent"         ) var continent       : String? = null,
    @SerializedName("flag_url"          ) var flagUrl         : String? = null,
    @SerializedName("gold_medals"       ) var goldMedals      : Int?    = null,
    @SerializedName("silver_medals"     ) var silverMedals    : Int?    = null,
    @SerializedName("bronze_medals"     ) var bronzeMedals    : Int?    = null,
    @SerializedName("total_medals"      ) var totalMedals     : Int?    = null,
    @SerializedName("rank"              ) var rank            : Int?    = null,
    @SerializedName("rank_total_medals" ) var rankTotalMedals : Int?    = null

)


data class CountryLinks (

    @SerializedName("url"    ) var url    : String?  = null,
    @SerializedName("label"  ) var label  : String?  = null,
    @SerializedName("active" ) var active : Boolean? = null

)

data class CMeta (

    @SerializedName("current_page" ) var currentPage : Int?             = null,
    @SerializedName("from"         ) var from        : Int?             = null,
    @SerializedName("last_page"    ) var lastPage    : Int?             = null,
    @SerializedName("links"        ) var links       : ArrayList<CountryLinks> = arrayListOf(),
    @SerializedName("path"         ) var path        : String?          = null,
    @SerializedName("per_page"     ) var perPage     : Int?             = null,
    @SerializedName("to"           ) var to          : Int?             = null,
    @SerializedName("total"        ) var total       : Int?             = null

)