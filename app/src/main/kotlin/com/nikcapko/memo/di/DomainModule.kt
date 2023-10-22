package com.nikcapko.memo.di

import com.nikcapko.memo.domain.FindPairsInteractor
import com.nikcapko.memo.domain.FindPairsInteractorImpl
import com.nikcapko.memo.domain.GamesInteractor
import com.nikcapko.memo.domain.GamesInteractorImpl
import com.nikcapko.memo.domain.SelectTranslateInteractor
import com.nikcapko.memo.domain.SelectTranslateInteractorImpl
import com.nikcapko.memo.domain.WordDetailsInteractor
import com.nikcapko.memo.domain.WordDetailsInteractorImpl
import com.nikcapko.memo.domain.WordListInteractor
import com.nikcapko.memo.domain.WordListInteractorImpl
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
