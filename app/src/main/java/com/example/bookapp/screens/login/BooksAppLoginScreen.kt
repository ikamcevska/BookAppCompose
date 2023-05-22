package com.example.bookapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.bookapp.R
import com.example.bookapp.components.EmailInput
import com.example.bookapp.components.PasswordInput
import com.example.bookapp.components.UserForm
import com.example.bookapp.navigation.BooksScreens
import io.grpc.ExperimentalApi

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookLoginScreen(navController: NavController,
                    viewModel: LoginScreenViewModel=androidx.lifecycle.viewmodel.compose.viewModel()) {
    androidx.compose.material.Surface(modifier = Modifier.fillMaxSize()) {
        val showLoginForm = rememberSaveable {
            mutableStateOf(true)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (showLoginForm.value) UserForm(
                loading = false,
                isCreateAccount = false
            ) { email, password ->
                viewModel.signInWithEmailAndPassword(email, password) {
                    navController.navigate(BooksScreens.HomeScreen.name)
                }
            }
            else {
                UserForm(loading = false, isCreateAccount = true) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(BooksScreens.HomeScreen.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                val text = if (showLoginForm.value) "Sign up" else "Login"
                Text(text = "New User?")
                Text(text,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92CBDF)
                )
            }

        }
    }
}








