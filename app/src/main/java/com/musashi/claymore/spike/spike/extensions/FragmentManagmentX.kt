package aqua.extensions

import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.system.Os.remove
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// esegue una delle fragment Transaction a scelta
inline fun FragmentManager.customTransaction(func : FragmentTransaction.() -> FragmentTransaction){
    beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).func().commit()
}

// aggiunge al container X il fragment Y
fun AppCompatActivity.addFragment(container : View, frag : Fragment) {
    supportFragmentManager.customTransaction { add(container.id, frag) }
}

// rimuove il fragment X
fun AppCompatActivity.removeFragment(frag : Fragment){
    supportFragmentManager.customTransaction { remove(frag) }
}

inline fun <reified T> AppCompatActivity.removeFragmentsOfType(){
    val list = supportFragmentManager.fragments.filter { it is T }
    for(fragment in list) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .remove(fragment).commit()
    }
}

fun AppCompatActivity.removeAllFragments(){
    for (fragment in supportFragmentManager.fragments) {
        removeFragment(fragment)
    }
}

fun AppCompatActivity.addFragmentToRoot(frag: Fragment){
    if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
        supportFragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(android.R.id.content, frag)
                .commit()
    }
}

// rimuove il fragment X
fun FragmentActivity.replaceFrag(containerId : View, frag : Fragment){
    supportFragmentManager.customTransaction { replace(containerId.id,frag) }
}

// da usare per ottenere il ViewGroup in un Fragment
fun Fragment.inflateInContainer(root : Int, container : ViewGroup?) : ViewGroup {
    return this.layoutInflater.inflate(root, container, false) as ViewGroup
}