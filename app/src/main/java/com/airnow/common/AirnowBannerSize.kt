package com.airnow.common

enum class AirnowBannerSize(val value: Triple<Int, Int, String>) {
    MATCH_VIEW(Triple(0, 0, "MATCH VIEW")),
    SIZE_320X50(Triple(320, 50, "320x50")),
    SIZE_468X60(Triple(468, 60, "468x60")),
    SIZE_728X90(Triple(728, 90, "728x90")),
    SIZE_300X250(Triple(300, 250, "300x250"));
}
