package com.notrika.cbar.cBarBase

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.notrika.cbar.animations.Fade
import com.notrika.cbar.animations.Fade.Out.getAnimation
import com.notrika.cbar.R

class CBarBase(activity: Activity) {
    var snackView: View? = null
    private var linf: LayoutInflater? = null
    private var insertPoint: ViewGroup? = null
    private var rlvHost: RelativeLayout? = null
    private var btnAction: Button? = null
    private var txtMessage: TextView? = null
    private var cBarEventListener: CBarEventListener? = null
    private var inAnim: Animation? = null
    private var outAnim: Animation? = null
    private fun initializeCBar(activity: Activity) {
        linf = activity.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        insertPoint = activity.findViewById(android.R.id.content)

        // Create view.
        snackView = linf!!.inflate(R.layout.layout_snack_normal, null)
        snackView?.visibility = View.GONE
        snackView?.let {
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
        rlvHost = snackView?.findViewById(R.id.normal_snack_bar_rlv)

        // Initialize textview.
        txtMessage = snackView?.findViewById(R.id.snack_bar_txt_message)

        // Action button.
        btnAction = snackView?.findViewById(R.id.snack_bar_btn_action)

        // Set default in anim.
        inAnim = Fade.In.getAnimation()

        // Set default out anim.
        outAnim = Fade.Out.getAnimation()
    }

    // Get view.
    fun getSnackView(): RelativeLayout? {
        return rlvHost
    }

    // Message.
    fun setMessage(message: String): CBarBase {
        val message = message
        txtMessage!!.text = message
        return this
    }

    // Duration
    fun setDuration(millisecond: Int): CBarBase {

        // Set duration.
        Handler().postDelayed({ dismiss() }, millisecond.toLong())
        return this
    }

    // Set button action.
    fun setAction(buttonText: String, clickListener: View.OnClickListener): CBarBase {

        // Change button visibility.
        btnAction!!.visibility = View.VISIBLE

        // Set button text.
        btnAction!!.text = buttonText

        // Set onClickListener.
        btnAction!!.setOnClickListener(clickListener)
        return this
    }

    // Background color (Color res).
    fun setBackColor(@ColorRes colorInt: Int): CBarBase {

        // Get current background drawable.
        val drawable = rlvHost!!.background

        // Change drawable background color.
        drawable.setColorFilter(rlvHost!!.context.resources.getColor(colorInt), PorterDuff.Mode.SRC)
        return this
    }

    // Background drawable (Drawable res).
    fun setBackgrounDrawable(@DrawableRes drawableInt: Int): CBarBase {

        // Set drawable to view.
        ViewCompat.setBackground(
            rlvHost!!, ContextCompat.getDrawable(
                rlvHost!!.context, drawableInt
            )
        )
        return this
    }

    // Change description text color.
    fun setTextColor(@ColorRes colorInt: Int): CBarBase {

        // Change text color.
        txtMessage!!.setTextColor(txtMessage!!.context.resources.getColor(colorInt))
        return this
    }

    // Change button text color.
    fun setButtonTextColor(@ColorRes colorInt: Int): CBarBase {

        // Change button text color.
        btnAction!!.setTextColor(btnAction!!.context.resources.getColor(colorInt))
        return this
    }

    fun setTextTypeFace(typeFace: Typeface): CBarBase {
        //Change text typeface.
        txtMessage!!.typeface = typeFace
        return this
    }

    fun setButtonTypeFace(typeFace: Typeface): CBarBase {
        //Change text typeface.
        btnAction!!.typeface = typeFace
        return this
    }

    // Set Listener.
    fun setListener(listener: CBarEventListener?): CBarBase {
        cBarEventListener = listener
        return this
    }

    // Set animation.
    fun setAnimation(inAnim: Animation?, outAnim: Animation?): CBarBase {
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
        if (cBarEventListener != null) {
            cBarEventListener!!.showedSnackBar()
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
        if (cBarEventListener != null) {
            cBarEventListener!!.stoppedSnackBar()
        }
    }

    fun dismissNow() {
        val animation = getAnimation(200)
        // Animation listener.
        animation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                snackView!!.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                snackView!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Set animation to view.
        snackView!!.startAnimation(animation)

        // Stop callback.
        if (cBarEventListener != null) {
            cBarEventListener!!.stoppedSnackBar()
        }
    }

    init {
        initializeCBar(activity)
    }
}