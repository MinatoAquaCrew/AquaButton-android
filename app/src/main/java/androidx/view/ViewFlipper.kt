package androidx.view

import android.widget.ViewFlipper

fun ViewFlipper.updateDisplayedId(id: Int) {
    if (displayedChild != id) {
        displayedChild = id
    }
}