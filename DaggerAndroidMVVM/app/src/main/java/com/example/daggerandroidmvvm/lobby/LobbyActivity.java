package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daggerandroidmvvm.R;
import com.example.daggerandroidmvvm.common.viewmodel.Response;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class LobbyActivity extends AppCompatActivity {

    @Inject
    LobbyViewModelFactory viewModelFactory;

    @BindView(R.id.greeting_textview)
    TextView greetingTextView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private LobbyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LobbyViewModel.class);

        viewModel.response().observe(this, response -> processResponse(response));
    }

    @OnClick(R.id.common_greeting_button)
    void onCommonGreetingButtonClicked() {
        viewModel.loadCommonGreeting();
    }

    @OnClick(R.id.lobby_greeting_button)
    void onLobbyGreetingButtonClicked() {
        viewModel.loadLobbyGreeting();
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;

            case SUCCESS:
                renderDataState(response.data);
                break;

            case ERROR:
                renderErrorState(response.error);
                break;
            default:
                break;
        }
    }

    private void renderLoadingState() {
        greetingTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void renderDataState(String greeting) {
        loadingIndicator.setVisibility(View.GONE);
        greetingTextView.setVisibility(View.VISIBLE);
        greetingTextView.setText(greeting);
    }

    private void renderErrorState(Throwable throwable) {
        Timber.e(throwable);
        loadingIndicator.setVisibility(View.GONE);
        greetingTextView.setVisibility(View.GONE);
        Toast.makeText(this, R.string.greeting_error, Toast.LENGTH_SHORT).show();
    }
}
