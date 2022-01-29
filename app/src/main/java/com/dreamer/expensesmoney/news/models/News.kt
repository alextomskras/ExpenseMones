package com.dreamer.expensesmoney.news.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.dreamer.expensesmoney.news.models.Article

class News {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("totalResult")
    @Expose
    var totalResult = 0

    @SerializedName("articles")
    @Expose
    var article: List<Article>? = null
}