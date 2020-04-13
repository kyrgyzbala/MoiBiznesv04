package com.kyrgyzcoder.moibiznesv04.soldItems

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.adapters.SoldItemsRecyclerViewAdapter
import com.kyrgyzcoder.moibiznesv04.allItems.AllItemsFragment
import kotlinx.android.synthetic.main.sold_items_fragment.*

class SoldItemsFragment : Fragment() {

    companion object {
        fun newInstance() = SoldItemsFragment()
        var gInflateMenu = true
    }

    private lateinit var viewModel: SoldItemsViewModel
    private lateinit var adapterS: SoldItemsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        gInflateMenu = true
        return inflater.inflate(R.layout.sold_items_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SoldItemsViewModel::class.java)
        adapterS = SoldItemsRecyclerViewAdapter()
        recyclerViewSoldItems.layoutManager = LinearLayoutManager(this.context)
        recyclerViewSoldItems.setHasFixedSize(true)

        recyclerViewSoldItems.adapter = adapterS
        viewModel.getAllSoldItems().observe(viewLifecycleOwner, Observer {
            adapterS.submitList(it)
        })

        onSwipeDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (gInflateMenu) {
            inflater.inflate(R.menu.sold_cat_menu, menu)
            gInflateMenu = false
            AllItemsFragment.gInflateMenuV = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_vesna -> {
                viewModel.getVesnaSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            R.id.action_leto -> {
                viewModel.getLetoSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            R.id.action_osen -> {
                viewModel.getOsenSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            R.id.action_shkolnyi -> {
                viewModel.getShkolnyiSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            R.id.action_zima -> {
                viewModel.getZimaSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            R.id.action_drugie -> {
                viewModel.getDrugieSold().observe(viewLifecycleOwner, Observer {
                    adapterS.submitList(it)
                })
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun onSwipeDelete() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val dialogBuilder = AlertDialog.Builder(activity!!, R.style.AlertDialogStyle)
                dialogBuilder.setMessage("Вы уверены что хотите удалить этот товар? Вы не сможете вернуть обратно")
                    .setCancelable(false)
                    .setPositiveButton("Удалить") { _, _ ->
                        viewModel.deleteSoldItem(adapterS.getItemAt(viewHolder.adapterPosition))
                        Toast.makeText(activity!!.applicationContext, "Удалено", Toast.LENGTH_SHORT)
                            .show()
                    }
                dialogBuilder.setNegativeButton("Отменить") { _, _ ->
                    Toast.makeText(activity!!.applicationContext, "Оменено", Toast.LENGTH_SHORT)
                        .show()
                    adapterS.notifyDataSetChanged()
                }
                val alert = dialogBuilder.create()
                alert.setTitle("ВНИМАНИЕ!!!")
                alert.show()
                alert.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
            }

        }).attachToRecyclerView(recyclerViewSoldItems)
    }
}
