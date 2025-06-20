package com.nikcapko.memo.core.ui.progressview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.nikcapko.memo.core.ui.R
import com.nikcapko.memo.core.ui.extensions.makeGone
import com.nikcapko.memo.core.ui.extensions.makeVisible

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), ProgressViewInterface {

    private var progress: LottieAnimationView
    private var rlErrorContainer: RelativeLayout
    private var tvErrorMessage: TextView
    private var btnRetry: MaterialButton
    var onRetryClick: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        val rootView = inflater.inflate(R.layout.view_progress, this, true)
        progress = rootView.findViewById(R.id.progress)
        rlErrorContainer = rootView.findViewById(R.id.rlErrorContainer)
        tvErrorMessage = rootView.findViewById(R.id.tvErrorMessage)
        btnRetry = rootView.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener {
            onRetryClick?.invoke()
        }
    }

    override fun startLoading() {
        makeVisible()
        progress.makeVisible()
        progress.playAnimation()
        rlErrorContainer.makeGone()
    }

    override fun errorLoading(errorMessage: String?) {
        makeVisible()
        progress.makeGone()
        progress.cancelAnimation()
        rlErrorContainer.makeVisible()
        tvErrorMessage.text = errorMessage
    }

    override fun completeLoading() {
        makeGone()
        progress.makeGone()
        progress.cancelAnimation()
        rlErrorContainer.makeGone()
    }
}
