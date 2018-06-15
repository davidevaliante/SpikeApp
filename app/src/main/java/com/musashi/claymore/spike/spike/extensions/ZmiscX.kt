
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStore
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.system.Os.remove
import android.text.Html
import android.text.Spanned
import android.text.TextUtils.replace
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import es.dmoral.toasty.Toasty

inline fun <reified T : ViewModel> AppCompatActivity.assignViewModel() : T{
    return ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(T::class.java)

}


fun String.fromHtml():Spanned{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

// prende un colore
fun Context.takeColor(color : Int) : Int = ContextCompat.getColor(this, color)

fun String.upperFirstLowerRest() : String{
    var firstLetter =""
    var rest=""
    if(this.isNotEmpty()) {
        firstLetter += this[0].toUpperCase()
        rest += this.drop(1).toLowerCase()
    }
    return firstLetter+rest
}










