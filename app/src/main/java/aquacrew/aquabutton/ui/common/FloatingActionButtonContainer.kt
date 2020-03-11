package aquacrew.aquabutton.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

class FloatingActionButtonContainer(context: Context, attrs: AttributeSet?)
    : FrameLayout(context, attrs), AttachedBehavior {

    override fun getBehavior(): Behavior {
        return Behavior()
    }

    class Behavior : CoordinatorLayout.Behavior<FloatingActionButtonContainer>() {

        private var mTranslationY: Float = 0f

        override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: FloatingActionButtonContainer,
            dependency: View
        ): Boolean {
            return dependency is SnackbarLayout
        }

        override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: FloatingActionButtonContainer,
            dependency: View
        ): Boolean {
            if (dependency is SnackbarLayout) {
                updateFabTranslationForSnackbar(parent, child, dependency)
            }
            return true
        }

        override fun onDependentViewRemoved(
            parent: CoordinatorLayout,
            child: FloatingActionButtonContainer,
            dependency: View
        ) {
            if (dependency is SnackbarLayout) {
                setTranslationY(parent, child, dependency, 0f)
            }
        }

        override fun onLayoutChild(
            parent: CoordinatorLayout,
            child: FloatingActionButtonContainer,
            layoutDirection: Int
        ): Boolean {
            parent.onLayoutChild(child, layoutDirection)
            mTranslationY = 0f
            return true
        }

        private fun updateFabTranslationForSnackbar(
            parent: CoordinatorLayout,
            layout: FloatingActionButtonContainer,
            snackbar: View
        ) {
            val translationY = getFabTranslationYForSnackbar(parent, layout)
            setTranslationY(parent, layout, snackbar, translationY)
        }

        private fun setTranslationY(
            parent: CoordinatorLayout,
            layout: FloatingActionButtonContainer,
            snackbar: View,
            translationY: Float
        ) {
            if (translationY != mTranslationY) {
                ViewCompat.animate(layout).cancel()
                if (Math.abs(translationY - mTranslationY) == snackbar.height.toFloat()) {
                    ViewCompat.animate(layout)
                        .translationY(translationY)
                        .setInterpolator(FastOutSlowInInterpolator())
                        .setListener(null)
                } else {
                    layout.translationY = translationY
                }
                mTranslationY = translationY
            }
        }

        private fun getFabTranslationYForSnackbar(
            parent: CoordinatorLayout,
            layout: FloatingActionButtonContainer
        ): Float {
            var minOffset = 0.0f
            parent.getDependencies(layout).forEach { view ->
                if (view is SnackbarLayout && parent.doViewsOverlap(layout, view)) {
                    minOffset = minOffset.coerceAtMost(
                        view.getTranslationY() - view.getHeight().toFloat())
                }
            }
            return minOffset
        }

    }

}