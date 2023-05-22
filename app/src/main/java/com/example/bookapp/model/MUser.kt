package com.example.bookapp.model

data class MUser(val id:String?,
                val userId:String,
                val displayName:String,
                 val quote:String,
                 val proffesion:String,
                val avatarUrl:String){
    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.proffesion,
            "avatarUrl" to this.avatarUrl
        )
    }
}
