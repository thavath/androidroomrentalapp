package com.example.user.messagerapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.app_bar_welcome.*

class WelcomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var userPosition: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        check if user is login or not

        setContentView(R.layout.activity_welcome)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard"


        var hView = nav_view.getHeaderView(0)
        var profileAvatar = hView.findViewById<ImageView>(R.id.profile_image)
        var userName = hView.findViewById<TextView>(R.id.user_full_name)

          val uid = FirebaseAuth.getInstance().uid
          if(uid != null){
               val ref = FirebaseDatabase.getInstance().getReference("/users")
               ref.addListenerForSingleValueEvent(object: ValueEventListener {
                   override fun onCancelled(p0: DatabaseError) {
                       TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                   }

                   override fun onDataChange(p0: DataSnapshot) {
                       p0.children.forEach {
                           var user = it.getValue(User::class.java) as User
                           if (user.uid == uid){
                               Picasso.get().load(user.imageUri).into(profileAvatar)
                               userName.text = user.username.capitalize()
                               supportActionBar?.title = user.username.capitalize()+"'s Dashboard"
                               userPosition = user.position
                               if (userPosition.equals(("Room Tenant").toString().trim())){
                                   var intent = Intent(this@WelcomeActivity, HomeActivity::class.java)
                                   startActivity(intent)
                                   finish()
                               }
                               Toast.makeText(this@WelcomeActivity, "You are $userPosition", Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               })
          }else{
               verifyUserIsLogin()
          }

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
//    verifyUserIsLogin
    private fun verifyUserIsLogin(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.welcome, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                FirebaseAuth.getInstance().signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_tenant_dashboard -> {
                // Handle the camera action
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                var intent = Intent(this, ShowRoomActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_room_available -> {
                var intent = Intent(this, BookedRoomActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_add_building -> {
                var intent = Intent(this, AddNewBuildingActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_add_room -> {
                var intent = Intent(this, AddRoomActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_my_room -> {
                var intent = Intent(this, MyRoomListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                var intent = Intent(this, ShareActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_send -> {
                var intent = Intent(this, FeedbackActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
