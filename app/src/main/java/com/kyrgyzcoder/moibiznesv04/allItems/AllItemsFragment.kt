package com.kyrgyzcoder.moibiznesv04.allItems

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import com.kyrgyzcoder.moibiznesv04.adapters.ItemsRecyclerViewAdapter
import com.kyrgyzcoder.moibiznesv04.editadd.EditAddActivity
import com.kyrgyzcoder.moibiznesv04.soldItems.SoldItemsFragment
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_ID
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_ITEM
import com.kyrgyzcoder.moibiznesv04.utils.REQUEST_ADD
import com.kyrgyzcoder.moibiznesv04.utils.REQUEST_EDIT
import kotlinx.android.synthetic.main.all_items_fragment.*

class AllItemsFragment : Fragment(), ItemsRecyclerViewAdapter.OnItemClickListenerN {

    companion object {
        fun newInstance() = AllItemsFragment()
        var gInflateMenuV = true
    }

    private lateinit var viewModel: AllItemsViewModel
    private lateinit var adapter: ItemsRecyclerViewAdapter
    private lateinit var recyclerViewAllItems: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        gInflateMenuV = true
        val view = inflater.inflate(R.layout.all_items_fragment, container, false)
        recyclerViewAllItems = view.findViewById(R.id.recyclerViewAllItems)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllItemsViewModel::class.java)
        adapter = ItemsRecyclerViewAdapter(this)

        recyclerViewAllItems.layoutManager = LinearLayoutManager(this.context)
        recyclerViewAllItems.setHasFixedSize(true)
        recyclerViewAllItems.adapter = adapter

        viewModel.getAllItems().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        fabVseTov.setOnClickListener {
            val intent = Intent(this.context, EditAddActivity::class.java)
            startActivityForResult(
                intent,
                REQUEST_ADD
            )
        }

        onSwipeDelete()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this.context, "Добавлен новый товар!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQUEST_EDIT && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this.context, "Товар изменен!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (gInflateMenuV) {
            inflater.inflate(R.menu.menu_vsetovary, menu)
            gInflateMenuV = false
            SoldItemsFragment.gInflateMenu = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this.context, EditAddActivity::class.java)
        viewModel.getAllItems().observe(this, Observer {
            intent.apply {
                putExtra(EXTRA_ID, it[position].id)
                putExtra(EXTRA_ITEM, it[position])
            }
        })
        startActivityForResult(
            intent,
            REQUEST_EDIT
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                deleteAll()
                true
            }
            R.id.action_newontop -> {
                Toast.makeText(this.requireContext(), "Сортировано! Новые в начале", Toast.LENGTH_LONG)
                    .show()
                viewModel.getAllItems().observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                })
                true
            }
            R.id.action_oldontop -> {
                Toast.makeText(this.requireContext(), "Сортировано! Старые в начале", Toast.LENGTH_LONG)
                    .show()
                viewModel.getAllSortedAs().observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Function that deletes an item when swiped right or left
     */
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
                        viewModel.deleteItem(adapter.getItemAt(viewHolder.adapterPosition))
                        Toast.makeText(activity!!.applicationContext, "Удалено", Toast.LENGTH_SHORT)
                            .show()
                    }
                dialogBuilder.setNegativeButton("Отменить") { _, _ ->
                    Toast.makeText(activity!!.applicationContext, "Оменено", Toast.LENGTH_SHORT)
                        .show()
                    adapter.notifyDataSetChanged()
                }
                val alert = dialogBuilder.create()
                alert.setTitle("ВНИМАНИЕ!!!")
                alert.show()
                alert.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
            }

        }).attachToRecyclerView(recyclerViewAllItems)
    }

    private fun deleteAll() {
        val dialogBuilder = AlertDialog.Builder(activity, R.style.AlertDialogStyle)

        dialogBuilder.setMessage("Вы уверены что хотите удалить все товары? Вы не сможете вернуть обратно")
            .setCancelable(false)
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAllItems()
            }
        dialogBuilder.setNegativeButton("Отменить") { _, _ ->
            Toast.makeText(this.context, "Отменено", Toast.LENGTH_SHORT).show()
        }
        val alert = dialogBuilder.create()
        alert.setTitle("ВНИМАНИЕ!!!")
        alert.show()
        alert.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
    }

}
