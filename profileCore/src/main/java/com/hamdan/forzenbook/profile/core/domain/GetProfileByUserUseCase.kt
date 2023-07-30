package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel.ProfileData

interface GetProfileByUserUseCase {
    suspend operator fun invoke(userId: Int): ProfileData
}
