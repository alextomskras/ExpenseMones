package com.dreamer.expensesmoney.ui.TransactionDetails

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dreamer.expensesmoney.R
import com.dreamer.expensesmoney.data.Transaction
import com.dreamer.expensesmoney.data.TransactionType
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailFragment : Fragment() {

    private lateinit var viewModel: TransactionDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        return inflater.inflate(R.layout.fragment_transaction_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // for calender
        transactdateSelect.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd")
        transactdateSelect.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd", Date())

        selFromDate.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd")
        selFromDate.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd", Date())

        todate.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd")
        todate.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd", Date())

        selFromDate.isEnabled = false
        todate.isEnabled = false
        recurring_transaction.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selFromDate.isEnabled = true
                todate.isEnabled = true
            } else {
                selFromDate.isEnabled = false
                todate.isEnabled = false
                selFromDate.editText?.setText("")
                todate.editText?.setText("")

            }
        }

/* to update to net balace remaining after every addition in the recycler view  and always use this
 this method to update the shared preference rather then the method used in login fragment */
//        val sharedPreferences: SharedPreferences =
//            this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
////        to set the name in the recycler view fragment
//        name_text.text = sharedPreferences.getString("Name", "illuminati").toString()


        val properties = mutableListOf<String>()

        TransactionType.values().forEach { properties.add(it.name) }

        val arrayAdapter =
            ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_item, properties)
        _type.adapter = arrayAdapter

        _type?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long,
            ) {
            }
        }

        val id = TransactionDetailFragmentArgs.fromBundle(requireArguments()).id
        viewModel.setTransactionId(id)

        // disabling the feilds
        if (id != 0L)
            disableFields()

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { setData(it) }
        })

        income.setOnClickListener {
            saveTransactionIncome()
        }

        expense.setOnClickListener {
            saveTransactionExpanse()
        }

        addappBar10.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.delete -> {
                    deleteTransaction()
                    true
                }

                R.id.edit_pen -> {
                    enableFields()
                    income.setOnClickListener {
                        saveTransactionIncome()
                    }

                    expense.setOnClickListener {
                        saveTransactionExpanse()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun deleteTransaction() {
        viewModel.deleteTransaction()


        this.requireActivity().onBackPressed()
    }

    private fun setData(transaction: Transaction) {
        TransactionName.editText?.setText(transaction.name)
        etamount.editText?.setText(transaction.amount.toString())
        transactdateSelect.editText?.setText(transaction.date)
        selFromDate.editText?.setText(transaction.fromDate)
        todate.editText?.setText(transaction.toDate)
        selectcategory.text = transaction.category
        comment.editText?.setText(transaction.comment)
        _type.setSelection(transaction.type)
    }

    private fun saveTransactionIncome() {
        val transactionName = TransactionName.editText?.text.toString()
        val amount = etamount.editText?.text.toString()
        val selectDate = transactdateSelect.editText?.text.toString()
        val fromDate = selFromDate.editText?.text.toString()
        val toDate = todate.editText?.text.toString()
        val type = _type.selectedItemPosition
        val category = selectcategory.text.toString()
        val comment = comment.editText?.text.toString()

//        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//        val strDate = sdf.parse(dateselect.toString())
//        if (Date().after(strDate)) {
//            println("true $dateselect")
        //                creating sharedPreference to update the net_amount remaining
//            val sharedPreferences: SharedPreferences =
//                this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//            val credit = sharedPreferences.getString("Budget", "0")
//            val value = (credit?.toInt() ?: 0) - amount.toInt()
//
//            sharedPreferences.edit().putString("Budget", value.toString()).apply()
        val transaction = Transaction(
            viewModel.transactionId.value!!,
            transactionName,
            amount.toFloat(),
            selectDate,
            fromDate,
            toDate,
            (selectDate.substring(0, 4) + selectDate.substring(5, 7)).toLong(),
            selectDate.substring(5, 7).toInt(),
            selectDate.substring(0, 4).toInt(),
            category,
            type,
            comment,
            1
        )
        viewModel.saveTransaction(transaction)
        this.requireActivity().onBackPressed()


    }

    private fun saveTransactionExpanse() {
        val transactionName = TransactionName.editText?.text.toString()
        val amount = etamount.editText?.text.toString()
        val selectDate = transactdateSelect.editText?.text.toString()
        val fromDate = selFromDate.editText?.text.toString()
        val toDate = todate.editText?.text.toString()
        val type = _type.selectedItemPosition
        val category = selectcategory.text.toString()
        val comment = comment.editText?.text.toString()

////        creating sharedPreference to update the net_amount remaining
//        val sharedPreferences: SharedPreferences =
//            this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//        val debit = sharedPreferences.getString("Budget", "0")
//        val value = (debit?.toInt() ?: 0) - amount.toInt()
//
//        sharedPreferences.edit().putString("Budget", value.toString()).apply()

//        sending values to the viewmodel

        val vary: Float = amount.toFloat() * (-1)
        val transaction = Transaction(
            viewModel.transactionId.value!!,
            transactionName,
            vary,
            selectDate,
            fromDate,
            toDate,
            (selectDate.substring(0, 4) + selectDate.substring(5, 7)).toLong(),
            selectDate.substring(5, 7).toInt(),
            selectDate.substring(0, 4).toInt(),
            category,
            type,
            comment,
            0
        )

        viewModel.saveTransaction(transaction)
        this.requireActivity().onBackPressed()
    }


    // Enabling fields
    private fun enableFields() {
        TransactionName.isEnabled = true
        etamount.isEnabled = true
        transactdateSelect.isEnabled = true
        selFromDate.isEnabled = true
        todate.isEnabled = true
        _type.isEnabled = true
        transactiontype.isEnabled = true
        comment.isEnabled = true
        expense.isEnabled = true
        income.isEnabled = true
    }

    // Disabling fields
    private fun disableFields() {
        TransactionName.isEnabled = false
        etamount.isEnabled = false
        transactdateSelect.isEnabled = false
        selFromDate.isEnabled = false
        todate.isEnabled = false
        _type.isEnabled = false
        transactiontype.isEnabled = false
        comment.isEnabled = false
        expense.isEnabled = false
        income.isEnabled = false

    }



//     yyyy-mm-dd

    //    for creating dialogue date picker
    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
//                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}