package nsmarinro.librarysignature

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc
import java.util.*

open class SignatureView : View {

    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private var mPath: Path? = null
    private var mPaint: Paint? = null
    private var bitmapPaint: Paint? = null
    private var mX: Float = 0F
    private var mY: Float = 0F
    private var mWidth: Double? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        bitmapPaint = Paint(Paint.DITHER_FLAG)
        mPath = Path()
        mPaint = Paint()

        mPaint?.isAntiAlias = true
        mPaint?.color = Color.BLACK
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeJoin = Paint.Join.ROUND
        mPaint?.strokeCap = Paint.Cap.ROUND
        mPaint?.strokeWidth = 4f

        this.setBackgroundColor(ResourcesCompat.getColor(context.resources, android.R.color.white, null))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPath != null && mPaint != null) {
            canvas.drawBitmap(mBitmap!!, 0F, 0F, bitmapPaint)
            canvas.drawPath(mPath!!, mPaint!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouch(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                moveTouch(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                upTouch()
                invalidate()
            }
        }
        return true
    }

    //private functions
    private fun upTouch() {
        mPath?.lineTo(mX, mY)
    }

    private fun startTouch(x: Float, y: Float) {
        mPath?.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun moveTouch(x: Float, y: Float) {
        val dx: Float = Math.abs(x - mX)
        val dy: Float = Math.abs(y - mY)
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun clearCanvas() {
        mPath?.reset()
        invalidate()
    }

    private fun getBitmap(view: View): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        bitmap.eraseColor(Color.WHITE)
        canvas.drawRGB(255, 255, 255)
        view.draw(canvas)
        return bitmap
    }

    private fun getImageFromBitmap(bitmap: Bitmap): Mat {
        val img = Mat()
        val bmp32: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        org.opencv.android.Utils.bitmapToMat(bmp32, img)

        return img
    }

    private fun getRoiSignature(inputFrame: Mat, roi: org.opencv.core.Rect): Mat {
        return Mat(inputFrame, roi)
    }

    private fun getGrayImage(image: Mat): Mat {
        val imgGray = Mat()
        imgGray.create(image.rows(), image.cols(), CvType.CV_8UC1)
        Imgproc.cvtColor(image, imgGray, Imgproc.COLOR_RGB2GRAY)

        return imgGray
    }

    private fun getBinaryImage(imageGray: Mat): Mat {
        val binary = Mat()
        Imgproc.adaptiveThreshold(
            imageGray, binary, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY_INV, 11, 12.0
        )

        return binary
    }

    //public methods
    fun signatureClear() {
        clearCanvas()
    }

    fun isSignature(): Boolean {
        return !mPath?.isEmpty!!
    }

    fun setPathColor(color: Int) {
        mPaint?.color = color
    }

    fun setWidth(width: Double) {
        mWidth = width
    }

    private fun calculateSize(image: Mat): org.opencv.core.Size {
        val k = image.height().toDouble().div(image.width().toDouble())
        return org.opencv.core.Size(mWidth!!, mWidth!! * k)
    }

    private fun setBackgroundToTransparent(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val arrayResult = IntArray(height * width)

        //get pixels array of original image
        bitmap.getPixels(arrayResult, 0, width, 0, 0, width, height)

        //change pixel color one by one
        for (i in 0 until height * width) {
            if (arrayResult[i] == Color.WHITE) arrayResult[i] = Color.TRANSPARENT
            else arrayResult[i] = mPaint?.color!!
        }

        //copy original image
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        resultBitmap.setPixels(arrayResult, 0, width, 0, 0, width, height)
        return resultBitmap
    }

    fun getSignatureBitmap(drawBoundingBox: Boolean = false): Bitmap? {
        if (isSignature()) {
            OpenCVLoader.initDebug()
            val bitmap: Bitmap = getBitmap(this)
            val img: Mat = getImageFromBitmap(bitmap)
            val imgGray: Mat = getGrayImage(img)
            val binary: Mat = getBinaryImage(imgGray)

            val contours = ArrayList<MatOfPoint>()
            Imgproc.findContours(binary, contours, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE)
            if (drawBoundingBox) {
                Imgproc.drawContours(img, contours, -1, org.opencv.core.Scalar(255.0, 255.0, 0.0), 3)
            }

            val rectangles = ArrayList<org.opencv.core.Rect>()
            contours.forEach { contour: MatOfPoint ->
                val mRect: org.opencv.core.Rect = Imgproc.boundingRect(contour)
                rectangles.add(mRect)
                if (drawBoundingBox) {
                    Imgproc.rectangle(
                        img,
                        org.opencv.core.Point(
                            mRect.x.toDouble(),
                            mRect.y.toDouble()
                        ),
                        org.opencv.core.Point(
                            mRect.x.toDouble() + mRect.width.toDouble(),
                            mRect.y.toDouble() + mRect.height.toDouble()
                        ),
                        org.opencv.core.Scalar(0.0, 255.0, 0.0),
                        2
                    )
                }
            }

            val pointsContour = ArrayList<org.opencv.core.Point>()
            rectangles.forEach { rect: org.opencv.core.Rect ->
                val point1 = org.opencv.core.Point(
                    rect.x.toDouble(),
                    rect.y.toDouble()
                )

                val point2 = org.opencv.core.Point(
                    rect.x.toDouble() + rect.width.toDouble(),
                    rect.y.toDouble()
                )

                val point3 = org.opencv.core.Point(
                    rect.x.toDouble(),
                    rect.y.toDouble() + rect.height.toDouble()
                )

                val point4 = org.opencv.core.Point(
                    rect.x.toDouble() + rect.width.toDouble(),
                    rect.y.toDouble() + rect.height.toDouble()
                )

                pointsContour.add(point1)
                pointsContour.add(point2)
                pointsContour.add(point3)
                pointsContour.add(point4)
            }

            val x = ArrayList<Double>()
            val y = ArrayList<Double>()

            pointsContour.forEach { point: org.opencv.core.Point ->
                x.add(point.x)
                y.add(point.y)
            }

            val mainRect = org.opencv.core.Rect(
                x.min()!!.toInt(),
                y.min()!!.toInt(),
                x.max()!!.toInt() - x.min()!!.toInt(),
                y.max()!!.toInt() - y.min()!!.toInt()
            )

            if (drawBoundingBox) {
                Imgproc.rectangle(
                    img,
                    org.opencv.core.Point(
                        mainRect.x.toDouble(),
                        mainRect.y.toDouble()
                    ),
                    org.opencv.core.Point(
                        mainRect.x.toDouble() + mainRect.width.toDouble(),
                        mainRect.y.toDouble() + mainRect.height.toDouble()
                    ),
                    org.opencv.core.Scalar(0.0, 255.0, 0.0),
                    2
                )
            }

            val roiRect = org.opencv.core.Rect(
                org.opencv.core.Point(
                    mainRect.x.toDouble(),
                    mainRect.y.toDouble()
                ),
                org.opencv.core.Point(
                    mainRect.x.toDouble() + mainRect.width.toDouble(),
                    mainRect.y.toDouble() + mainRect.height.toDouble()
                )
            )

            val signatureImage = getRoiSignature(img, roiRect)
            if (mWidth != null && signatureImage.width().toDouble() > mWidth!!) org.opencv.imgproc.Imgproc.resize(
                signatureImage,
                signatureImage,
                calculateSize(signatureImage)
            )

            val bitOutput: Bitmap = Bitmap.createBitmap(
                signatureImage.cols(),
                signatureImage.rows()
                , Bitmap.Config.ARGB_8888
            )
            bitOutput.eraseColor(Color.WHITE)
            bitOutput.setHasAlpha(false)

            org.opencv.android.Utils.matToBitmap(signatureImage, bitOutput)
            if (drawBoundingBox) return bitOutput
            return setBackgroundToTransparent(bitOutput)
        }
        return null
    }

    companion object {
        private const val TOLERANCE = 5f
    }

}
