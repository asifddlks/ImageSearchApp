package com.asifddlks.imagesearchapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.asifddlks.imagesearchapp.databinding.ActivityFullscreenImageBinding
import com.asifddlks.imagesearchapp.model.ImageModel
import com.asifddlks.imagesearchapp.plugin.OnSwipeTouchListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@AndroidEntryPoint
class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenImageBinding
    private lateinit var context: Context
    //private lateinit var currentPostion: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        context = this

        val bundle: Bundle? = intent.extras
        val listAsString = bundle!!.getString("listAsString")
        var position = bundle.getInt("position", 0)

        val imageList: List<ImageModel> = Gson().fromJson<List<ImageModel>>(
            listAsString,
            object : TypeToken<List<ImageModel?>?>() {}.type
        )

        Log.d("FullScreen", "imageList")

        Glide.with(this)
            .load(imageList[position].urls.regular)
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            .into(binding.imageView)


        binding.imageView.setOnTouchListener(object :
            OnSwipeTouchListener(this@FullscreenImageActivity) {
            override fun onSwipeRight() {

                Glide.with(context)
                    .load(imageList[--position].urls.regular)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(binding.imageView)
            }

            override fun onSwipeLeft() {
                Glide.with(context)
                    .load(imageList[++position].urls.regular)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(binding.imageView)
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // goto previous activity when actionbar backbutton clicked
        return super.onSupportNavigateUp()
    }
}