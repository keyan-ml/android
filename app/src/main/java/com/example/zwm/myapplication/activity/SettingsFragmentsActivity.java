package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.CheckUpdateFragment;
import com.example.zwm.myapplication.fragment.ClearSpaceFragment;
import com.example.zwm.myapplication.fragment.HelpFragment;
import com.example.zwm.myapplication.fragment.MakeDarkFragment;

public class SettingsFragmentsActivity extends AppCompatActivity {
    public static Activity instance;

    private MakeDarkFragment makeDarkFragment;
    private ClearSpaceFragment clearSpaceFragment;
    private CheckUpdateFragment checkUpdateFragment;
    private HelpFragment helpFragment;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_fragments);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;

        makeDarkFragment = null;
        clearSpaceFragment = null;
        checkUpdateFragment = null;
        helpFragment = null;
    }

    private void initEvents() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (makeDarkFragment == null) {
            makeDarkFragment = new MakeDarkFragment();
            transaction.add(R.id.settings_fragment_content, makeDarkFragment);
        }
        if (clearSpaceFragment == null) {
            clearSpaceFragment = new ClearSpaceFragment();
            transaction.add(R.id.settings_fragment_content, clearSpaceFragment);
        }
        if (checkUpdateFragment == null) {
            checkUpdateFragment = new CheckUpdateFragment();
            transaction.add(R.id.settings_fragment_content, checkUpdateFragment);
        }
        if (helpFragment == null) {
            helpFragment = new HelpFragment();
            transaction.add(R.id.settings_fragment_content, helpFragment);
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("FragmentId");
        switch (id) {
            case "MakeDarkFragment":
                hideFragments(transaction);
                transaction.show(makeDarkFragment);
                break;
            case "ClearSpaceFragment":
                hideFragments(transaction);
                transaction.show(clearSpaceFragment);
                break;
            case "CheckUpdateFragment":
                hideFragments(transaction);
                transaction.show(checkUpdateFragment);
                break;
            case "HelpFragment":
                hideFragments(transaction);
                transaction.show(helpFragment);
        }

        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (makeDarkFragment != null) {
            transaction.hide(makeDarkFragment);
        }
        if (clearSpaceFragment != null) {
            transaction.hide(clearSpaceFragment);
        }
        if (checkUpdateFragment != null) {
            transaction.hide(checkUpdateFragment);
        }
        if (helpFragment != null) {
            transaction.hide(helpFragment);
        }
    }
}
