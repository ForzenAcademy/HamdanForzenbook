package com.hamdan.forzenbook.profile.core.domain.mocks

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.profile.core.domain.GetPersonalProfileUseCase
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel

class MockGetPersonalProfileUseCase : GetPersonalProfileUseCase {
    override suspend fun invoke(): BaseProfileViewModel.ProfileData {
        val x: MutableList<PostData> = mutableListOf()
        for (i in 1..14) {
            x.add(
                PostData(
                    "Hamdan",
                    "Syed",
                    "Africa",
                    "https://cdn.dribbble.com/users/6255537/screenshots/14454071/media/0fd6c8dac9e38f5b5ca8ef6532eb703f.jpg?resize=400x0",
                    0,
                    i,
                    "post id: $i",
                    "text",
                    "2023-07-18 15:31:29"
                )
            )
        }
        return BaseProfileViewModel.ProfileData(
            firstName = "Hamdan",
            lastName = "Syed",
            userId = 0,
            isOwner = true,
            postSet = x,
            firstPostId = 1,
            lastPostId = null,
            userIconPath = "https://cdn.dribbble.com/users/6255537/screenshots/14454071/media/0fd6c8dac9e38f5b5ca8ef6532eb703f.jpg?resize=400x0",
            dateJoined = "2023-07-18 15:31:29",
            aboutUser = "This is some about stuff from the Mock :)",
        )
    }
}
