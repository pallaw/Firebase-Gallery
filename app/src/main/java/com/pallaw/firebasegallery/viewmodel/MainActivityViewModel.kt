package com.pallaw.firebasegallery.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pallaw.firebasegallery.data.local.PhotoDatabase
import com.pallaw.firebasegallery.data.remote.FirebaseDataManager
import com.pallaw.firebasegallery.data.repository.PhotoRepository
import com.pallaw.firebasegallery.data.resources.Photo
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single

/**
 * Created by Pallaw Pathak on 20/04/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhotoRepository

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao, FirebaseDataManager.getInstance(application))
//        allWords = repository.allWords
    }

    fun uploadNewPhoto(photoUri: Uri):Single<Photo> {
        return repository.uploadNewPhoto(photoUri)
    }
}