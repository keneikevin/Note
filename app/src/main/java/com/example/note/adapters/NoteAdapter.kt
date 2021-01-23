package com.example.note.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.data.local.entities.Note
import com.example.note.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private var onItemClickListener: ((Note) -> Unit)? = null

    private val differ = AsyncListDiffer(this, diffCallback)

    var notes: List<Note>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            tvTitle.text = note.title
            if (!note.isSynced) {
                ivSynced.setImageResource(R.drawable.ic_cross)
                tvSynced.text = "Not Synced"
            } else {
                ivSynced.setImageResource(R.drawable.ic_check)
                tvSynced.text = "Synced"
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
            val dateString = dateFormat.format(note.date)
            tvDate.text = dateString

            //Todo drawable resources

          /*  val drawable = ResourcesCompat.getDrawable(res, R.drawable.circle_shape, null)
            drawable?.let {
                val wrappedDrawable = DrawableCompat.wrap(it)

                val color = Color.parseColor("#${note.color}")
                DrawableCompat.setTint(wrappedDrawable, color)
                viewNoteColor.background = it
            }}*/
            onItemClickListener?.let { click ->
                click(note)

            }
        }
    }
    fun setOnItemClickListener(onItemClick: (Note)-> Unit) {
        this.onItemClickListener = onItemClick

    }}


























