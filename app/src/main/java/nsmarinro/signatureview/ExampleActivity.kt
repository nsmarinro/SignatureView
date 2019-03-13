package nsmarinro.signatureview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import kotlinx.android.synthetic.main.activity_example.*
import java.io.ByteArrayOutputStream

class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        signatureView.setPathColor(Color.BLACK)
        signatureView.setWidth(200.0)

        buttonCancel.setOnClickListener {
            signatureView.signatureClear()
        }

        buttonDone.setOnClickListener {
            val imageBitmap: Bitmap? = signatureView.getSignatureBitmap()
            if (imageBitmap != null) {
                val imageFinal: ByteArray = bitmapToByteArray(imageBitmap)
                val imageBoundingBox: ByteArray = bitmapToByteArray(signatureView.getSignatureBitmap(true)!!)
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
