package com.pallaw.firebasegallery.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.pallaw.firebasegallery.Util.LogTags
import com.pallaw.firebasegallery.data.local.PhotoDatabase
import com.pallaw.firebasegallery.data.remote.FirebaseDataManager
import com.pallaw.firebasegallery.data.repository.PhotoRepository
import com.pallaw.firebasegallery.data.resources.Photo
import com.pallaw.firebasegallery.data.resources.PhotoList
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by Pallaw Pathak on 20/04/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhotoRepository

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao, FirebaseDataManager.getInstance(application))
    }

    fun uploadNewPhoto(photoUri: Uri): Single<Photo> {
        return repository.uploadNewPhoto(photoUri)
    }

    fun getAllPhotos(): Observable<PhotoList> {

        return repository.getPhotos()
            .map {
                Timber.tag(LogTags.PHOTO).d("Mapping users to UIData...")
                PhotoList(it, "Photos list")
            }
            .onErrorReturn {
                Timber.tag(LogTags.PHOTO).d("An error occurred $it")
                PhotoList(emptyList(), "An error occurred", it)
            }
    }
}