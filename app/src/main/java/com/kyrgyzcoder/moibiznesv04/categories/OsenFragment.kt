package com.kyrgyzcoder.moibiznesv04.categories

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
import com.kyrgyzcoder.moibiznesv04.allItems.AllItemsViewModel
import com.kyrgyzcoder.moibiznesv04.editadd.EditAddActivity
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_ID
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_ITEM
import com.kyrgyzcoder.moibiznesv04.utils.REQUEST_ADD
import com.kyrgyzcoder.moibiznesv04.utils.REQUEST_EDIT
import kotlinx.android.synthetic.main.fragment_osen.*

/**
 * A simple [Fragment] subclass.
 */
class OsenFragment : Fragment(), ItemsRecyclerViewAdapter.OnItemClickListenerN {

    private lateinit var viewModel: AllItemsViewModel
    private lateinit var adapterO: ItemsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_osen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllItemsViewModel::class.java)
        recyclerViewOsen.layoutManager = LinearLayoutManager(this.context)
        recyclerViewOsen.setHasFixedSize(true)

        adapterO = ItemsRecyclerViewAdapter(this)
        recyclerViewOsen.adapter = adapterO

        viewModel.getOsenItems().observe(viewLifecycleOwner, Observer {
            adapterO.submitList(it)
        })

        fabOsen.setOnClickListener {
            val intent = Intent(this.context, EditAddActivity::class.java)
            startActivityForResult(
                intent,
                REQUEST_ADD
            )
        }
        onSwipeDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.osen_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_nur -> {
                Toast.makeText(this.requireContext(), "Action Nur clicked!", Toast.LENGTH_LONG)
                    .show()
                true
            }
            R.id.action_nur2 -> {
                Toast.makeText(this.requireContext(), "Action Nur2 clicked!", Toast.LENGTH_LONG)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this.context, EditAddActivity::class.java)
        viewModel.getOsenItems().observe(viewLifecycleOwner, Observer {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this.context, "Добавлен новый товар!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQUEST_EDIT && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this.context, "Товар изменен!", Toast.LENGTH_SHORT).show()
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
                        viewModel.deleteItem(adapterO.getItemAt(viewHolder.adapterPosition))
                        Toast.makeText(activity!!.applicationContext, "Удалено", Toast.LENGTH_SHORT)
                            .show()
                    }
                dialogBuilder.setNegativeButton("Отменить") { _, _ ->
                    Toast.makeText(activity!!.applicationContext, "Оменено", Toast.LENGTH_SHORT)
                        .show()
                    adapterO.notifyDataSetChanged()
                }
                val alert = dialogBuilder.create()
                alert.setTitle("ВНИМАНИЕ!!!")
                alert.show()
                alert.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
            }

        }).attachToRecyclerView(recyclerViewOsen)
    }

}
