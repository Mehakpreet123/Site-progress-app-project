package com.example.siteprogress.data.model

import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick

data class QuickAction(
    val name: String,
    val iconResId: Int,
    val onClick: (() -> Unit)? = null
)