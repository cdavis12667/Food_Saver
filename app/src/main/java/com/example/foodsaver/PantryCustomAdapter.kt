package com.example.foodsaver

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/*This class inherits from base adapter so it has methods that must be overwritten and a
consturctor to be used
 */
class PantryCustomAdapter(var activity: Activity, var imageTextList: ArrayList<ImageTextView>): BaseAdapter() {

    //this private class just defines a view with three different views
    private class ViewHolder(row: View?) {
        //I have a layout I'm making vars and binding them
        var foodString: TextView
        var dateString: TextView
        var imageID: ImageView
        //on init initialize the variables
        init {
            //I'm binding these vars to my layouts just like I would in an activity screen
            this.foodString = row?.findViewById(R.id.foodString) as TextView
            this.dateString = row.findViewById(R.id.dateString) as TextView
            this.imageID = row.findViewById(R.id.imageID) as ImageView
        }
    }
    override fun getCount(): Int {
        //Here we just return the number of objects in the arraylist
        return imageTextList.count()
    }
    override fun getItem(p0: Int): Any {
        //just return the item
        return imageTextList[p0]
    }
    override fun getItemId(p0: Int): Long {
        //return the index which would just be the passed int p0
        return p0.toLong()
    }
    //changed var to val in method below
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        // Making some vars for later
        val view: View?
       val viewHolder: ViewHolder
        //if our view is null then make a view
        if(p1 == null){
            //create a layout inflater from context
            val layout = LayoutInflater.from(activity)
            //this sets the layout to our custom_list_view
            view = layout.inflate(R.layout.custom_list_view, p2, false)
            //the view holder class takes in a view and is assigned the views we need see above
           viewHolder = ViewHolder(view)
            //tag stores data in a view so were just storing viewHolder in our main view
           view.tag = viewHolder
        }
        else{
            //if we already have a view then just set the vars
            view = p1
            //set view holder to views tag and typecast to ViewHolder
            viewHolder = view.tag as ViewHolder
        }
        //View holder now containts data for view as it's set to tag so just bind the data
        viewHolder.foodString.text = imageTextList[p0].foodString
        viewHolder.dateString.text = imageTextList[p0].dateString
        viewHolder.imageID.setImageResource(imageTextList[p0].imageID)
        return view as View


        /*
        var row: View? = p1
        val inflater: LayoutInflater = activity.layoutInflater
        if(p1 == null){
            row = inflater.inflate(R.layout.custom_list_view, p2, false)
            var foodString: TextView = row.findViewById(R.id.foodString)
            var dateString: TextView = row.findViewById(R.id.dateString)
            var imageID: ImageView = row.findViewById(R.id.imageID)
            foodString.text = imageTextList[p0].foodString
            dateString.text = imageTextList[p0].dateString
            imageID.setImageResource(imageTextList[p0].imageID)

        }
        return row!!

         */
    }

}