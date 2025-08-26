package com.example.dashboard.data


import com.google.gson.annotations.SerializedName

data class EventsResponse (

    @SerializedName("data"  ) var data  : ArrayList<Event> = arrayListOf(),
    @SerializedName("links" ) var links : EventLinks?          = EventLinks(),
    @SerializedName("meta"  ) var meta  : Meta?           = Meta()

)

data class Competitors (

    @SerializedName("country_id"            ) var countryId            : String? = null,
    @SerializedName("country_flag_url"      ) var countryFlagUrl       : String? = null,
    @SerializedName("competitor_name"       ) var competitorName       : String? = null,
    @SerializedName("position"              ) var position             : Int?    = null,
    @SerializedName("result_position"       ) var resultPosition       : String? = null,
    @SerializedName("result_winnerLoserTie" ) var resultWinnerLoserTie : String? = null,
    @SerializedName("result_mark"           ) var resultMark           : String? = null

)

data class Event (

    @SerializedName("id"                   ) var id                  : Int?                   = null,
    @SerializedName("day"                  ) var day                 : String?                = null,
    @SerializedName("discipline_name"      ) var disciplineName      : String?                = null,
    @SerializedName("discipline_pictogram" ) var disciplinePictogram : String?                = null,
    @SerializedName("name"                 ) var name                : String?                = null,
    @SerializedName("venue_name"           ) var venueName           : String?                = null,
    @SerializedName("event_name"           ) var eventName           : String?                = null,
    @SerializedName("detailed_event_name"  ) var detailedEventName   : String?                = null,
    @SerializedName("start_date"           ) var startDate           : String?                = null,
    @SerializedName("end_date"             ) var endDate             : String?                = null,
    @SerializedName("status"               ) var status              : String?                = null,
    @SerializedName("is_medal_event"       ) var isMedalEvent        : Int?                   = null,
    @SerializedName("is_live"              ) var isLive              : Int?                   = null,
    @SerializedName("gender_code"          ) var genderCode          : String?                = null,
    @SerializedName("competitors"          ) var competitors         : ArrayList<Competitors> = arrayListOf()

)

data class EventLinks (

    @SerializedName("first" ) var first : String? = null,
    @SerializedName("last"  ) var last  : String? = null,
    @SerializedName("prev"  ) var prev  : String? = null,
    @SerializedName("next"  ) var next  : String? = null

)


data class MetaLinks (

    @SerializedName("url"    ) var url    : String?  = null,
    @SerializedName("label"  ) var label  : String?  = null,
    @SerializedName("active" ) var active : Boolean? = null

)

data class Meta (

    @SerializedName("current_page" ) var currentPage : Int?             = null,
    @SerializedName("from"         ) var from        : Int?             = null,
    @SerializedName("last_page"    ) var lastPage    : Int?             = null,
    @SerializedName("links"        ) var links       : ArrayList<MetaLinks> = arrayListOf(),
    @SerializedName("path"         ) var path        : String?          = null,
    @SerializedName("per_page"     ) var perPage     : Int?             = null,
    @SerializedName("to"           ) var to          : Int?             = null,
    @SerializedName("total"        ) var total       : Int?             = null

)