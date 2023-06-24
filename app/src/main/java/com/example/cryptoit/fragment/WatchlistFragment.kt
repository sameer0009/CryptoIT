package com.example.cryptoit.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoit.adapter.MarketAdapter
import com.example.cryptoit.apis.ApiInterface
import com.example.cryptoit.apis.ApiUtilities
import com.example.cryptoit.databinding.FragmentWatchlistBinding
import com.example.cryptoit.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var watchlist : ArrayList<String>
    private lateinit var watchListitem: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(layoutInflater)

        readData()

        lifecycleScope.launch(Dispatchers.IO){

            val res = ApiUtilities.getInstance().create(ApiInterface::class.java)
                .getMarketData()
            if (res.body() != null){
                withContext(Dispatchers.Main){
                    watchListitem = ArrayList()
                    watchListitem.clear()

                    for(watchData in watchlist){
                        for(item in res.body()!!.data.cryptoCurrencyList){
                            if (watchData == item.symbol){
                                watchListitem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility=GONE
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(),watchListitem, "watchFragment")
                }
            }


        }

        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist ", Context.MODE_PRIVATE)
        val gson= Gson()
        val json  = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json, type)


    }
}