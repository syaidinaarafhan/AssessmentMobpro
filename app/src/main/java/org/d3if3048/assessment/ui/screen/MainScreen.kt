package org.d3if3048.assessment.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3048.assessment.R
import org.d3if3048.assessment.ui.theme.AssessmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                Text(text = "Silahkan Cek Kesehatan Anda")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("aboutScreen") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.info_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        MainPreview(
            Modifier.padding(padding)
        )
    }
}

@Composable
fun MainPreview(modifier: Modifier) {
    var tidur by rememberSaveable {
        mutableStateOf("")
    }
    var tidurError by rememberSaveable {
        mutableStateOf(false)
    }
    var sudahMinum by rememberSaveable {
        mutableStateOf(false)
    }
    var sudahMakan by rememberSaveable {
        mutableStateOf(false)
    }
    var hasilKesehatan by rememberSaveable {
        mutableStateOf("")
    }
    var context = LocalContext.current

    Column (
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),

    ) {

        Text(text = stringResource(id = R.string.berapa_jam_tidur))
        OutlinedTextField(
            value = tidur,
            onValueChange = { if (it.isDigitsOnly()) tidur = it },
            label = { stringResource(id = R.string.jam_tidur) },
            isError = tidurError,
            trailingIcon = { IconPicker(isError = tidurError, unit = "Jam") },
            supportingText = {ErrorHint(isError = tidurError)},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),

        )
        Text(text = stringResource(id = R.string.minum))
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = sudahMinum,
                onCheckedChange = {sudahMinum = it},

                )
            Text(text = stringResource(id = R.string.sudah))
        }

        Text(text = stringResource(id = R.string.makan))
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = sudahMakan,
                onCheckedChange = {sudahMakan = it},

                )
            Text(text = stringResource(id = R.string.sudah))
        }
            Button(
                modifier = Modifier.padding(start = 140.dp),
                onClick = {

                    tidurError = (tidur == "")
                    if (tidurError){
                        Toast.makeText(context, "Textfield Harus Diisi!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    hasilKesehatan = prosesKesehatan(
                    tidur = tidur,
                    sudahMinum = sudahMinum,
                    sudahMakan = sudahMakan,
                ) }
            ) {
                Text(text = stringResource(id = R.string.button_main))
            }
        if (tidur != ""){
            Text(
                text = hasilKesehatan,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = {
                    ShareData(
                        context = context,
                        message = hasilKesehatan
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.bagikan))
            }
        }

    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError){
        Text(text = stringResource(id = R.string.input_invalid))
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {

    if (isError){
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null
        )

    }else {
        Text(text = unit)
    }
}

private fun prosesKesehatan ( tidur:String,sudahMinum: Boolean,  sudahMakan: Boolean ): String {
    var pointKesehatan = 0

    if (tidur.toInt() >= 5){
        pointKesehatan+=3

    }else if (tidur.toInt() >= 3){
        pointKesehatan++
    }

    if (sudahMakan) {
        pointKesehatan++
    }

    if (sudahMinum) {
        pointKesehatan++
    }
    val hasilKesehatan = when {
        pointKesehatan >= 5 -> "Anda Sangat Sehat"
        pointKesehatan >= 3 -> "Anda Harus Tidur Yang Cukup"
        else -> "Anda Harus meningkatkan pola makan minum dan tidur untuk menjaga kesehatan anda!!!"
    }
    return hasilKesehatan
}

private fun ShareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    AssessmentTheme {
        MainScreen(rememberNavController())
    }
}