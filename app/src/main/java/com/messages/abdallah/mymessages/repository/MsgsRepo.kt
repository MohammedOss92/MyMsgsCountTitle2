package com.messages.abdallah.mymessages.repository

import com.messages.abdallah.mymessages.api.ApiService
import com.messages.abdallah.mymessages.db.LocaleSource
import com.messages.abdallah.mymessages.models.FavoriteModel
import com.messages.abdallah.mymessages.models.MsgsModel

class MsgsRepo constructor(val apiService: ApiService, private val localeSource: LocaleSource) {

    suspend fun getMsgs_Ser(ID_Type_id: Int) = apiService.getMsgs_Ser(ID_Type_id)

    suspend fun getMsgs_Dao(id: Int) = localeSource.getMsgs_Dao(id)

    suspend fun getMsgWithTitle(id: Int) = localeSource.getMsgsWithTitle(id)
    suspend fun insert_msgs(msgs: List<MsgsModel>?) {
        if (!msgs.isNullOrEmpty()) {
            localeSource.insert_msgs(msgs)
        }
    }
    // update msg_table items favorite state
    suspend fun update_fav(id: Int,state:Boolean) {

        localeSource.update_fav(id,state)
    }

    /***************************/
    suspend fun add_fav(fav: FavoriteModel) {

        localeSource.add_fav(fav)
    }

    suspend fun getAllFav() = localeSource.getAllFav()

    // delete favorite item from db
    suspend fun deleteFav(fav: FavoriteModel) {

        localeSource.delete_fav(fav)
    }

}