package app.beer.neon_keyboard.ui.fragments.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.beer.neon_keyboard.R
import app.beer.neon_keyboard.ui.fragments.main.MainFragment
import app.beer.neon_keyboard.utils.APP_ACTIVITY
import app.beer.neon_keyboard.utils.replaceFragment

class SplashFragment : Fragment() {

    private lateinit var loading: TextView
    private lateinit var loading1: TextView
    private lateinit var loading2: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = view.findViewById(R.id.loading)
        loading1 = view.findViewById(R.id.loading_2)
        loading2 = view.findViewById(R.id.loading_3)
    }

    override fun onStart() {
        super.onStart()

        val handler = Handler()

        handler.postDelayed({
            loading1.visibility = View.VISIBLE
        }, 1000)
        handler.postDelayed({
            loading2.visibility = View.VISIBLE
        }, 1700)
        handler.postDelayed({
            try {
                replaceFragment(MainFragment())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 2000)
    }

}