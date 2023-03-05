package com.messages.abdallah.mymessages.db.Dao

import androidx.room.*
import com.messages.abdallah.mymessages.models.FavoriteModel
import com.messages.abdallah.mymessages.models.MsgsModel

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add_fav(fav: FavoriteModel)

    //    @Query("Select * from Favorite_table")
    @Query("select e.*, c.MsgTypes as typeTitle from  Favorite_table " +
            "e left join msg_types_table c  on " +
            " c.id = e.ID_Type_id where " +
            "e.ID_Type_id order by c.id DESC")
    suspend fun getAllFav(): List<FavoriteModel>

    // delete favorite item from db
    @Delete
    suspend fun deletefav(item:FavoriteModel)


}