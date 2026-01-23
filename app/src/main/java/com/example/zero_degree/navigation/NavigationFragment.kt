package com.example.zero_degree.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.zero_degree.R
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.features.auth.impl.presentation.AuthorizationFragment
import com.example.zero_degree.features.bars.impl.presentation.BarFragment
import com.example.zero_degree.features.bars.impl.presentation.BarsMapFragment
import com.example.zero_degree.features.drinks.impl.presentation.DrinkCatalogFragment
import com.example.zero_degree.features.events.impl.presentation.EventsFragment
import com.example.zero_degree.features.home.impl.presentation.HomeFragment
import com.example.zero_degree.features.profile.impl.presentation.ProfileFragment

class NavigationFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.navigation_menu, container, false) as LinearLayout

        for (i in 0 until rootView.childCount) {
            val child: View = rootView.getChildAt(i)
            if (child is Button) {
                val buttonId = resources.getResourceEntryName(child.id)
                child.setOnClickListener {
                    val fragment = fragments[buttonId]?.invoke()
                    if (fragment != null) {
                        NavigationHelper.replaceFragment(activity, fragment)
                    }
                }
            }
        }

        return rootView
    }

    private val fragments = mapOf<String, (() -> Fragment)>(
        "home_button" to { HomeFragment() },
        "profile_button" to { ProfileFragment() },
        "auth_button" to { AuthorizationFragment() },
        "bars_button" to { BarsMapFragment() },
        "bar_button" to { BarFragment() },
        "events_button" to { EventsFragment() },
        "drink_button" to { DrinkCatalogFragment() },
        "menu_button" to { DrinkCatalogFragment() }
    )
}