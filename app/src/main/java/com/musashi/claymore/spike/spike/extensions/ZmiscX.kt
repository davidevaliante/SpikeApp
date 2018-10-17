
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStore
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.system.Os.remove
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils.replace
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.helpers.PicassoImageGetter
import es.dmoral.toasty.Toasty
import java.util.*
import kotlin.text.Regex.Companion.escape

// per trasformare in snakeCase il nome di una slot
fun String.toSnakeCase():String{
    var f = ""
    val re = Regex("[^A-Za-z0-9 ]")
    val splitted = this.split("\\s+".toRegex())
    val cleanedWords = splitted.map { re.replace(it.trim(),"") }
    cleanedWords.forEachIndexed{ index, x ->
        f += if(index==0) "${x.toLowerCase()}" else "_${x.toLowerCase()}"
    }
    return f
}

inline fun <reified T : ViewModel> AppCompatActivity.assignViewModel() : T{
    return ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(T::class.java)

}

fun String.fromHtml(imageGetter: PicassoImageGetter?=null):Spanned{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
    } else {
        Html.fromHtml(this)
    }
}

fun TextView.setClickableHtmlWithImages(htmlString : String?, activity: Activity){
    val sequence = htmlString?.fromHtml(imageGetter = PicassoImageGetter(this, activity))
    if (sequence != null){
        val strBuild = SpannableStringBuilder(sequence)
        val urls = strBuild.getSpans(0, sequence.length, URLSpan::class.java)
        urls.forEach { makeLinkClickable(strBuild, it, activity) }
        this.text = strBuild
        this.movementMethod = LinkMovementMethod.getInstance()
    }
}

fun makeLinkClickable(strBuilder:SpannableStringBuilder, span:URLSpan, activity: Activity)
{
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val click = object :ClickableSpan(){
        override fun onClick(widget: View?) {
            val url = span.url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(activity, intent, null)
        }
    }
    strBuilder.setSpan(click, start, end, flags)
    strBuilder.removeSpan(span)
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

// 0..10 will return an `Int` between 0 and 10 (incl.)
fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start







