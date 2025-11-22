package com.example.zero_degree.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.zero_degree.R
import com.example.zero_degree.authorization.AuthorizationFragment
import com.example.zero_degree.bar.BarFragment
import com.example.zero_degree.bars.BarsFragment
import com.example.zero_degree.drink.DrinkFragment
import com.example.zero_degree.events.EventsFragment
import com.example.zero_degree.home.HomeFragment
import com.example.zero_degree.profile.ProfileFragment

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
                    replaceFragment(fragments[buttonId]?.invoke())
                    println(buttonId)
                }
            }
        }

        return rootView
    }

    private fun replaceFragment(fragment: Fragment?) {
        if (fragment == null) return Unit
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private val fragments = mapOf<String, (() -> Fragment)>(
        "home_button" to { HomeFragment() },
        "profile_button" to { ProfileFragment() },
        "auth_button" to { AuthorizationFragment() },
        "bars_button" to { BarsFragment() },
        "bar_button" to { BarFragment() },
        "events_button" to { EventsFragment() },
        "drink_button" to { DrinkFragment() }
    )
}