package com.pallaw.firebasegallery.ui.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pallaw.firebasegallery.R
import com.pallaw.firebasegallery.dummy.DummyContent
import com.pallaw.firebasegallery.viewmodel.MainActivityViewModel
import com.pallaw.firebasegallery.viewmodel.factory.MainViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    GalleryFragment.OnListFragmentInteractionListener {

    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mFileBucketRef: StorageReference
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application)
        ).get(MainActivityViewModel::class.java)

        // init firebase variables
        initFirebase()

        //setActions
        setActions()
    }

    private fun setActions() {
        fab.setOnClickListener { view ->
            pickImage()
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun initFirebase() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images")
        mFileBucketRef = FirebaseStorage.getInstance().getReference("images")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handlePickedImage(resultCode, data)

    }

    private fun handlePickedImage(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            fileUri?.let {
                uploadImageToFirebase(it)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToFirebase(file: Uri) {

        viewModel.uploadNewPhoto(file).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(applicationContext, "Image uploaded successfully", Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(applicationContext, "Image uploaded error", Toast.LENGTH_LONG).show()
        })
    }

}
