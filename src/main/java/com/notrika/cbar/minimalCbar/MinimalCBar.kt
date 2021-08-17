package com.notrika.cbar.minimalCbar

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.notrika.cbar.animations.Fade
import com.notrika.cbar.R

class MinimalCBar(activity: Activity) {
    var snackView: View? = null
    private var linf: LayoutInflater? = null
    private var insertPoint: ViewGroup? = null
    private var lnrHost: LinearLayout? = null
    private var minimalCBarEventListener: MinimalCBarEventListener? = null
    private var inAnim: Animation? = null
    private var outAnim: Animation? = null
    private fun initializeMinimalBar(activity: Activity) {
        linf =
            activity.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        insertPoint = activity.findViewById(android.R.id.content)

        // Create view.
        snackView = linf!!.inflate(R.layout.layout_snack_small, null)
        snackView?.setVisibility(View.GONE)
        snackView?.let{
            ViewCompat.setTranslationZ(it, 999f)
        }
        insertPoint?.addView(
            snackView,
            1,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // Initialize component view.
        lnrHost = snackView?.findViewById(R.id.minimal_snack_bar_rlv)

        // Set default in anim.
        inAnim = Fade.In.getAnimation()

        // Set default out anim.
        outAnim = Fade.Out.getAnimation()
    }

    // Get view.
    val minimalSnackView: View?
        get() = lnrHost

    // Message.
    fun setMessage(message: String): MinimalCBar {
        var message = message

        // Initialize textview.
        val txtMessage = snackView!!.findViewById<TextView>(R.id.minimal_snack_bar_txt_message)
        txtMessage.text = message
        return this
    }

    // Duration
    fun setDuration(millisecond: Int): MinimalCBar {

        // Set duration.
        Handler().postDelayed({ dismiss() }, millisecond.toLong())
        return this
    }

    // Style
    fun setStyle(style: Int): MinimalCBar {

        // Check style
        when (style) {
            MinimalCBarStyle.STYLE_DEFAULT -> lnrHost!!.setBackgroundColor(
                snackView!!.context.resources.getColor(
                    R.color.cBar_default
                )
            )
            MinimalCBarStyle.STYLE_INFO -> lnrHost!!.setBackgroundColor(
                snackView!!.context.resources.getColor(
                    R.color.cBar_info
                )
            )
            MinimalCBarStyle.STYLE_SUCCESS -> lnrHost!!.setBackgroundColor(
                snackView!!.context.resources.getColor(
                    R.color.cBar_success
                )
            )
            MinimalCBarStyle.STYLE_ERROR -> lnrHost!!.setBackgroundColor(
                snackView!!.context.resources.getColor(
                    R.color.cBar_error
                )
            )
            MinimalCBarStyle.STYLE_WARNING -> lnrHost!!.setBackgroundColor(
                snackView!!.context.resources.getColor(
                    R.color.cBar_warning
                )
            )
        }
        return this
    }

    // Background color (Color res).
    fun setBackgroundColor(@ColorRes colorInt: Int): MinimalCBar {

        // Set color.
        lnrHost!!.setBackgroundColor(snackView!!.context.resources.getColor(colorInt))
        return this
    }

    // Background drawable (Drawable res).
    fun setBackgrounDrawable(@DrawableRes drawableInt: Int): MinimalCBar {

        // Set drawable to view.
        ViewCompat.setBackground(
            lnrHost!!, ContextCompat.getDrawable(
                lnrHost!!.context, drawableInt
            )
        )
        //lnrHost.setBackground(snackView.getContext().getResources().getDrawable(drawableInt));
        return this
    }

    // Set Listener.
    fun setListener(listener: MinimalCBarEventListener?): MinimalCBar {
        minimalCBarEventListener = listener
        return this
    }

    // Set animation.
    fun setAnimation(inAnim: Animation?, outAnim: Animation?): MinimalCBar {
        this.inAnim = inAnim
        this.outAnim = outAnim
        return this
    }

    fun show() {

        // Animation listener.
        inAnim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                snackView!!.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                snackView!!.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Set animation to view.
        snackView!!.startAnimation(inAnim)

        // Start callback.
        if (minimalCBarEventListener != null) {
            minimalCBarEventListener!!.showedMinimalSnackBar()
        }
    }

    fun dismiss() {

        // Animation listener.
        outAnim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                snackView!!.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                snackView!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Set animation to view.
        snackView!!.startAnimation(outAnim)

        // Stop callback.
        if (minimalCBarEventListener != null) {
            minimalCBarEventListener!!.stoppedMinimalSnackBar()
        }
    }

    init {
        initializeMinimalBar(activity)
    }
}