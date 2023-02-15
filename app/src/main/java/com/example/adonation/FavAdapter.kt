//package com.example.adonation
//
//import android.content.Context
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import com.google.firebase.database.DataSnapshot
//
//class FavAdapter(private val context: Context, favItemList: List<FavItem>) :
//    RecyclerView.Adapter<FavAdapter.ViewHolder?>() {
//    private val favItemList: List<FavItem>
//    private var favDB: FavDB? = null
//    private var refLike: DatabaseReference? = null
//
//    init {
//        this.favItemList = favItemList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view: View = LayoutInflater.from(parent.getContext()).inflate(
//            R.layout.fav_item,
//            parent, false
//        )
//        favDB = FavDB(context)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.favTextView.setText(favItemList[position].getItem_title())
//        holder.favImageView.setImageResource(favItemList[position].getItem_image())
//    }
//
//    override fun getItemCount(): Int {
//        return favItemList.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var favTextView: TextView
//        var favBtn: Button
//        var favImageView: ImageView
//
//        init {
//            favTextView = itemView.findViewById<TextView>(R.id.favTextView)
//            favBtn = itemView.findViewById<Button>(R.id.favBtn)
//            favImageView = itemView.findViewById<ImageView>(R.id.favImageView)
//            refLike = FirebaseDatabase.getInstance().getReference().child("likes")
//            //remove from fav after click
//            favBtn.setOnClickListener {
//                val position: Int = getAdapterPosition()
//                val favItem: FavItem = favItemList[position]
//                val upvotesRefLike: DatabaseReference =
//                    refLike.child(favItemList[position].getKey_id())
//                favDB.remove_fav(favItem.getKey_id())
//                removeItem(position)
//                upvotesRefLike.runTransaction(object : Handler() {
//                    fun doTransaction(mutableData: MutableData): Transaction.Result {
//                        try {
//                            val currentValue: Int = mutableData.getValue(Int::class.java)
//                            if (currentValue == null) {
//                                mutableData.setValue(1)
//                            } else {
//                                mutableData.setValue(currentValue - 1)
//                            }
//                        } catch (e: Exception) {
//                            throw e
//                        }
//                        return Transaction.success(mutableData)
//                    }
//
//                    fun onComplete(
//                        databaseError: DatabaseError?,
//                        b: Boolean,
//                        dataSnapshot: DataSnapshot?
//                    ) {
//                        println("Transaction completed")
//                    }
//                })
//            }
//        }
//    }
//
//    private fun removeItem(position: Int) {
//        favItemList.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, favItemList.size)
//    }
//}