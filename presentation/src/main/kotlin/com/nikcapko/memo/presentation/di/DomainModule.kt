package com.nikcapko.memo.presentation.di

import com.nikcapko.memo.presentation.domain.FindPairsInteractor
import com.nikcapko.memo.presentation.domain.FindPairsInteractorImpl
import com.nikcapko.memo.presentation.domain.GamesInteractor
import com.nikcapko.memo.presentation.domain.GamesInteractorImpl
import com.nikcapko.memo.presentation.domain.SelectTranslateInteractor
import com.nikcapko.memo.presentation.domain.SelectTranslateInteractorImpl
import com.nikcapko.memo.presentation.domain.WordDetailsInteractor
import com.nikcapko.memo.presentation.domain.WordDetailsInteractorImpl
import com.nikcapko.memo.presentation.domain.WordListInteractor
import com.nikcapko.memo.presentation.domain.WordListInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class DomainModule {

    @Binds
    abstract fun bindWordListInteractor(impl: WordListInteractorImpl): WordListInteractor

    @Binds
    abstract fun bindWordDetailsInteractor(impl: WordDetailsInteractorImpl): WordDetailsInteractor

    @Binds
    abstract fun bindGamesInteractor(impl: GamesInteractorImpl): GamesInteractor

    @Binds
    abstract fun bindSelectTranslateInteractor(impl: SelectTranslateInteractorImpl): SelectTranslateInteractor

    @Binds
    abstract fun bindFindPairsInteractor(impl: FindPairsInteractorImpl): FindPairsInteractor
}
