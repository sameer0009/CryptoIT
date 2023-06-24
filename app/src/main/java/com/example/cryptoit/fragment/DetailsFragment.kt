package com.example.cryptoit.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoit.R
import com.example.cryptoit.R.drawable.active_button
import com.example.cryptoit.databinding.FragmentDetailsBinding
import com.example.cryptoit.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.annotation.SuppressLint as SuppressLint1

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding

    private val item: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val data: CryptoCurrency = item.data!!
        setUpDetalis(data)

        loadChart(data)

        setButtonOnClick(data)

        addToWatchList(data)

        return binding.root
    }

    var watchlist : ArrayList<String>? = null
    var watchlistIsChecked = false

    private fun addToWatchList(data: CryptoCurrency) {
        readData()

        watchlistIsChecked = if (watchlist!!.contains(data.symbol)){

            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{

            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false

        }

        binding.addWatchlistButton.setOnClickListener{

            watchlistIsChecked =
                if (!watchlistIsChecked){

                    if (!watchlist!!.contains(data.symbol)){
                        watchlist!!.add(data.symbol)
                    }

                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)

                    true
                }else{

                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchlist!!.remove(data.symbol)
                    false
                }

        }


    }

    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist ", Context.MODE_PRIVATE)
        val editor= sharedPreferences.edit()
        val gson = Gson()
        val json =gson.toJson(watchlist)
        editor.putString("watchlist",json)
        editor.apply()



    }
    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist ", Context.MODE_PRIVATE)
        val gson= Gson()
        val json  = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json, type)


    }

    private fun setButtonOnClick(item: CryptoCurrency) {

        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5

        val clickListener = View.OnClickListener {
            when (it.id) {
                fifteenMinute.id -> loadChartData(
                    it,
                    "15",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )
                oneHour.id -> loadChartData(
                    it,
                    "1H",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    fifteenMinute
                )
                fourHour.id -> loadChartData(
                    it,
                    "4H",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fifteenMinute,
                    oneHour
                )
                oneDay.id -> loadChartData(
                    it,
                    "1D",
                    item,
                    fifteenMinute,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )
                oneWeek.id -> loadChartData(
                    it,
                    "1W",
                    item,
                    oneDay,
                    oneMonth,
                    fifteenMinute,
                    fourHour,
                    oneHour
                )
                oneMonth.id -> loadChartData(
                    it,
                    "1M",
                    item,
                    oneDay,
                    fifteenMinute,
                    oneWeek,
                    fourHour,
                    oneHour
                )

            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {

       disableButton(oneDay,oneHour,oneMonth,oneWeek,fourHour)
      // requireView().setBackgroundResource(active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=" +
                    "[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=" +
                    "[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"


        )
    }

    private fun disableButton(oneDay: AppCompatButton, oneHour: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton) {
        oneDay.background=null
        oneHour.background=null
        oneMonth.background=null
        oneWeek.background=null
        fourHour.background=null
    }

    private fun loadChart(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
            .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT")
    }

    @SuppressLint1("SetTextI18n")
    private fun setUpDetalis(data:CryptoCurrency) {
        binding.detailSymbolTextView.text=data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id +".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = "+ ${String.format("$%.4f",data.quotes[0].price)} "

        if(data.quotes[0].percentChange24h > 0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text="+ ${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text=" ${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }

    }

}