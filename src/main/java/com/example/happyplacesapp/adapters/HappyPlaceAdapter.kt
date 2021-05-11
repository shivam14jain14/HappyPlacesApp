package com.example.happyplacesapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.R
import com.example.happyplacesapp.activities.AddHappyPlaceActivity
import com.example.happyplacesapp.activities.MainActivity
import com.example.happyplacesapp.models.HappyPlaceModel
import com.example.happyplacesapp.mydatabase.DatabaseHandler
import kotlinx.android.synthetic.main.item_happy_place.view.*

open class HappyPlaceAdapter(private val context: Context, private val list: ArrayList<HappyPlaceModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
private var onClickListener: OnClickListener?=null
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )
    }
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int)
    {
       val intent= Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
//        ABOVE LINE BECAUSE IF YOU MAKE A CHANGE IN THE ADAPTER THEN YOU NEED TO NOTIFY IT ASWELL
    }
    fun removeAt( position: Int)
    {
        val dbHandler=DatabaseHandler(context,null)
        val isDelete=dbHandler.deleteHappyPlace(list[position])
        notifyItemRemoved(position)
        if(isDelete>0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
//        ABOVE LINE BECAUSE IF YOU MAKE A CHANGE IN THE ADAPTER THEN YOU NEED TO NOTIFY IT ASWELL
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val model = list[position]
            if (holder is MyViewHolder) {
                holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
                holder.itemView.tvTitle.text = model.title
                holder.itemView.tvDescription.text = model.description
                holder.itemView.setOnClickListener{
                    if(onClickListener!=null)
                {
                    onClickListener!!.onClick(position,model)
                }
                }

            }
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    // END

    // TODO (Step 4: Create an interface for onclickListener)
    // START
    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}