package com.prodmobile.template.di

import android.app.Application
import com.prodmobile.template.core.http_client.HttpClientFactory
import com.prodmobile.template.data.DefaultTemplateApi
import com.prodmobile.template.data.TemplateApi
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSourceImpl
import com.prodmobile.template.data.accounts.account_info.accountInfoDataStore
import com.prodmobile.template.data.accounts.mentor.remote.MentorAccountRemoteDataSource
import com.prodmobile.template.data.accounts.mentor.remote.MentorsAccountsRemoteDataSourceImpl
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSourceImpl
import com.prodmobile.template.data.auth.mentor.MentorAuthRemoteDataSource
import com.prodmobile.template.data.auth.mentor.remote.MentorAuthRemoteDataSourceImpl
import com.prodmobile.template.data.auth.student.StudentAuthRemoteDataSource
import com.prodmobile.template.data.auth.student.remote.StudentAuthRemoteDataSourceImpl
import com.prodmobile.template.data.feed.remote.FeedRemoteDataSource
import com.prodmobile.template.data.feed.remote.FeedRemoteDataSourceImpl
import com.prodmobile.template.feature.account.view_model.mentor.MentorViewModel
import com.prodmobile.template.feature.account.view_model.mentor.OwnMentorViewModel
import com.prodmobile.template.feature.account.view_model.student.OwnStudentViewModel
import com.prodmobile.template.feature.account.view_model.student.StudentViewModel
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationViewModel
import com.prodmobile.template.feature.auth.student.view_model.StudentRegistrationViewModel
import com.prodmobile.template.feature.favourite.view_model.FavouritesViewModel
import com.prodmobile.template.feature.feed.view_model.FeedScreenViewModel
import com.prodmobile.template.feature.requests.view_model.MentorsRequestsViewModel
import com.prodmobile.template.feature.requests.view_model.StudentsRequestsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create() }
    single<TemplateApi> { DefaultTemplateApi() }

    // auth section
    singleOf(::StudentAuthRemoteDataSourceImpl).bind<StudentAuthRemoteDataSource>()
    singleOf(::MentorAuthRemoteDataSourceImpl).bind<MentorAuthRemoteDataSource>()
    single { AccountInfoDataSourceImpl(get<Application>().accountInfoDataStore) }.bind<AccountInfoDataSource>()
    factoryOf(::StudentRegistrationViewModel)
    factoryOf(::MentorRegistrationViewModel)
    singleOf(::StudentAccountRemoteDataSourceImpl).bind<StudentAccountRemoteDataSource>()
    factoryOf(::MentorViewModel)
    factoryOf(::OwnStudentViewModel)
    factoryOf(::StudentViewModel)
    factoryOf(::FavouritesViewModel)
    factoryOf(::StudentsRequestsViewModel)
    factoryOf(::MentorsRequestsViewModel)
    factoryOf(::OwnMentorViewModel)
    singleOf(::MentorsAccountsRemoteDataSourceImpl).bind<MentorAccountRemoteDataSource>()
    factoryOf(::FeedScreenViewModel)
    singleOf(::FeedRemoteDataSourceImpl).bind<FeedRemoteDataSource>()
}
