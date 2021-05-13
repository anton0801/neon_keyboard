package app.beer.neon_keyboard.ui.fragments.theme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.neon_keyboard.R
import app.beer.neon_keyboard.models.Theme
import app.beer.neon_keyboard.utils.APP_ACTIVITY
import app.beer.neon_keyboard.utils.BaseAdapterRecyclerView

class ThemesFragment : Fragment(), BaseAdapterRecyclerView.OnItemClickListener<Theme> {

    private var themes = arrayListOf<Theme>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var themesAdapter: BaseAdapterRecyclerView<Theme>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_themes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.themes_recycle_view)
        themesAdapter = BaseAdapterRecyclerView(R.layout.theme_item)
        themesAdapter.setOnItemClickListener(this)
        recyclerView.apply {
            adapter = themesAdapter
            layoutManager = GridLayoutManager(APP_ACTIVITY, 2)
            setHasFixedSize(true)
        }
        setData()
    }

    private fun setData() {
        themes.clear()
        themes.addAll(
            listOf(
                Theme("Color strip", R.drawable.keyboard_1),
                Theme("Color strip", R.drawable.keyboard_2),
                Theme("Color strip", R.drawable.keyboard_3)
            )
        )
        themesAdapter.setItems(themes)
    }

    override fun onClick(item: Theme) {
        // save them for keyboard
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

}