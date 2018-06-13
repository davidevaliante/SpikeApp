package aqua.extensions

import android.app.Activity
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

infix fun String.log(string : String) = Log.d(this,string)


infix fun Any.logFrom(from : Activity) {
    if (this!=null && from!=null)
        Log.d(from::class.java.simpleName.toUpperCase(),this.toString())
    else
        Log.d(from::class.java.simpleName.toUpperCase(), "null value found")
}

infix fun Any.logFrom(from : Fragment) {
    if (this!=null && from!=null)
        Log.d(from::class.java.simpleName.toUpperCase(),this.toString())
    else
        Log.d(from::class.java.simpleName.toUpperCase(), "null value found")
}

// mostra un Toast di base per le activity
fun Context.showMessage(message : String, duration : Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, "$message", duration).show()
}

// mostra messaggio di errore (background rosso) per le Activity
fun Context.showError (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    Toasty.error(this, message, duration).show()
}

// mostra messaggio di successo (background verde)
fun Context.showSuccess (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    Toasty.success(this, message, duration).show()
}

// mostra messaggio di info (background blu)
fun Context.showInfo (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    Toasty.info(this, message, duration).show()
}

// mostra un Toast di base per le activity
fun Fragment.showMessage(message : String, duration : Int = Toast.LENGTH_SHORT){
    Toast.makeText(activity, "$message", duration).show()
}

// mostra messaggio di errore (background rosso) per le Activity
fun Fragment.showError (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    context?.let{Toasty.error(it, message, duration).show()}
}

// mostra messaggio di successo (background verde)
fun Fragment.showSuccess (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    context?.let{Toasty.success(it, message, duration).show()}
}

// mostra messaggio di info (background blu)
fun Fragment.showInfo (message : CharSequence, duration : Int = Toast.LENGTH_SHORT){
    context?.let{Toasty.info(it, message, duration).show()}
}