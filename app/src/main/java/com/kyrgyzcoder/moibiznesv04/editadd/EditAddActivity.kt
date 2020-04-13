package com.kyrgyzcoder.moibiznesv04.editadd

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import com.kyrgyzcoder.moibiznesv04.*
import com.kyrgyzcoder.moibiznesv04.allItems.AllItemsViewModel
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.Item
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSold
import com.kyrgyzcoder.moibiznesv04.soldItems.SoldItemsViewModel
import com.kyrgyzcoder.moibiznesv04.utils.*
import kotlinx.android.synthetic.main.activity_edit_add.*
import kotlinx.android.synthetic.main.content_edit.*
import yuku.ambilwarna.AmbilWarnaDialog
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class EditAddActivity : AppCompatActivity(), SellDialog.SellDialogListener,
    AdapterView.OnItemSelectedListener {

    companion object {
        var quantityG: Int = 0
    }

    private val categories = arrayOf(
        CAT_VESNA_TXT,
        CAT_LETO_TXT,
        CAT_OSEN_TXT,
        CAT_SHKOLNYI_TXT,
        CAT_ZIMA_TXT,
        CAT_DRUGUIE_TXT,
        CAT_TXT
    )

    private var choosenCategory: Int = 6
    private lateinit var itemViewModel: AllItemsViewModel
    private lateinit var soldItemViewModel: SoldItemsViewModel
    private lateinit var item: Item
    private var itemColor: Int = 0
    private lateinit var buttonChooseColor: Button
    private lateinit var textViewItemSize: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_add)
        setSupportActionBar(toolbar_edit)

        initActivity()

        val sizePlus: TextView = findViewById(R.id.textViewPlusSize)
        val sizeMinus: TextView = findViewById(R.id.textViewMinusSize)

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter(this, R.layout.categories_spinner_item, categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.setSelection(getPositionSpinner(CAT_CAT))

        title = if (getIntent().hasExtra(EXTRA_ID)) {
            fillBlanks()
            "Изменить данные"
        } else {
            "Добавить товар"
        }

        quantityG = Integer.valueOf(editTextQuantity.text.toString().trim())

        itemViewModel = ViewModelProviders.of(this).get(AllItemsViewModel::class.java)
        soldItemViewModel = ViewModelProviders.of(this).get(SoldItemsViewModel::class.java)

        buttonChooseColor.setOnClickListener {
            openColorPicker()
        }

        textViewPlus.setOnClickListener {
            addQuantity()
        }
        textViewMinus.setOnClickListener {
            subtractQuantity()
        }
        sizePlus.setOnClickListener {
            addSize()
        }

        sizeMinus.setOnClickListener {
            subtrSize()
        }

        buttonShowTotal.setOnClickListener {
            showTotal()
        }
        buttonSell.setOnClickListener {
            if (buttonSell.visibility == View.VISIBLE) {
                showSellDialog()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Called from sellDialog save data sold and updates item left automatically
     */
    override fun setQuantity(quantity: String, prodano: Boolean) {
        if (prodano) {
            if (editTextItemName.text.toString().trim().isBlank()
                || editTextPrice.text.toString().trim().isBlank()
                || editTextQuantity.text.toString().trim().isBlank()
                || choosenCategory == CAT_CAT
            ) {
                Toast.makeText(this, "Пожалуйста заполните всё!!!", Toast.LENGTH_LONG)
                    .show()

            } else {
                val timeSold: Date
                val itemTotal = Integer.valueOf(quantity) * Integer.valueOf(
                    editTextPrice.text.toString().trim()
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val currentT = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
                    currentT.format(formatter)
                    timeSold = Date.from(currentT.atZone(ZoneId.systemDefault()).toInstant())
                } else {
                    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
                    formatter.timeZone = TimeZone.getTimeZone("UTC")
                    timeSold = Calendar.getInstance().time
                }
                val newSold = ItemSold(
                    editTextItemName.text.toString().trim(),
                    textViewItemSize.text.toString(),
                    itemColor,
                    choosenCategory,
                    Integer.valueOf(editTextPrice.text.toString().trim()),
                    itemTotal,
                    Integer.valueOf(quantity),
                    timeSold
                )
                soldItemViewModel.insertSoldItem(newSold)
                val q = Integer.valueOf(editTextQuantity.text.toString())
                val qLeft = q - Integer.valueOf(quantity)
                editTextQuantity.setText(qLeft.toString())
                val itemName = editTextItemName.text.toString().trim()
                val itemPrice = Integer.valueOf(editTextPrice.text.toString().trim())
                val itemQuantity = Integer.valueOf(editTextQuantity.text.toString().trim())

                val newItem = Item(
                    itemName,
                    textViewItemSize.text.toString(),
                    itemColor,
                    choosenCategory,
                    itemPrice,
                    itemQuantity,
                    timeSold
                )
                if (getIntent().getIntExtra(EXTRA_ID, -1) != -1) {
                    val uItem = getIntent().getSerializableExtra(EXTRA_ITEM) as Item
                    newItem.id = uItem.id
                    itemViewModel.updateItem(newItem)
                } else {
                    itemViewModel.insertItem(newItem)
                }
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Пожалуйста выберите категорию", Toast.LENGTH_LONG)
            .show()
        spinner.setSelection(getPositionSpinner(CAT_CAT))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        choosenCategory = when (position) {
            0 -> CAT_VESNA
            1 -> CAT_LETO
            2 -> CAT_OSEN
            3 -> CAT_SHKOLNYI
            4 -> CAT_ZIMA
            5 -> CAT_DRUGUIE
            else ->
                CAT_CAT
        }
    }

    private fun initActivity() {
        itemColor = ResourcesCompat.getColor(resources, R.color.defItemColor, null)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView7.visibility = View.INVISIBLE
        textViewTotalCost.visibility = View.INVISIBLE
        textViewSom2.visibility = View.INVISIBLE
        buttonSell.visibility = View.INVISIBLE

        buttonChooseColor = findViewById(R.id.buttonChooseColor)
        textViewItemSize = findViewById(R.id.textViewItemSize)
    }

    /**
     * The function that fills blanks if the mode is EDIT mode
     */
    private fun fillBlanks() {
        buttonSell.visibility = View.VISIBLE
        item = getIntent().getSerializableExtra(EXTRA_ITEM) as Item
        editTextItemName.setText(item.itemName)
        spinner.setSelection(getPositionSpinner(item.itemCategory))
        editTextQuantity.setText(item.itemQuantity.toString())
        editTextPrice.setText(item.itemPrice.toString())
        itemColor = item.itemColor
        buttonChooseColor.setBackgroundColor(itemColor)
    }

    /**
     * Function to show color picker
     */

    private fun openColorPicker() {
        val colorPicker =
            AmbilWarnaDialog(this, itemColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {

                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    Toast.makeText(
                        this@EditAddActivity,
                        "Цвет товара не изменен!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    itemColor = color
                    buttonChooseColor.setBackgroundColor(itemColor)
                }
            })
        colorPicker.show()
    }

    /**
     * Function that gets spinner position to set category of the editing item
     */
    private fun getPositionSpinner(itemCategory: Int): Int {
        return when (itemCategory) {
            CAT_VESNA -> 0
            CAT_LETO -> 1
            CAT_OSEN -> 2
            CAT_SHKOLNYI -> 3
            CAT_ZIMA -> 4
            CAT_DRUGUIE -> 5
            else -> 6
        }
    }

    /**
     * Function that increments quantity by one when plus is clicked
     */
    private fun addQuantity() {
        val q = Integer.valueOf(editTextQuantity.text.toString()) + 1
        editTextQuantity.setText(q.toString())
        quantityG = q
    }

    /**
     * Function that decrements quantity by one when minus sign is clicked
     */
    private fun subtractQuantity() {
        var q = Integer.valueOf(editTextQuantity.text.toString())
        if (q > 0) {
            q -= 1
            editTextQuantity.setText(q.toString())
        }
        quantityG = q
    }

    /**
     * Function that increments size when plus sign is clicked
     */

    private fun subtrSize() {
        var size: String = textViewItemSize.text.toString()
        size = when (size) {
            "XS" -> "XXL"
            "S" -> "XS"
            "M" -> "S"
            "L" -> "M"
            "XL" -> "L"
            else -> "XL"
        }
        textViewItemSize.text = size
    }

    /**
     * Function that decrements size when minus sign is clicked
     */
    private fun addSize() {
        var size: String = textViewItemSize.text.toString()
        size = when (size) {
            "XS" -> "S"
            "S" -> "M"
            "M" -> "L"
            "L" -> "XL"
            "XL" -> "XXL"
            else -> "XS"
        }
        textViewItemSize.text = size
    }

    /**
     *  Function that shows total price
     */
    private fun showTotal() {
        if (editTextQuantity.text.toString().trim().isBlank() ||
            editTextPrice.text.toString().trim().isBlank()
        ) {
            Toast.makeText(this, "Пожалуйста вводтите цену и количество!", Toast.LENGTH_SHORT)
                .show()
        } else {
            val price = Integer.valueOf(editTextPrice.text.toString().trim())
            val q = Integer.valueOf(editTextQuantity.text.toString().trim())
            val total = price * q
            textViewTotalCost.text = total.toString()

            textView7.visibility = View.VISIBLE
            textViewTotalCost.visibility = View.VISIBLE
            textViewSom2.visibility = View.VISIBLE
        }
    }

    /**
     * Function that opens Sell Dialog
     */
    private fun showSellDialog() {
        val sD = SellDialog()
        sD.show(supportFragmentManager, "Sell")
    }

    /**
     * Function that saves New or Edited Item when save button is clicked
     */
    private fun saveItem() {
        if (editTextItemName.text.toString().trim().isBlank()
            || editTextPrice.text.toString().trim().isBlank()
            || editTextQuantity.text.toString().trim().isBlank()
            || choosenCategory == CAT_CAT
        ) {
            Toast.makeText(this, "Пожалуйста заполните всё!!!", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent()

        val itemName = editTextItemName.text.toString().trim()
        val itemPrice = Integer.valueOf(editTextPrice.text.toString().trim())
        val itemQuantity = Integer.valueOf(editTextQuantity.text.toString().trim())
        val timeUpdate: Date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentT = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            currentT.format(formatter)
            timeUpdate = Date.from(currentT.atZone(ZoneId.systemDefault()).toInstant())
        } else {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            timeUpdate = Calendar.getInstance().time
        }

        val newItem = Item(
            itemName,
            textViewItemSize.text.toString(),
            itemColor,
            choosenCategory,
            itemPrice,
            itemQuantity,
            timeUpdate
        )
        if (getIntent().hasExtra(EXTRA_ID)) {
            val uItem = getIntent().getSerializableExtra(EXTRA_ITEM) as Item
            newItem.id = uItem.id
            newItem.dateCreated = uItem.dateCreated
            itemViewModel.updateItem(newItem)
            Log.d("NURR", "UpdateColor")
        } else {
            itemViewModel.insertItem(newItem)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
