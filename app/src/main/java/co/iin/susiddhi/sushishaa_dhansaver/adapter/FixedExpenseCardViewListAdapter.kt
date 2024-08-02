package co.iin.susiddhi.susishaa_dhansaver.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.database.ExpenseClassModel
import co.iin.susiddhi.susishaa_dhansaver.database.*
import co.iin.susiddhi.susishaa_dhansaver.ui.ExpenseCardViewListFragment

class FixedExpenseCardViewListAdapter (private val context: Context, courseModelArrayList: ArrayList<FixedExpenseClassModel>,
                                       private val onItemClick: (FixedExpenseClassModel) -> Unit) :
    RecyclerView.Adapter<FixedExpenseCardViewListAdapter.Viewholder>(){

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ExpenseRupee: TextView
        val DateTime: TextView
        val occurrence: TextView
        val SubCategory: TextView
        val Category: TextView
        val Reason: TextView
        val CardViewId: CardView
        init {
            ExpenseRupee = itemView.findViewById(R.id.textFixedExpenseRupee)
            DateTime = itemView.findViewById(R.id.textDateTime)
            occurrence = itemView.findViewById(R.id.textOccurrence)
            SubCategory = itemView.findViewById(R.id.textSubCategory)
            Category = itemView.findViewById(R.id.textCategory)
            Reason = itemView.findViewById(R.id.textComments)
            CardViewId = itemView.findViewById(R.id.cardViewId)
        }
    }

    private val courseModelArrayList: ArrayList<FixedExpenseClassModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixedExpenseCardViewListAdapter.Viewholder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fixed_expense_cardview,
            parent,
            false
        )
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: FixedExpenseCardViewListAdapter.Viewholder, position: Int) {
        // to set data to textview and imageview of each card layout

        val model: FixedExpenseClassModel = courseModelArrayList[position]
        Log.e("TAG", "courseModelArrayList: "+ courseModelArrayList.size + "  position:"+position)
        Log.i("TAG", model.category)

        if(model.category == SUSISHAA_CATEGORY_INCOME)
        {
            holder?.CardViewId?.setCardBackgroundColor(context.getColor(R.color.creditColorTexts))
            holder?.ExpenseRupee?.setText(model.expense.toString())
            holder?.ExpenseRupee?.setBackgroundColor(context.getColor(R.color.creditColor))
        }
        else
        {
            holder?.CardViewId?.setCardBackgroundColor(context.getColor(R.color.fixedDebitColor))
            holder?.ExpenseRupee?.setText(model.expense.toString())
            holder?.ExpenseRupee?.setBackgroundColor(context.getColor(R.color.fixedDebitColor1))
            //holder?.ExpenseRupee?.setTextColor(Color.RED)
        }
        holder?.DateTime?.setText(model.enddate)
        var subText1 = model.sub_occurrence.split(":")[0]
        var subText2 = model.sub_occurrence.split(":")[1]
        if(subText1 == "DAY")
        {
            subText1 = "-"
        }
        if(subText2 == "MONTH")
        {
            subText2 = "-"
        }
        holder?.occurrence?.setText("${model.occurrence}:${subText1}:${subText2}")
        holder?.Category?.setText(model.category)
        holder?.SubCategory?.setText(model.sub_category)
        holder?.Reason?.setText(model.comments)

        holder.itemView.setOnClickListener {
            onItemClick(model)
        }

    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }
}