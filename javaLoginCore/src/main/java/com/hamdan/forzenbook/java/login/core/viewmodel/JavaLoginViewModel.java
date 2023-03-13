package com.hamdan.forzenbook.java.login.core.viewmodel;

import androidx.lifecycle.ViewModel;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.login.core.domain.LoginEntrys;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetStoredCredentialsUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginStringValidationUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginValidationUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class JavaLoginViewModel extends ViewModel {

    private final LoginStringValidationUseCase loginStringValidationUseCase;
    private final LoginGetCredentialsFromNetworkUseCase getTokenFromNetworkUseCase;
    private final LoginGetStoredCredentialsUseCase getTokenFromDatabaseUseCase;
    private final LoginValidationUseCase requestValidationCode;

    private final BehaviorSubject<JavaLoginState> _state = BehaviorSubject.createDefault(new JavaLoginState());

    @Inject
    public JavaLoginViewModel(LoginStringValidationUseCase loginStringValidationUseCase, LoginGetCredentialsFromNetworkUseCase getTokenFromNetworkUseCase, LoginGetStoredCredentialsUseCase getTokenFromDatabaseUseCase, LoginValidationUseCase requestValidationCode) {
        this.loginStringValidationUseCase = loginStringValidationUseCase;
        this.getTokenFromNetworkUseCase = getTokenFromNetworkUseCase;
        this.getTokenFromDatabaseUseCase = getTokenFromDatabaseUseCase;
        this.requestValidationCode = requestValidationCode;
    }

    public Flowable<JavaLoginState> getState() {
        return _state.toFlowable(BackpressureStrategy.BUFFER);
    }

    private JavaLoginState viewableState() {
        return _state.getValue();
    }

    private void setState(JavaLoginState inState) {
        _state.onNext(inState);
    }

    public void updateLoginTexts(String email, String code) {
        LoginEntrys stringStates = loginStringValidationUseCase.invoke(
                new LoginEntrys(
                        new Entry(email, viewableState().getEmail().getError()),
                        new Entry(code, viewableState().getCode().getError())
                )
        );
        setState(
                new JavaLoginState(
                        stringStates.getEmail(),
                        stringStates.getCode(),
                        viewableState().shouldShowInfoDialog(),
                        viewableState().getInputtingCode(),
                        viewableState().getLoading(),
                        viewableState().getHasError()
                )
        );
    }

    private void loginNormalView() {
        setState(new JavaLoginState(
                        viewableState().getEmail(),
                        viewableState().getCode(),
                        false,
                        viewableState().getInputtingCode(),
                        false,
                        false
                )
        );
    }

    public void loginDismissErrorClicked() {
        loginNormalView();
    }

    public void loginDismissInfoClicked() {
        loginNormalView();
    }

    private void submitLogin() {
        setState(
                new JavaLoginState(
                        viewableState().getEmail(),
                        viewableState().getCode(),
                        viewableState().shouldShowInfoDialog(),
                        true,
                        true,
                        false
                )
        );
        Single
                .fromCallable(
                        () -> {
                            getTokenFromNetworkUseCase.invoke(viewableState().getEmail().getText(), viewableState().getCode().getText());
                            return true;
                        }
                ).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        state -> {
                            if (state) {
                                setState(new JavaLoginState());
                            }
                            else{
                                setState(
                                        new JavaLoginState(
                                                viewableState().getEmail(),
                                                viewableState().getCode(),
                                                false,
                                                true,
                                                false,
                                                true
                                        )
                                );
                            }
                        },
                        error -> {
                            setState(
                                    new JavaLoginState(
                                            viewableState().getEmail(),
                                            viewableState().getCode(),
                                            false,
                                            true,
                                            false,
                                            true
                                    )
                            );
                        }
                );

    }

    private void requestLoginValidationCode() {
        setState(
                new JavaLoginState(
                        viewableState().getEmail(),
                        viewableState().getCode(),
                        viewableState().shouldShowInfoDialog(),
                        viewableState().getInputtingCode(),
                        true,
                        viewableState().getHasError()
                )
        );
        Single
                .fromCallable(
                        () -> {
                            requestValidationCode.invoke(viewableState().getEmail().getText());
                            return true;
                        }
                ).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        filler -> {
                            if (filler) {
                                setState(
                                        new JavaLoginState(
                                                viewableState().getEmail(),
                                                viewableState().getCode(),
                                                true,
                                                true,
                                                false,
                                                viewableState().getHasError()
                                        )
                                );
                            } else {
                                setState(
                                        new JavaLoginState(
                                                viewableState().getEmail(),
                                                viewableState().getCode(),
                                                viewableState().shouldShowInfoDialog(),
                                                false,
                                                false,
                                                true
                                        )
                                );
                            }
                        },
                        error -> {
                            setState(
                                    new JavaLoginState(
                                            viewableState().getEmail(),
                                            viewableState().getCode(),
                                            viewableState().shouldShowInfoDialog(),
                                            false,
                                            false,
                                            true
                                    )
                            );
                        }
                );
    }

    public void loginClicked() {
        if (viewableState().getInputtingCode()) {
            submitLogin();
        } else {
            requestLoginValidationCode();
        }
    }
}
