import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.example.fypfinal.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(video: ArrayList<Int>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    private var sliderList: ArrayList<Int> = video

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item, null)

        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        viewHolder?.videoView?.setVideoURI(Uri.parse("android.resource://" +
                viewHolder?.videoView?.context?.packageName + "/" + sliderList[position]))
        viewHolder?.videoView?.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.setVolume(0f, 0f)
            mediaPlayer.isLooping = true
            viewHolder?.videoView?.start()
            viewHolder?.mediaController?.hide()
            viewHolder?.mediaController?.setAnchorView(viewHolder?.videoView)
            viewHolder?.videoView?.setMediaController(viewHolder?.mediaController)
        }
    }

    inner class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var videoView: VideoView = itemView!!.findViewById(R.id.myvideos)
        var mediaController: MediaController = MediaController(itemView!!.context)

        init {
            mediaController.visibility = View.INVISIBLE
        }
    }
}
