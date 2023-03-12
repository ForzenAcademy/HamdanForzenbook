package com.hamdan.forzenbook.java.createaccount.core.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountEntrys;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountResult;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountUseCase;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountValidationUseCase;
import com.hamdan.forzenbook.ui.core.R;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class JavaCreateAccountViewModel extends ViewModel {

    private final CreateAccountValidationUseCase createAccountValidationUseCase;
    private final CreateAccountUseCase createAccountUseCase;

    private final BehaviorSubject<JavaCreateAccountState> state = BehaviorSubject.createDefault(new JavaCreateAccountState());

    @Inject
    public JavaCreateAccountViewModel(CreateAccountValidationUseCase createAccountValidationUseCase, CreateAccountUseCase createAccountUseCase) {
        this.createAccountValidationUseCase = createAccountValidationUseCase;
        this.createAccountUseCase = createAccountUseCase;
    }

    public Flowable<JavaCreateAccountState> getState() {
        return state.toFlowable(BackpressureStrategy.BUFFER);
    }

    @NonNull
    private JavaCreateAccountState viewableState() {
        return state.getValue();
    }

    private void setState(JavaCreateAccountState inState) {
        state.onNext(inState);
    }

    public void createAccountDateDialogClicked() {
        createAccountShowDateDialog();
    }

    public void createAccountDateDialogSubmitClicked() {
        closeAccountShowDateDialog();
    }

    public void createAccountDateDialogDismiss() {
        closeAccountShowDateDialog();
    }

    public void createAccountDismissErrorClicked() {
        createAccountNormalView();
    }

    private void closeAccountShowDateDialog() {
        setState(new JavaCreateAccountState(
                viewableState().getErrorId(),
                viewableState().getFirstName(),
                viewableState().getLastName(),
                viewableState().getBirthDay(),
                viewableState().getEmail(),
                viewableState().getLocation(),
                false,
                false,
                viewableState().getLoading()
        ));
    }

    private void createAccountShowDateDialog() {
        setState(
                new JavaCreateAccountState(
                        viewableState().getErrorId(),
                        viewableState().getFirstName(),
                        viewableState().getLastName(),
                        viewableState().getBirthDay(),
                        viewableState().getEmail(),
                        viewableState().getLocation(),
                        viewableState().getIsDateDialogOpen(),
                        true,
                        viewableState().getLoading()
                )
        );
    }

    public void onDateDialogCreate(){
        setState(
                new JavaCreateAccountState(
                        viewableState().getErrorId(),
                        viewableState().getFirstName(),
                        viewableState().getLastName(),
                        viewableState().getBirthDay(),
                        viewableState().getEmail(),
                        viewableState().getLocation(),
                        true,
                        true,
                        viewableState().getLoading()
                )
        );
    }

    private void createAccountNormalView() {
        setState(
                new JavaCreateAccountState(
                        null,
                        viewableState().getFirstName(),
                        viewableState().getLastName(),
                        viewableState().getBirthDay(),
                        viewableState().getEmail(),
                        viewableState().getLocation(),
                        viewableState().getIsDateDialogOpen(),
                        viewableState().getOpenDateDialog(),
                        viewableState().getLoading()
                )
        );
    }

    public void updateCreateAccountTextAndErrors(
            String firstName,
            String lastName,
            String birthDay,
            String email,
            String location
    ) {
        CreateAccountEntrys stringStates = createAccountValidationUseCase.invoke(
                toCreateAccountEntrys(
                        new Entry(firstName, state.getValue().getFirstName().getError()),
                        new Entry(lastName, state.getValue().getLastName().getError()),
                        new Entry(birthDay, state.getValue().getBirthDay().getError()),
                        new Entry(email, state.getValue().getEmail().getError()),
                        new Entry(location, state.getValue().getLocation().getError()))
        );
        setState(
                new JavaCreateAccountState(
                        viewableState().getErrorId(),
                        stringStates.getFirstName(),
                        stringStates.getLastName(),
                        stringStates.getBirthDay(),
                        stringStates.getEmail(),
                        stringStates.getLocation(),
                        viewableState().getIsDateDialogOpen(),
                        viewableState().getOpenDateDialog(),
                        viewableState().getLoading()
                )
        );
    }

    public void createAccount() {
        setState(
                new JavaCreateAccountState(
                        null,
                        viewableState().getFirstName(),
                        viewableState().getLastName(),
                        viewableState().getBirthDay(),
                        viewableState().getEmail(),
                        viewableState().getLocation(),
                        viewableState().getIsDateDialogOpen(),
                        viewableState().getOpenDateDialog(),
                        true
                )
        );
        Single
                .fromCallable(
                        () -> {
                            String[] split = viewableState().getBirthDay().getText().split("-");
                            String actualDate = split[2] + "-" + split[0] + "-" + split[1];
                            return createAccountUseCase.invoke(
                                    viewableState().getFirstName().getText(),
                                    viewableState().getLastName().getText(),
                                    actualDate,
                                    viewableState().getEmail().getText(),
                                    viewableState().getLocation().getText()
                            );
                        }
                ).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        result -> {
                            if (result == CreateAccountResult.CREATE_SUCCESS) {
                                setState(new JavaCreateAccountState());
                                // TODO add a onAccountCreateSuccess() here in some way
                            } else if (result == CreateAccountResult.CREATE_EXISTS) {
                                setState(
                                        new JavaCreateAccountState(
                                                R.string.create_account_error_user_exists,
                                                viewableState().getFirstName(),
                                                viewableState().getLastName(),
                                                viewableState().getBirthDay(),
                                                viewableState().getEmail(),
                                                viewableState().getLocation(),
                                                viewableState().getIsDateDialogOpen(),
                                                viewableState().getOpenDateDialog(),
                                                false
                                        )
                                );
                            } else {
                                setState(
                                        new JavaCreateAccountState(
                                                R.string.create_account_error_generic,
                                                viewableState().getFirstName(),
                                                viewableState().getLastName(),
                                                viewableState().getBirthDay(),
                                                viewableState().getEmail(),
                                                viewableState().getLocation(),
                                                viewableState().getIsDateDialogOpen(),
                                                viewableState().getOpenDateDialog(),
                                                false
                                        )
                                );
                            }
                        },
                        error -> {
                            setState(
                                    new JavaCreateAccountState(
                                            R.string.create_account_error_generic,
                                            viewableState().getFirstName(),
                                            viewableState().getLastName(),
                                            viewableState().getBirthDay(),
                                            viewableState().getEmail(),
                                            viewableState().getLocation(),
                                            viewableState().getIsDateDialogOpen(),
                                            viewableState().getOpenDateDialog(),
                                            false
                                    )
                            );
                        }
                );
    }

    @NonNull
    private CreateAccountEntrys toCreateAccountEntrys(Entry firstName, Entry lastName, Entry birthDay, Entry email, Entry location) {
        return new CreateAccountEntrys(firstName, lastName, birthDay, email, location);
    }
}
