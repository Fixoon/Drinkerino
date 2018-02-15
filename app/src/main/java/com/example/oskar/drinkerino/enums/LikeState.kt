package com.example.oskar.drinkerino.enums

enum class LikeState(val boolInt:Int?) {
    LIKED(1),
    NOT_LIKED(0),
    IGNORE(null);

    companion object {
        private val map = LikeState.values().associateBy(LikeState::boolInt);
        fun fromInt(type: Int) = map[type]
    }
}