package com.example.mysocialmedia.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmedia.R
import com.example.mysocialmedia.adapters.PostAdapter
import com.example.mysocialmedia.databinding.FragmentHomeBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.Post
import com.example.mysocialmedia.ui.fragments.activities.ProfileActivity
import com.example.mysocialmedia.utils.Constants
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var posts: List<Post>
    private lateinit var postsArrayList: ArrayList<Post>
    private lateinit var postAdapter: PostAdapter
    private lateinit var db: FirebaseFirestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)
        postsArrayList = arrayListOf()


        postAdapter = PostAdapter(requireContext(),postsArrayList)
        binding.recyclerView.adapter = postAdapter
        EventChangeListener()
        val sharedPreferences = requireActivity().getSharedPreferences(Constants.MYPREFERENCES,Context.MODE_PRIVATE)
        val postId = sharedPreferences.getString(
            Constants.POST_ID,"")!!



        return root
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.POSTS)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            postsArrayList.add(dc.document.toObject(Post::class.java))
                        }
                    }

                    postAdapter.notifyDataSetChanged()
                }

            })
    }










    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.personal_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_profile -> {
                startActivity(Intent(activity, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)


    }

}