package com.ericyl.example.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.example.util.AppProperties;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.JavaCommand;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RootActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String CMD_MASTER_CLEAR = "am broadcast -a android.intent.action.MASTER_CLEAR";
    private static final String CMD_UNINSTALL = "pm uninstall " + AppProperties.getContext().getPackageName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initActionBar(toolbar);
    }

    @OnClick({R.id.btn_uninstall, R.id.btn_reboot})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_uninstall:
                uninstall();
                break;
            case R.id.btn_reboot:
                reboot();
                break;
        }

    }

    private void uninstall() {
        JavaCommand rebootCommand = new JavaCommand(0, false, this, CMD_UNINSTALL) {
            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);
            }
        };
        try {
            RootTools.getShell(true).add(rebootCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            Toast.makeText(this, "time out", Toast.LENGTH_SHORT).show();
        } catch (RootDeniedException e) {
            e.printStackTrace();
            Toast.makeText(this, "root 权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void reboot() {
        JavaCommand rebootCommand = new JavaCommand(0, false, this, "reboot") {
            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);
            }
        };
        try {
            RootTools.getShell(true).add(rebootCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            Toast.makeText(this, "time out", Toast.LENGTH_SHORT).show();
        } catch (RootDeniedException e) {
            e.printStackTrace();
            Toast.makeText(this, "root 权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }
}
