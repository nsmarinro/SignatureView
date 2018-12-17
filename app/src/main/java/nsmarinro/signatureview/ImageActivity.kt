package nsmarinro.signatureview

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        //get data
        val imageFinal = intent.getByteArrayExtra("imageFinal")
        val imageBoundingBox = intent.getByteArrayExtra("imageBoundingBox")

        //convert to Bitmap
        val bitmapImageFinal = BitmapFactory.decodeByteArray(imageFinal, 0, imageFinal.size)
        val bitmapImageBoundingBox = BitmapFactory.decodeByteArray(imageBoundingBox, 0, imageBoundingBox.size)

        //converting bitmap to drawable
        val drawableImageFinal = BitmapDrawable(resources, bitmapImageFinal)
        val drawableImageBoundingBox = BitmapDrawable(resources, bitmapImageBoundingBox)

        //show images
        imageView.setImageDrawable(drawableImageBoundingBox)
        imageViewFinalImage.setImageDrawable(drawableImageFinal)

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }
}
