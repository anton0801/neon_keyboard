package app.beer.neon_keyboard.ui.objects.keyboard

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.view.KeyEvent
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import app.beer.neon_keyboard.R
import java.io.File


class KeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    var isCaps = false

    override fun swipeRight() {

    }

    override fun onPress(primaryCode: Int) {
    }

    override fun onRelease(primaryCode: Int) {
    }

    override fun swipeLeft() {

    }

    override fun swipeUp() {

    }

    override fun swipeDown() {

    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection = currentInputConnection
        playClick(primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE ->
                inputConnection.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps
                keyboard.isShifted = isCaps
                keyboardView.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_ENTER
                )
            )
            else -> {
                var code = primaryCode.toChar()
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code)
                }
                inputConnection.commitText(code.toString(), 1)
            }
        }
    }

    override fun onText(text: CharSequence?) {

    }

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView
        val fileBg = File(filesDir, "keyboard_img.png")
        if (fileBg.exists()) {
            keyboardView.background =
                BitmapFactory.decodeFile(fileBg.absolutePath).toDrawable(resources)
        } else {
            keyboardView.setBackgroundColor(Color.parseColor("#000000"))
        }
        keyboard = Keyboard(this, R.xml.eng_default_querty0)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    private fun playClick(i: Int) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (i) {
            32 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

}