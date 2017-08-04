package com.example.ominext.chatfirebase.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.Status
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


/**
 * Created by Ominext on 8/2/2017.
 */
class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.progressbar_loading)
    lateinit var progressBar: ProgressBar

    companion object {
        @JvmField val RC_SIGN_IN = 12
    }

    var authFirebase: FirebaseAuth? = ChatApplication.app?.firebaseAuth
    var userFirebase: FirebaseUser? = ChatApplication.app?.firebaseUser
    lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        if (userFirebase != null) {
            val intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
            finish()
        }

        progressBar.isEnabled = false
        progressBar.visibility = View.GONE

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressBar.isEnabled = false
        progressBar.visibility = View.GONE

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                println(result.status)
                Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        authFirebase?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        addUserToDatabase(account)
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Authentication successfully.", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            moveToMainFragment()
                        }, 2000)
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun addUserToDatabase(account: GoogleSignInAccount?) {
        userFirebase = authFirebase?.currentUser
        val user = User(userFirebase?.uid, account?.email, account?.displayName, account?.photoUrl?.toString(), Status.ONLINE.ordinal)
        val db = FirebaseDatabase.getInstance().reference
        db.child(ChatConstant.USERS).child(user.uid).setValue(user)
        db.child(ChatConstant.USERS).child(user.uid).child(ChatConstant.STATUS).onDisconnect().setValue(Status.OFFLINE.ordinal)
    }

    private fun moveToMainFragment() {
        val intent = Intent(this, ChatListActivity::class.java)
        startActivity(intent)
        finish()
    }

    @OnClick(R.id.layout_login)
    fun onClickLoginGoogle() {
        progressBar.isEnabled = true
        progressBar.visibility = View.VISIBLE

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @OnClick(R.id.layout_loginFB)
    fun onClickLoginFacebook(){
        progressBar.isEnabled = true
        progressBar.visibility = View.VISIBLE

        //todo facebook
    }

    @OnClick(R.id.textview_close)
    fun onClickClose() {
        finish()
    }
}
