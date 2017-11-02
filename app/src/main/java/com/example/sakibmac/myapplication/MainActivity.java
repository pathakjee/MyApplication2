package com.example.sakibmac.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String value;
    Realm myreal;
    Sign sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myreal = Realm.getInstance(this);
        Bundle b = getIntent().getExtras();
        value = b.getString("login");

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayMenu();
    }


    private void displayMenu() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Menu menu = navigationView.getMenu();

        // SubMenu menu1 = menu.addSubMenu("");

        menu.add("Home").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        if (value.equals("Teacher")) {
            menu.add("Student").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Studentlist()).commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
        menu.add("Teacher").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Teacherlist()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add("Subject").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SubjectList()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add("Class Routine").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClassRoutine()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        if (value.equals("Teacher")) {
            menu.add("Daily Attendance").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new DailyAttendance()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        } else if (value.equals("Parent")) {
            menu.add("Student Attendance").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new StudentAttendance()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
        menu.add("Exam Marks").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MarksFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        menu.add("NoticeBoard").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new NoticeBoard()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add("Profile").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Accounting()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add("Change Password").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChangePassword()).commit();

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                myreal.beginTransaction();

                sign = myreal.createObject(Sign.class);

                sign.setImage("");
                sign.setName("");
                sign.setEmail("");
                sign.setPassword("");
                sign.setContact("");
                sign.setAddress("");
                sign.setFathername("");
                sign.setMothername("");
                sign.setClas("");
                sign.setBirthday("");
                sign.setGender("");
                sign.setParentid("");
                sign.setSection("");
                sign.setRollno("");
                sign.setBlood("");
                sign.setReligion("");

                myreal.commitTransaction();


                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
