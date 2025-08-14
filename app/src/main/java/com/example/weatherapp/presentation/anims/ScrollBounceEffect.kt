package com.example.weatherapp.presentation.anims

import android.graphics.Canvas
import android.widget.EdgeEffect
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.4f
private const val FLING_TRANSLATION_MAGNITUDE = 0.4f


class ScrollBounceEffect : RecyclerView.EdgeEffectFactory() {


    override fun createEdgeEffect(rv: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(rv.context) {

            private fun isVertical() =
                direction == DIRECTION_TOP || direction == DIRECTION_BOTTOM

            private fun sign() =
                if (direction == DIRECTION_TOP || direction == DIRECTION_LEFT) 1f else -1f

            private fun applyOffset(delta: Float) {
                val distancePx = (if (isVertical()) rv.height else rv.width) *
                        delta * OVERSCROLL_TRANSLATION_MAGNITUDE * sign()

                for (i in 0 until rv.childCount) {
                    val child = rv.getChildAt(i)
                    if (isVertical()) {
                        child.translationY += distancePx
                    } else {
                        child.translationX += distancePx
                    }
                }
                rv.invalidate()
            }

            override fun onPull(deltaDistance: Float) {
                applyOffset(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                applyOffset(deltaDistance)
            }

            override fun onRelease() {
                for (i in 0 until rv.childCount) {
                    val child = rv.getChildAt(i)
                    if (isVertical()) {
                        createSpringAnim(child, SpringAnimation.TRANSLATION_Y).start()
                    } else {
                        createSpringAnim(child, SpringAnimation.TRANSLATION_X).start()
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                val velocityPx = sign() * velocity * FLING_TRANSLATION_MAGNITUDE
                for (i in 0 until rv.childCount) {
                    if (isVertical()) {
                        createSpringAnim(rv.getChildAt(i), SpringAnimation.TRANSLATION_Y)
                            .setStartVelocity(velocityPx)
                            .start()
                    } else {
                        createSpringAnim(rv.getChildAt(i), SpringAnimation.TRANSLATION_X)
                            .setStartVelocity(velocityPx)
                            .start()
                    }
                }
            }

            override fun draw(canvas: Canvas?): Boolean = false
            override fun isFinished(): Boolean = true

            private fun createSpringAnim(view: View, property: DynamicAnimation.ViewProperty) =
                SpringAnimation(view, property)
                    .setSpring(
                        SpringForce()
                            .setFinalPosition(0f)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                            .setStiffness(SpringForce.STIFFNESS_LOW)
                    )
        }
    }
}
