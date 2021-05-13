package app.beer.neon_keyboard.utils

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.beer.neon_keyboard.MainActivity
import app.beer.neon_keyboard.R

lateinit var APP_ACTIVITY: MainActivity

fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    if (fragment != null) {
        val fm = APP_ACTIVITY.supportFragmentManager
        if (!fm.isDestroyed) {
            if (addToBackStack) {
                fm.beginTransaction()
                    .setCustomAnimations(R.anim.page_enter_anim, R.anim.page_exit_anim)
                    .addToBackStack(null)
                    .replace(R.id.data_container, fragment)
                    .commit()
            } else {
                fm.beginTransaction()
                    .setCustomAnimations(R.anim.page_enter_anim, R.anim.page_exit_anim)
                    .replace(R.id.data_container, fragment)
                    .commit()
            }
        }
    }
}

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.finish()
    APP_ACTIVITY.startActivity(intent)
}