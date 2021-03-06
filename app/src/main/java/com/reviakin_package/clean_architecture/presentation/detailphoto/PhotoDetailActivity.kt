package com.reviakin_package.clean_architecture.presentation.detailphoto

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.reviakin_package.clean_architecture.R
import com.reviakin_package.clean_architecture.databinding.ActivityPhotoDetailBinding
import com.reviakin_package.clean_architecture.presentation.loadImageFull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailActivity : AppCompatActivity(), OnPhotoDetailCallback {

    private val TAG = PhotoDetailActivity::class.java.name
    private lateinit var  activityPhotoDetailBinding: ActivityPhotoDetailBinding
    private val viewModel: PhotoDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPhotoDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityPhotoDetailBinding.photoDetailViewModel = viewModel

        val photoId = intent?.extras?.getLong(KEY_PHOTO_ID) ?: return
        viewModel.getDetail(photoId)
        viewModel.checkFavoriteStatus(photoId)

        viewModel.photoData.observe(
            this, {
                it?.let {
                    activityPhotoDetailBinding.detailTitleTextView.text = it?.title
                    activityPhotoDetailBinding.detailToolbarImageView.loadImageFull(it?.url)
                }
            }
        )

        viewModel.isFavorite.observe(
            this, {
                it?.let {
                    activityPhotoDetailBinding.detailFab.setImageResource(if (it) R.drawable.ic_star_full_vector else R.drawable.ic_star_empty_white_vector)
                }
            }
        )

        activityPhotoDetailBinding.detailFab.setOnClickListener {
            viewModel.updateFavoriteStatus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                supportFinishAfterTransition()
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        private val KEY_PHOTO_ID = "KEY_PHOTO_ID"
    }
}