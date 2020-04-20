package com.pallaw.firebasegallery.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pallaw.firebasegallery.R
import com.pallaw.firebasegallery.data.resources.Photo
import com.pallaw.firebasegallery.dummy.DummyContent.DummyItem
import com.pallaw.firebasegallery.viewmodel.PhotoViewModel
import com.pallaw.firebasegallery.viewmodel.factory.PhotoViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryGridFragment : Fragment() {

    private lateinit var galleryGridAdapter: GalleryGridAdapter
    private lateinit var viewModel: PhotoViewModel
    private lateinit var mStorageRef: StorageReference
    val photoList: ArrayList<Photo> = arrayListOf()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStorageRef = FirebaseStorage.getInstance().reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //init viewmodel
        initViewModel()

        //setup photo list
        setupPhotoList()

        val subscribe = viewModel.getAllPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { photos ->
                photoList.clear()
                photoList.addAll(photos)
                galleryGridAdapter.notifyDataSetChanged()
            }
    }

    private fun initViewModel() {
        activity?.let {
            viewModel = ViewModelProvider(
                viewModelStore,
                PhotoViewModelFactory(it.application)
            ).get(PhotoViewModel::class.java)
        }
    }

    private fun setupPhotoList() {
        galleryGridAdapter = GalleryGridAdapter(
            photoList,
            listener
        )
        with(photo_list) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = galleryGridAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

}
