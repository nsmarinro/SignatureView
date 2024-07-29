package nsmarinro.signatureview

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import nsmarinro.signatureview.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get data
        val imageFinal = intent.getByteArrayExtra("imageFinal")
        val imageBoundingBox = intent.getByteArrayExtra("imageBoundingBox")

        //convert to Bitmap
        val bitmapImageFinal = BitmapFactory.decodeByteArray(imageFinal, 0, imageFinal?.size ?: 0)
        val bitmapImageBoundingBox = BitmapFactory.decodeByteArray(imageBoundingBox, 0, imageBoundingBox?.size ?: 0)

        //converting bitmap to drawable
        val drawableImageFinal = BitmapDrawable(resources, bitmapImageFinal)
        val drawableImageBoundingBox = BitmapDrawable(resources, bitmapImageBoundingBox)

        //show images
        binding.imageView.setImageDrawable(drawableImageBoundingBox)
        binding.imageViewFinalImage.setImageDrawable(drawableImageFinal)

        binding.toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }
}
