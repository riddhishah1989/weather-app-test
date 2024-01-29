package com.jpmorgan.test.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.ConnectivityManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jpmorgan.test.R
import java.io.IOException
import java.util.Locale

class CommonUtils {

    companion object {

        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null
        }

        /*
            Get the city name from Lat-Long
        */
        fun getCityNameFromLatLong(context: Context, latitude: Double, longitude: Double): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            var cityName = ""

            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        cityName = addresses[0].locality ?: ""
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return cityName
        }

        /*
            Load Image using Glide with caching strategy with Success & Error handling
        */
        fun showImage(context: Context, url: String, imageView: ImageView) {
            println("Image URL = $url")

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Cache the image
                .error(R.drawable.error_loading_place_holder)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .placeholder(R.drawable.image_place_holder)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        println("Image Error: $e")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(imageView)
        }

        fun showMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}