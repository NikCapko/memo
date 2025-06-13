package com.nikcapko.memo.core.ui.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class ViewModelLazy<VM : LazyViewModel> @JvmOverloads constructor(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val extrasProducer: () -> CreationExtras = { CreationExtras.Empty },
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            return cached ?: run {
                val factory = factoryProducer()
                val store = storeProducer()
                val viewModel = ViewModelProvider(
                    store,
                    factory,
                    extrasProducer()
                )[viewModelClass.java]
                    .also { cached = it }
                return viewModel.also { viewModel.onCreateViewModel() }
            }
        }

    override fun isInitialized(): Boolean = cached != null
}

@MainThread
inline fun <reified VM : LazyViewModel> Fragment.lazyViewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { ownerProducer() }
    return createUlViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { owner.viewModelStore },
        extrasProducer = {
            extrasProducer?.invoke()
                ?: (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
                ?: CreationExtras.Empty
        },
        factoryProducer = factoryProducer ?: {
            (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                ?: defaultViewModelProviderFactory
        }
    )
}

@MainThread
inline fun <reified VM : LazyViewModel> Fragment.lazyActivityViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> = createUlViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { requireActivity().viewModelStore },
    extrasProducer = {
        extrasProducer?.invoke() ?: requireActivity().defaultViewModelCreationExtras
    },
    factoryProducer = factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)

@MainThread
fun <VM : LazyViewModel> Fragment.createUlViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    extrasProducer: () -> CreationExtras = { defaultViewModelCreationExtras },
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(viewModelClass, storeProducer, factoryPromise, extrasProducer)
}

@MainThread
inline fun <reified VM : LazyViewModel> ComponentActivity.lazyViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryPromise,
        extrasProducer = { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
