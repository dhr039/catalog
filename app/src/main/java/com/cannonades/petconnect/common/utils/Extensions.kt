package com.cannonades.petconnect.common.utils

//import com.realworld.android.logging.Logger
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//fun ImageView.setImage(url: String) {
//    Glide.with(this.context)
//        .load(url.ifEmpty { null })
//        .error(R.drawable.cat_derp)
//        .centerCrop()
//        .transition(DrawableTransitionOptions.withCrossFade())
//        .into(this)
//}

inline fun CoroutineScope.createExceptionHandler(
    message: String,
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->
//  Logger.e(throwable, message)
    Log.e("Extensions.kt", "$throwable $message")
    throwable.printStackTrace()

    /**
     * A [CoroutineExceptionHandler] can be called from any thread. So, if [action] is supposed to
     * run in the main thread, you need to be careful and call this function on the a scope that
     * runs in the main thread, such as a [viewModelScope].
     */
    launch {
        action(throwable)
    }
}
