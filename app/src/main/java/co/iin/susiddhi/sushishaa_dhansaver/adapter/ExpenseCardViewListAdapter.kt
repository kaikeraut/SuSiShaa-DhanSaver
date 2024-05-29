package co.iin.susiddhi.sushishaa_dhansaver.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.ExpenseClassModel
import co.iin.susiddhi.sushishaa_dhansaver.database.*

class ExpenseCardViewListAdapter (private val context: Context, courseModelArrayList: ArrayList<ExpenseClassModel>) :
    RecyclerView.Adapter<ExpenseCardViewListAdapter.Viewholder>(){

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*
        val textExpenseRupee: TextView = itemView.findViewById(R.id.textExpenseRupee)
        val textDateTime: TextView  = itemView.findViewById(R.id.textDateTime)
        val textMode: TextView  = itemView.findViewById(R.id.textMode)
        val textSubCategory: TextView  = itemView.findViewById(R.id.textSubCategory)
        val textCategory: TextView  = itemView.findViewById(R.id.textCategory)
        val textReason: TextView  = itemView.findViewById(R.id.textReason)

         */
        val ExpenseRupee: TextView
        val DateTime: TextView
        val Mode: TextView
        val SubCategory: TextView
        val Category: TextView
        val Reason: TextView
        val CardViewId: CardView
        init {
            ExpenseRupee = itemView.findViewById(R.id.textExpenseRupee)
            DateTime = itemView.findViewById(R.id.textDateTime)
            Mode = itemView.findViewById(R.id.textMode)
            SubCategory = itemView.findViewById(R.id.textSubCategory)
            Category = itemView.findViewById(R.id.textCategory)
            Reason = itemView.findViewById(R.id.textReason)
            CardViewId = itemView.findViewById(R.id.cardViewId)
        }
    }

    private val courseModelArrayList: ArrayList<ExpenseClassModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCardViewListAdapter.Viewholder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.expense_cardview_adapter_layout,
            parent,
            false
        )
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ExpenseCardViewListAdapter.Viewholder, position: Int) {
        // to set data to textview and imageview of each card layout

        val model: ExpenseClassModel = courseModelArrayList[position]
        Log.e("TAG", "courseModelArrayList: "+ courseModelArrayList.size + "  position:"+position)
        Log.i("TAG", model.category)

        if(model.category == SUSISHAA_CATEGORY_INCOME)
        {
            holder?.CardViewId?.setCardBackgroundColor(context.getColor(R.color.creditColorTexts))
            holder?.ExpenseRupee?.setText(model.rupee.toString())
            holder?.ExpenseRupee?.setBackgroundColor(context.getColor(R.color.creditColor))
        }
        else
        {
            holder?.CardViewId?.setCardBackgroundColor(context.getColor(R.color.debitColorText))
            holder?.ExpenseRupee?.setText(model.rupee.toString())
            holder?.ExpenseRupee?.setBackgroundColor(context.getColor(R.color.debitColor))
            //holder?.ExpenseRupee?.setTextColor(Color.RED)
        }
        holder?.DateTime?.setText(model.date)
        holder?.Mode?.setText(model.mode)
        holder?.Category?.setText(model.category)
        holder?.SubCategory?.setText(model.sub_category)
        holder?.Reason?.setText(model.purpose)

        /*
        holder?.ExpenseRupee?.setText("model.rupee")
        holder?.DateTime?.setText("model.date")
        holder?.Mode?.setText("model.mode")
        holder?.Category?.setText("model.category")
        holder?.SubCategory?.setText("model.sub_category")
        holder?.Reason?.setText("model.purpose")*/

    }

    /*
        // View holder class for initializing of your views such as TextView and Imageview.
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textExpenseRupee: TextView
            private val textDateTime: TextView
            private val textMode: TextView
            private val textSubCategory: TextView
            private val textCategory: TextView
            private val textReason: TextView
            init {
                textExpenseRupee = itemView.findViewById(R.id.textExpenseRupee)
                textDateTime = itemView.findViewById(R.id.textDateTime)
                textMode = itemView.findViewById(R.id.textMode)
                textSubCategory = itemView.findViewById(R.id.textSubCategory)
                textCategory = itemView.findViewById(R.id.textCategory)
                textReason = itemView.findViewById(R.id.textReason)
            }
        }
    */
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }
}