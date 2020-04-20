package com.pallaw.firebasegallery.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.pallaw.firebasegallery.data.local.dao.PhotoDao
import com.pallaw.firebasegallery.data.remote.FirebaseDataManager
import com.pallaw.firebasegallery.data.resources.Photo
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by Pallaw Pathak on 20/04/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
class PhotoRepository(
    private val photoDao: PhotoDao,
    private val firebaseDataManager: FirebaseDataManager
) {

    val isPhotoUploaded: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun uploadNewPhoto(photoUri: Uri): Single<Photo> {
       return Single.create<Photo> { callback ->
            firebaseDataManager.uploadPhoto(photoUri)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { photo ->
                        photoDao.insert(photo)
                        callback.onSuccess(photo)
                    }, { error ->
                        callback.onError(error)
                    }
                )
        }
    }

    fun getPhotos(): Flowable<List<Photo>> {
        return photoDao.getAllPhotos()
    }
}