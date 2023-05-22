package com.example.bookapp.screens.login

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
   // val loadingState= MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth=Firebase.auth

    private val _loading= MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit){
        if(_loading.value == false){
            _loading.value=true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        val displayName=task.result?.user?.email?.split('@')?.get(0) //me [0] @gmail.com [1]
                        createUser(displayName)
                        home()
                    }else{
                        Log.d(TAG,"createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value=false
                }
        }

    }

    private fun createUser(displayName: String?) {
        val userId=auth.currentUser?.uid
        val user=MUser( userId=userId.toString(),
        displayName = displayName.toString(),
        avatarUrl = "",
        quote = "Life is beautiful",
        proffesion = "Android Developer",
        id=null).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }

    fun signInWithEmailAndPassword(email:String,password:String,home: () -> Unit)
    =viewModelScope.launch{
        try{
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                       //TODO("Take them home")
                        home()
                        Log.d("FB","signInWithEmailAndPassword: Yayayayay! ${task.result.toString()}")
                    }else{
                        Log.d("FB","signInWithEmailAndPassword: ${task.result.toString()}")
                    }
                }
        }catch(ex: java.lang.Exception){
            Log.d("FB","signInWithEmailAndPassword: ${ex.message}")
        }
    }

}