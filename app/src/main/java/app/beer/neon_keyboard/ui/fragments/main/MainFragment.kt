package app.beer.neon_keyboard.ui.fragments.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.beer.neon_keyboard.R
import app.beer.neon_keyboard.ui.fragments.change_image.ChangeImageFragment
import app.beer.neon_keyboard.ui.fragments.theme.ThemesFragment
import app.beer.neon_keyboard.ui.objects.keyboard.KeyboardService
import app.beer.neon_keyboard.utils.APP_ACTIVITY
import app.beer.neon_keyboard.utils.replaceFragment
import app.beer.neon_keyboard.utils.showToast
import com.google.android.material.card.MaterialCardView

class MainFragment : Fragment() {

    private lateinit var changeImageBtn: MaterialCardView
    private lateinit var setThemeBtn: MaterialCardView
    private lateinit var changeLanguageBtn: MaterialCardView
    private lateinit var fontStyleBtn: MaterialCardView

    private lateinit var keyboardTest: EditText

    private lateinit var settingsBtn: LinearLayout
    private lateinit var shareBtn: LinearLayout

    private lateinit var step1: LinearLayout
    private lateinit var step2: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            changeImageBtn = findViewById(R.id.change_image_btn)
            setThemeBtn = findViewById(R.id.set_theme_btn)
            changeLanguageBtn = findViewById(R.id.change_language_btn)
            fontStyleBtn = findViewById(R.id.font_style_btn)

            keyboardTest = findViewById(R.id.keyboard_test)
            settingsBtn = findViewById(R.id.btn_settings)
            shareBtn = findViewById(R.id.btn_share)

            step1 = findViewById(R.id.step_1)
            step2 = findViewById(R.id.step_2)
        }

        changeImageBtn.setOnClickListener { replaceFragment(ChangeImageFragment(), true) }
        setThemeBtn.setOnClickListener { replaceFragment(ThemesFragment(), true) }

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(Intent.EXTRA_TEXT, "link to google play")
            try {
                startActivity(Intent.createChooser(intent, "Select app"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        step1.setOnClickListener {
            startActivityForResult(Intent("android.settings.INPUT_METHOD_SETTINGS"), 99)
        }
        step2.setOnClickListener {
            val inputMethodManager =
                APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            if (inputMethodManager != null) {
                inputMethodManager.showInputMethodPicker()
            } else {
                showToast("Some went wrong")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }

    @SuppressLint("ServiceCast")
    private fun keyboardIsEnabled() =
        (APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).enabledInputMethodList
            .toString().contains(APP_ACTIVITY.packageName)

    private fun keyboardIsSet() = ComponentName(
        APP_ACTIVITY,
        KeyboardService::class.java
    ) == ComponentName.unflattenFromString(
        Secure.getString(
            APP_ACTIVITY.contentResolver,
            "default_input_method"
        )
    )

    private fun startAnimation() {
        val isKeyboardEnabled = keyboardIsEnabled()
        val isKeyboardSet = keyboardIsSet()
        val animation = AnimationUtils.loadAnimation(APP_ACTIVITY, R.anim.button_animation)
        if (!isKeyboardEnabled) {
            step1.startAnimation(animation)
        } else if (isKeyboardSet) {
            step2.clearAnimation()
        } else {
            step1.clearAnimation()
            step2.startAnimation(animation)
        }
    }

}