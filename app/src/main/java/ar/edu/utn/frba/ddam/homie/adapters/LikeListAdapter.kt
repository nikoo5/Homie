package ar.edu.utn.frba.ddam.homie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import java.text.NumberFormat

class LikeListAdapter(private var context : Context, private var likesList: MutableList<Post>, val onClick: (Int) -> Unit, val onLongClick: (Int) -> Unit) : RecyclerView.Adapter<LikeListAdapter.LikesHolder>() {
    companion object {
        private val TAG = "LikesListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_like,parent,false)
        return (LikesHolder(view))
    }

    override fun getItemCount(): Int {
        return likesList.size
    }

    fun setData(newData: ArrayList<Post>) {
        this.likesList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LikesHolder, position: Int) {
        val post = likesList[position];

        if(post.id > 0) {
            val building = post.getBuilding(context)!!
            val location = building.getLocation(context)!!

            if (building.images.count() > 0) {
                holder.setImage(building.images[0]);
            }

            holder.setStatus(post.status);
            holder.setTitle(building.type, building.surfaceOpen, building.rooms);
            holder.setAddress(location.address, location.number);
            holder.setPrice(post.price, post.currency);
            holder.setExpenses(post.expenses, post.currency);

            holder.getCardLayout().setOnClickListener {
                onClick(post.id);
            }

            holder.getCardLayout().setOnLongClickListener {
                onLongClick(post.id);
                true;
            }
        }
    }


    class LikesHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun getCardLayout () : CardView {
            return view.findViewById(R.id.item_like_card_view)
        }

        fun setImage(imgUrl : String) {
            val img = view.findViewById<ImageView>(R.id.ivLikeImage);
            Utils.setImage(view.context, view, img, null, imgUrl, R.drawable.no_image, view.context.resources.getString(R.string.error_post_detail_image))
        }

        fun setStatus(status : String) {
            val cv = view.findViewById<CardView>(R.id.cvLikeStatus)
            val tv = view.findViewById<TextView>(R.id.tvLikeStatus)
            if (status == "reserved") {
                cv.visibility = View.VISIBLE;
            } else {
                cv.visibility = View.INVISIBLE;
            }
            tv.text = Utils.getString(view.context, "posts_status_${status}");
        }

        fun setTitle(type : String, surface : Long, rooms : Int) {
            val tv = view.findViewById<TextView>(R.id.tvLikeTitle)
            var txt : String = "${Utils.getString(view.context, "buildings_types_${type}")} · ${surface.toString()}m² · ${rooms.toString()} ${view.resources.getString(R.string.room)}";
            if(rooms > 1) txt += "s";
            tv.text = txt;
            tv.isSelected = true
        }

        fun setAddress(address : String, number : Int) {
            val tv = view.findViewById<TextView>(R.id.tvLikeAddress)
            val txt = "$address ${number.toString()}";
            tv.text = txt
        }

        fun setPrice(price : Int, currency : String) {
            val tv = view.findViewById<TextView>(R.id.tvLikePrice);
            val txt = "$currency ${NumberFormat.getIntegerInstance().format(price).replace(",", ".")}";
            tv.text = txt
        }

        fun setExpenses(expenses : Int, currency: String) {
            val tv = view.findViewById<TextView>(R.id.tvLikeExpenses);
            if(expenses > 0) {
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.INVISIBLE
            }
            val txt = "+ $currency ${NumberFormat.getIntegerInstance().format(expenses).replace(",", ".")} ${view.resources.getString(R.string.expenses)}";
            tv.text = txt
        }
    }
}