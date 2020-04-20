package com.pallaw.firebasegallery.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pallaw.firebasegallery.data.resources.Photo
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Pallaw Pathak on 20/04/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
@Dao
interface PhotoDao {
    @Insert
    fun insert(photo: Photo)

    @Delete
    fun delete(photo: Photo)


    //    @Query("DELETE FROM images")
//    fun deleteAll(photo: Photo)
//
    @Query("SELECT * from images ")
    fun getAllPhotos(): Flowable<List<Photo>>
}