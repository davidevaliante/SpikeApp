package aqua.extensions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.WindowManager
import android.os.Build
import android.support.v4.content.ContextCompat

fun View.onClick(func:()->Unit){
    this.setOnClickListener { func() }
}

fun Activity.makeStatusbarTranslucent(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w = window // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}

fun AppCompatActivity.hideActionBar(){
    val decorView = window.decorView
    val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
    decorView.systemUiVisibility = uiOptions
}

inline fun <reified T : Activity> AppCompatActivity.goTo(bundle : Bundle? = null ){
    val intent = Intent(this, T::class.java)
    if(bundle == null)
        startActivity(intent)
    else {
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}



fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getSupportDrawable(id : Int):Drawable?{
    return ContextCompat.getDrawable(this,id)
}