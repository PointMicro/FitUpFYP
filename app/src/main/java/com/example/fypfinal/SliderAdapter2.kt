package com.example.fypfinal


import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter



class SliderAdapter2(private val images: ArrayList<String>) :
    SliderViewAdapter<SliderAdapter2.SliderViewHolder>() {

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider, null)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        if (position == 0) {
            // For the first item, set the weather card image
            val inflater = LayoutInflater.from(viewHolder.itemView.context)
            val view = inflater.inflate(R.layout.weather_tab, null)
            val bitmap = getBitmapFromView(view)
            viewHolder.imageView.setImageBitmap(bitmap)
        } else {
            // For the other items, set the regular image from the drawable resource
            viewHolder.imageView.setImageResource(
                viewHolder.itemView.context.resources.getIdentifier(
                    images[position], "drawable", viewHolder.itemView.context.packageName
                )
            )
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    inner class SliderViewHolder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.my_images)
    }
}
