package com.nik.capko.memo.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.nik.capko.memo.R
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), IProgressView {

    private var progress: LottieAnimationView

    init {
        val rootView = LayoutInflater.from(context)
            .inflate(R.layout.view_progress, this, true)
        progress = rootView.findViewById(R.id.progress)
    }

    override fun startLoading() {
        progress.makeVisible()
    }

    override fun errorLoading(errorMessage: String?) {
        progress.makeGone()
    }

    override fun completeLoading() {
        progress.makeGone()
    }
}
