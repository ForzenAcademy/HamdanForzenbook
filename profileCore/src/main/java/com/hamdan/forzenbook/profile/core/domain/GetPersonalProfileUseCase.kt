package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel.ProfileData

interface GetPersonalProfileUseCase {
    suspend operator fun invoke(): ProfileData
}
