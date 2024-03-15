package br.com.fiap.weatherapp2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.weatherapp2.model.Clima
import br.com.fiap.weatherapp2.service.RetrofitFactory
import br.com.fiap.weatherapp2.ui.theme.WeatherApp2Theme
import coil.compose.rememberAsyncImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAppScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherAppScreen() {

    // variaveis de estado
    var cidadeState by remember { mutableStateOf("Chicago") }
    var climaState by remember { mutableStateOf(Clima()) }

    // variaveis do clima para acessar api
    // Dia atual
    val cidade = climaState.location?.name
    val temp = climaState.current?.temp
    val descricao = climaState.current?.condition?.text
    val icone = climaState.current?.condition?.icon
    val vento = climaState.current?.wind
    val umidade = climaState.current?.humidity
    val sensacao = climaState.current?.feelslike
    val alerta = climaState.alerts?.alert

    //Horas do dia
    val listaHoras = climaState.forecast?.forecastday?.get(0)?.hour


    Box(modifier = Modifier
        .fillMaxSize()
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()) {

            // Previsão do dia
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedTextField(
                        value = cidadeState,
                        onValueChange = {
                            cidadeState = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Qual a sua cidade?")
                        },
                        trailingIcon = {
                            IconButton(onClick = {

                                val call = RetrofitFactory()
                                    .getClimaService()
                                    .getClima(cidadeState)

                                call.enqueue(object : Callback<Clima> {
                                    override fun onResponse(
                                        call: Call<Clima>,
                                        response: Response<Clima>
                                    ) {
                                        climaState = response.body()!!

                                        Log.i("clima", "${climaState}")
                                    }

                                    override fun onFailure(call: Call<Clima>, t: Throwable) {
                                        Log.i("Error", "Erro: ${t.message}")
                                    }

                                })
                            })
                            {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = ""
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))

                // imagem dinamica do clima
                Box(modifier = Modifier
                    .padding(10.dp)
                    .height(100.dp)
                    .width(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberAsyncImagePainter(
                        model = "https:${icone}"
                    )
                    Image(
                        painter = painter,
                        contentDescription = "icone",
                        modifier = Modifier.size(400.dp)
                    )
                }
                Text(text = "$temp",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "$descricao")

                Spacer(modifier = Modifier.size(30.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column {
                        Image(painter = painterResource(id = R.drawable.humidity) , contentDescription = "umidade" )
                        Text(text = "$sensacao")
                        Text(text = "Sensação")
                    }
                    Column {
                        Image(painter = painterResource(id = R.drawable.humidity) , contentDescription = "umidade" )
                        Text(text = "$umidade")
                        Text(text = "Umidade")
                    }
                    Column {
                        Image(painter = painterResource(id = R.drawable.humidity) , contentDescription = "umidade" )
                        Text(text = "${vento} km")
                        Text(text = "Vento")
                    }
                }
            }

            // Alerta: Só aparece caso não esteja vazio
            if (alerta?.isEmpty() == false) {
                Spacer(modifier = Modifier.size(30.dp))
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    Text(text = "Alerta: ${alerta[0].event}")
                }
            } else {
                Log.e("alerta" , "Alerta: Sem alertas nesta cidade hoje")
            }

            // Cards inferiores com previsao de 24 horas
            Spacer(modifier = Modifier.size(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Hoje")
                    Text(text = "Previsão de 24 horas")
                }
                Spacer(modifier = Modifier.size(10.dp))

                LazyRow() {
                    if (listaHoras != null) {
                        items (listaHoras.size) { item ->
                            val date = listaHoras[item].time
                            val tempHora = listaHoras[item].tempHour
                            val iconeHora = listaHoras[item].conditionHour.icon
                            val hora = formatDate(date)
                            PrevisaoCard(hora, tempHora, iconeHora)
                        }
                    }
                }

            }


            
        }



    }
}

fun formatDate(date: String): String {

    val hora = date.split(" ")
    return hora[1].toString()
}


@Composable
fun PrevisaoCard(hora: String?, tempHora: String?, iconeHora: String?) {

    Card(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$hora")

            Box(modifier = Modifier
                .height(40.dp)
                .width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = "https:${iconeHora}"
                )
                Image(
                    painter = painter,
                    contentDescription = "icone",
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(text = "$tempHora")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherAppScreenPreview() {
    WeatherApp2Theme {
        WeatherAppScreen()
    }
}