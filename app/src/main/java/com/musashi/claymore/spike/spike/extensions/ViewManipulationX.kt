package aqua.extensions

import android.support.v7.app.AppCompatActivity
import android.view.View

fun AppCompatActivity.hideActionBar(){
    val decorView = window.decorView
    val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
    decorView.systemUiVisibility = uiOptions
}

fun View.invertVisibility(){
    when(this.visibility){
        View.GONE -> this.visibility = View.VISIBLE
        View.VISIBLE -> this.visibility = View.GONE
        else -> null
    }
}

infix fun View.setPaddingInDpi(dpi:Int){
    val scale = context.resources.displayMetrics.density
    val valueInDpi = (dpi * scale + 0.5f).toInt()
    setPadding(valueInDpi,valueInDpi,valueInDpi,valueInDpi)
}

infix fun View.setPaddingStartInDpi(dpi:Int){
    val scale = context.resources.displayMetrics.density
    val valueInDpi = (dpi * scale + 0.5f).toInt()
    setPadding(valueInDpi,0,0,0)
}

infix fun View.setPaddingTopInDpi(dpi:Int){
    val scale = context.resources.displayMetrics.density
    val valueInDpi = (dpi * scale + 0.5f).toInt()
    setPadding(0,valueInDpi,0,0)
}

infix fun View.setPaddingEndInDpi(dpi:Int){
    val scale = context.resources.displayMetrics.density
    val valueInDpi = (dpi * scale + 0.5f).toInt()
    setPadding(0,0,valueInDpi,0)
}

infix fun View.setPaddingBottomInDpi(dpi:Int){
    val scale = context.resources.displayMetrics.density
    val valueInDpi = (dpi * scale + 0.5f).toInt()
    setPadding(0,0,0,valueInDpi)
}