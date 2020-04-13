package com.kyrgyzcoder.moibiznesv04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kyrgyzcoder.moibiznesv04.adapters.SectionPagerAdapter
import kotlinx.android.synthetic.main.fragment_both_vse.*

/**
 * A simple [Fragment] subclass.
 */
class BothVseFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_both_vse, container, false)

        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabs)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.title = "Мой бизнес"
        val sectionPagerAdapter = SectionPagerAdapter(this.context!!, activity!!.supportFragmentManager)
        viewPager.adapter = sectionPagerAdapter
        tabLayout.setupWithViewPager(view_pager)
    }
}
