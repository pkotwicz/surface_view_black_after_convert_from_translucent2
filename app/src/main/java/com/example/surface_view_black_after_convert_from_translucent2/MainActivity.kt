package com.example.surface_view_black_after_convert_from_translucent2

import android.content.Context
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var layout = android.widget.FrameLayout(this)
        setContentView(layout)

        Handler().postDelayed(object: Runnable{
            override fun run() {
                try {
                    MainActivity::class.java.getMethod("convertFromTranslucent").invoke(this@MainActivity)
                } catch (e:Exception) {
                    e.printStackTrace();
                }
            }
        }, 1000)

        Handler().postDelayed(object: Runnable{
            override fun run() {
                val surfaceView = DrawingSurfaceView(this@MainActivity).apply {
                    layoutParams = ViewGroup.MarginLayoutParams(500, 300).apply { bottomMargin = 100 }
                }
                layout.addView(surfaceView)
            }
        }, 3000)
    }

    internal inner class DrawingSurfaceView : SurfaceView {

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
            holder.addCallback(object : SurfaceHolder.Callback2 {

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    draw();
                }

            })
        }

        private var path: Path? = null

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.BLUE
        }

        private val fillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.rgb(-0, 0, 255)
        }


        override fun onTouchEvent(event: MotionEvent): Boolean {
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    path = Path().also { it.moveTo(event.x, event.y) }
                }
                event.action == MotionEvent.ACTION_MOVE -> path?.lineTo(event.x, event.y)
                event.action == MotionEvent.ACTION_UP -> path?.lineTo(event.x, event.y)
            }

            draw()
            return true
        }

        fun draw(color: Int = Color.rgb(100, 100, 255)) {
            fillPaint.color = color
            val canvas = holder.lockCanvas()
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
            path?.let { canvas?.drawPath(it, paint) }
            holder.unlockCanvasAndPost(canvas)
        }


    }
}