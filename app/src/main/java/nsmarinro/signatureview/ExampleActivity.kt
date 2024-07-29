package nsmarinro.signatureview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
                val imageFinal: ByteArray = bitmapToByteArray(imageBitmap)
                val imageBoundingBox: ByteArray = bitmapToByteArray(binding.signatureView.getSignatureBitmap(true)!!)
                val stringImage = byteArrayToBase64(imageFinal)
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra("imageFinal", imageFinal)
                intent.putExtra("imageBoundingBox", imageBoundingBox)
                startActivity(intent)
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBase64(byteArray: ByteArray?): String {
        if (byteArray == null) return ""
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}
