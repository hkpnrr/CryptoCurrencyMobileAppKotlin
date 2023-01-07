package com.halilakpinar.cryptocurrencymobileappkotlin.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.halilakpinar.cryptocurrencymobileappkotlin.Adapter.RecyclerAdapter
import com.halilakpinar.cryptocurrencymobileappkotlin.Model.CryptoModel
import com.halilakpinar.cryptocurrencymobileappkotlin.R
import com.halilakpinar.cryptocurrencymobileappkotlin.Service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),RecyclerAdapter.Listener {
    private val BASE_URL="https://raw.githubusercontent.com/"
    private lateinit var cryptoModels:ArrayList<CryptoModel>
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable= CompositeDisposable()

        val layoutManager : RecyclerView.LayoutManager =LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        loadData()


    }

    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))


        /*
        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()

        call.enqueue(object :Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        cryptoModels = ArrayList(it)

                        recyclerAdapter= RecyclerAdapter(cryptoModels,this@MainActivity)
                        recyclerView.adapter=recyclerAdapter

                        for(cryptoModel:CryptoModel in cryptoModels){

                            println(cryptoModel.price+" "+cryptoModel.currency)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
        */

    }

    private fun handleResponse(cryptoList:List<CryptoModel>){
        cryptoModels=ArrayList(cryptoList)
        cryptoModels?.let {
            recyclerAdapter= RecyclerAdapter(it,this@MainActivity)
            recyclerView.adapter=recyclerAdapter
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked: ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}