package com.hegsam.retrofitcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hegsam.retrofitcompose.model.CryptoModel
import com.hegsam.retrofitcompose.service.CryptoAPI
import com.hegsam.retrofitcompose.ui.theme.RetrofitComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen ()
{
    val cryptoList = remember {mutableStateListOf<CryptoModel>()}
    getData(cryptoList)
    Scaffold(topBar = {AppBar()}) {
        CryptoList(cryptos = cryptoList)
    }
}

@Composable
fun CryptoList (cryptos : List<CryptoModel>)
{
    LazyColumn {
        items(cryptos) { crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow (crypto : CryptoModel)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(2.dp))

        Text(text = crypto.price,
        modifier = Modifier.padding(2.dp),
        style = MaterialTheme.typography.h6)
    }
}


@Composable
fun AppBar ()
{
    TopAppBar(backgroundColor = Color.Blue) {
        Text(text = "Retrofit Compose",
        color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitComposeTheme {
        CryptoRow(crypto = CryptoModel("BTC","-1"))
    }
}

fun getData (list : SnapshotStateList<CryptoModel>)
{
    val baseURL = "https://api.nomics.com/v1/"
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()
        .create(CryptoAPI::class.java)

    CoroutineScope (Dispatchers.IO).launch {
        val response = retrofit.getData()
        withContext(Dispatchers.Main)
        {
            if (response.isSuccessful)
            {
                response.body()?.let {
                    list.addAll(it)
                }
            }
        }
    }

}