package com.kyrgyzcoder.moibiznesv04.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kyrgyzcoder.moibiznesv04.allItems.AllItemsFragment
import com.kyrgyzcoder.moibiznesv04.soldItems.SoldItemsFragment

private val TAB_TITLES = arrayOf(
    "Все товары",
    "Продано"
)

class SectionPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> AllItemsFragment()
            else -> SoldItemsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return 2
    }

}