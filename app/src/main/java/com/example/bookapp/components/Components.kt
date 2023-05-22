package com.example.bookapp.components

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.*
import com.example.bookapp.model.MBook
import com.example.bookapp.navigation.BooksScreens
import com.google.firebase.auth.FirebaseAuth
import com.example.bookapp.R


@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState: MutableState<String>,
               labelId:String="Email",
               enabled:Boolean=true,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default){
    InputField(modifier = modifier,
        valueState=emailState,
        labelId=labelId,
        enabled=enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)
}
@Composable
fun InputField(
    modifier: Modifier=Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean=true,
    keyboardType:KeyboardType=KeyboardType.Text,
    imeAction:ImeAction=ImeAction.Done,
    onAction: KeyboardActions=KeyboardActions.Default
) {
    OutlinedTextField(value = valueState.value, onValueChange ={valueState.value=it},
        label={ Text(text=labelId) },
        singleLine=isSingleLine,
        textStyle = TextStyle(fontSize=18.sp,
            color= MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        enabled=enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInput(modifier: Modifier,
                  passwordState: MutableState<String>,
                  labelId: String, enabled: Boolean,
                  passwordVisibility: MutableState<Boolean>,
                  imeAction: ImeAction=ImeAction.Done,
                  onAction: KeyboardActions=KeyboardActions.Default) {
    val visualTransformation=if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value, onValueChange={
        passwordState.value=it
    },
        label={Text(text=labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp,color=MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation=visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility=passwordVisibility)},
        keyboardActions = onAction)

}
@ExperimentalComposeUiApi
@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close

    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean=false,
    isCreateAccount: Boolean=false,
    onDone: (String,String) -> Unit = {email,pwd ->}
){
    val email= rememberSaveable{ mutableStateOf("") }
    val password= rememberSaveable{ mutableStateOf("") }
    val passwordVisibility= rememberSaveable{ mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController= LocalSoftwareKeyboardController.current
    val valid = remember(email.value,password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier= Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LoginLottie()
    }
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        EmailInput(emailState = email, enabled = true, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            })
        SubmitButton(
            textId=if(isCreateAccount) "Create Account" else "Login",
            loading=loading,
            validInputs=valid
        ){
            onDone(email.value.trim(),password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                onClick:() -> Unit) {
        Button(
            onClick=onClick,
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            enabled = !loading && validInputs,
            shape= CircleShape
        ){
            if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
            else Text(text=textId, modifier = Modifier.padding(5.dp))

        }
}

@Composable
fun LoginLottie() {
    var isPlaying = true;
    val speed = 1.5f
    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(com.example.bookapp.R.raw.login)
    )


    val progress by animateLottieCompositionAsState(

        composition,

        iterations = LottieConstants.IterateForever,


        isPlaying = isPlaying,


        speed = speed,


        restartOnPlay = false
    )
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 40.dp, start = 0.dp, end = 0.dp)
        )

    }
}

@Composable
fun TitleSection(modifier: Modifier=Modifier,label:String){
    Surface(modifier = modifier.padding(start=5.dp,top=1.dp)){
        Column{
            Text(text=label,
                fontSize=19.sp,
                fontStyle= FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}
@Composable
fun BookAppBar(
    title:String,
    icon: ImageVector?=null,
    showProfile:Boolean=true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
){
    TopAppBar(title={
        Row(verticalAlignment = Alignment.CenterVertically){
            if(showProfile){
                Icon(imageVector = Icons.Default.Book,
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f))
            }
            if(icon !=null){
                Icon(imageVector = icon, contentDescription = "arrow back",
                tint=Color.Red.copy(alpha=0.7f),
                modifier = Modifier.clickable{ onBackArrowClicked.invoke() })
            }
            Spacer(modifier=Modifier.width(40.dp))
            Text(text=title,
                color=Color.Black.copy(alpha=0.7f),
                style= androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))

        }
    },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(BooksScreens.LoginScreen.name)
                }
            }){
                if(showProfile)Row(){
                    Icon(imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color(0xFF92CBDF))
                }else Box() {
                    
                }


            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp)



}
@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {onTap() }, shape = RoundedCornerShape(50.dp), backgroundColor = MaterialTheme.colors.background) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a Book",tint=MaterialTheme.colors.onSecondary)
    }

}
@Composable
fun BookRating(score: Double=4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),shape= RoundedCornerShape(56.dp),elevation =6.dp,color=Color.White) {
        Column(modifier =Modifier.padding(4.dp)){
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription ="Star",
                modifier = Modifier.padding(3.dp))
            Text(text=score.toString(),style=MaterialTheme.typography.subtitle1)
        }

    }

}
@Composable
fun BookCard(book: MBook,
             onPressDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(242.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder, contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating(score = 3.5)
                }
            }
            Text(
                text = "Book title", modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Authors:All..", modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                RoundedButton(label = "Reading", radius = 70) {

                }
            }
        }
    }
}
@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = com.example.bookapp.R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter { it ->
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG)
        .show()
}



@Composable
fun ListCard(book: MBook,
             onPressDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center) {

                Image(painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))
                Spacer(modifier = Modifier.width(50.dp))

                Column(modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp))

                    BookRating(score = book.rating!!)
                }

            }
            Text(text = book.title.toString(), modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = book.authors.toString(), modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption) }

        val isStartedReading = remember {
            mutableStateOf(false)
        }

        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom) {
            isStartedReading.value = book.startedReading != null


            RoundedButton(label = if (isStartedReading.value)  "Reading" else "Not Yet",
                radius = 70){}

        }
    }



}

@Composable
fun RoundedButton(
    label:String,
    radius:Int=29,
    onPress:() ->Unit
){
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent=radius,
        topStartPercent = radius)),
        color=Color(0xFF92CBDF)
    ) {
        Column(modifier= Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text=label,style=androidx.compose.ui.text.TextStyle(color=Color.White,fontSize=15.sp))

        }

    }

}