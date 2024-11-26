package nsmarinro.signatureview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import nsmarinro.signatureview.databinding.ActivityExampleBinding
import java.io.ByteArrayOutputStream

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signatureView.setPathColor(Color.BLACK)
        binding.signatureView.setWidth(200.0)

        binding.buttonCancel.setOnClickListener {
            binding.signatureView.signatureClear()
        }

        binding.buttonDone.setOnClickListener {
            val imageBitmap: Bitmap? = binding.signatureView.getSignatureBitmap()
            if (imageBitmap != null) {
                val imageFinal = bitmapToByteArray(imageBitmap)
                val imageBoundingBox =
                    bitmapToByteArray(
                        binding.signatureView.getSignatureBitmap(true),
                    )
                val stringImage = byteArrayToBase64(imageFinal)

                Intent(this, ImageActivity::class.java)
                    .apply {
                        this.putExtra("imageFinal", imageFinal)
                        this.putExtra("imageBoundingBox", imageBoundingBox)
                    }.also { intent -> startActivity(intent) }
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBase64(byteArray: ByteArray?): String {
        if (byteArray == null) return ""
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
