package com.iamsmk.shopify

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.iamsmk.shopify.Utilities.ProductData
import com.iamsmk.shopify.Utilities.model.Products
import com.iamsmk.shopify.ui.theme.ShopifyTheme
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(textState)
        ItemList(state = textState)
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 5.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(10.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun ItemList(state: MutableState<TextFieldValue>) {
    val jsonString = loadJSONFromAsset(context = LocalContext.current)
    val productList = ProductData(jsonString).productList.Products
    var searchedProducts by remember { mutableStateOf(productList) }
    val searchedText = state.value.text
    searchedProducts = if (searchedText.isEmpty()) {
        productList
    } else {
        val resultList = ArrayList<Products>()
        for (item in productList) {
            if (item.name?.lowercase(Locale.getDefault())
                    ?.contains(searchedText.lowercase(Locale.getDefault())) == true
            ) {
                resultList.add(item)
            }
        }
        resultList
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(searchedProducts.size) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Max)
                    .padding(10.dp)
                    .shadow(elevation = 15.dp),
                colors = CardDefaults.cardColors(Color.White),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = rememberImagePainter(searchedProducts[it].imgUrl),
                        contentDescription = "My content description",
                        modifier = Modifier
                            .size(160.dp)
                            .fillMaxWidth()
                            .padding(12.dp),
                        alignment = Alignment.Center
                    )
                    searchedProducts[it].name?.let { it1 ->
                        Text(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = it1
                        )
                    }
                    Text(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        text = "₹ ${searchedProducts[it].slashPrice}"
                    )
                    Row {
                        Text(text = "M.R.P.: ₹")
                        Text(
                            fontSize = 16.sp,
                            text = "${searchedProducts[it].price}",
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                    }
                }
            }
        }
    }
}

fun loadJSONFromAsset(context: Context): String? {
    var json: String? = null
    val charset: Charset = Charsets.UTF_8
    json = try {
        val `is`: InputStream = context.assets.open("ProductJSON.json")
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        String(buffer, charset)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}
